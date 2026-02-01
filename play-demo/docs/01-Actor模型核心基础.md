# Actor模型核心基础学习文档

## 1. Actor模型简介

### 1.1 什么是Actor模型

Actor模型是一种并发计算模型，由Carl Hewitt在1973年提出。它将"Actor"作为并发计算的基本单元。Actor模型的核心思想是：
- **万物皆Actor**：系统中的所有实体都是Actor
- **消息驱动**：Actor之间通过异步消息进行通信
- **无共享状态**：Actor之间不共享内存，避免了传统并发编程中的锁和竞态条件

### 1.2 为什么需要Actor模型

传统的并发编程面临的问题：
- **共享内存并发**：需要使用锁来保护共享数据，容易导致死锁
- **复杂性高**：线程同步、竞态条件等问题难以调试
- **扩展性差**：难以在分布式环境中扩展

Actor模型的优势：
- **简化并发编程**：通过消息传递避免共享状态
- **易于理解和调试**：每个Actor独立运行，状态封装在内部
- **天然支持分布式**：消息传递机制可以透明地跨网络传输

## 2. Actor模型核心概念

### 2.1 Actor

Actor是一个封装了状态和行为的独立计算单元：
- **状态（State）**：Actor内部的私有数据
- **行为（Behavior）**：Actor如何处理接收到的消息
- **邮箱（Mailbox）**：存储接收到的消息的队列

### 2.2 消息传递机制

Actor之间通过异步消息进行通信：

```java
// 发送消息（Fire and Forget）
actorRef.tell(message, getSelf());

// 发送消息并等待响应（Ask Pattern）
CompletionStage<Object> result = Patterns.ask(actorRef, message, timeout);
```

**消息传递特点**：
1. **异步性**：发送消息后立即返回，不等待处理完成
2. **不可变性**：消息应该是不可变的，避免并发修改
3. **有序性**：同一发送者发送给同一接收者的消息保持顺序
4. **最多一次传递**：消息可能丢失，但不会重复

### 2.3 生命周期管理

Actor有完整的生命周期：

```java
public class MyActor extends AbstractActor {

    @Override
    public void preStart() throws Exception {
        // Actor启动前调用
        // 用于初始化资源、订阅事件等
        super.preStart();
        log.info("Actor starting");
    }

    @Override
    public void postStop() throws Exception {
        // Actor停止后调用
        // 用于清理资源、取消订阅等
        log.info("Actor stopping");
        super.postStop();
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        // Actor重启前调用
        // 默认会停止所有子Actor
        super.preRestart(reason, message);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        // Actor重启后调用
        // 默认会调用preStart
        super.postRestart(reason);
    }
}
```

**生命周期阶段**：
1. **创建**：通过ActorSystem或Context创建Actor
2. **启动**：preStart()方法被调用
3. **运行**：处理消息
4. **重启**：发生异常时，根据监督策略决定是否重启
5. **停止**：postStop()方法被调用

### 2.4 监督策略

监督策略定义了当子Actor失败时父Actor应该如何处理：

```java
@Override
public SupervisorStrategy supervisorStrategy() {
    return new OneForOneStrategy(
        10,  // 最大重试次数
        Duration.create(1, TimeUnit.MINUTES),  // 时间窗口
        DeciderBuilder
            .match(ArithmeticException.class, e -> SupervisorStrategy.resume())     // 继续
            .match(NullPointerException.class, e -> SupervisorStrategy.restart())   // 重启
            .match(IllegalArgumentException.class, e -> SupervisorStrategy.stop())  // 停止
            .matchAny(o -> SupervisorStrategy.escalate())  // 升级到上层
            .build()
    );
}
```

**监督策略类型**：
1. **OneForOneStrategy**：只影响失败的子Actor
2. **AllForOneStrategy**：影响所有子Actor

**监督决策**：
1. **Resume**：忽略错误，继续处理消息
2. **Restart**：重启Actor，清空邮箱
3. **Stop**：停止Actor
4. **Escalate**：将错误升级到父Actor

## 3. Actor系统架构

### 3.1 Actor层次结构

