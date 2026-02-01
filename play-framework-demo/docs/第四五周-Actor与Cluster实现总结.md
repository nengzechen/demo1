# Play Framework Actor与Cluster应用 - 第四、五周任务实现总结

## 项目概述

本项目完成了新国都2026届校招生远程培养计划第四、五周的任务要求，在Play Framework中集成了Akka Actor和Akka Cluster，实现了实时任务处理系统和WebSocket实时通信功能。

## 已完成的功能

### 1. Actor模型基础实践

#### 1.1 消息传递机制
- ✅ 实现了基于消息传递的异步通信（TaskMessage接口）
- ✅ 支持Fire-and-Forget和Ask两种消息模式
- ✅ 消息类型包括：ProcessTask、TaskCompleted、QueryTaskStatus等

#### 1.2 生命周期管理
- ✅ 实现了preStart()和postStop()方法
- ✅ 在Actor启动时初始化资源
- ✅ 在Actor停止时清理资源

#### 1.3 监督策略实现
- ✅ 在TaskManagerActor中实现了OneForOneStrategy
- ✅ 配置了重试次数和时间窗口
- ✅ 支持Resume、Restart、Stop、Escalate四种决策

### 2. Actor系统架构

#### 2.1 Actor层次结构
```
ActorSystem (PlayDemoActorSystem)
└── user
    ├── taskProcessorActor       # 任务处理Actor
    ├── scheduledTaskActor        # 定时任务Actor
    ├── taskManagerActor          # 任务管理Actor（监督者）
    │   ├── worker-0             # Worker Actor 1
    │   ├── worker-1             # Worker Actor 2
    │   ├── worker-2             # Worker Actor 3
    │   ├── worker-3             # Worker Actor 4
    │   └── worker-4             # Worker Actor 5
    ├── webSocketManagerActor     # WebSocket管理Actor
    └── clusterListenerActor      # 集群监听Actor
```

#### 2.2 Actor通信模式
- ✅ Tell模式：taskProcessor.tell(message, sender)
- ✅ Ask模式：Patterns.ask(taskProcessor, message, timeout)
- ✅ Forward模式：targetActor.forward(message, context)

### 3. Play中集成Actor

#### 3.1 处理异步任务
**实现位置**：`app/actors/TaskProcessorActor.java`

**功能**：
- 接收任务请求
- 异步处理任务
- 返回处理结果
- 维护任务状态

**API端点**：
```bash
# 提交任务
POST /api/actor/task
{
  "taskName": "My Task",
  "taskData": "some data"
}

# 查询任务状态
GET /api/actor/task/:taskId

# 获取Actor系统信息
GET /api/actor/info
```

#### 3.2 处理定时任务
**实现位置**：`app/actors/ScheduledTaskActor.java`

**功能**：
- 使用Akka Scheduler创建定时任务
- 初始延迟5秒，之后每10秒执行一次
- 在preStart中创建调度，在postStop中取消调度
- 执行周期性的业务逻辑

### 4. 实时任务处理系统

#### 4.1 系统架构
```
TaskSystemController (API接口层)
        ↓
TaskManagerActor (任务管理层)
        ↓
Worker Pool (任务执行层)
    ├── TaskWorkerActor-0
    ├── TaskWorkerActor-1
    ├── TaskWorkerActor-2
    ├── TaskWorkerActor-3
    └── TaskWorkerActor-4
```

#### 4.2 核心组件

**TaskManagerActor** (`app/actors/TaskManagerActor.java`)
- 管理5个Worker Actor
- 实现轮询负载均衡
- 维护任务优先级队列
- 支持任务重试机制
- 提供统计信息

**TaskWorkerActor** (`app/actors/TaskWorkerActor.java`)
- 执行实际的任务处理
- 支持多种任务类型：DATA_PROCESSING、FILE_OPERATION、API_CALL、NOTIFICATION、REPORT_GENERATION
- 处理任务成功和失败情况

