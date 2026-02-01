package actors;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import models.Task;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;

/**
 * TaskWorkerActor单元测试
 * 使用Akka TestKit测试所有任务类型处理和Actor生命周期
 */
public class TaskWorkerActorTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("TaskWorkerActorTestSystem");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testProcessDataTask() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            Task task = new Task("data-task-001", "Data Processing Task",
                                "DATA_PROCESSING", "sample data", Task.TaskPriority.NORMAL);

            // When
            workerRef.tell(task, getRef());

            // Then
            Task response = expectMsgClass(Duration.ofSeconds(5), Task.class);
            assertEquals(Task.TaskStatus.COMPLETED, response.getStatus());
            assertNotNull(response.getResult());
            assertTrue(response.getResult().contains("Data processed"));
            assertTrue(response.getResult().contains("sample data"));
            assertNotNull(response.getStartedAt());
            assertNotNull(response.getCompletedAt());
        }};
    }

    @Test
    public void testProcessFileTask() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            Task task = new Task("file-task-001", "File Operation Task",
                                "FILE_OPERATION", "file.txt", Task.TaskPriority.HIGH);

            // When
            workerRef.tell(task, getRef());

            // Then
            Task response = expectMsgClass(Duration.ofSeconds(5), Task.class);
            assertEquals(Task.TaskStatus.COMPLETED, response.getStatus());
            assertNotNull(response.getResult());
            assertTrue(response.getResult().contains("File operation completed"));
            assertTrue(response.getResult().contains("file.txt"));
        }};
    }

    @Test
    public void testProcessApiTask() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            Task task = new Task("api-task-001", "API Call Task",
                                "API_CALL", "https://api.example.com", Task.TaskPriority.URGENT);

            // When
            workerRef.tell(task, getRef());

            // Then
            Task response = expectMsgClass(Duration.ofSeconds(5), Task.class);
            assertEquals(Task.TaskStatus.COMPLETED, response.getStatus());
            assertNotNull(response.getResult());
            assertTrue(response.getResult().contains("API call successful"));
            assertTrue(response.getResult().contains("https://api.example.com"));
        }};
    }

    @Test
    public void testProcessNotificationTask() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            Task task = new Task("notif-task-001", "Notification Task",
                                "NOTIFICATION", "user@example.com", Task.TaskPriority.NORMAL);

            // When
            workerRef.tell(task, getRef());

            // Then
            Task response = expectMsgClass(Duration.ofSeconds(5), Task.class);
            assertEquals(Task.TaskStatus.COMPLETED, response.getStatus());
            assertNotNull(response.getResult());
            assertTrue(response.getResult().contains("Notification sent"));
            assertTrue(response.getResult().contains("user@example.com"));
        }};
    }

    @Test
    public void testProcessReportTask() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            Task task = new Task("report-task-001", "Report Generation Task",
                                "REPORT_GENERATION", "monthly_report", Task.TaskPriority.LOW);

            // When
            workerRef.tell(task, getRef());

            // Then
            Task response = expectMsgClass(Duration.ofSeconds(6), Task.class);
            assertEquals(Task.TaskStatus.COMPLETED, response.getStatus());
            assertNotNull(response.getResult());
            assertTrue(response.getResult().contains("Report generated"));
            assertTrue(response.getResult().contains("monthly_report"));
        }};
    }

    @Test
    public void testProcessDefaultTask() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            Task task = new Task("default-task-001", "Unknown Type Task",
                                "UNKNOWN_TYPE", "data", Task.TaskPriority.NORMAL);

            // When
            workerRef.tell(task, getRef());

            // Then
            Task response = expectMsgClass(Duration.ofSeconds(5), Task.class);
            assertEquals(Task.TaskStatus.COMPLETED, response.getStatus());
            assertNotNull(response.getResult());
            assertTrue(response.getResult().contains("Task processed"));
            assertTrue(response.getResult().contains("Unknown Type Task"));
        }};
    }

    @Test
    public void testTaskStatusTransition() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            Task task = new Task("status-task-001", "Status Test",
                                "DATA_PROCESSING", "data", Task.TaskPriority.NORMAL);

            // Initial status
            assertEquals(Task.TaskStatus.PENDING, task.getStatus());

            // When
            workerRef.tell(task, getRef());

            // Then
            Task response = expectMsgClass(Duration.ofSeconds(5), Task.class);
            assertEquals(Task.TaskStatus.COMPLETED, response.getStatus());
        }};
    }

    @Test
    public void testTaskTimestamps() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            Task task = new Task("timestamp-task-001", "Timestamp Test",
                                "NOTIFICATION", "data", Task.TaskPriority.NORMAL);

            // When
            workerRef.tell(task, getRef());

            // Then
            Task response = expectMsgClass(Duration.ofSeconds(5), Task.class);
            assertNotNull(response.getCreatedAt());
            assertNotNull(response.getStartedAt());
            assertNotNull(response.getCompletedAt());
            assertTrue(response.getStartedAt().isAfter(response.getCreatedAt()) ||
                      response.getStartedAt().isEqual(response.getCreatedAt()));
            assertTrue(response.getCompletedAt().isAfter(response.getStartedAt()) ||
                      response.getCompletedAt().isEqual(response.getStartedAt()));
        }};
    }

    @Test
    public void testMultipleTasksSequentially() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            Task task1 = new Task("seq-task-001", "Task 1", "NOTIFICATION", "data1", Task.TaskPriority.NORMAL);
            Task task2 = new Task("seq-task-002", "Task 2", "API_CALL", "data2", Task.TaskPriority.HIGH);
            Task task3 = new Task("seq-task-003", "Task 3", "DATA_PROCESSING", "data3", Task.TaskPriority.LOW);

            // When
            workerRef.tell(task1, getRef());
            Task response1 = expectMsgClass(Duration.ofSeconds(5), Task.class);

            workerRef.tell(task2, getRef());
            Task response2 = expectMsgClass(Duration.ofSeconds(5), Task.class);

            workerRef.tell(task3, getRef());
            Task response3 = expectMsgClass(Duration.ofSeconds(5), Task.class);

            // Then
            assertEquals(Task.TaskStatus.COMPLETED, response1.getStatus());
            assertEquals(Task.TaskStatus.COMPLETED, response2.getStatus());
            assertEquals(Task.TaskStatus.COMPLETED, response3.getStatus());
            assertEquals("seq-task-001", response1.getTaskId());
            assertEquals("seq-task-002", response2.getTaskId());
            assertEquals("seq-task-003", response3.getTaskId());
        }};
    }

    @Test
    public void testTaskWithAllPriorities() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            Task.TaskPriority[] priorities = Task.TaskPriority.values();

            // When & Then - test all priorities
            for (Task.TaskPriority priority : priorities) {
                Task task = new Task("priority-task-" + priority, "Task",
                                    "NOTIFICATION", "data", priority);
                workerRef.tell(task, getRef());
                Task response = expectMsgClass(Duration.ofSeconds(5), Task.class);
                assertEquals(Task.TaskStatus.COMPLETED, response.getStatus());
                assertEquals(priority, response.getPriority());
            }
        }};
    }

    @Test
    public void testUnknownMessage() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            // When - send unknown message type
            String unknownMessage = "This is an unknown message";
            workerRef.tell(unknownMessage, getRef());

            // Then - actor should not crash, just log warning
            // No message should be sent back
            expectNoMessage(Duration.ofSeconds(1));
        }};
    }

    @Test
    public void testWorkerReusability() {
        new TestKit(system) {{
            // Given - same worker handles multiple tasks
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            // When - send different task types to same worker
            String[] taskTypes = {"DATA_PROCESSING", "FILE_OPERATION", "API_CALL",
                                 "NOTIFICATION", "REPORT_GENERATION"};

            for (int i = 0; i < taskTypes.length; i++) {
                Task task = new Task("reuse-task-" + i, "Task " + i,
                                    taskTypes[i], "data", Task.TaskPriority.NORMAL);
                workerRef.tell(task, getRef());

                Task response = expectMsgClass(Duration.ofSeconds(6), Task.class);
                assertEquals(Task.TaskStatus.COMPLETED, response.getStatus());
            }

            // Then - worker successfully processed all tasks
        }};
    }

    @Test
    public void testTaskIdPreservation() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            String uniqueTaskId = "unique-task-id-12345";
            Task task = new Task(uniqueTaskId, "Task", "DATA_PROCESSING", "data", Task.TaskPriority.NORMAL);

            // When
            workerRef.tell(task, getRef());

            // Then - task ID should be preserved
            Task response = expectMsgClass(Duration.ofSeconds(5), Task.class);
            assertEquals(uniqueTaskId, response.getTaskId());
        }};
    }

    @Test
    public void testTaskDataPreservation() {
        new TestKit(system) {{
            // Given
            final akka.actor.ActorRef workerRef =
                system.actorOf(TaskWorkerActor.props());

            String taskData = "important-data-12345";
            Task task = new Task("task-001", "Task", "DATA_PROCESSING", taskData, Task.TaskPriority.NORMAL);

            // When
            workerRef.tell(task, getRef());

            // Then - task data should be in result
            Task response = expectMsgClass(Duration.ofSeconds(5), Task.class);
            assertEquals(taskData, response.getTaskData());
            assertTrue(response.getResult().contains(taskData));
        }};
    }
}