Actor系统采用树形结构：

```
ActorSystem
└── user (所有用户创建的Actor的父Actor)
    ├── myActor1
    │   ├── childActor1
    │   └── childActor2
    └── myActor2
        └── childActor3
```

**层次结构的好处**：
- **故障隔离**：子Actor的失败不会影响兄弟Actor
- **监督机制**：父Actor监督子Actor
- **资源管理**：停止父Actor会自动停止所有子Actor

### 3.2 Actor路径

每个Actor都有唯一的路径：

```
akka://ActorSystemName/user/parentActor/childActor
```

路径组成：
- **协议**：akka:// (本地) 或 akka.tcp:// (远程)
- **系统名**：ActorSystem的名称
- **路径**：从根到Actor的层次路径

### 3.3 ActorRef

ActorRef是Actor的引用，用于向Actor发送消息：

```java
// 创建Actor并获取引用
ActorRef actorRef = actorSystem.actorOf(Props.create(MyActor.class), "myActor");

// 通过路径查找Actor
ActorSelection selection = actorSystem.actorSelection("/user/myActor");
```

**ActorRef的特点**：
- **位置透明**：可以是本地或远程Actor
- **可序列化**：可以通过网络传输
- **生命周期独立**：ActorRef可以存活于Actor之外

## 4. Actor通信模式

### 4.1 Tell模式（Fire and Forget）

最简单的消息发送方式，发送后不等待响应：

```java
actorRef.tell(message, getSelf());
```

适用场景：
- 不需要响应的场景
- 高吞吐量的场景

### 4.2 Ask模式（Request-Response）

发送消息并等待响应：

```java
CompletionStage<Object> future = Patterns.ask(actorRef, message, timeout);
future.thenAccept(response -> {
    // 处理响应
});
```

适用场景：
- 需要获取处理结果
- 请求-响应模式

### 4.3 Forward模式

转发消息给其他Actor，保持原始发送者：

```java
targetActor.forward(message, getContext());
```

适用场景：
- 代理模式
- 路由场景

## 5. 项目实践示例

### 5.1 TaskProcessorActor

位置：`app/actors/TaskProcessorActor.java`

功能：处理异步任务

关键特性：
- 使用消息传递处理任务
- 异步执行耗时操作
- 维护任务状态

### 5.2 ScheduledTaskActor

位置：`app/actors/ScheduledTaskActor.java`

功能：执行定时任务

关键特性：
- 使用Akka调度器
- 生命周期管理（启动时创建调度，停止时取消）
- 定期执行任务逻辑

### 5.3 TaskManagerActor

位置：`app/actors/TaskManagerActor.java`

功能：管理多个Worker Actor

关键特性：
- 监督策略实现
- Worker池管理
- 任务分发和负载均衡
- 优先级队列

## 6. 最佳实践

### 6.1 消息设计

1. **不可变性**：消息类应该是不可变的
2. **序列化**：消息类应该可序列化（实现Serializable）
3. **类型安全**：使用类型化的消息类

### 6.2 Actor设计

1. **单一职责**：每个Actor只负责一个明确的功能
2. **状态封装**：不要暴露Actor的内部状态
3. **避免阻塞**：不要在Actor中执行阻塞操作

### 6.3 错误处理

1. **使用监督策略**：让父Actor处理子Actor的错误
2. **故障隔离**：将可能失败的操作隔离到单独的Actor
3. **重试机制**：在监督策略中实现重试逻辑

## 7. 学习资源

### 官方文档
- [Akka Actor官方文档](https://doc.akka.io/docs/akka/current/typed/actors.html)
- [Akka Java API](https://doc.akka.io/japi/akka/current/)

### 推荐阅读
- 《Reactive Design Patterns》
- 《Akka in Action》
- 《Designing Data-Intensive Applications》

## 8. 练习建议

1. **创建简单Actor**：实现一个计数器Actor
2. **实现请求-响应**：使用Ask模式实现查询功能
3. **监督策略实践**：创建父子Actor，测试不同的监督策略
4. **性能测试**：测试Actor的消息处理吞吐量
