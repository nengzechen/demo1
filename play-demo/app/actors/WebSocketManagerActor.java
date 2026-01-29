package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket管理器Actor
 * 管理所有WebSocket连接，提供广播和消息推送功能
 */
public class WebSocketManagerActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    // 存储所有活跃的WebSocket连接
    private final Map<String, ActorRef> connections = new HashMap<>();

    public static Props props() {
        return Props.create(WebSocketManagerActor.class);
    }

    /**
     * 注册消息 - WebSocket连接注册
     */
    public static class Register {
        private final String clientId;

        public Register(String clientId) {
            this.clientId = clientId;
        }

        public String getClientId() {
            return clientId;
        }
    }

    /**
     * 取消注册消息
     */
    public static class Unregister {
        private final String clientId;

        public Unregister(String clientId) {
            this.clientId = clientId;
        }

        public String getClientId() {
            return clientId;
        }
    }

    /**
     * 广播消息
     */
    public static class Broadcast {
        private final String message;

        public Broadcast(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * 发送给特定客户端的消息
     */
    public static class SendToClient {
        private final String clientId;
        private final Object message;

        public SendToClient(String clientId, Object message) {
            this.clientId = clientId;
            this.message = message;
        }

        public String getClientId() {
            return clientId;
        }

        public Object getMessage() {
            return message;
        }
    }

    /**
     * 获取连接数
     */
    public static class GetConnectionCount {
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("WebSocketManagerActor started");
    }

    @Override
    public void postStop() throws Exception {
        log.info("WebSocketManagerActor stopped. Total connections managed: {}", connections.size());
        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Register.class, this::handleRegister)
                .match(Unregister.class, this::handleUnregister)
                .match(Broadcast.class, this::handleBroadcast)
                .match(SendToClient.class, this::handleSendToClient)
                .match(GetConnectionCount.class, this::handleGetConnectionCount)
                .match(Terminated.class, this::handleTerminated)
                .matchAny(o -> log.warning("Received unknown message: {}", o))
                .build();
    }

    /**
     * 处理注册
     */
    private void handleRegister(Register register) {
        String clientId = register.getClientId();
        ActorRef connection = getSender();

        connections.put(clientId, connection);
        getContext().watch(connection);  // 监控连接Actor

        log.info("WebSocket connection registered: {} (Total connections: {})",
                clientId, connections.size());
    }

    /**
     * 处理取消注册
     */
    private void handleUnregister(Unregister unregister) {
        String clientId = unregister.getClientId();
        ActorRef connection = connections.remove(clientId);

        if (connection != null) {
            getContext().unwatch(connection);
            log.info("WebSocket connection unregistered: {} (Remaining connections: {})",
                    clientId, connections.size());
        }
    }

    /**
     * 处理广播消息
     */
    private void handleBroadcast(Broadcast broadcast) {
        log.info("Broadcasting message to {} connections: {}",
                connections.size(), broadcast.getMessage());

        for (Map.Entry<String, ActorRef> entry : connections.entrySet()) {
            entry.getValue().tell(broadcast.getMessage(), getSelf());
        }
    }

    /**
     * 发送消息给特定客户端
     */
    private void handleSendToClient(SendToClient sendToClient) {
        String clientId = sendToClient.getClientId();
        ActorRef connection = connections.get(clientId);

        if (connection != null) {
            log.info("Sending message to client: {}", clientId);
            connection.tell(sendToClient.getMessage(), getSelf());
        } else {
            log.warning("Client not found: {}", clientId);
        }
    }

    /**
     * 获取连接数
     */
    private void handleGetConnectionCount(GetConnectionCount request) {
        getSender().tell(connections.size(), getSelf());
    }

    /**
     * 处理Actor终止
     */
    private void handleTerminated(Terminated terminated) {
        ActorRef terminatedActor = terminated.getActor();

        // 从连接映射中移除已终止的Actor
        connections.entrySet().removeIf(entry -> {
            if (entry.getValue().equals(terminatedActor)) {
                log.info("WebSocket connection terminated: {} (Remaining: {})",
                        entry.getKey(), connections.size() - 1);
                return true;
            }
            return false;
        });
    }
}
