package models;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.time.LocalDateTime;

/**
 * TaskModel单元测试
 * 覆盖任务创建、字段验证、枚举值、默认值和序列化
 */
public class TaskModelTest {

    @Test
    public void testTaskCreationWithAllParameters() {
        // Given
        String taskId = "task-001";
        String taskName = "Test Task";
        String taskType = "DATA_PROCESSING";
        String taskData = "test data";
        Task.TaskPriority priority = Task.TaskPriority.HIGH;

        // When
        Task task = new Task(taskId, taskName, taskType, taskData, priority);

        // Then
        assertEquals(taskId, task.getTaskId());
        assertEquals(taskName, task.getTaskName());
        assertEquals(taskType, task.getTaskType());
        assertEquals(taskData, task.getTaskData());
        assertEquals(priority, task.getPriority());
        assertEquals(Task.TaskStatus.PENDING, task.getStatus());
        assertEquals(0, task.getRetryCount());
        assertEquals(3, task.getMaxRetries());
        assertNotNull(task.getCreatedAt());
        assertNull(task.getStartedAt());
        assertNull(task.getCompletedAt());
    }

    @Test
    public void testTaskCreationWithNullPriority() {
        // Given
        String taskId = "task-002";
        String taskName = "Test Task";
        String taskType = "API_CALL";
        String taskData = "test data";

        // When - passing null priority should default to NORMAL
        Task task = new Task(taskId, taskName, taskType, taskData, null);

        // Then
        assertEquals(Task.TaskPriority.NORMAL, task.getPriority());
    }

    @Test
    public void testTaskPriorityEnumValues() {
        // Test all priority values
        assertEquals(1, Task.TaskPriority.LOW.getValue());
        assertEquals(2, Task.TaskPriority.NORMAL.getValue());
        assertEquals(3, Task.TaskPriority.HIGH.getValue());
        assertEquals(4, Task.TaskPriority.URGENT.getValue());
    }

    @Test
    public void testTaskPriorityOrdering() {
        // Verify priorities can be compared
        assertTrue(Task.TaskPriority.URGENT.getValue() > Task.TaskPriority.HIGH.getValue());
        assertTrue(Task.TaskPriority.HIGH.getValue() > Task.TaskPriority.NORMAL.getValue());
        assertTrue(Task.TaskPriority.NORMAL.getValue() > Task.TaskPriority.LOW.getValue());
    }

    @Test
    public void testTaskStatusEnum() {
        // Test all status values exist
        Task.TaskStatus[] statuses = Task.TaskStatus.values();
        assertEquals(5, statuses.length);

        // Verify each status can be accessed
        assertNotNull(Task.TaskStatus.PENDING);
        assertNotNull(Task.TaskStatus.RUNNING);
        assertNotNull(Task.TaskStatus.COMPLETED);
        assertNotNull(Task.TaskStatus.FAILED);
        assertNotNull(Task.TaskStatus.CANCELLED);
    }

    @Test
    public void testDefaultValues() {
        // When
        Task task = new Task("id", "name", "type", "data", Task.TaskPriority.NORMAL);

        // Then - verify defaults
        assertEquals(Task.TaskStatus.PENDING, task.getStatus());
        assertEquals(Task.TaskPriority.NORMAL, task.getPriority());
        assertEquals(3, task.getMaxRetries());
        assertEquals(0, task.getRetryCount());
        assertNotNull(task.getCreatedAt());
    }

    @Test
    public void testNoArgConstructor() {
        // When
        Task task = new Task();

        // Then - all fields should be null or 0
        assertNull(task.getTaskId());
        assertNull(task.getTaskName());
        assertNull(task.getTaskType());
        assertNull(task.getTaskData());
        assertNull(task.getPriority());
        assertNull(task.getStatus());
        assertEquals(0, task.getRetryCount());
        assertEquals(0, task.getMaxRetries());
    }

    @Test
    public void testSettersAndGetters() {
        // Given
        Task task = new Task();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusHours(1);
        LocalDateTime complete = now.plusHours(2);

        // When
        task.setTaskId("task-003");
        task.setTaskName("Updated Task");
        task.setTaskType("NOTIFICATION");
        task.setTaskData("updated data");
        task.setPriority(Task.TaskPriority.URGENT);
        task.setStatus(Task.TaskStatus.RUNNING);
        task.setCreatedAt(now);
        task.setStartedAt(later);
        task.setCompletedAt(complete);
        task.setResult("success result");
        task.setErrorMessage("test error");
        task.setRetryCount(2);
        task.setMaxRetries(5);

        // Then
        assertEquals("task-003", task.getTaskId());
        assertEquals("Updated Task", task.getTaskName());
        assertEquals("NOTIFICATION", task.getTaskType());
        assertEquals("updated data", task.getTaskData());
        assertEquals(Task.TaskPriority.URGENT, task.getPriority());
        assertEquals(Task.TaskStatus.RUNNING, task.getStatus());
        assertEquals(now, task.getCreatedAt());
        assertEquals(later, task.getStartedAt());
        assertEquals(complete, task.getCompletedAt());
        assertEquals("success result", task.getResult());
        assertEquals("test error", task.getErrorMessage());
        assertEquals(2, task.getRetryCount());
        assertEquals(5, task.getMaxRetries());
    }