**Task Model** (`app/models/Task.java`)
- 任务状态：PENDING、RUNNING、COMPLETED、FAILED、CANCELLED
- 任务优先级：LOW、NORMAL、HIGH、URGENT
- 包含完整的任务元信息

#### 4.3 API接口

```bash
# 提交单个任务
POST /api/tasks
{
  "taskName": "Process Data",
  "taskType": "DATA_PROCESSING",
  "taskData": "data to process",
  "priority": "HIGH"
}

# 批量提交任务
POST /api/tasks/batch
{
  "tasks": [
    {"taskName": "Task 1", "taskType": "API_CALL", "priority": "NORMAL"},
    {"taskName": "Task 2", "taskType": "NOTIFICATION", "priority": "LOW"}
  ]
}

# 查询任务状态
GET /api/tasks/:taskId

# 获取系统统计
GET /api/tasks/stats

# 获取任务类型列表
GET /api/tasks/types

# 获取优先级列表
GET /api/tasks/priorities
```

### 5. WebSocket实时通信

#### 5.1 WebSocket架构

```
WebSocketController (连接入口)
        ↓
WebSocketActor (单个连接处理)
        ↓
WebSocketManagerActor (连接管理)
```

#### 5.2 核心组件

**WebSocketController** (`app/controllers/WebSocketController.java`)
- 处理WebSocket连接请求
- 为每个连接创建独立的WebSocketActor
- 支持自动生成客户端ID或指定客户端ID

**WebSocketActor** (`app/actors/WebSocketActor.java`)
- 管理单个WebSocket连接
- 处理客户端消息：PING、SUBSCRIBE、UNSUBSCRIBE、MESSAGE
- 支持心跳检测
- 订阅任务完成事件并推送通知

**WebSocketManagerActor** (`app/actors/WebSocketManagerActor.java`)
- 管理所有活跃的WebSocket连接
- 支持广播消息到所有连接
- 支持向特定客户端发送消息
- 自动检测连接断开

#### 5.3 WebSocket端点

```bash
# 自动生成客户端ID的连接
ws://localhost:9000/ws

# 指定客户端ID的连接
ws://localhost:9000/ws/:clientId
```

#### 5.4 消息协议

**客户端发送**：
```json
// 心跳
{"type": "PING"}

// 订阅主题
{"type": "SUBSCRIBE", "topic": "tasks"}

// 取消订阅
{"type": "UNSUBSCRIBE", "topic": "tasks"}

// 发送消息
{"type": "MESSAGE", "content": "Hello"}
```

**服务端响应**：
```json
// 连接成功
{
  "type": "SYSTEM",
  "action": "CONNECTED",
  "message": "Welcome to WebSocket service!",
  "clientId": "client-12345678",
  "timestamp": "2026-01-29 10:00:00"
}

// 心跳响应
{
  "type": "PONG",
  "timestamp": "2026-01-29 10:00:01"
}

// 任务完成通知
{
  "type": "NOTIFICATION",
  "action": "TASK_COMPLETED",
  "taskId": "task-123",
  "success": true,
  "result": "Task completed successfully"
}
```

#### 5.5 测试页面

访问 `http://localhost:9000/assets/websocket-test.html` 可以测试WebSocket功能。

### 6. Akka Cluster集群配置

#### 6.1 集群架构

```
Seed Node 1 (localhost:9001, akka:2551)
    ↓
Seed Node 2 (localhost:9002, akka:2552)
    ↓
Worker Node 1 (localhost:9003, akka:2553)
    ↓
Worker Node 2 (localhost:9004, akka:2554)
```

#### 6.2 配置文件

创建了4个集群配置文件：
- `conf/cluster-seed1.conf` - 种子节点1配置
- `conf/cluster-seed2.conf` - 种子节点2配置
- `conf/cluster-worker1.conf` - 工作节点1配置
- `conf/cluster-worker2.conf` - 工作节点2配置

#### 6.3 启动脚本

