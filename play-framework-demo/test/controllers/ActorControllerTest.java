package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.*;

/**
 * ActorController单元测试
 * 使用WithApplication模式测试HTTP端点、请求验证和错误处理
 */
public class ActorControllerTest extends WithApplication {

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
    public void testSubmitTask_Success() throws Exception {
        // Given
        ObjectNode body = Json.newObject();
        body.put("taskName", "Test Task");
        body.put("taskData", "test data");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task")
                .bodyJson(body);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());
        assertEquals("application/json", result.contentType().orElse(""));

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("taskId"));
        assertTrue(jsonNode.has("status"));
        assertTrue(jsonNode.has("message"));

        assertEquals("ACCEPTED", jsonNode.get("status").asText());
    }

    @Test
    public void testSubmitTask_MissingBody() throws Exception {
        // Given
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task");

        // When
        Result result = route(app, request);

        // Then
        assertEquals(BAD_REQUEST, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("error"));
        assertTrue(jsonNode.get("error").asText().contains("Missing request body"));
    }

    @Test
    public void testSubmitTask_WithDefaultValues() throws Exception {
        // Given - empty JSON body
        ObjectNode body = Json.newObject();

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task")
                .bodyJson(body);

        // When
        Result result = route(app, request);

        // Then - should use defaults: taskName="default-task", taskData=""
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertEquals("ACCEPTED", jsonNode.get("status").asText());
    }

    @Test
    public void testSubmitTask_WithOnlyTaskName() throws Exception {
        // Given
        ObjectNode body = Json.newObject();
        body.put("taskName", "Only Name Task");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task")
                .bodyJson(body);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertEquals("ACCEPTED", jsonNode.get("status").asText());
        assertNotNull(jsonNode.get("taskId"));
    }

    @Test
    public void testSubmitTask_WithOnlyTaskData() throws Exception {
        // Given
        ObjectNode body = Json.newObject();
        body.put("taskData", "only data");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task")
                .bodyJson(body);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertEquals("ACCEPTED", jsonNode.get("status").asText());
    }

    @Test
    public void testGetTaskStatus_Processing() throws Exception {
        // Given - first submit a task
        ObjectNode body = Json.newObject();
        body.put("taskName", "Status Test Task");
        body.put("taskData", "test data");

        Http.RequestBuilder submitRequest = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task")
                .bodyJson(body);

        Result submitResult = route(app, submitRequest);
        JsonNode submitJson = Json.parse(contentAsString(submitResult));
        String taskId = submitJson.get("taskId").asText();

        // When - query the task status
        Http.RequestBuilder getRequest = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/actor/task/" + taskId);

        Result getResult = route(app, getRequest);

        // Then
        assertEquals(OK, getResult.status());

        JsonNode jsonNode = Json.parse(contentAsString(getResult));
        assertEquals(taskId, jsonNode.get("taskId").asText());
        assertTrue(jsonNode.has("status"));
        assertTrue(jsonNode.has("message"));
    }

    @Test
    public void testGetTaskStatus_NotFound() throws Exception {
        // Given - non-existent task ID
        String nonExistentTaskId = "non-existent-task-12345";

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/actor/task/" + nonExistentTaskId);

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertEquals(nonExistentTaskId, jsonNode.get("taskId").asText());
        assertEquals("NOT_FOUND", jsonNode.get("status").asText());
    }

    @Test
    public void testGetActorInfo() throws Exception {
        // Given
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/actor/info");

        // When
        Result result = route(app, request);

        // Then
        assertEquals(OK, result.status());
        assertEquals("application/json", result.contentType().orElse(""));

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("actorSystem"));
        assertTrue(jsonNode.has("taskProcessorActor"));
        assertTrue(jsonNode.has("scheduledTaskActor"));

        assertNotNull(jsonNode.get("actorSystem").asText());
        assertTrue(jsonNode.get("taskProcessorActor").asText().contains("taskProcessor"));
        assertTrue(jsonNode.get("scheduledTaskActor").asText().contains("scheduledTask"));
    }

    @Test
    public void testSubmitMultipleTasks() throws Exception {
        // Given
        int taskCount = 5;

        // When - submit multiple tasks
        for (int i = 0; i < taskCount; i++) {
            ObjectNode body = Json.newObject();
            body.put("taskName", "Batch Task " + i);
            body.put("taskData", "data " + i);

            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(POST)
                    .uri("/api/actor/task")
                    .bodyJson(body);

            Result result = route(app, request);

            // Then - each should succeed
            assertEquals(OK, result.status());

            JsonNode jsonNode = Json.parse(contentAsString(result));
            assertEquals("ACCEPTED", jsonNode.get("status").asText());
        }
    }

    @Test
    public void testTaskIdGeneration() throws Exception {
        // Given
        ObjectNode body = Json.newObject();
        body.put("taskName", "ID Generation Test");
        body.put("taskData", "test");

        // When - submit two tasks
        Http.RequestBuilder request1 = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task")
                .bodyJson(body);

        Result result1 = route(app, request1);
        JsonNode json1 = Json.parse(contentAsString(result1));
        String taskId1 = json1.get("taskId").asText();

        Http.RequestBuilder request2 = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task")
                .bodyJson(body);

        Result result2 = route(app, request2);
        JsonNode json2 = Json.parse(contentAsString(result2));
        String taskId2 = json2.get("taskId").asText();

        // Then - task IDs should be unique
        assertNotEquals(taskId1, taskId2);
        assertTrue(taskId1.length() > 0);
        assertTrue(taskId2.length() > 0);
    }

    @Test
    public void testSubmitAndQuery() throws Exception {
        // Given - submit a task
        ObjectNode body = Json.newObject();
        body.put("taskName", "Submit and Query Test");
        body.put("taskData", "important data");

        Http.RequestBuilder submitRequest = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task")
                .bodyJson(body);

        Result submitResult = route(app, submitRequest);
        JsonNode submitJson = Json.parse(contentAsString(submitResult));
        String taskId = submitJson.get("taskId").asText();

        // When - query immediately
        Http.RequestBuilder queryRequest = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/actor/task/" + taskId);

        Result queryResult = route(app, queryRequest);
        JsonNode queryJson = Json.parse(contentAsString(queryResult));

        // Then - task should be found
        assertEquals(taskId, queryJson.get("taskId").asText());
        assertNotEquals("NOT_FOUND", queryJson.get("status").asText());
    }

    @Test
    public void testResponseStructure() throws Exception {
        // Given
        ObjectNode body = Json.newObject();
        body.put("taskName", "Structure Test");
        body.put("taskData", "data");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task")
                .bodyJson(body);

        // When
        Result result = route(app, request);

        // Then - verify complete response structure
        JsonNode jsonNode = Json.parse(contentAsString(result));

        assertTrue(jsonNode.has("taskId"));
        assertTrue(jsonNode.has("status"));
        assertTrue(jsonNode.has("message"));

        assertNotNull(jsonNode.get("taskId").asText());
        assertEquals("ACCEPTED", jsonNode.get("status").asText());
        assertFalse(jsonNode.get("message").asText().isEmpty());
    }

    @Test
    public void testContentType() throws Exception {
        // Given
        ObjectNode body = Json.newObject();
        body.put("taskName", "Content Type Test");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/actor/task")
                .bodyJson(body);

        // When
        Result result = route(app, request);

        // Then
        assertEquals("application/json", result.contentType().orElse(""));
    }

    @Test
    public void testActorInfoContentType() throws Exception {
        // Given
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/actor/info");

        // When
        Result result = route(app, request);

        // Then
        assertEquals("application/json", result.contentType().orElse(""));
    }
}
