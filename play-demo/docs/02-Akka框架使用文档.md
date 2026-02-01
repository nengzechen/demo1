# Akka框架使用文档

## 1. Akka简介

### 1.1 什么是Akka

Akka是一个用于构建高并发、分布式和容错应用的工具包和运行时。它基于Actor模型，提供了简单而强大的并发和分布式编程抽象。

### 1.2 Akka核心组件

- **Akka Actor**：基于Actor模型的并发编程
- **Akka Streams**：响应式流处理
- **Akka Cluster**：分布式Actor系统
- **Akka HTTP**：HTTP服务器和客户端
- **Akka Persistence**：事件溯源和CQRS

## 2. 在Play Framework中集成Akka

### 2.1 添加依赖

在`build.sbt`中添加Akka依赖：

```scala
libraryDependencies ++= Seq(
  // Akka Actor 支持
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20",
  "com.typesafe.akka" %% "akka-actor" % "2.6.20",
  "com.typesafe.akka" %% "akka-stream" % "2.6.20",

  // Akka Cluster 支持
  "com.typesafe.akka" %% "akka-cluster-typed" % "2.6.20",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.6.20",

  // Akka 序列化
  "com.typesafe.akka" %% "akka-serialization-jackson" % "2.6.20"
)
```

### 2.2 配置Akka

在`application.conf`中配置Akka：

```hocon
akka {
  loggers = ["akka.event.slf4j.Slf4jLogger"]
  loglevel = "INFO"

  actor {
    provider = "cluster"

    # 序列化配置
    serialization-bindings {
      "actors.TaskMessage" = jackson-json
    }

    # 默认调度器
    default-dispatcher {
      type = Dispatcher
      executor = "fork-join-executor"
      fork-join-executor {
        parallelism-min = 2
        parallelism-factor = 2.0
        parallelism-max = 10
      }
      throughput = 100
    }
  }
}
```

### 2.3 创建ActorSystem管理器

创建一个单例类来管理ActorSystem的生命周期：

```java
@Singleton
public class ActorSystemManager {
    private final ActorSystem actorSystem;

    @Inject
    public ActorSystemManager(ApplicationLifecycle lifecycle) {
        Config config = ConfigFactory.load();
        this.actorSystem = ActorSystem.create("MyActorSystem", config);

        // 注册应用关闭时的清理逻辑
        lifecycle.addStopHook(() -> {
            actorSystem.terminate();
            return CompletableFuture.completedFuture(null);
        });
    }

    public ActorSystem getActorSystem() {
        return actorSystem;
    }
}
```

## 3. 创建和使用Actor

### 3.1 定义消息类

```java
public interface TaskMessage extends Serializable {
    class ProcessTask implements TaskMessage {
        private final String taskId;
        private final String taskData;

        public ProcessTask(String taskId, String taskData) {
            this.taskId = taskId;
            this.taskData = taskData;
        }

        // Getters...
    }

    class TaskCompleted implements TaskMessage {
        private final String taskId;
        private final String result;

        public TaskCompleted(String taskId, String result) {
            this.taskId = taskId;
            this.result = result;
        }

        // Getters...
    }
}
```

### 3.2 创建Actor

```java
public class TaskProcessorActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(TaskProcessorActor.class);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("TaskProcessorActor started");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(TaskMessage.ProcessTask.class, this::handleProcessTask)
            .matchAny(o -> log.warning("Unknown message: {}", o))
            .build();
    }

    private void handleProcessTask(TaskMessage.ProcessTask task) {
        log.info("Processing task: {}", task.getTaskId());

        // 异步处理任务
        CompletableFuture.runAsync(() -> {
            // 处理逻辑
            String result = processTask(task);

            // 发送完成消息
            getSender().tell(new TaskMessage.TaskCompleted(
                task.getTaskId(),
                result
            ), getSelf());
        });
    }

    private String processTask(TaskMessage.ProcessTask task) {
        // 实际的任务处理逻辑
        return "Task processed: " + task.getTaskData();
    }
}
```

### 3.3 在Controller中使用Actor

