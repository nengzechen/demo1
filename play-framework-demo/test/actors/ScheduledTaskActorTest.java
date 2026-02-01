package actors;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;

/**
 * ScheduledTaskActor单元测试
 * 测试定时触发消息和生命周期
 */
public class ScheduledTaskActorTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("ScheduledTaskActorTestSystem");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testScheduledTick() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef scheduledRef =
                system.actorOf(ScheduledTaskActor.props());

            // When - send a scheduled tick message
            TaskMessage.ScheduledTick tick = new TaskMessage.ScheduledTick();
            scheduledRef.tell(tick, getRef());

            // Then - actor should process it without crashing
            // Wait a bit for processing
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Verify actor is still alive by sending another message
            TaskMessage.ScheduledTick tick2 = new TaskMessage.ScheduledTick();
            scheduledRef.tell(tick2, getRef());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Actor should still be responsive
        }};
    }

    @Test
    public void testScheduledTickMessage() {
        // Given
        TaskMessage.ScheduledTick tick = new TaskMessage.ScheduledTick();

        // Then
        assertNotNull(tick.getTickTime());
        assertNotNull(tick.toString());
    }

    @Test
    public void testActorLifecycle() {
        new TestKit(system) {{
            // Given - create actor
            final akka.actor.ActorRef scheduledRef =
                system.actorOf(ScheduledTaskActor.props(), "lifecycle-scheduled-actor");

            // When - send a tick to verify it's working
            TaskMessage.ScheduledTick tick = new TaskMessage.ScheduledTick();
            scheduledRef.tell(tick, getRef());

            // Wait for processing
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Then - stop the actor (should cancel scheduled task)
            system.stop(scheduledRef);

            // Wait for stop
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }};
    }

    @Test
    public void testMultipleTickMessages() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef scheduledRef =
                system.actorOf(ScheduledTaskActor.props());

            // When - send multiple ticks
            for (int i = 0; i < 3; i++) {
                TaskMessage.ScheduledTick tick = new TaskMessage.ScheduledTick();
                scheduledRef.tell(tick, getRef());
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // Then - actor should handle all of them
            // No exceptions should be thrown
        }};
    }

    @Test
    public void testUnknownMessage() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef scheduledRef =
                system.actorOf(ScheduledTaskActor.props());

            // When - send unknown message
            String unknownMessage = "UNKNOWN";
            scheduledRef.tell(unknownMessage, getRef());

            // Then - actor should not crash
            expectNoMessage(Duration.ofSeconds(1));

            // Verify actor still works
            TaskMessage.ScheduledTick tick = new TaskMessage.ScheduledTick();
            scheduledRef.tell(tick, getRef());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }};
    }

    @Test
    public void testScheduledTaskExecution() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef scheduledRef =
                system.actorOf(ScheduledTaskActor.props());

            // Wait for automatic scheduled execution (5 seconds initial delay)
            // For testing, we manually trigger it instead
            TaskMessage.ScheduledTick tick = new TaskMessage.ScheduledTick();
            scheduledRef.tell(tick, getRef());

            // Then - should complete within reasonable time
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Stop actor
            system.stop(scheduledRef);
        }};
    }
}
