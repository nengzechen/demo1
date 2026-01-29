package actors;

import akka.actor.AbstractActor;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务Actor
 * 展示如何使用Actor实现定时任务调度
 */
public class ScheduledTaskActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Cancellable scheduledTask;
    private int executionCount = 0;

    /**
     * 创建Actor的Props
     */
    public static Props props() {
        return Props.create(ScheduledTaskActor.class);
    }

    /**
     * Actor启动时初始化定时任务
     */
    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("ScheduledTaskActor {} started", getSelf().path());

        // 使用Akka的调度器创建定时任务
        // 初始延迟5秒，之后每10秒执行一次
        scheduledTask = getContext().getSystem().scheduler().schedule(
                Duration.create(5, TimeUnit.SECONDS),      // 初始延迟
                Duration.create(10, TimeUnit.SECONDS),     // 执行间隔
                getSelf(),                                  // 目标Actor
                new TaskMessage.ScheduledTick(),           // 发送的消息
                getContext().getDispatcher(),              // 执行上下文
                getSelf()                                   // 发送者
        );

        log.info("Scheduled task initialized: first run in 5 seconds, then every 10 seconds");
    }

    /**
     * Actor停止时取消定时任务
     */
    @Override
    public void postStop() throws Exception {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel();
            log.info("Scheduled task cancelled");
        }
        log.info("ScheduledTaskActor {} stopped, executed {} times", getSelf().path(), executionCount);
        super.postStop();
    }

    /**
     * 消息处理器
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TaskMessage.ScheduledTick.class, this::handleScheduledTick)
                .matchAny(o -> log.warning("Received unknown message: {}", o))
                .build();
    }

    /**
     * 处理定时任务触发
     */
    private void handleScheduledTick(TaskMessage.ScheduledTick message) {
        executionCount++;
        String currentTime = LocalDateTime.now().format(formatter);

        log.info("====== Scheduled Task Execution #{} ======", executionCount);
        log.info("Execution time: {}", currentTime);
        log.info("Tick time: {}", message.getTickTime().format(formatter));

        // 执行定时任务的业务逻辑
        performScheduledTask();

        log.info("====== Task Execution #{} Completed ======", executionCount);
    }

    /**
     * 执行定时任务的业务逻辑
     */
    private void performScheduledTask() {
        // 这里可以添加实际的定时任务逻辑
        // 例如：
        // - 数据同步
        // - 缓存清理
        // - 报表生成
        // - 健康检查
        // - 发送通知

        log.info("Performing scheduled task logic...");

        // 模拟任务处理
        try {
            Thread.sleep(1000);
            log.info("Scheduled task logic completed successfully");
        } catch (InterruptedException e) {
            log.error(e, "Scheduled task execution interrupted");
            Thread.currentThread().interrupt();
        }
    }
}
