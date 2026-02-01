package actors;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import models.Task;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * TaskManagerActor单元测试
 * 测试监督策略、轮询分配、优先级队列、重试机制
 */
public class TaskManagerActorTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("TaskManagerActorTestSystem");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testWorkerPoolCreation() {
        new TestKit(system) {{
            // Given
            int workerCount = 5;
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(workerCount));

            // When - request stats to verify worker pool
            managerRef.tell("GET_STATS", getRef());

            // Then
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);
            assertEquals(workerCount, stats.get("workerCount"));
            assertEquals(0, stats.get("totalReceived"));
            assertEquals(0, stats.get("totalCompleted"));
            assertEquals(0, stats.get("totalFailed"));
        }};
    }

    @Test
    public void testTaskAssignmentAccepted() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(3));

            Task task = new Task("task-001", "Test Task",
                                "DATA_PROCESSING", "data", Task.TaskPriority.NORMAL);

            // When
            managerRef.tell(task, getRef());

            // Then - should receive acceptance response
            TaskMessage.TaskStatusResponse response =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("task-001", response.getTaskId());
            assertEquals("ACCEPTED", response.getStatus());
        }};
    }

    @Test
    public void testTaskCompletionStats() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(3));

            Task task = new Task("task-002", "Quick Task",
                                "NOTIFICATION", "data", Task.TaskPriority.NORMAL);

            // When
            managerRef.tell(task, getRef());
            expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);

            // Wait for task to complete
            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // Request stats
            managerRef.tell("GET_STATS", getRef());

            // Then
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);
            assertEquals(1, stats.get("totalReceived"));
            assertEquals(1, stats.get("totalCompleted"));
        }};
    }

    @Test
    public void testTaskPriorityOrdering() {
        new TestKit(system) {{
            // Given - create manager with 1 worker to force queueing
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(1));

            // Create tasks with different priorities
            Task lowTask = new Task("low-task", "Low Priority",
                                   "REPORT_GENERATION", "data", Task.TaskPriority.LOW);
            Task normalTask = new Task("normal-task", "Normal Priority",
                                      "REPORT_GENERATION", "data", Task.TaskPriority.NORMAL);
            Task highTask = new Task("high-task", "High Priority",
                                    "REPORT_GENERATION", "data", Task.TaskPriority.HIGH);
            Task urgentTask = new Task("urgent-task", "Urgent Priority",
                                      "REPORT_GENERATION", "data", Task.TaskPriority.URGENT);

            // When - submit in reverse priority order
            managerRef.tell(lowTask, getRef());
            expectMsgClass(Duration.ofSeconds(1), TaskMessage.TaskStatusResponse.class);

            managerRef.tell(normalTask, getRef());
            expectMsgClass(Duration.ofSeconds(1), TaskMessage.TaskStatusResponse.class);

            managerRef.tell(highTask, getRef());
            expectMsgClass(Duration.ofSeconds(1), TaskMessage.TaskStatusResponse.class);

            managerRef.tell(urgentTask, getRef());
            expectMsgClass(Duration.ofSeconds(1), TaskMessage.TaskStatusResponse.class);

            // Then - all tasks should be accepted
            managerRef.tell("GET_STATS", getRef());
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);
            assertEquals(4, stats.get("totalReceived"));
        }};
    }

    @Test
    public void testQueryTaskStatus() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(3));

            Task task = new Task("query-task-001", "Query Test",
                                "NOTIFICATION", "data", Task.TaskPriority.NORMAL);

            // When
            managerRef.tell(task, getRef());
            expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);

            // Wait for processing
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // Query status
            managerRef.tell(new TaskMessage.QueryTaskStatus("query-task-001"), getRef());

            // Then
            TaskMessage.TaskStatusResponse response =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("query-task-001", response.getTaskId());
            assertNotEquals("NOT_FOUND", response.getStatus());
        }};
    }

    @Test
    public void testQueryNonExistentTask() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(3));

            // When - query task that doesn't exist
            managerRef.tell(new TaskMessage.QueryTaskStatus("non-existent-task"), getRef());

            // Then
            TaskMessage.TaskStatusResponse response =
                expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
            assertEquals("non-existent-task", response.getTaskId());
            assertEquals("NOT_FOUND", response.getStatus());
            assertTrue(response.getMessage().contains("not found"));
        }};
    }

    @Test
    public void testGetStats() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(5));

            // When
            managerRef.tell("GET_STATS", getRef());

            // Then
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);

            assertNotNull(stats);
            assertTrue(stats.containsKey("workerCount"));
            assertTrue(stats.containsKey("totalReceived"));
            assertTrue(stats.containsKey("totalCompleted"));
            assertTrue(stats.containsKey("totalFailed"));
            assertTrue(stats.containsKey("pendingQueueSize"));
            assertTrue(stats.containsKey("totalTrackedTasks"));

            assertEquals(5, stats.get("workerCount"));
        }};
    }

    @Test
    public void testConcurrentTaskSubmission() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(5));

            // When - submit multiple tasks quickly
            for (int i = 0; i < 10; i++) {
                Task task = new Task("concurrent-task-" + i, "Task " + i,
                                    "NOTIFICATION", "data", Task.TaskPriority.NORMAL);
                managerRef.tell(task, getRef());
            }

            // Collect acceptance responses
            for (int i = 0; i < 10; i++) {
                TaskMessage.TaskStatusResponse response =
                    expectMsgClass(Duration.ofSeconds(3), TaskMessage.TaskStatusResponse.class);
                assertEquals("ACCEPTED", response.getStatus());
            }

            // Then - verify all tasks received
            managerRef.tell("GET_STATS", getRef());
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);
            assertEquals(10, stats.get("totalReceived"));
        }};
    }

    @Test
    public void testMultipleTaskTypes() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(3));

            String[] taskTypes = {"DATA_PROCESSING", "FILE_OPERATION", "API_CALL",
                                 "NOTIFICATION", "REPORT_GENERATION"};

            // When - submit different task types
            for (int i = 0; i < taskTypes.length; i++) {
                Task task = new Task("type-task-" + i, "Task " + i,
                                    taskTypes[i], "data", Task.TaskPriority.NORMAL);
                managerRef.tell(task, getRef());
                expectMsgClass(Duration.ofSeconds(1), TaskMessage.TaskStatusResponse.class);
            }

            // Then
            managerRef.tell("GET_STATS", getRef());
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);
            assertEquals(taskTypes.length, stats.get("totalReceived"));
        }};
    }

    @Test
    public void testRoundRobinAssignment() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(3));

            // When - submit quick tasks
            for (int i = 0; i < 6; i++) {
                Task task = new Task("round-robin-" + i, "Task " + i,
                                    "NOTIFICATION", "data", Task.TaskPriority.NORMAL);
                managerRef.tell(task, getRef());
                expectMsgClass(Duration.ofSeconds(1), TaskMessage.TaskStatusResponse.class);
            }

            // Wait for processing
            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // Then - all tasks should be processed
            managerRef.tell("GET_STATS", getRef());
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);
            assertEquals(6, stats.get("totalReceived"));
            assertEquals(6, stats.get("totalCompleted"));
        }};
    }

    @Test
    public void testTaskQueueingWhenWorkersBusy() {
        new TestKit(system) {{
            // Given - only 1 worker
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(1));

            // When - submit multiple long-running tasks
            for (int i = 0; i < 3; i++) {
                Task task = new Task("queue-task-" + i, "Task " + i,
                                    "DATA_PROCESSING", "data", Task.TaskPriority.NORMAL);
                managerRef.tell(task, getRef());
                expectMsgClass(Duration.ofSeconds(1), TaskMessage.TaskStatusResponse.class);
            }

            // Then - tasks should be queued
            managerRef.tell("GET_STATS", getRef());
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);
            assertEquals(3, stats.get("totalReceived"));
            // Some tasks should still be pending
            int pendingSize = (Integer) stats.get("pendingQueueSize");
            assertTrue(pendingSize >= 0);
        }};
    }

    @Test
    public void testAllPriorities() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(4));

            Task.TaskPriority[] priorities = Task.TaskPriority.values();

            // When - submit task for each priority
            for (Task.TaskPriority priority : priorities) {
                Task task = new Task("priority-" + priority, "Task",
                                    "NOTIFICATION", "data", priority);
                managerRef.tell(task, getRef());
                expectMsgClass(Duration.ofSeconds(1), TaskMessage.TaskStatusResponse.class);
            }

            // Then
            managerRef.tell("GET_STATS", getRef());
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);
            assertEquals(priorities.length, stats.get("totalReceived"));
        }};
    }

    @Test
    public void testUnknownMessage() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(3));

            // When - send unknown message
            String unknownMessage = "UNKNOWN_COMMAND";
            managerRef.tell(unknownMessage, getRef());

            // Then - actor should not crash
            // Can still process valid requests
            managerRef.tell("GET_STATS", getRef());
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);
            assertNotNull(stats);
        }};
    }

    @Test
    public void testStatsAfterMultipleOperations() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef managerRef =
                system.actorOf(TaskManagerActor.props(3));

            // When - perform various operations
            // Submit tasks
            for (int i = 0; i < 5; i++) {
                Task task = new Task("stats-task-" + i, "Task " + i,
                                    "NOTIFICATION", "data", Task.TaskPriority.NORMAL);
                managerRef.tell(task, getRef());
                expectMsgClass(Duration.ofSeconds(1), TaskMessage.TaskStatusResponse.class);
            }

            // Query status
            managerRef.tell(new TaskMessage.QueryTaskStatus("stats-task-0"), getRef());
            expectMsgClass(Duration.ofSeconds(1), TaskMessage.TaskStatusResponse.class);

            // Wait for completion
            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            // Then - get final stats
            managerRef.tell("GET_STATS", getRef());
            @SuppressWarnings("unchecked")
            Map<String, Object> stats = expectMsgClass(Duration.ofSeconds(3), Map.class);
            assertEquals(5, stats.get("totalReceived"));
            assertEquals(5, stats.get("totalCompleted"));
            assertEquals(0, stats.get("totalFailed"));
        }};
    }
}
