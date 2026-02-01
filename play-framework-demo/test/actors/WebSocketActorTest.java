package actors;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;

/**
 * WebSocketActor单元测试
 * 测试ping/pong、subscribe/unsubscribe和聊天消息
 */
public class WebSocketActorTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("WebSocketActorTestSystem");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testPingPong() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef wsActor =
                system.actorOf(WebSocketActor.props(getRef(), "test-client-1"));

            // First, receive the welcome message
            expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);

            // When - send PING
            ObjectNode ping = Json.newObject();
            ping.put("type", "PING");
            wsActor.tell(ping, getRef());

            // Then - should receive PONG
            ObjectNode response = expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);
            assertEquals("PONG", response.get("type").asText());
            assertTrue(response.has("timestamp"));
            assertEquals("test-client-1", response.get("clientId").asText());
        }};
    }

    @Test
    public void testSubscribe() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef wsActor =
                system.actorOf(WebSocketActor.props(getRef(), "test-client-2"));

            // Receive welcome message
            expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);

            // When - send SUBSCRIBE
            ObjectNode subscribe = Json.newObject();
            subscribe.put("type", "SUBSCRIBE");
            subscribe.put("topic", "tasks");
            wsActor.tell(subscribe, getRef());

            // Then - should receive subscription confirmation
            ObjectNode response = expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);
            assertEquals("SYSTEM", response.get("type").asText());
            assertEquals("SUBSCRIBED", response.get("action").asText());
            assertEquals("tasks", response.get("topic").asText());
            assertTrue(response.get("message").asText().contains("Successfully subscribed"));
        }};
    }

    @Test
    public void testUnsubscribe() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef wsActor =
                system.actorOf(WebSocketActor.props(getRef(), "test-client-3"));

            // Receive welcome message
            expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);

            // When - send UNSUBSCRIBE
            ObjectNode unsubscribe = Json.newObject();
            unsubscribe.put("type", "UNSUBSCRIBE");
            unsubscribe.put("topic", "tasks");
            wsActor.tell(unsubscribe, getRef());

            // Then - should receive unsubscription confirmation
            ObjectNode response = expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);
            assertEquals("SYSTEM", response.get("type").asText());
            assertEquals("UNSUBSCRIBED", response.get("action").asText());
            assertEquals("tasks", response.get("topic").asText());
            assertTrue(response.get("message").asText().contains("Successfully unsubscribed"));
        }};
    }

    @Test
    public void testChatMessage() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef wsActor =
                system.actorOf(WebSocketActor.props(getRef(), "test-client-4"));

            // Receive welcome message
            expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);

            // When - send chat MESSAGE
            ObjectNode message = Json.newObject();
            message.put("type", "MESSAGE");
            message.put("content", "Hello, World!");
            wsActor.tell(message, getRef());

            // Then - should receive message echo
            ObjectNode response = expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);
            assertEquals("MESSAGE", response.get("type").asText());
            assertEquals("test-client-4", response.get("from").asText());
            assertEquals("Hello, World!", response.get("content").asText());
            assertTrue(response.has("timestamp"));
        }};
    }

    @Test
    public void testUnknownMessageType() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef wsActor =
                system.actorOf(WebSocketActor.props(getRef(), "test-client-5"));

            // Receive welcome message
            expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);

            // When - send unknown message type
            ObjectNode unknown = Json.newObject();
            unknown.put("type", "UNKNOWN_TYPE");
            wsActor.tell(unknown, getRef());

            // Then - should receive error response
            ObjectNode response = expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);
            assertEquals("ERROR", response.get("type").asText());
            assertTrue(response.get("message").asText().contains("Unknown message type"));
        }};
    }

    @Test
    public void testWelcomeMessage() {
        new TestKit(system) {{
            // Given & When
            final akka.actor.ActorRef wsActor =
                system.actorOf(WebSocketActor.props(getRef(), "test-client-6"));

            // Then - should receive welcome message
            ObjectNode welcome = expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);
            assertEquals("SYSTEM", welcome.get("type").asText());
            assertEquals("CONNECTED", welcome.get("action").asText());
            assertEquals("test-client-6", welcome.get("clientId").asText());
            assertTrue(welcome.get("message").asText().contains("Welcome"));
            assertTrue(welcome.has("timestamp"));
        }};
    }

    @Test
    public void testBroadcastMessage() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef wsActor =
                system.actorOf(WebSocketActor.props(getRef(), "test-client-7"));

            // Receive welcome message
            expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);

            // When - send broadcast string
            String broadcast = "Server announcement";
            wsActor.tell(broadcast, getRef());

            // Then - should receive broadcast
            ObjectNode response = expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);
            assertEquals("BROADCAST", response.get("type").asText());
            assertEquals("Server announcement", response.get("message").asText());
            assertTrue(response.has("timestamp"));
        }};
    }

    @Test
    public void testTaskCompletedNotification() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef wsActor =
                system.actorOf(WebSocketActor.props(getRef(), "test-client-8"));

            // Receive welcome message
            expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);

            // When - send task completed notification
            TaskMessage.TaskCompleted taskCompleted =
                new TaskMessage.TaskCompleted("task-123", true, "Task completed successfully");
            wsActor.tell(taskCompleted, getRef());

            // Then - should receive notification
            ObjectNode response = expectMsgClass(Duration.ofSeconds(2), ObjectNode.class);
            assertEquals("NOTIFICATION", response.get("type").asText());
            assertEquals("TASK_COMPLETED", response.get("action").asText());
            assertEquals("task-123", response.get("taskId").asText());
            assertTrue(response.get("success").asBoolean());
            assertEquals("Task completed successfully", response.get("result").asText());
        }};
    }
}
