package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import models.Task;
import scala.concurrent.duration.Duration;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 任务管理器Actor
 * 作为监督者管理多个TaskWorkerActor，实现任务调度和分发
 */
public class TaskManagerActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    // Worker池
    private final List<ActorRef> workers = new ArrayList<>();
    private final int workerCount;
    private int currentWorkerIndex = 0;

    // 任务存储
    private final Map<String, Task> tasks = new HashMap<>();
    private final Queue<Task> pendingTasks = new PriorityQueue<>(
            (t1, t2) -> Integer.compare(t2.getPriority().getValue(), t1.getPriority().getValue())
    );

    // 统计信息
    private int totalTasksReceived = 0;
    private int totalTasksCompleted = 0;
    private int totalTasksFailed = 0;

    public TaskManagerActor(int workerCount) {
        this.workerCount = workerCount;
    }

    public static Props props(int workerCount) {
        return Props.create(TaskManagerActor.class, workerCount);
    }

    /**
     * 监督策略 - 实现监督策略模式
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
                10,  // 最大重试次数
                Duration.create(1, TimeUnit.MINUTES),  // 时间窗口
                DeciderBuilder
                        .match(Exception.class, e -> {
                            log.error(e, "Worker encountered an error");
                            return SupervisorStrategy.restart();  // 重启失败的worker
                        })
                        .matchAny(o -> SupervisorStrategy.escalate())  // 其他情况升级
                        .build()
        );
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("TaskManagerActor started with {} workers", workerCount);

        // 创建Worker池
        for (int i = 0; i < workerCount; i++) {
            ActorRef worker = getContext().actorOf(
                    TaskWorkerActor.props(),
                    "worker-" + i
            );
            workers.add(worker);
            log.info("Created worker: {}", worker.path());
        }
    }

    @Override
    public void postStop() throws Exception {
        log.info("TaskManagerActor stopped. Stats - Received: {}, Completed: {}, Failed: {}",
                totalTasksReceived, totalTasksCompleted, totalTasksFailed);
        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Task.class, this::handleNewTask)
                .match(TaskMessage.QueryTaskStatus.class, this::handleQueryStatus)
                .matchEquals("GET_STATS", s -> handleGetStats())
                .matchAny(o -> {
                    // 检查是否是Worker返回的已完成任务
                    if (o instanceof Task) {
                        handleTaskCompleted((Task) o);
                    } else {
                        log.warning("Received unknown message: {}", o);
                    }
                })
                .build();
    }

    /**
     * 处理新任务
     */
    private void handleNewTask(Task task) {
        totalTasksReceived++;
        log.info("Received new task: {} (Priority: {}, Total received: {})",
                task.getTaskId(), task.getPriority(), totalTasksReceived);

        // 存储任务
        tasks.put(task.getTaskId(), task);

        // 尝试分配给Worker
        if (!assignTaskToWorker(task)) {
            // 如果所有Worker都在忙，加入待处理队列
            pendingTasks.offer(task);
            log.info("Task {} added to pending queue (Queue size: {})",
                    task.getTaskId(), pendingTasks.size());
        }

        // 响应任务已接收
        getSender().tell(new TaskMessage.TaskStatusResponse(
                task.getTaskId(),
                "ACCEPTED",
                "Task accepted and queued for processing"
        ), getSelf());
    }

    /**
     * 将任务分配给Worker
     */
    private boolean assignTaskToWorker(Task task) {
        if (workers.isEmpty()) {
            return false;
        }

        // 使用轮询方式选择Worker
        ActorRef worker = workers.get(currentWorkerIndex);
        currentWorkerIndex = (currentWorkerIndex + 1) % workers.size();

        log.info("Assigning task {} to worker {}", task.getTaskId(), worker.path().name());
        worker.tell(task, getSelf());

        return true;
    }

    /**
     * 处理任务完成
     */
    private void handleTaskCompleted(Task task) {
        if (task.getStatus() == Task.TaskStatus.COMPLETED) {
            totalTasksCompleted++;
            log.info("Task {} completed successfully (Total completed: {})",
                    task.getTaskId(), totalTasksCompleted);
        } else if (task.getStatus() == Task.TaskStatus.FAILED) {
            totalTasksFailed++;
            log.warning("Task {} failed (Retry: {}/{}, Total failed: {})",
                    task.getTaskId(), task.getRetryCount(), task.getMaxRetries(), totalTasksFailed);

            // 如果还有重试次数，重新加入队列
            if (task.getRetryCount() < task.getMaxRetries()) {
                log.info("Retrying task {}", task.getTaskId());
                task.setStatus(Task.TaskStatus.PENDING);
                pendingTasks.offer(task);
            }
        }

        // 更新任务存储
        tasks.put(task.getTaskId(), task);

        // 处理待处理队列中的下一个任务
        processNextPendingTask();
    }

    /**
     * 处理待处理队列中的下一个任务
     */
    private void processNextPendingTask() {
        if (!pendingTasks.isEmpty()) {
            Task nextTask = pendingTasks.poll();
            log.info("Processing next pending task: {} (Remaining: {})",
                    nextTask.getTaskId(), pendingTasks.size());
            assignTaskToWorker(nextTask);
        }
    }

    /**
     * 查询任务状态
     */
    private void handleQueryStatus(TaskMessage.QueryTaskStatus query) {
        Task task = tasks.get(query.getTaskId());

        if (task != null) {
            getSender().tell(new TaskMessage.TaskStatusResponse(
                    task.getTaskId(),
                    task.getStatus().name(),
                    "Task found: " + task.getResult()
            ), getSelf());
        } else {
            getSender().tell(new TaskMessage.TaskStatusResponse(
                    query.getTaskId(),
                    "NOT_FOUND",
                    "Task not found"
            ), getSelf());
        }
    }

    /**
     * 获取统计信息
     */
    private void handleGetStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("workerCount", workerCount);
        stats.put("totalReceived", totalTasksReceived);
        stats.put("totalCompleted", totalTasksCompleted);
        stats.put("totalFailed", totalTasksFailed);
        stats.put("pendingQueueSize", pendingTasks.size());
        stats.put("totalTrackedTasks", tasks.size());

        getSender().tell(stats, getSelf());
    }
}