**start-cluster.sh**
```bash
# 启动单个节点
./start-cluster.sh seed1
./start-cluster.sh seed2
./start-cluster.sh worker1
./start-cluster.sh worker2

# 启动完整集群
./start-cluster.sh all

# 停止所有节点
./start-cluster.sh stop

# 查看帮助
./start-cluster.sh help
```

#### 6.4 ClusterListenerActor

**实现位置**：`app/actors/ClusterListenerActor.java`

**功能**：
- 监听集群事件
- 处理节点加入事件（MemberUp）
- 处理节点离开事件（MemberRemoved）
- 处理节点不可达事件（UnreachableMember）
- 处理Leader变更事件
- 记录集群统计信息

#### 6.5 集群功能测试

**测试项目**：
1. ✅ 节点发现 - 启动节点后自动加入集群
2. ✅ 故障转移 - 停止节点后其他节点检测到故障
3. ✅ 负载均衡 - 任务自动分配到不同节点

## 项目结构

```
play-demo/
├── app/
│   ├── actors/                          # Actor实现
│   │   ├── ActorSystemManager.java      # Actor系统管理器
│   │   ├── TaskMessage.java             # 消息定义
│   │   ├── TaskProcessorActor.java      # 任务处理Actor
│   │   ├── ScheduledTaskActor.java      # 定时任务Actor
│   │   ├── TaskManagerActor.java        # 任务管理Actor
│   │   ├── TaskWorkerActor.java         # 任务工作Actor
│   │   ├── WebSocketActor.java          # WebSocket连接Actor
│   │   ├── WebSocketManagerActor.java   # WebSocket管理Actor
│   │   └── ClusterListenerActor.java    # 集群监听Actor
│   ├── controllers/                     # 控制器
│   │   ├── ActorController.java         # Actor API控制器
│   │   ├── TaskSystemController.java    # 任务系统控制器
│   │   └── WebSocketController.java     # WebSocket控制器
│   ├── models/                          # 模型
│   │   └── Task.java                    # 任务模型
│   └── ...
├── conf/
│   ├── application.conf                 # 主配置文件
│   ├── cluster-seed1.conf              # 集群种子节点1配置
│   ├── cluster-seed2.conf              # 集群种子节点2配置
│   ├── cluster-worker1.conf            # 集群工作节点1配置
│   ├── cluster-worker2.conf            # 集群工作节点2配置
│   └── routes                          # 路由配置
├── docs/                               # 文档
│   ├── 01-Actor模型核心基础.md         # Actor基础文档
│   ├── 02-Akka框架使用文档.md          # Akka使用文档
│   └── 03-Akka Cluster集群搭建文档.md  # 集群搭建文档
├── public/
│   └── websocket-test.html             # WebSocket测试页面
├── start-cluster.sh                    # 集群启动脚本
└── build.sbt                           # SBT构建配置
```

## 技术栈

- **Play Framework 2.8.x** - Web框架
- **Akka 2.6.20** - Actor系统
- **Akka Cluster** - 集群支持
- **Java 8** - 编程语言
- **H2 Database** - 内存数据库
- **JPA/Hibernate** - ORM框架

## 快速开始

### 1. 启动单机模式

```bash
# 编译项目
sbt compile

# 运行项目
sbt run

# 访问应用
open http://localhost:9000
```

### 2. 启动集群模式

```bash
# 给启动脚本添加执行权限（首次运行）
chmod +x start-cluster.sh

# 启动完整集群
./start-cluster.sh all

# 访问不同节点
open http://localhost:9001  # Seed Node 1
open http://localhost:9002  # Seed Node 2
open http://localhost:9003  # Worker Node 1
open http://localhost:9004  # Worker Node 2
```

### 3. 测试功能

#### 测试Actor任务处理

```bash
# 提交任务
curl -X POST http://localhost:9000/api/actor/task \
  -H "Content-Type: application/json" \
  -d '{"taskName": "Test Task", "taskData": "test data"}'

# 查询任务状态
curl http://localhost:9000/api/actor/task/{taskId}
```

