# Akka Cluster集群搭建文档

## 1. Akka Cluster简介

### 1.1 什么是Akka Cluster

Akka Cluster提供了一个容错的、去中心化的、基于P2P的集群成员服务，没有单点故障或单点瓶颈。它使用gossip协议和自动故障检测器。

### 1.2 Akka Cluster核心特性

- **去中心化**：无主节点，所有节点平等
- **故障检测**：自动检测节点故障
- **成员管理**：自动维护集群成员列表
- **位置透明**：Actor引用可以跨节点透明使用
- **负载均衡**：支持多种负载均衡策略

### 1.3 应用场景

- **高可用性**：多节点冗余，单点故障不影响服务
- **水平扩展**：动态添加节点提高处理能力
- **负载均衡**：将请求分散到多个节点
- **数据分片**：使用Cluster Sharding分散数据

## 2. Cluster基础概念

### 2.1 集群成员

每个加入集群的ActorSystem称为一个成员（Member）。

**成员状态**：
- **Joining**：节点正在加入集群
- **Up**：节点已完全加入并可用
- **Leaving**：节点准备离开集群
- **Exiting**：节点正在退出
- **Down**：节点被标记为down
- **Removed**：节点已从集群移除

### 2.2 种子节点（Seed Nodes）

种子节点是集群的初始联系点，新节点通过连接种子节点加入集群。

**特点**：
- 至少配置2个种子节点以提高可用性
- 种子节点自身也是普通集群成员
- 可以动态更新种子节点列表

### 2.3 集群角色（Roles）

可以为节点分配角色，实现不同类型节点的分工：

```hocon
akka.cluster.roles = ["frontend", "backend", "database"]
```

常见角色：
- **seed**：种子节点
- **frontend**：前端服务节点
- **backend**：后端处理节点
- **worker**：工作节点

### 2.4 Gossip协议

集群使用Gossip协议传播成员信息和集群状态，确保最终一致性。

## 3. 配置Akka Cluster

### 3.1 添加依赖

在`build.sbt`中添加Cluster依赖：

```scala
libraryDependencies ++= Seq(
  // Akka Cluster 支持
  "com.typesafe.akka" %% "akka-cluster-typed" % "2.6.20",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.6.20",
  "com.typesafe.akka" %% "akka-cluster-sharding-typed" % "2.6.20",

  // Akka Serialization
  "com.typesafe.akka" %% "akka-serialization-jackson" % "2.6.20",

  // Akka Management (集群管理)
  "com.lightbend.akka.management" %% "akka-management" % "1.1.4",
  "com.lightbend.akka.management" %% "akka-management-cluster-http" % "1.1.4"
)
```

### 3.2 基础配置

在`application.conf`中配置：

```hocon
akka {
  actor {
    # 使用Cluster作为Actor provider
    provider = "cluster"

    # 序列化配置
    serialization-bindings {
      "java.io.Serializable" = jackson-json
    }
  }

  # Remote配置
  remote {
    artery {
      enabled = on
      transport = tcp
      canonical.hostname = "127.0.0.1"
      canonical.port = 2551
    }
  }

  # Cluster配置
  cluster {
    # 种子节点列表
    seed-nodes = [
      "akka://PlayDemoActorSystem@127.0.0.1:2551",
      "akka://PlayDemoActorSystem@127.0.0.1:2552"
    ]

    # 关闭自动down功能（生产环境推荐）
    auto-down-unreachable-after = off

    # 集群角色
    roles = ["worker"]

    # 最小节点数
    min-nr-of-members = 1

    # 角色最小节点数
    role {
      frontend.min-nr-of-members = 1
      backend.min-nr-of-members = 2
    }
  }
}
```

### 3.3 多节点配置

为不同节点创建独立的配置文件：

**conf/cluster-seed1.conf**
```hocon
include "application.conf"

play.server.http.port = 9001

akka.remote.artery.canonical {
  hostname = "127.0.0.1"
  port = 2551
}

akka.cluster.roles = ["seed", "worker"]
```

