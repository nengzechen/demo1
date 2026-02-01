package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Task;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.*;

/**
 * TaskSystemController单元测试
 * 测试所有任务类型、优先级、批量提交和状态查询
 */
public class TaskSystemControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        Map<String, Object> config = new HashMap<>();
        config.put("play.application.name", "play-demo");
        config.put("akka.actor.warn-about-java-serializer-usage", "off");

        return new GuiceApplicationBuilder()
                .configure(config)
                .build();
    }

    @Test
    public void testSubmitTask_DataProcessing() throws Exception {
        // Given
        ObjectNode body = Json.newObject();
        body.put("taskName", "Data Processing Task");
        body.put("taskType", "DATA_PROCESSING");
        body.put("taskData", "sample data");
        body.put("priority", "NORMAL");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks")
                .bodyJson(body);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("taskId"));
        assertTrue(jsonNode.has("status"));
        assertTrue(jsonNode.has("taskName"));
        assertTrue(jsonNode.has("taskType"));
        assertTrue(jsonNode.has("priority"));

        assertEquals("ACCEPTED", jsonNode.get("status").asText());
        assertEquals("Data Processing Task", jsonNode.get("taskName").asText());
        assertEquals("DATA_PROCESSING", jsonNode.get("taskType").asText());
        assertEquals("NORMAL", jsonNode.get("priority").asText());
    }

    @Test
    public void testSubmitTask_AllTaskTypes() throws Exception {
        // Given
        String[] taskTypes = {"DATA_PROCESSING", "FILE_OPERATION", "API_CALL",
                             "NOTIFICATION", "REPORT_GENERATION"};

        for (String taskType : taskTypes) {
            ObjectNode body = Json.newObject();
            body.put("taskName", taskType + " Task");
            body.put("taskType", taskType);
            body.put("taskData", "test data");
            body.put("priority", "NORMAL");

            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(POST)
                    .uri("/api/tasks")
                    .bodyJson(body);

            // When
            Result result = route(app, request);

            // Then
            assertEquals(OK, result.status());

            JsonNode jsonNode = Json.parse(contentAsString(result));
            assertEquals("ACCEPTED", jsonNode.get("status").asText());
            assertEquals(taskType, jsonNode.get("taskType").asText());
        }
    }

    @Test
    public void testSubmitTask_AllPriorities() throws Exception {
        // Given
        Task.TaskPriority[] priorities = Task.TaskPriority.values();

        for (Task.TaskPriority priority : priorities) {
            ObjectNode body = Json.newObject();
            body.put("taskName", priority.name() + " Task");
            body.put("taskType", "NOTIFICATION");
            body.put("taskData", "test");
            body.put("priority", priority.name());

            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(POST)
                    .uri("/api/tasks")
                    .bodyJson(body);

            // When
            Result result = route(app, request);

            // Then
            assertEquals(OK, result.status());

            JsonNode jsonNode = Json.parse(contentAsString(result));
            assertEquals("ACCEPTED", jsonNode.get("status").asText());
            assertEquals(priority.name(), jsonNode.get("priority").asText());
        }
    }

    @Test
    public void testSubmitTask_MissingBody() throws Exception {
        // Given
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks");

        // When
        Result result = route(app, request);

        // Then
        assertEquals(BAD_REQUEST, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("error"));
        assertTrue(jsonNode.get("error").asText().contains("Missing request body"));
    }

    @Test
    public void testSubmitTask_DefaultValues() throws Exception {
        // Given - empty body to trigger defaults
        ObjectNode body = Json.newObject();

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks")
                .bodyJson(body);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertEquals("ACCEPTED", jsonNode.get("status").asText());
        assertEquals("Unnamed Task", jsonNode.get("taskName").asText());
        assertEquals("DEFAULT", jsonNode.get("taskType").asText());
        assertEquals("NORMAL", jsonNode.get("priority").asText());
    }

    @Test
    public void testSubmitTask_InvalidPriority() throws Exception {
        // Given - invalid priority should default to NORMAL
        ObjectNode body = Json.newObject();
        body.put("taskName", "Invalid Priority Task");
        body.put("taskType", "DATA_PROCESSING");
        body.put("taskData", "data");
        body.put("priority", "INVALID_PRIORITY");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks")
                .bodyJson(body);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertEquals("NORMAL", jsonNode.get("priority").asText());
    }

    @Test
    public void testGetTaskStatus() throws Exception {
        // Given - submit a task first
        ObjectNode body = Json.newObject();
        body.put("taskName", "Status Query Task");
        body.put("taskType", "NOTIFICATION");
        body.put("taskData", "test");
        body.put("priority", "HIGH");

        Http.RequestBuilder submitRequest = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks")
                .bodyJson(body);

        Result submitResult = route(app, submitRequest);
        JsonNode submitJson = Json.parse(contentAsString(submitResult));
        String taskId = submitJson.get("taskId").asText();

        // When
        Http.RequestBuilder getRequest = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/tasks/" + taskId);

        Result getResult = route(app, getRequest);

        // Then
        assertEquals(OK, getResult.status());

        JsonNode jsonNode = Json.parse(contentAsString(getResult));
        assertEquals(taskId, jsonNode.get("taskId").asText());
        assertTrue(jsonNode.has("status"));
        assertTrue(jsonNode.has("message"));
    }

    @Test
    public void testSubmitBatchTasks_Success() throws Exception {
        // Given
        ObjectNode requestBody = Json.newObject();
        ArrayNode tasks = Json.newArray();

        for (int i = 0; i < 5; i++) {
            ObjectNode task = Json.newObject();
            task.put("taskName", "Batch Task " + i);
            task.put("taskType", "DATA_PROCESSING");
            task.put("taskData", "data " + i);
            task.put("priority", "NORMAL");
            tasks.add(task);
        }

        requestBody.set("tasks", tasks);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks/batch")
                .bodyJson(requestBody);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("totalTasks"));
        assertTrue(jsonNode.has("tasks"));

        assertEquals(5, jsonNode.get("totalTasks").asInt());

        ArrayNode resultTasks = (ArrayNode) jsonNode.get("tasks");
        assertEquals(5, resultTasks.size());

        for (JsonNode task : resultTasks) {
            assertTrue(task.has("taskId"));
            assertTrue(task.has("taskName"));
            assertTrue(task.has("status"));
            assertEquals("SUBMITTED", task.get("status").asText());
        }
    }

    @Test
    public void testSubmitBatchTasks_Empty() throws Exception {
        // Given
        ObjectNode requestBody = Json.newObject();
        ArrayNode tasks = Json.newArray();
        requestBody.set("tasks", tasks);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks/batch")
                .bodyJson(requestBody);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertEquals(0, jsonNode.get("totalTasks").asInt());
    }

    @Test
    public void testSubmitBatchTasks_MissingTasksArray() throws Exception {
        // Given
        ObjectNode requestBody = Json.newObject();
        // No "tasks" field

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks/batch")
                .bodyJson(requestBody);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(BAD_REQUEST, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("error"));
        assertTrue(jsonNode.get("error").asText().contains("Missing tasks array"));
    }

    @Test
    public void testSubmitBatchTasks_MixedPriorities() throws Exception {
        // Given
        ObjectNode requestBody = Json.newObject();
        ArrayNode tasks = Json.newArray();

        String[] priorities = {"LOW", "NORMAL", "HIGH", "URGENT"};
        for (int i = 0; i < priorities.length; i++) {
            ObjectNode task = Json.newObject();
            task.put("taskName", "Task " + i);
            task.put("taskType", "NOTIFICATION");
            task.put("taskData", "data");
            task.put("priority", priorities[i]);
            tasks.add(task);
        }

        requestBody.set("tasks", tasks);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks/batch")
                .bodyJson(requestBody);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertEquals(priorities.length, jsonNode.get("totalTasks").asInt());
    }

    @Test
    public void testGetStats() throws Exception {
        // Given - submit some tasks first
        for (int i = 0; i < 3; i++) {
            ObjectNode body = Json.newObject();
            body.put("taskName", "Stats Task " + i);
            body.put("taskType", "NOTIFICATION");
            body.put("taskData", "data");
            body.put("priority", "NORMAL");

            Http.RequestBuilder submitRequest = new Http.RequestBuilder()
                    .method(POST)
                    .uri("/api/tasks")
                    .bodyJson(body);

            route(app, submitRequest);
        }

        // When
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/tasks/stats");

        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("workerCount"));
        assertTrue(jsonNode.has("totalReceived"));
        assertTrue(jsonNode.has("totalCompleted"));
        assertTrue(jsonNode.has("totalFailed"));
        assertTrue(jsonNode.has("pendingQueueSize"));
        assertTrue(jsonNode.has("totalTrackedTasks"));

        assertTrue(jsonNode.get("totalReceived").asInt() >= 3);
    }

    @Test
    public void testGetTaskTypes() throws Exception {
        // Given
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/tasks/types");

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("taskTypes"));

        ArrayNode taskTypes = (ArrayNode) jsonNode.get("taskTypes");
        assertEquals(5, taskTypes.size());

        assertTrue(containsString(taskTypes, "DATA_PROCESSING"));
        assertTrue(containsString(taskTypes, "FILE_OPERATION"));
        assertTrue(containsString(taskTypes, "API_CALL"));
        assertTrue(containsString(taskTypes, "NOTIFICATION"));
        assertTrue(containsString(taskTypes, "REPORT_GENERATION"));
    }

    @Test
    public void testGetPriorities() throws Exception {
        // Given
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/tasks/priorities");

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("priorities"));

        ArrayNode priorities = (ArrayNode) jsonNode.get("priorities");
        assertEquals(4, priorities.size());

        // Verify structure
        for (JsonNode priority : priorities) {
            assertTrue(priority.has("name"));
            assertTrue(priority.has("value"));
        }

        // Verify specific priorities
        assertTrue(containsPriority(priorities, "LOW", 1));
        assertTrue(containsPriority(priorities, "NORMAL", 2));
        assertTrue(containsPriority(priorities, "HIGH", 3));
        assertTrue(containsPriority(priorities, "URGENT", 4));
    }

    @Test
    public void testTaskIdUniqueness() throws Exception {
        // Given
        ObjectNode body = Json.newObject();
        body.put("taskName", "Uniqueness Test");
        body.put("taskType", "DATA_PROCESSING");
        body.put("taskData", "test");
        body.put("priority", "NORMAL");

        // When - submit same task twice
        Http.RequestBuilder request1 = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks")
                .bodyJson(body);
        Result result1 = route(app, request1);
        JsonNode json1 = Json.parse(contentAsString(result1));
        String taskId1 = json1.get("taskId").asText();

        Http.RequestBuilder request2 = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks")
                .bodyJson(body);
        Result result2 = route(app, request2);
        JsonNode json2 = Json.parse(contentAsString(result2));
        String taskId2 = json2.get("taskId").asText();

        // Then - task IDs should be different
        assertNotEquals(taskId1, taskId2);
    }

    @Test
    public void testContentTypes() throws Exception {
        // Test submit task
        ObjectNode body = Json.newObject();
        body.put("taskName", "Test");
        body.put("taskType", "NOTIFICATION");

        Http.RequestBuilder submitRequest = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/tasks")
                .bodyJson(body);
        Result submitResult = route(app, submitRequest);
        assertEquals("application/json", submitResult.contentType().orElse(""));

        // Test get types
        Http.RequestBuilder typesRequest = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/tasks/types");
        Result typesResult = route(app, typesRequest);
        assertEquals("application/json", typesResult.contentType().orElse(""));

        // Test get priorities
        Http.RequestBuilder prioritiesRequest = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/tasks/priorities");
        Result prioritiesResult = route(app, prioritiesRequest);
        assertEquals("application/json", prioritiesResult.contentType().orElse(""));
    }

    @Test
    public void testSubmitTask_CaseSensitivePriority() throws Exception {
        // Given - test different case variations
        String[] priorityCases = {"high", "HIGH", "High", "HiGh"};

        for (String priorityCase : priorityCases) {
            ObjectNode body = Json.newObject();
            body.put("taskName", "Case Test");
            body.put("taskType", "NOTIFICATION");
            body.put("taskData", "test");
            body.put("priority", priorityCase);

            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(POST)
                    .uri("/api/tasks")
                    .bodyJson(body);

            // When
            Result result = route(app, request);

            // Then - should normalize to uppercase
            assertEquals(OK, result.status());

            JsonNode jsonNode = Json.parse(contentAsString(result));
            assertEquals("HIGH", jsonNode.get("priority").asText());
        }
    }

    // Helper methods
    private boolean containsString(ArrayNode array, String value) {
        for (JsonNode node : array) {
            if (node.asText().equals(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsPriority(ArrayNode array, String name, int value) {
        for (JsonNode node : array) {
            if (node.get("name").asText().equals(name) &&
                node.get("value").asInt() == value) {
                return true;
            }
        }
        return false;
    }
}