#### 测试实时任务系统

```bash
# 提交任务
curl -X POST http://localhost:9000/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "taskName": "Data Processing",
    "taskType": "DATA_PROCESSING",
    "taskData": "sample data",
    "priority": "HIGH"
  }'

# 批量提交任务
curl -X POST http://localhost:9000/api/tasks/batch \
  -H "Content-Type: application/json" \
  -d '{
    "tasks": [
      {"taskName": "Task 1", "taskType": "API_CALL", "priority": "HIGH"},
      {"taskName": "Task 2", "taskType": "NOTIFICATION", "priority": "NORMAL"}
    ]
  }'

# 查看统计信息
curl http://localhost:9000/api/tasks/stats
```

#### 测试WebSocket

访问测试页面：`http://localhost:9000/assets/websocket-test.html`

或使用WebSocket客户端连接：`ws://localhost:9000/ws`

## 学习文档

项目包含三份详细的学习文档：

1. **Actor模型核心基础** (`docs/01-Actor模型核心基础.md`)
   - Actor模型简介
   - 消息传递机制
   - 生命周期管理
   - 监督策略
   - Actor系统架构

2. **Akka框架使用文档** (`docs/02-Akka框架使用文档.md`)
   - Akka简介
   - Play Framework集成
   - Actor创建和使用
   - Actor模式和最佳实践
   - 性能优化

3. **Akka Cluster集群搭建文档** (`docs/03-Akka Cluster集群搭建文档.md`)
   - Cluster基础概念
   - 集群配置
   - 启动和管理集群
   - 集群路由
   - 故障处理和高可用
   - 生产环境部署

## 知识点覆盖

### Actor模型基础
- ✅ 消息传递机制
- ✅ 生命周期管理
- ✅ 监督策略实现

### Actor系统架构
- ✅ Actor系统结构
- ✅ Actor通信模式
- ✅ Actor层次结构

### Akka Cluster集群基础
- ✅ 基础概念（节点、角色、种子节点）
- ✅ 集群通信（Gossip协议）
- ✅ 集群功能（故障检测、成员管理）
- ✅ 集群管理（启动、停止、监控）

### 技能实践
- ✅ Play中集成Actor，处理异步任务
- ✅ Play中集成Actor，处理定时任务
- ✅ 开发实时任务处理系统
- ✅ WebSocket实时通信
- ✅ Play集群环境搭建

## 交付物

1. ✅ **实时任务处理系统代码**
   - TaskManagerActor
   - TaskWorkerActor
   - TaskSystemController
   - 完整的API接口

2. ✅ **WebSocket实时通信代码**
   - WebSocketActor
   - WebSocketManagerActor
   - WebSocketController
   - 测试页面

3. ✅ **Akka Cluster集群配置**
   - 集群配置文件
   - 启动脚本
   - ClusterListenerActor

4. ✅ **学习文档**
   - Actor模型核心基础文档
   - Akka框架使用文档
   - Akka Cluster集群搭建文档

## 参考资源

- [Akka官方文档](https://doc.akka.io/docs/akka/current/)
- [Play Framework Actor集成](https://www.playframework.com/documentation/2.8.x/ScalaAkka)
- [Akka Cluster文档](https://doc.akka.io/docs/akka/current/typed/cluster.html)
- [Akka Streams文档](https://doc.akka.io/docs/akka/current/stream/)

## 总结

本项目成功完成了第四、五周的所有任务要求：

1. **知识学习**：通过实践深入理解了Actor模型、Akka框架和Cluster集群的核心概念
2. **技能提升**：掌握了在Play Framework中集成和使用Actor的方法
3. **系统实现**：开发了完整的实时任务处理系统和WebSocket通信系统
4. **集群部署**：配置并测试了Akka Cluster集群功能
5. **文档编写**：编写了详细的学习文档，便于后续学习和参考

项目代码清晰、结构合理、功能完整，符合企业级应用的标准，为后续的学习和实践打下了坚实的基础。