**conf/cluster-seed2.conf**
```hocon
include "application.conf"

play.server.http.port = 9002

akka.remote.artery.canonical {
  hostname = "127.0.0.1"
  port = 2552
}

akka.cluster.roles = ["seed", "worker"]
```

**conf/cluster-worker1.conf**
```hocon
include "application.conf"

play.server.http.port = 9003

akka.remote.artery.canonical {
  hostname = "127.0.0.1"
  port = 2553
}

akka.cluster.roles = ["worker"]
```

## 4. 启动集群

### 4.1 使用启动脚本

项目提供了`start-cluster.sh`脚本：

```bash
# 启动单个节点
./start-cluster.sh seed1      # 启动种子节点1
./start-cluster.sh seed2      # 启动种子节点2
./start-cluster.sh worker1    # 启动工作节点1

# 启动完整集群
./start-cluster.sh all

# 停止所有节点
./start-cluster.sh stop
```

### 4.2 手动启动

使用sbt手动启动：

```bash
# 启动种子节点1
sbt -Dconfig.file=conf/cluster-seed1.conf run

# 在另一个终端启动种子节点2
sbt -Dconfig.file=conf/cluster-seed2.conf run

# 启动工作节点
sbt -Dconfig.file=conf/cluster-worker1.conf run
```

### 4.3 验证集群

查看日志输出：

```
[INFO] Member is Up: akka://PlayDemoActorSystem@127.0.0.1:2551
[INFO] Member is Up: akka://PlayDemoActorSystem@127.0.0.1:2552
[INFO] Member is Up: akka://PlayDemoActorSystem@127.0.0.1:2553
```

## 5. 集群事件监听

### 5.1 创建ClusterListenerActor

```java
public class ClusterListenerActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final Cluster cluster = Cluster.get(getContext().getSystem());

    @Override
    public void preStart() throws Exception {
        super.preStart();

        // 订阅集群事件
        cluster.subscribe(
            getSelf(),
            ClusterEvent.initialStateAsEvents(),
            ClusterEvent.MemberEvent.class,
            ClusterEvent.UnreachableMember.class
        );
    }

    @Override
    public void postStop() throws Exception {
        cluster.unsubscribe(getSelf());
        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(ClusterEvent.MemberUp.class, this::handleMemberUp)
            .match(ClusterEvent.MemberRemoved.class, this::handleMemberRemoved)
            .match(ClusterEvent.UnreachableMember.class, this::handleUnreachableMember)
            .build();
    }

    private void handleMemberUp(ClusterEvent.MemberUp event) {
        log.info("Member is Up: {}", event.member().address());
    }

    private void handleMemberRemoved(ClusterEvent.MemberRemoved event) {
        log.warning("Member is Removed: {}", event.member().address());
    }

    private void handleUnreachableMember(ClusterEvent.UnreachableMember event) {
        log.error("Member detected as unreachable: {}", event.member().address());
    }
}
```

### 5.2 订阅集群事件

在ActorSystemManager中创建监听器：

```java
ActorRef clusterListener = actorSystem.actorOf(
    ClusterListenerActor.props(),
    "clusterListener"
);
```

## 6. 集群路由

### 6.1 Cluster-aware Routers

使用集群感知路由器分发消息到集群中的Actor：

```hocon
akka.actor.deployment {
  /taskRouter {
    router = round-robin-group
    routees.paths = ["/user/taskProcessor"]
    cluster {
      enabled = on
      max-nr-of-instances-per-node = 3
      allow-local-routees = on
      use-roles = ["worker"]
    }
  }
}
```

使用路由器：

```java
ActorRef router = actorSystem.actorOf(
    FromConfig.getInstance().props(TaskProcessorActor.props()),
    "taskRouter"
);
```

### 6.2 Cluster-aware Pool

