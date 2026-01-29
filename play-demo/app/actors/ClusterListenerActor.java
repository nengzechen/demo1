package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * 集群监听Actor
 * 监听集群事件，如节点加入、节点离开、leader变更等
 */
public class ClusterListenerActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final Cluster cluster = Cluster.get(getContext().getSystem());

    public static Props props() {
        return Props.create(ClusterListenerActor.class);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();

        // 订阅集群事件
        cluster.subscribe(
                getSelf(),
                ClusterEvent.initialStateAsEvents(),
                ClusterEvent.MemberEvent.class,
                ClusterEvent.UnreachableMember.class,
                ClusterEvent.ReachableMember.class,
                ClusterEvent.LeaderChanged.class
        );

        log.info("ClusterListenerActor started and subscribed to cluster events");
        log.info("Current cluster address: {}", cluster.selfAddress());
        log.info("Current cluster roles: {}", cluster.selfRoles());
    }

    @Override
    public void postStop() throws Exception {
        // 取消订阅
        cluster.unsubscribe(getSelf());
        log.info("ClusterListenerActor stopped and unsubscribed from cluster events");
        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClusterEvent.MemberUp.class, this::handleMemberUp)
                .match(ClusterEvent.MemberJoined.class, this::handleMemberJoined)
                .match(ClusterEvent.MemberLeft.class, this::handleMemberLeft)
                .match(ClusterEvent.MemberExited.class, this::handleMemberExited)
                .match(ClusterEvent.MemberRemoved.class, this::handleMemberRemoved)
                .match(ClusterEvent.UnreachableMember.class, this::handleUnreachableMember)
                .match(ClusterEvent.ReachableMember.class, this::handleReachableMember)
                .match(ClusterEvent.LeaderChanged.class, this::handleLeaderChanged)
                .match(ClusterEvent.CurrentClusterState.class, this::handleCurrentClusterState)
                .matchAny(o -> log.warning("Received unknown cluster event: {}", o))
                .build();
    }

    /**
     * 处理成员加入事件 - 节点已完全加入集群
     */
    private void handleMemberUp(ClusterEvent.MemberUp event) {
        log.info("====== Member UP ======");
        log.info("Member is Up: {} (Roles: {})", event.member().address(), event.member().getRoles());
        log.info("======================");

        logClusterStats();
    }

    /**
     * 处理成员正在加入事件
     */
    private void handleMemberJoined(ClusterEvent.MemberJoined event) {
        log.info("Member is Joining: {} (Roles: {})",
                event.member().address(), event.member().getRoles());
    }

    /**
     * 处理成员离开事件
     */
    private void handleMemberLeft(ClusterEvent.MemberLeft event) {
        log.warning("Member is Leaving: {} (Roles: {})",
                event.member().address(), event.member().getRoles());
    }

    /**
     * 处理成员已退出事件
     */
    private void handleMemberExited(ClusterEvent.MemberExited event) {
        log.warning("Member Exited: {} (Roles: {})",
                event.member().address(), event.member().getRoles());
    }

    /**
     * 处理成员移除事件 - 节点已从集群中移除
     */
    private void handleMemberRemoved(ClusterEvent.MemberRemoved event) {
        log.warning("====== Member REMOVED ======");
        log.warning("Member is Removed: {} (Roles: {})",
                event.member().address(), event.member().getRoles());
        log.warning("Previous Status: {}", event.previousStatus());
        log.warning("===========================");

        logClusterStats();
    }

    /**
     * 处理成员不可达事件 - 故障检测
     */
    private void handleUnreachableMember(ClusterEvent.UnreachableMember event) {
        log.error("====== Member UNREACHABLE ======");
        log.error("Member detected as unreachable: {} (Roles: {})",
                event.member().address(), event.member().getRoles());
        log.error("===============================");
    }

    /**
     * 处理成员重新可达事件 - 故障恢复
     */
    private void handleReachableMember(ClusterEvent.ReachableMember event) {
        log.info("====== Member REACHABLE ======");
        log.info("Member is reachable again: {} (Roles: {})",
                event.member().address(), event.member().getRoles());
        log.info("=============================");
    }

    /**
     * 处理Leader变更事件
     */
    private void handleLeaderChanged(ClusterEvent.LeaderChanged event) {
        if (event.leader() != null) {
            log.info("====== Leader CHANGED ======");
            log.info("New Leader: {}", event.leader());
            log.info("===========================");
        } else {
            log.warning("====== Leader CHANGED ======");
            log.warning("Leader is now: NONE (no leader elected)");
            log.warning("===========================");
        }
    }

    /**
     * 处理当前集群状态快照
     */
    private void handleCurrentClusterState(ClusterEvent.CurrentClusterState state) {
        log.info("====== Current Cluster State ======");
        log.info("Leader: {}", state.getLeader());

        // 计算成员数量
        int memberCount = 0;
        for (akka.cluster.Member member : state.getMembers()) {
            memberCount++;
            log.info("  - {} (Status: {}, Roles: {})",
                    member.address(),
                    member.status(),
                    member.getRoles());
        }
        log.info("Total Members: {}", memberCount);

        // 检查不可达节点
        int unreachableCount = 0;
        for (akka.cluster.Member member : state.getUnreachable()) {
            unreachableCount++;
        }

        if (unreachableCount > 0) {
            log.warning("Unreachable members: {}", unreachableCount);
            for (akka.cluster.Member member : state.getUnreachable()) {
                log.warning("  - {} (Roles: {})",
                        member.address(),
                        member.getRoles());
            }
        }

        log.info("==================================");
    }

    /**
     * 记录集群统计信息
     */
    private void logClusterStats() {
        int memberCount = 0;
        for (akka.cluster.Member member : cluster.state().getMembers()) {
            memberCount++;
        }
        log.info("Current cluster members count: {}", memberCount);
        akka.actor.Address leader = cluster.state().getLeader();
        log.info("Current cluster leader: {}", leader);
        if (leader != null) {
            log.info("Is this node the leader: {}", leader.equals(cluster.selfUniqueAddress().address()));
        } else {
            log.info("No leader elected yet");
        }
    }
}
