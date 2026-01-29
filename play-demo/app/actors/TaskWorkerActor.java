package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import models.Task;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 任务工作者Actor
 * 负责实际执行任务的处理逻辑
 */
public class TaskWorkerActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final Random random = new Random();

    public static Props props() {
        return Props.create(TaskWorkerActor.class);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("TaskWorker {} started", getSelf().path());
    }

    @Override
    public void postStop() throws Exception {
        log.info("TaskWorker {} stopped", getSelf().path());
        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Task.class, this::handleTask)
                .matchAny(o -> log.warning("Received unknown message: {}", o))
                .build();
    }

    /**
     * 处理任务
     */
    private void handleTask(Task task) {
        log.info("Worker {} processing task: {}", getSelf().path().name(), task.getTaskId());

        task.setStatus(Task.TaskStatus.RUNNING);
        task.setStartedAt(LocalDateTime.now());

        try {
            // 根据任务类型执行不同的处理逻辑
            String result = executeTaskByType(task);

            // 任务成功完成
            task.setStatus(Task.TaskStatus.COMPLETED);
            task.setCompletedAt(LocalDateTime.now());
            task.setResult(result);

            log.info("Task {} completed successfully by worker {}", task.getTaskId(), getSelf().path().name());

            // 将结果发送回发送者（通常是TaskManagerActor）
            getSender().tell(task, getSelf());

        } catch (Exception e) {
            // 任务执行失败
            log.error(e, "Task {} failed on worker {}", task.getTaskId(), getSelf().path().name());

            task.setStatus(Task.TaskStatus.FAILED);
            task.setCompletedAt(LocalDateTime.now());
            task.setErrorMessage(e.getMessage());
            task.setRetryCount(task.getRetryCount() + 1);

            // 发送失败的任务回去
            getSender().tell(task, getSelf());
        }
    }

    /**
     * 根据任务类型执行不同的处理逻辑
     */
    private String executeTaskByType(Task task) throws Exception {
        String taskType = task.getTaskType();

        switch (taskType) {
            case "DATA_PROCESSING":
                return processDataTask(task);

            case "FILE_OPERATION":
                return processFileTask(task);

            case "API_CALL":
                return processApiTask(task);

            case "NOTIFICATION":
                return processNotificationTask(task);

            case "REPORT_GENERATION":
                return processReportTask(task);

            default:
                return processDefaultTask(task);
        }
    }

    /**
     * 数据处理任务
     */
    private String processDataTask(Task task) throws InterruptedException {
        log.info("Processing data task: {}", task.getTaskName());
        Thread.sleep(1000 + random.nextInt(2000));
        return "Data processed: " + task.getTaskData();
    }

    /**
     * 文件操作任务
     */
    private String processFileTask(Task task) throws InterruptedException {
        log.info("Processing file task: {}", task.getTaskName());
        Thread.sleep(1500 + random.nextInt(1500));
        return "File operation completed: " + task.getTaskData();
    }

    /**
     * API调用任务
     */
    private String processApiTask(Task task) throws InterruptedException {
        log.info("Processing API task: {}", task.getTaskName());
        Thread.sleep(500 + random.nextInt(1000));
        return "API call successful: " + task.getTaskData();
    }

    /**
     * 通知任务
     */
    private String processNotificationTask(Task task) throws InterruptedException {
        log.info("Processing notification task: {}", task.getTaskName());
        Thread.sleep(300 + random.nextInt(500));
        return "Notification sent: " + task.getTaskData();
    }

    /**
     * 报表生成任务
     */
    private String processReportTask(Task task) throws InterruptedException {
        log.info("Processing report task: {}", task.getTaskName());
        Thread.sleep(2000 + random.nextInt(3000));
        return "Report generated: " + task.getTaskData();
    }

    /**
     * 默认任务处理
     */
    private String processDefaultTask(Task task) throws InterruptedException {
        log.info("Processing default task: {}", task.getTaskName());
        Thread.sleep(1000);
        return "Task processed: " + task.getTaskName();
    }
}