```java
int totalInstances = 100;
int maxInstancesPerNode = 3;

Props clusterRouterPoolProps = new ClusterRouterPool(
    new RoundRobinPool(totalInstances),
    new ClusterRouterPoolSettings(
        totalInstances,
        maxInstancesPerNode,
        true,
        Collections.singleton("worker")
    )
).props(TaskWorkerActor.props());

ActorRef router = actorSystem.actorOf(
    clusterRouterPoolProps,
    "workerRouter"
);
```

## 7. 集群分片（Cluster Sharding）

### 7.1 配置Sharding

```hocon
akka.cluster.sharding {
  # 分片数量
  number-of-shards = 100

  # 记住实体
  remember-entities = true

  # 状态存储
  state-store-mode = ddata
}
```

### 7.2 使用Sharding

```java
// 定义消息提取器
ShardRegion.MessageExtractor messageExtractor = new ShardRegion.MessageExtractor() {
    @Override
    public String entityId(Object message) {
        if (message instanceof TaskMessage) {
            return ((TaskMessage) message).getTaskId();
        }
        return null;
    }

    @Override
    public Object entityMessage(Object message) {
        return message;
    }

    @Override
    public String shardId(Object message) {
        int numberOfShards = 100;
        return String.valueOf(Math.abs(entityId(message).hashCode() % numberOfShards));
    }
};

// 启动Shard Region
ActorRef shardRegion = ClusterSharding.get(actorSystem).start(
    "TaskProcessor",
    TaskProcessorActor.props(),
    ClusterShardingSettings.create(actorSystem),
    messageExtractor
);
```

## 8. 故障处理和高可用

### 8.1 故障检测

Akka Cluster使用Phi Accrual故障检测器：

```hocon
akka.cluster {
  failure-detector {
    # 故障检测器的阈值
    threshold = 8.0

    # 最大检测间隔
    max-sample-size = 1000

    # 检测间隔
    acceptable-heartbeat-pause = 3s
  }
}
```

### 8.2 Split Brain Resolver

防止脑裂：

```scala
// 添加依赖
"com.lightbend.akka" %% "akka-split-brain-resolver" % "1.1.25"
```

配置：

```hocon
akka.cluster {
  downing-provider-class = "akka.cluster.sbr.SplitBrainResolverProvider"

  split-brain-resolver {
    # 策略: keep-majority, static-quorum, keep-oldest, down-all
    active-strategy = keep-majority

    # keep-majority策略的角色
    keep-majority {
      role = ""
    }
  }
}
```

### 8.3 节点优雅退出

```java
public void leaveCluster(ActorSystem system) {
    Cluster cluster = Cluster.get(system);

    // 优雅离开集群
    cluster.leave(cluster.selfAddress());

    // 等待节点状态变为Removed
    cluster.registerOnMemberRemoved(() -> {
        // 执行清理工作
        system.terminate();
    });
}
```

## 9. 集群管理和监控

### 9.1 Akka Management HTTP

访问集群管理API：

```bash
# 查看集群成员
curl http://localhost:8558/cluster/members

# 查看集群状态
curl http://localhost:8558/cluster/members/self
```

### 9.2 集群指标

```java
public class ClusterMetricsActor extends AbstractActor {
    private final Cluster cluster = Cluster.get(getContext().getSystem());

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(GetMetrics.class, this::handleGetMetrics)
            .build();
    }

    private void handleGetMetrics(GetMetrics request) {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("members", cluster.state().getMembers().size());
        metrics.put("unreachable", cluster.state().getUnreachable().size());
        metrics.put("leader", cluster.state().getLeader());
        metrics.put("isLeader", cluster.selfAddress().equals(cluster.state().getLeader()));

        getSender().tell(metrics, getSelf());
    }
}
```

## 10. 测试集群功能

### 10.1 节点发现测试

1. 启动种子节点1和2
2. 观察日志确认节点加入
3. 启动工作节点
4. 验证工作节点成功加入

### 10.2 故障转移测试

1. 启动完整集群
2. 停止一个工作节点
3. 观察其他节点检测到故障
4. 验证任务被重新分配到其他节点

