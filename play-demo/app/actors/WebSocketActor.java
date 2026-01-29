package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * WebSocket连接Actor
 * 每个WebSocket连接对应一个Actor实例
 */
public class WebSocketActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ActorRef out;
    private final String clientId;
    private LocalDateTime connectedAt;
    private int messageCount = 0;

    public WebSocketActor(ActorRef out, String clientId) {
        this.out = out;
        this.clientId = clientId;
    }

    public static Props props(ActorRef out, String clientId) {
        return Props.create(WebSocketActor.class, out, clientId);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        connectedAt = LocalDateTime.now();
        log.info("WebSocket connection established for client: {}", clientId);

        // 发送欢迎消息
        ObjectNode welcome = Json.newObject();
        welcome.put("type", "SYSTEM");
        welcome.put("action", "CONNECTED");
        welcome.put("message", "Welcome to WebSocket service!");
        welcome.put("clientId", clientId);
        welcome.put("timestamp", LocalDateTime.now().format(formatter));

        out.tell(welcome, getSelf());

        // 订阅任务完成通知（如果需要）
        getContext().getSystem().eventStream().subscribe(getSelf(), TaskMessage.TaskCompleted.class);
    }

    @Override
    public void postStop() throws Exception {
        log.info("WebSocket connection closed for client: {} (Messages: {}, Duration: {})",
                clientId, messageCount, java.time.Duration.between(connectedAt, LocalDateTime.now()));

        // 取消订阅
        getContext().getSystem().eventStream().unsubscribe(getSelf());

        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JsonNode.class, this::handleClientMessage)
                .match(TaskMessage.TaskCompleted.class, this::handleTaskCompleted)
                .match(String.class, this::handleBroadcastMessage)
                .matchAny(o -> log.warning("Received unknown message: {}", o))
                .build();
    }

    /**
     * 处理客户端发来的消息
     */
    private void handleClientMessage(JsonNode message) {
        messageCount++;
        log.info("Received message from client {}: {}", clientId, message);

        String messageType = message.path("type").asText("UNKNOWN");

        switch (messageType) {
            case "PING":
                handlePing();
                break;

            case "SUBSCRIBE":
                handleSubscribe(message);
                break;

            case "UNSUBSCRIBE":
                handleUnsubscribe(message);
                break;

            case "MESSAGE":
                handleChatMessage(message);
                break;

            default:
                handleUnknownMessage(message);
                break;
        }
    }

    /**
     * 处理心跳检测
     */
    private void handlePing() {
        ObjectNode pong = Json.newObject();
        pong.put("type", "PONG");
        pong.put("timestamp", LocalDateTime.now().format(formatter));
        pong.put("clientId", clientId);

        out.tell(pong, getSelf());
    }

    /**
     * 处理订阅请求
     */
    private void handleSubscribe(JsonNode message) {
        String topic = message.path("topic").asText("");
        log.info("Client {} subscribing to topic: {}", clientId, topic);

        ObjectNode response = Json.newObject();
        response.put("type", "SYSTEM");
        response.put("action", "SUBSCRIBED");
        response.put("topic", topic);
        response.put("message", "Successfully subscribed to topic: " + topic);
        response.put("timestamp", LocalDateTime.now().format(formatter));

        out.tell(response, getSelf());
    }

    /**
     * 处理取消订阅请求
     */
    private void handleUnsubscribe(JsonNode message) {
        String topic = message.path("topic").asText("");
        log.info("Client {} unsubscribing from topic: {}", clientId, topic);

        ObjectNode response = Json.newObject();
        response.put("type", "SYSTEM");
        response.put("action", "UNSUBSCRIBED");
        response.put("topic", topic);
        response.put("message", "Successfully unsubscribed from topic: " + topic);
        response.put("timestamp", LocalDateTime.now().format(formatter));

        out.tell(response, getSelf());
    }

    /**
     * 处理聊天消息
     */
    private void handleChatMessage(JsonNode message) {
        String content = message.path("content").asText("");
        log.info("Chat message from client {}: {}", clientId, content);

        ObjectNode response = Json.newObject();
        response.put("type", "MESSAGE");
        response.put("from", clientId);
        response.put("content", content);
        response.put("timestamp", LocalDateTime.now().format(formatter));

        out.tell(response, getSelf());
    }

    /**
     * 处理未知消息
     */
    private void handleUnknownMessage(JsonNode message) {
        log.warning("Unknown message type from client {}: {}", clientId, message);

        ObjectNode response = Json.newObject();
        response.put("type", "ERROR");
        response.put("message", "Unknown message type");
        response.put("timestamp", LocalDateTime.now().format(formatter));

        out.tell(response, getSelf());
    }

    /**
     * 处理任务完成通知
     */
    private void handleTaskCompleted(TaskMessage.TaskCompleted taskCompleted) {
        log.info("Notifying client {} of task completion: {}", clientId, taskCompleted.getTaskId());

        ObjectNode notification = Json.newObject();
        notification.put("type", "NOTIFICATION");
        notification.put("action", "TASK_COMPLETED");
        notification.put("taskId", taskCompleted.getTaskId());
        notification.put("success", taskCompleted.isSuccess());
        notification.put("result", taskCompleted.getResult());
        notification.put("timestamp", LocalDateTime.now().format(formatter));

        out.tell(notification, getSelf());
    }

    /**
     * 处理广播消息
     */
    private void handleBroadcastMessage(String message) {
        ObjectNode broadcast = Json.newObject();
        broadcast.put("type", "BROADCAST");
        broadcast.put("message", message);
        broadcast.put("timestamp", LocalDateTime.now().format(formatter));

        out.tell(broadcast, getSelf());
    }
}
