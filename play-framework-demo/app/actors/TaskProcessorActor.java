package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 任务处理Actor
 * 用于处理异步任务，展示Actor模型的消息传递机制和生命周期管理
 */
public class TaskProcessorActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    // 任务状态存储
    private final Map<String, String> taskStatusMap = new HashMap<>();

    /**
     * 创建Actor的Props
     */
    public static Props props() {
        return Props.create(TaskProcessorActor.class);
    }

    /**
     * Actor生命周期 - 启动前
     */
    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("TaskProcessorActor {} started", getSelf().path());
    }

    /**
     * Actor生命周期 - 停止后
     */
    @Override
    public void postStop() throws Exception {
        log.info("TaskProcessorActor {} stopped", getSelf().path());
        super.postStop();
    }

    /**
     * 消息处理器
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TaskMessage.ProcessTask.class, this::handleProcessTask)
                .match(TaskMessage.QueryTaskStatus.class, this::handleQueryStatus)
                .matchAny(o -> log.warning("Received unknown message: {}", o))
                .build();
    }

    /**
     * 处理任务
     */
    private void handleProcessTask(TaskMessage.ProcessTask message) {
        log.info("Processing task: {}", message);

        // 更新任务状态为处理中
        taskStatusMap.put(message.getTaskId(), "PROCESSING");

        // 模拟异步任务处理
        CompletableFuture.runAsync(() -> {
            try {
                // 模拟耗时操作
                Thread.sleep(2000);

                // 处理任务逻辑
                String result = processTaskLogic(message);

                // 发送任务完成消息给自己
                getSelf().tell(new TaskMessage.TaskCompleted(
                        message.getTaskId(),
                        true,
                        result
                ), getSelf());

            } catch (Exception e) {
                log.error(e, "Task processing failed: {}", message.getTaskId());
                getSelf().tell(new TaskMessage.TaskCompleted(
                        message.getTaskId(),
                        false,
                        "Error: " + e.getMessage()
                ), getSelf());
            }
        });

        // 立即响应任务已接收
        getSender().tell(new TaskMessage.TaskStatusResponse(
                message.getTaskId(),
                "ACCEPTED",
                "Task accepted and processing started"
        ), getSelf());
    }

    /**
     * 查询任务状态
     */
    private void handleQueryStatus(TaskMessage.QueryTaskStatus message) {
        String status = taskStatusMap.getOrDefault(message.getTaskId(), "NOT_FOUND");
        getSender().tell(new TaskMessage.TaskStatusResponse(
                message.getTaskId(),
                status,
                "Current task status"
        ), getSelf());
    }

    /**
     * 任务处理逻辑
     */
    private String processTaskLogic(TaskMessage.ProcessTask message) {
        log.info("Executing task logic for: {}", message.getTaskName());

        // 这里可以添加实际的业务逻辑
        // 例如：数据处理、文件操作、API调用等

        return String.format("Task %s completed successfully. Data: %s",
                message.getTaskName(),
                message.getTaskData());
    }

    /**
     * 处理任务完成消息
     */
    private void handleTaskCompleted(TaskMessage.TaskCompleted message) {
        log.info("Task completed: {}", message);

        // 更新任务状态
        if (message.isSuccess()) {
            taskStatusMap.put(message.getTaskId(), "COMPLETED");
        } else {
            taskStatusMap.put(message.getTaskId(), "FAILED");
        }
    }
}