### 10.3 负载均衡测试

1. 启动多个工作节点
2. 提交批量任务
3. 观察任务分配情况
4. 验证负载均衡效果

## 11. 生产环境部署

### 11.1 配置建议

```hocon
akka {
  cluster {
    # 使用配置的种子节点
    seed-nodes = [
      "akka://MySystem@node1.example.com:2551",
      "akka://MySystem@node2.example.com:2551",
      "akka://MySystem@node3.example.com:2551"
    ]

    # 关闭自动down
    auto-down-unreachable-after = off

    # 最小节点数
    min-nr-of-members = 3

    # 使用Split Brain Resolver
    downing-provider-class = "akka.cluster.sbr.SplitBrainResolverProvider"
  }

  remote {
    artery {
      # 使用实际的hostname
      canonical.hostname = ${?HOSTNAME}
      canonical.port = ${?AKKA_PORT}
    }
  }
}
```

### 11.2 Docker部署

```dockerfile
FROM openjdk:8-jre-alpine

WORKDIR /app

COPY target/universal/stage .

ENV HOSTNAME=localhost
ENV AKKA_PORT=2551
ENV HTTP_PORT=9000

EXPOSE ${HTTP_PORT}
EXPOSE ${AKKA_PORT}

CMD ["bin/play-demo",
     "-Dconfig.file=conf/cluster-prod.conf",
     "-Dhttp.port=${HTTP_PORT}"]
```

### 11.3 Kubernetes部署

使用StatefulSet部署集群：

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: play-demo-cluster
spec:
  serviceName: play-demo
  replicas: 3
  selector:
    matchLabels:
      app: play-demo
  template:
    metadata:
      labels:
        app: play-demo
    spec:
      containers:
      - name: play-demo
        image: play-demo:latest
        ports:
        - containerPort: 9000
          name: http
        - containerPort: 2551
          name: akka
        env:
        - name: HOSTNAME
          valueFrom:
            fieldRef:
              fieldPath: status.podIP
        - name: AKKA_PORT
          value: "2551"
```

## 12. 故障排查

### 12.1 常见问题

**问题1：节点无法加入集群**

解决：
1. 检查种子节点配置
2. 确认网络连通性
3. 检查端口是否被占用
4. 查看日志中的错误信息

**问题2：节点频繁被标记为unreachable**

解决：
1. 调整failure-detector阈值
2. 检查网络延迟
3. 增加心跳超时时间

**问题3：脑裂（Split Brain）**

解决：
1. 配置Split Brain Resolver
2. 确保有足够的种子节点
3. 使用keep-majority策略

### 12.2 日志分析

启用详细日志：

```hocon
akka {
  loglevel = "DEBUG"
  log-config-on-start = on

  actor {
    debug {
      receive = on
      autoreceive = on
      lifecycle = on
    }
  }

  cluster {
    log-info = on
  }
}
```

## 13. 性能优化

### 13.1 序列化优化

使用高效的序列化框架：

```hocon
akka.actor {
  serialization-bindings {
    "java.io.Serializable" = jackson-json
    "com.google.protobuf.Message" = proto
  }
}
```

### 13.2 调整分片数量

```hocon
akka.cluster.sharding {
  number-of-shards = 300  # 根据集群规模调整
}
```

### 13.3 网络配置

```hocon
akka.remote.artery {
  advanced {
    # 增大缓冲区
    outbound-message-queue-size = 3072
    maximum-frame-size = 1024 KiB

    # 压缩
    compression {
      actor-refs = on
      manifests = on
    }
  }
}
```

## 14. 学习资源

- [Akka Cluster官方文档](https://doc.akka.io/docs/akka/current/typed/cluster.html)
- [Akka Management文档](https://doc.akka.io/docs/akka-management/current/)
- [Lightbend Production Guide](https://developer.lightbend.com/)
- [Reactive Architecture](https://www.reactivemanifesto.org/)
