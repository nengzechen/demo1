package actors;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;

/**
 * TaskProcessorActor单元测试
 * 测试ProcessTask消息处理、QueryTaskStatus查询和并发处理
 */
public class TaskProcessorActorTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("TaskProcessorActorTestSystem");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testProcessTaskMessage() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            TaskMessage.ProcessTask task = new TaskMessage.ProcessTask(
                "task-001",
                "Test Task",
                "test data"
            );

            // When
            processorRef.tell(task, getRef());

            // Then - should receive immediate acceptance
            TaskMessage.TaskStatusResponse response =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("task-001", response.getTaskId());
            assertEquals("ACCEPTED", response.getStatus());
            assertTrue(response.getMessage().contains("accepted"));
        }};
    }

    @Test
    public void testQueryTaskStatus_Processing() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            TaskMessage.ProcessTask task = new TaskMessage.ProcessTask(
                "query-task-001",
                "Query Test",
                "data"
            );

            // When - submit task and immediately query
            processorRef.tell(task, getRef());
            expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);

            // Query status while processing
            processorRef.tell(new TaskMessage.QueryTaskStatus("query-task-001"), getRef());

            // Then
            TaskMessage.TaskStatusResponse statusResponse =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("query-task-001", statusResponse.getTaskId());
            assertEquals("PROCESSING", statusResponse.getStatus());
        }};
    }

    @Test
    public void testQueryTaskStatus_Completed() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            TaskMessage.ProcessTask task = new TaskMessage.ProcessTask(
                "complete-task-001",
                "Completion Test",
                "data"
            );

            // When - submit task and wait for completion
            processorRef.tell(task, getRef());
            expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);

            // Wait for task to complete
            try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // Query status after completion
            processorRef.tell(new TaskMessage.QueryTaskStatus("complete-task-001"), getRef());

            // Then
            TaskMessage.TaskStatusResponse statusResponse =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("complete-task-001", statusResponse.getTaskId());
            assertEquals("COMPLETED", statusResponse.getStatus());
        }};
    }

    @Test
    public void testQueryTaskStatus_NotFound() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            // When - query non-existent task
            processorRef.tell(new TaskMessage.QueryTaskStatus("non-existent"), getRef());

            // Then
            TaskMessage.TaskStatusResponse response =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("non-existent", response.getTaskId());
            assertEquals("NOT_FOUND", response.getStatus());
        }};
    }

    @Test
    public void testConcurrentTaskProcessing() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            // When - submit multiple tasks
            for (int i = 0; i < 5; i++) {
                TaskMessage.ProcessTask task = new TaskMessage.ProcessTask(
                    "concurrent-task-" + i,
                    "Task " + i,
                    "data " + i
                );
                processorRef.tell(task, getRef());
            }

            // Then - should receive 5 acceptance responses
            for (int i = 0; i < 5; i++) {
                TaskMessage.TaskStatusResponse response =
                    expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
                assertEquals("ACCEPTED", response.getStatus());
            }
        }};
    }

    @Test
    public void testTaskStateTransition() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            TaskMessage.ProcessTask task = new TaskMessage.ProcessTask(
                "transition-task",
                "Transition Test",
                "data"
            );

            // When - submit task
            processorRef.tell(task, getRef());
            expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);

            // Then - verify state transitions

            // State 1: PROCESSING (immediately after acceptance)
            processorRef.tell(new TaskMessage.QueryTaskStatus("transition-task"), getRef());
            TaskMessage.TaskStatusResponse response1 =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("PROCESSING", response1.getStatus());

            // State 2: COMPLETED (after waiting)
            try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            processorRef.tell(new TaskMessage.QueryTaskStatus("transition-task"), getRef());
            TaskMessage.TaskStatusResponse response2 =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("COMPLETED", response2.getStatus());
        }};
    }

    @Test
    public void testMultipleTasksStatusTracking() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            String[] taskIds = {"track-1", "track-2", "track-3"};

            // When - submit multiple tasks
            for (String taskId : taskIds) {
                TaskMessage.ProcessTask task = new TaskMessage.ProcessTask(
                    taskId,
                    "Task " + taskId,
                    "data"
                );
                processorRef.tell(task, getRef());
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            }

            // Then - each task should have its own status
            for (String taskId : taskIds) {
                processorRef.tell(new TaskMessage.QueryTaskStatus(taskId), getRef());
                TaskMessage.TaskStatusResponse response =
                    expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
                assertEquals(taskId, response.getTaskId());
                assertEquals("PROCESSING", response.getStatus());
            }
        }};
    }

    @Test
    public void testUnknownMessage() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            // When - send unknown message
            String unknownMessage = "UNKNOWN_MESSAGE";
            processorRef.tell(unknownMessage, getRef());

            // Then - actor should not crash, just log warning
            expectNoMessage(Duration.ofSeconds(1));

            // Verify actor still works
            TaskMessage.ProcessTask task = new TaskMessage.ProcessTask(
                "recovery-task",
                "Recovery Test",
                "data"
            );
            processorRef.tell(task, getRef());

            TaskMessage.TaskStatusResponse response =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("ACCEPTED", response.getStatus());
        }};
    }

    @Test
    public void testTaskMessageFields() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            String taskId = "field-test-task";
            String taskName = "Field Test Task Name";
            String taskData = "Important Task Data";

            TaskMessage.ProcessTask task = new TaskMessage.ProcessTask(
                taskId,
                taskName,
                taskData
            );

            // Verify message fields
            assertEquals(taskId, task.getTaskId());
            assertEquals(taskName, task.getTaskName());
            assertEquals(taskData, task.getTaskData());
            assertNotNull(task.getCreatedAt());

            // When
            processorRef.tell(task, getRef());

            // Then
            TaskMessage.TaskStatusResponse response =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals(taskId, response.getTaskId());
        }};
    }

    @Test
    public void testActorLifecycle() {
        new TestKit(system) {{
            // Given - create actor
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props(), "lifecycle-actor");

            // When - send task to verify it's working
            TaskMessage.ProcessTask task = new TaskMessage.ProcessTask(
                "lifecycle-task",
                "Lifecycle Test",
                "data"
            );
            processorRef.tell(task, getRef());
            expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);

            // Then - stop the actor
            system.stop(processorRef);

            // Wait for stop
            try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }};
    }

    @Test
    public void testSequentialTaskProcessing() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            // When - submit tasks sequentially
            TaskMessage.ProcessTask task1 = new TaskMessage.ProcessTask(
                "seq-task-1", "Task 1", "data 1"
            );
            processorRef.tell(task1, getRef());
            TaskMessage.TaskStatusResponse response1 =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("ACCEPTED", response1.getStatus());

            TaskMessage.ProcessTask task2 = new TaskMessage.ProcessTask(
                "seq-task-2", "Task 2", "data 2"
            );
            processorRef.tell(task2, getRef());
            TaskMessage.TaskStatusResponse response2 =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("ACCEPTED", response2.getStatus());

            // Then - both tasks should be tracked
            processorRef.tell(new TaskMessage.QueryTaskStatus("seq-task-1"), getRef());
            TaskMessage.TaskStatusResponse status1 =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertNotEquals("NOT_FOUND", status1.getStatus());

            processorRef.tell(new TaskMessage.QueryTaskStatus("seq-task-2"), getRef());
            TaskMessage.TaskStatusResponse status2 =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertNotEquals("NOT_FOUND", status2.getStatus());
        }};
    }

    @Test
    public void testTaskIdUniqueness() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef processorRef =
                system.actorOf(TaskProcessorActor.props());

            String taskId = "unique-task-id";

            // When - submit two tasks with same ID
            TaskMessage.ProcessTask task1 = new TaskMessage.ProcessTask(
                taskId, "First Task", "data 1"
            );
            TaskMessage.ProcessTask task2 = new TaskMessage.ProcessTask(
                taskId, "Second Task", "data 2"
            );

            processorRef.tell(task1, getRef());
            expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);

            processorRef.tell(task2, getRef());
            expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);

            // Then - last task should overwrite status
            processorRef.tell(new TaskMessage.QueryTaskStatus(taskId), getRef());
            TaskMessage.TaskStatusResponse response =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("PROCESSING", response.getStatus());
        }};
    }
}