    @Test
    public void testToString() {
        // Given
        Task task = new Task("task-004", "Test", "FILE_OPERATION", "data", Task.TaskPriority.LOW);

        // When
        String result = task.toString();

        // Then
        assertTrue(result.contains("task-004"));
        assertTrue(result.contains("Test"));
        assertTrue(result.contains("FILE_OPERATION"));
        assertTrue(result.contains("LOW"));
        assertTrue(result.contains("PENDING"));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        // Given
        Task original = new Task("task-005", "Serializable Task", "REPORT_GENERATION",
                                 "report data", Task.TaskPriority.HIGH);
        original.setStatus(Task.TaskStatus.COMPLETED);
        original.setResult("Report generated");

        // When - serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();

        // Then - deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Task deserialized = (Task) ois.readObject();
        ois.close();

        // Verify
        assertEquals(original.getTaskId(), deserialized.getTaskId());
        assertEquals(original.getTaskName(), deserialized.getTaskName());
        assertEquals(original.getTaskType(), deserialized.getTaskType());
        assertEquals(original.getTaskData(), deserialized.getTaskData());
        assertEquals(original.getPriority(), deserialized.getPriority());
        assertEquals(original.getStatus(), deserialized.getStatus());
        assertEquals(original.getResult(), deserialized.getResult());
    }

    @Test
    public void testStatusTransition() {
        // Given
        Task task = new Task("task-006", "Status Test", "DATA_PROCESSING", "data", Task.TaskPriority.NORMAL);

        // When & Then - simulate task lifecycle
        assertEquals(Task.TaskStatus.PENDING, task.getStatus());

        task.setStatus(Task.TaskStatus.RUNNING);
        assertEquals(Task.TaskStatus.RUNNING, task.getStatus());

        task.setStatus(Task.TaskStatus.COMPLETED);
        assertEquals(Task.TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    public void testRetryMechanism() {
        // Given
        Task task = new Task("task-007", "Retry Test", "API_CALL", "data", Task.TaskPriority.NORMAL);

        // Then - initial state
        assertEquals(0, task.getRetryCount());
        assertEquals(3, task.getMaxRetries());

        // When - simulate retries
        task.setRetryCount(1);
        assertTrue(task.getRetryCount() < task.getMaxRetries());

        task.setRetryCount(2);
        assertTrue(task.getRetryCount() < task.getMaxRetries());

        task.setRetryCount(3);
        assertFalse(task.getRetryCount() < task.getMaxRetries());
    }

    @Test
    public void testTaskWithDifferentTypes() {
        // Test creating tasks with different types
        String[] taskTypes = {
            "DATA_PROCESSING",
            "FILE_OPERATION",
            "API_CALL",
            "NOTIFICATION",
            "REPORT_GENERATION",
            "CUSTOM_TYPE"
        };

        for (String type : taskTypes) {
            Task task = new Task("id-" + type, "name", type, "data", Task.TaskPriority.NORMAL);
            assertEquals(type, task.getTaskType());
        }
    }

    @Test
    public void testTaskWithAllPriorities() {
        // Test creating tasks with all priority levels
        Task.TaskPriority[] priorities = Task.TaskPriority.values();

        for (Task.TaskPriority priority : priorities) {
            Task task = new Task("id", "name", "type", "data", priority);
            assertEquals(priority, task.getPriority());
        }
    }

    @Test
    public void testCreatedAtTimestamp() {
        // Given
        LocalDateTime before = LocalDateTime.now();

        // When
        Task task = new Task("task-008", "Timestamp Test", "type", "data", Task.TaskPriority.NORMAL);

        // Then
        LocalDateTime after = LocalDateTime.now();
        assertNotNull(task.getCreatedAt());
        assertFalse(task.getCreatedAt().isBefore(before.minusSeconds(1)));
        assertFalse(task.getCreatedAt().isAfter(after.plusSeconds(1)));
    }

    @Test
    public void testTaskCompletionFlow() {
        // Given
        Task task = new Task("task-009", "Completion Test", "DATA_PROCESSING",
                            "data", Task.TaskPriority.HIGH);
        LocalDateTime started = LocalDateTime.now();
        LocalDateTime completed = started.plusMinutes(5);

        // When - simulate complete flow
        task.setStatus(Task.TaskStatus.RUNNING);
        task.setStartedAt(started);

        task.setStatus(Task.TaskStatus.COMPLETED);
        task.setCompletedAt(completed);
        task.setResult("Processing completed successfully");

        // Then
        assertEquals(Task.TaskStatus.COMPLETED, task.getStatus());
        assertEquals(started, task.getStartedAt());
        assertEquals(completed, task.getCompletedAt());
        assertEquals("Processing completed successfully", task.getResult());
        assertNull(task.getErrorMessage());
    }

    @Test
    public void testTaskFailureFlow() {
        // Given
        Task task = new Task("task-010", "Failure Test", "API_CALL",
                            "data", Task.TaskPriority.NORMAL);

        // When - simulate failure
        task.setStatus(Task.TaskStatus.RUNNING);
        task.setStatus(Task.TaskStatus.FAILED);
        task.setErrorMessage("Connection timeout");
        task.setRetryCount(task.getRetryCount() + 1);

        // Then
        assertEquals(Task.TaskStatus.FAILED, task.getStatus());
        assertEquals("Connection timeout", task.getErrorMessage());
        assertEquals(1, task.getRetryCount());
    }
}