```java
@Singleton
public class TaskController extends Controller {
    private final ActorRef taskProcessor;

    @Inject
    public TaskController(ActorSystemManager actorSystemManager) {
        this.taskProcessor = actorSystemManager.getActorSystem()
            .actorOf(TaskProcessorActor.props(), "taskProcessor");
    }

    public CompletableFuture<Result> submitTask() {
        JsonNode json = request().body().asJson();
        String taskId = UUID.randomUUID().toString();
        String taskData = json.path("data").asText();

        TaskMessage.ProcessTask task = new TaskMessage.ProcessTask(taskId, taskData);

        return Patterns
            .ask(taskProcessor, task, Duration.create(5, TimeUnit.SECONDS))
            .thenApply(response -> {
                if (response instanceof TaskMessage.TaskCompleted) {
                    TaskMessage.TaskCompleted completed = (TaskMessage.TaskCompleted) response;
                    ObjectNode result = Json.newObject();
                    result.put("taskId", completed.getTaskId());
                    result.put("result", completed.getResult());
                    return ok(result);
                }
                return internalServerError("Unexpected response");
            })
            .toCompletableFuture()
            .exceptionally(throwable -> {
                return internalServerError("Task failed: " + throwable.getMessage());
            });
    }
}
```

## 4. Actor模式和最佳实践

### 4.1 定时任务

使用Akka的调度器实现定时任务：

```java
public class ScheduledTaskActor extends AbstractActor {
    private Cancellable scheduledTask;

    @Override
    public void preStart() throws Exception {
        super.preStart();

        // 创建定时任务
        scheduledTask = getContext().getSystem().scheduler().schedule(
            Duration.create(5, TimeUnit.SECONDS),      // 初始延迟
            Duration.create(10, TimeUnit.SECONDS),     // 执行间隔
            getSelf(),                                  // 目标Actor
            new TickMessage(),                         // 发送的消息
            getContext().getDispatcher(),              // 执行上下文
            getSelf()                                   // 发送者
        );
    }

    @Override
    public void postStop() throws Exception {
        if (scheduledTask != null) {
            scheduledTask.cancel();
        }
        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(TickMessage.class, this::handleTick)
            .build();
    }

    private void handleTick(TickMessage tick) {
        // 执行定时任务逻辑
        log.info("Scheduled task executed");
    }
}
```

### 4.2 路由模式

使用路由器将消息分发给多个Worker：

```java
public class TaskManagerActor extends AbstractActor {
    private final List<ActorRef> workers = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    public void preStart() throws Exception {
        super.preStart();

        // 创建Worker池
        for (int i = 0; i < 5; i++) {
            ActorRef worker = getContext().actorOf(
                TaskWorkerActor.props(),
                "worker-" + i
            );
            workers.add(worker);
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(Task.class, this::distributeTask)
            .build();
    }

    private void distributeTask(Task task) {
        // 轮询分配
        ActorRef worker = workers.get(currentIndex);
        currentIndex = (currentIndex + 1) % workers.size();

        worker.tell(task, getSelf());
    }
}
```

### 4.3 监督模式

实现父Actor监督子Actor：

```java
@Override
public SupervisorStrategy supervisorStrategy() {
    return new OneForOneStrategy(
        10,  // 最大重试次数
        Duration.create(1, TimeUnit.MINUTES),
        DeciderBuilder
            .match(Exception.class, e -> {
                log.error(e, "Worker failed");
                return SupervisorStrategy.restart();
            })
            .matchAny(o -> SupervisorStrategy.escalate())
            .build()
    );
}
```

## 5. Akka Streams基础

### 5.1 创建简单的Stream

```java
public class StreamExample {
    public void simpleStream(ActorSystem system) {
        // 创建Source
        Source<Integer, NotUsed> source = Source.range(1, 100);

        // 创建Flow（转换）
        Flow<Integer, Integer, NotUsed> flow = Flow.of(Integer.class)
            .map(x -> x * 2);

        // 创建Sink
        Sink<Integer, CompletionStage<Done>> sink = Sink.foreach(x ->
            System.out.println(x)
        );

        // 连接并运行
        source.via(flow).runWith(sink, system);
    }
}
```

