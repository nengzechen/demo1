package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Actor系统管理器
 * 负责创建和管理ActorSystem，以及初始化各种Actor
 */
@Singleton
public class ActorSystemManager {

    private final ActorSystem actorSystem;
    private final ActorRef taskProcessorActor;
    private final ActorRef scheduledTaskActor;
    private final ActorRef taskManagerActor;
    private final ActorRef webSocketManagerActor;
    private final ActorRef clusterListenerActor;

    @Inject
    public ActorSystemManager(ActorSystem actorSystem) {
        // 使用Play Framework注入的ActorSystem
        this.actorSystem = actorSystem;

        // 创建TaskProcessorActor
        this.taskProcessorActor = actorSystem.actorOf(
                TaskProcessorActor.props(),
                "taskProcessorActor"
        );

        // 创建ScheduledTaskActor
        this.scheduledTaskActor = actorSystem.actorOf(
                ScheduledTaskActor.props(),
                "scheduledTaskActor"
        );

        // 创建TaskManagerActor（带有5个Worker）
        this.taskManagerActor = actorSystem.actorOf(
                TaskManagerActor.props(5),
                "taskManagerActor"
        );

        // 创建WebSocketManagerActor
        this.webSocketManagerActor = actorSystem.actorOf(
                WebSocketManagerActor.props(),
                "webSocketManagerActor"
        );

        // 创建ClusterListenerActor
        this.clusterListenerActor = actorSystem.actorOf(
                ClusterListenerActor.props(),
                "clusterListenerActor"
        );

        System.out.println("ActorSystem initialized successfully");
        System.out.println("TaskProcessorActor: " + taskProcessorActor.path());
        System.out.println("ScheduledTaskActor: " + scheduledTaskActor.path());
        System.out.println("TaskManagerActor: " + taskManagerActor.path());
        System.out.println("WebSocketManagerActor: " + webSocketManagerActor.path());
        System.out.println("ClusterListenerActor: " + clusterListenerActor.path());
    }

    /**
     * 获取ActorSystem
     */
    public ActorSystem getActorSystem() {
        return actorSystem;
    }

    /**
     * 获取TaskProcessorActor引用
     */
    public ActorRef getTaskProcessorActor() {
        return taskProcessorActor;
    }

    /**
     * 获取ScheduledTaskActor引用
     */
    public ActorRef getScheduledTaskActor() {
        return scheduledTaskActor;
    }

    /**
     * 获取TaskManagerActor引用
     */
    public ActorRef getTaskManagerActor() {
        return taskManagerActor;
    }

    /**
     * 获取WebSocketManagerActor引用
     */
    public ActorRef getWebSocketManagerActor() {
        return webSocketManagerActor;
    }

    /**
     * 获取ClusterListenerActor引用
     */
    public ActorRef getClusterListenerActor() {
        return clusterListenerActor;
    }
}
