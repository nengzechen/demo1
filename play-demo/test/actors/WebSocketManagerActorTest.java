package actors;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;

/**
 * WebSocketManagerActor单元测试
 * 测试客户端注册、广播和连接管理
 */
public class WebSocketManagerActorTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("WebSocketManagerActorTestSystem");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testRegisterClient() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            // When - register a client
            WebSocketManagerActor.Register register =
                new WebSocketManagerActor.Register("client-1");
            managerRef.tell(register, getRef());

            // Then - client should be registered
            // Verify by getting connection count
            managerRef.tell(new WebSocketManagerActor.GetConnectionCount(), getRef());
            Integer count = expectMsgClass(Duration.ofSeconds(2), Integer.class);
            assertEquals(Integer.valueOf(1), count);
        }};
    }

    @Test
    public void testUnregisterClient() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            // Register a client
            WebSocketManagerActor.Register register =
                new WebSocketManagerActor.Register("client-2");
            managerRef.tell(register, getRef());

            // When - unregister the client
            WebSocketManagerActor.Unregister unregister =
                new WebSocketManagerActor.Unregister("client-2");
            managerRef.tell(unregister, getRef());

            // Then - client should be removed
            managerRef.tell(new WebSocketManagerActor.GetConnectionCount(), getRef());
            Integer count = expectMsgClass(Duration.ofSeconds(2), Integer.class);
            assertEquals(Integer.valueOf(0), count);
        }};
    }

    @Test
    public void testBroadcastMessage() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            // Register this test actor as a client
            WebSocketManagerActor.Register register =
                new WebSocketManagerActor.Register("broadcast-client");
            managerRef.tell(register, getRef());

            // When - send broadcast message
            WebSocketManagerActor.Broadcast broadcast =
                new WebSocketManagerActor.Broadcast("Hello everyone!");
            managerRef.tell(broadcast, getRef());

            // Then - should receive the broadcast
            String message = expectMsgClass(Duration.ofSeconds(2), String.class);
            assertEquals("Hello everyone!", message);
        }};
    }

    @Test
    public void testSendToClient() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            // Register this test actor as a client
            WebSocketManagerActor.Register register =
                new WebSocketManagerActor.Register("target-client");
            managerRef.tell(register, getRef());

            // When - send message to specific client
            WebSocketManagerActor.SendToClient sendToClient =
                new WebSocketManagerActor.SendToClient("target-client", "Private message");
            managerRef.tell(sendToClient, getRef());

            // Then - should receive the message
            String message = expectMsgClass(Duration.ofSeconds(2), String.class);
            assertEquals("Private message", message);
        }};
    }

    @Test
    public void testSendToNonExistentClient() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            // When - send message to non-existent client
            WebSocketManagerActor.SendToClient sendToClient =
                new WebSocketManagerActor.SendToClient("non-existent", "Lost message");
            managerRef.tell(sendToClient, getRef());

            // Then - no message should be received
            expectNoMessage(Duration.ofSeconds(1));
        }};
    }

    @Test
    public void testGetConnectionCount() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            // Initially, no connections
            managerRef.tell(new WebSocketManagerActor.GetConnectionCount(), getRef());
            Integer count0 = expectMsgClass(Duration.ofSeconds(2), Integer.class);
            assertEquals(Integer.valueOf(0), count0);

            // Register some clients
            for (int i = 1; i <= 3; i++) {
                WebSocketManagerActor.Register register =
                    new WebSocketManagerActor.Register("client-" + i);
                managerRef.tell(register, getRef());
            }

            // When - get connection count
            managerRef.tell(new WebSocketManagerActor.GetConnectionCount(), getRef());

            // Then - should return 3
            Integer count = expectMsgClass(Duration.ofSeconds(2), Integer.class);
            assertEquals(Integer.valueOf(3), count);
        }};
    }

    @Test
    public void testMultipleRegistrations() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            // When - register multiple clients
            for (int i = 1; i <= 5; i++) {
                WebSocketManagerActor.Register register =
                    new WebSocketManagerActor.Register("multi-client-" + i);
                managerRef.tell(register, getRef());
            }

            // Then - all should be registered
            managerRef.tell(new WebSocketManagerActor.GetConnectionCount(), getRef());
            Integer count = expectMsgClass(Duration.ofSeconds(2), Integer.class);
            assertEquals(Integer.valueOf(5), count);
        }};
    }

    @Test
    public void testBroadcastToMultipleClients() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            // Create multiple test probes to simulate clients
            TestKit client1 = new TestKit(system);
            TestKit client2 = new TestKit(system);
            TestKit client3 = new TestKit(system);

            // Register clients
            managerRef.tell(new WebSocketManagerActor.Register("client-1"), client1.getRef());
            managerRef.tell(new WebSocketManagerActor.Register("client-2"), client2.getRef());
            managerRef.tell(new WebSocketManagerActor.Register("client-3"), client3.getRef());

            // When - broadcast message
            WebSocketManagerActor.Broadcast broadcast =
                new WebSocketManagerActor.Broadcast("Broadcast to all");
            managerRef.tell(broadcast, getRef());

            // Then - all clients should receive
            client1.expectMsgEquals(Duration.ofSeconds(2), "Broadcast to all");
            client2.expectMsgEquals(Duration.ofSeconds(2), "Broadcast to all");
            client3.expectMsgEquals(Duration.ofSeconds(2), "Broadcast to all");
        }};
    }

    @Test
    public void testRegisterSameClientTwice() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            TestKit client1 = new TestKit(system);
            TestKit client2 = new TestKit(system);

            // When - register same client ID twice with different actors
            managerRef.tell(new WebSocketManagerActor.Register("duplicate"), client1.getRef());
            managerRef.tell(new WebSocketManagerActor.Register("duplicate"), client2.getRef());

            // Then - should have 1 connection (second overwrites first)
            managerRef.tell(new WebSocketManagerActor.GetConnectionCount(), getRef());
            Integer count = expectMsgClass(Duration.ofSeconds(2), Integer.class);
            assertEquals(Integer.valueOf(1), count);
        }};
    }

    @Test
    public void testUnregisterNonExistentClient() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            // When - unregister non-existent client
            WebSocketManagerActor.Unregister unregister =
                new WebSocketManagerActor.Unregister("non-existent");
            managerRef.tell(unregister, getRef());

            // Then - should handle gracefully
            managerRef.tell(new WebSocketManagerActor.GetConnectionCount(), getRef());
            Integer count = expectMsgClass(Duration.ofSeconds(2), Integer.class);
            assertEquals(Integer.valueOf(0), count);
        }};
    }

    @Test
    public void testUnknownMessage() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(WebSocketManagerActor.props());

            // When - send unknown message
            String unknownMessage = "UNKNOWN";
            managerRef.tell(unknownMessage, getRef());

            // Then - actor should not crash
            expectNoMessage(Duration.ofSeconds(1));

            // Verify manager still works
            managerRef.tell(new WebSocketManagerActor.GetConnectionCount(), getRef());
            Integer count = expectMsgClass(Duration.ofSeconds(2), Integer.class);
            assertEquals(Integer.valueOf(0), count);
        }};
    }
}