### 5.2 背压处理

Akka Streams自动处理背压：

```java
Source.range(1, 1000)
    .throttle(10, Duration.ofSeconds(1))  // 限流
    .buffer(100, OverflowStrategy.backpressure())  // 缓冲
    .runWith(Sink.foreach(System.out::println), system);
```

## 6. 项目实践

### 6.1 项目中的Actor实现

本项目实现了以下Actor：

1. **TaskProcessorActor**：处理异步任务
2. **ScheduledTaskActor**：执行定时任务
3. **TaskManagerActor**：管理Worker池，实现负载均衡
4. **TaskWorkerActor**：实际执行任务
5. **WebSocketActor**：处理WebSocket连接
6. **WebSocketManagerActor**：管理所有WebSocket连接
7. **ClusterListenerActor**：监听集群事件

### 6.2 API端点

```
# Actor任务处理
POST   /api/actor/task              # 提交任务
GET    /api/actor/task/:taskId      # 查询任务状态
GET    /api/actor/info              # 获取Actor系统信息

# 实时任务系统
POST   /api/tasks                   # 提交任务
POST   /api/tasks/batch             # 批量提交任务
GET    /api/tasks/:taskId           # 查询任务状态
GET    /api/tasks/stats             # 获取统计信息

# WebSocket
GET    /ws                          # WebSocket连接
GET    /ws/:clientId                # 指定客户端ID的连接
```

## 7. 性能优化

### 7.1 调度器配置

为不同类型的任务配置专用调度器：

```hocon
my-dispatcher {
  type = Dispatcher
  executor = "fork-join-executor"
  fork-join-executor {
    parallelism-min = 2
    parallelism-factor = 2.0
    parallelism-max = 10
  }
  throughput = 100
}
```

使用：

```java
Props props = Props.create(MyActor.class)
    .withDispatcher("my-dispatcher");
```

### 7.2 消息批处理

```java
@Override
public Receive createReceive() {
    return receiveBuilder()
        .match(Message.class, this::handleMessage)
        .build();
}

private void handleMessage(Message msg) {
    messageBuffer.add(msg);

    if (messageBuffer.size() >= BATCH_SIZE) {
        processBatch(messageBuffer);
        messageBuffer.clear();
    }
}
```

### 7.3 邮箱配置

配置有界邮箱防止内存溢出：

```hocon
bounded-mailbox {
  mailbox-type = "akka.dispatch.BoundedMailbox"
  mailbox-capacity = 1000
  mailbox-push-timeout-time = 10s
}
```

## 8. 测试

### 8.1 单元测试

```java
public class TaskProcessorActorTest {
    private TestKit testKit;

    @Before
    public void setup() {
        ActorSystem system = ActorSystem.create("test");
        testKit = new TestKit(system);
    }

    @Test
    public void testProcessTask() {
        ActorRef actor = testKit.getSystem()
            .actorOf(TaskProcessorActor.props());

        // 发送消息
        actor.tell(new TaskMessage.ProcessTask("task1", "data"), testKit.getRef());

        // 验证响应
        TaskMessage.TaskCompleted response = testKit.expectMsgClass(
            Duration.ofSeconds(5),
            TaskMessage.TaskCompleted.class
        );

        assertEquals("task1", response.getTaskId());
    }
}
```

## 9. 常见问题和解决方案

### 9.1 消息丢失

问题：消息发送后没有被处理

解决：
1. 检查Actor是否正确启动
2. 检查消息类型是否匹配
3. 使用Ask模式确认消息送达

### 9.2 死锁

问题：多个Actor相互等待响应

解决：
1. 避免在Actor中使用阻塞操作
2. 使用超时机制
3. 使用Future进行异步处理

### 9.3 内存泄漏

问题：Actor持有的资源没有释放

解决：
1. 在postStop中清理资源
2. 使用有界邮箱
3. 定期清理过期数据

## 10. 学习资源

- [Akka官方文档](https://doc.akka.io/docs/akka/current/)
- [Play Framework Actor集成](https://www.playframework.com/documentation/2.8.x/ScalaAkka)
- [Lightbend Academy](https://academy.lightbend.com/)
