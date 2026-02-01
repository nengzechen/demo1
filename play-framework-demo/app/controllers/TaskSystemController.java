package controllers;

import actors.ActorSystemManager;
import actors.TaskMessage;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Task;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.concurrent.duration.Duration;
import akka.util.Timeout;
import scala.compat.java8.FutureConverters;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 实时任务处理系统控制器
 * 提供任务提交、查询、统计等功能
 */
public class TaskSystemController extends Controller {

    private final ActorSystemManager actorSystemManager;

    @Inject
    public TaskSystemController(ActorSystemManager actorSystemManager) {
        this.actorSystemManager = actorSystemManager;
    }

    /**
     * 提交新任务
     * POST /api/tasks
     *
     * Request body:
     * {
     *   "taskName": "My Task",
     *   "taskType": "DATA_PROCESSING",
     *   "taskData": "some data",
     *   "priority": "HIGH"
     * }
     */
    public CompletableFuture<Result> submitTask(Http.Request request) {
        JsonNode json = request.body().asJson();

        if (json == null) {
            return CompletableFuture.completedFuture(
                    badRequest(createErrorResponse("Missing request body"))
            );
        }

        // 解析请求参数
        final String taskName = json.path("taskName").asText("Unnamed Task");
        final String taskType = json.path("taskType").asText("DEFAULT");
        String taskData = json.path("taskData").asText("");
        String priorityStr = json.path("priority").asText("NORMAL");

        // 解析优先级
        Task.TaskPriority priorityTemp;
        try {
            priorityTemp = Task.TaskPriority.valueOf(priorityStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            priorityTemp = Task.TaskPriority.NORMAL;
        }
        final Task.TaskPriority priority = priorityTemp;

        // 创建任务
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(taskId, taskName, taskType, taskData, priority);

        // 发送任务给TaskManagerActor
        ActorRef taskManager = actorSystemManager.getTaskManagerActor();

        return FutureConverters.toJava(Patterns
                .ask(taskManager, task, Timeout.apply(5, TimeUnit.SECONDS)))
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response instanceof TaskMessage.TaskStatusResponse) {
                        TaskMessage.TaskStatusResponse statusResponse = (TaskMessage.TaskStatusResponse) response;
                        ObjectNode result = Json.newObject();
                        result.put("taskId", statusResponse.getTaskId());
                        result.put("status", statusResponse.getStatus());
                        result.put("message", statusResponse.getMessage());
                        result.put("taskName", taskName);
                        result.put("taskType", taskType);
                        result.put("priority", priority.name());
                        return ok(result);
                    }
                    return internalServerError(createErrorResponse("Unexpected response type"));
                })
                .toCompletableFuture()
                .exceptionally(throwable -> {
                    return internalServerError(createErrorResponse("Task submission failed: " + throwable.getMessage()));
                });
    }

    /**
     * 查询任务状态
     * GET /api/tasks/:taskId
     */
    public CompletableFuture<Result> getTaskStatus(String taskId) {
        ActorRef taskManager = actorSystemManager.getTaskManagerActor();
        TaskMessage.QueryTaskStatus query = new TaskMessage.QueryTaskStatus(taskId);

        return FutureConverters.toJava(Patterns
                .ask(taskManager, query, Timeout.apply(5, TimeUnit.SECONDS)))
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response instanceof TaskMessage.TaskStatusResponse) {
                        TaskMessage.TaskStatusResponse statusResponse = (TaskMessage.TaskStatusResponse) response;
                        ObjectNode result = Json.newObject();
                        result.put("taskId", statusResponse.getTaskId());
                        result.put("status", statusResponse.getStatus());
                        result.put("message", statusResponse.getMessage());
                        return ok(result);
                    }
                    return internalServerError(createErrorResponse("Unexpected response type"));
                })
                .toCompletableFuture()
                .exceptionally(throwable -> {
                    return internalServerError(createErrorResponse("Query failed: " + throwable.getMessage()));
                });
    }

    /**
     * 批量提交任务
     * POST /api/tasks/batch
     *
     * Request body:
     * {
     *   "tasks": [
     *     {
     *       "taskName": "Task 1",
     *       "taskType": "DATA_PROCESSING",
     *       "taskData": "data1",
     *       "priority": "HIGH"
     *     },
     *     ...
     *   ]
     * }
     */
    public CompletableFuture<Result> submitBatchTasks(Http.Request request) {
        JsonNode json = request.body().asJson();

        if (json == null || !json.has("tasks")) {
            return CompletableFuture.completedFuture(
                    badRequest(createErrorResponse("Missing tasks array"))
            );
        }

        ArrayNode tasksArray = (ArrayNode) json.get("tasks");
        ArrayNode results = Json.newArray();

        ActorRef taskManager = actorSystemManager.getTaskManagerActor();

        // 提交所有任务
        for (JsonNode taskJson : tasksArray) {
            String taskName = taskJson.path("taskName").asText("Unnamed Task");
            String taskType = taskJson.path("taskType").asText("DEFAULT");
            String taskData = taskJson.path("taskData").asText("");
            String priorityStr = taskJson.path("priority").asText("NORMAL");

            Task.TaskPriority priority;
            try {
                priority = Task.TaskPriority.valueOf(priorityStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                priority = Task.TaskPriority.NORMAL;
            }

            String taskId = UUID.randomUUID().toString();
            Task task = new Task(taskId, taskName, taskType, taskData, priority);

            // 发送任务（fire and forget方式）
            taskManager.tell(task, ActorRef.noSender());

            ObjectNode taskResult = Json.newObject();
            taskResult.put("taskId", taskId);
            taskResult.put("taskName", taskName);
            taskResult.put("status", "SUBMITTED");
            results.add(taskResult);
        }

        ObjectNode response = Json.newObject();
        response.put("totalTasks", tasksArray.size());
        response.set("tasks", results);

        return CompletableFuture.completedFuture(ok(response));
    }

    /**
     * 获取任务系统统计信息
     * GET /api/tasks/stats
     */
    public CompletableFuture<Result> getStats() {
        ActorRef taskManager = actorSystemManager.getTaskManagerActor();

        return FutureConverters.toJava(Patterns
                .ask(taskManager, "GET_STATS", Timeout.apply(5, TimeUnit.SECONDS)))
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> stats = (Map<String, Object>) response;
                        return ok(Json.toJson(stats));
                    }
                    return internalServerError(createErrorResponse("Unexpected response type"));
                })
                .toCompletableFuture()
                .exceptionally(throwable -> {
                    return internalServerError(createErrorResponse("Stats retrieval failed: " + throwable.getMessage()));
                });
    }

    /**
     * 获取任务类型列表
     * GET /api/tasks/types
     */
    public Result getTaskTypes() {
        ArrayNode types = Json.newArray();
        types.add("DATA_PROCESSING");
        types.add("FILE_OPERATION");
        types.add("API_CALL");
        types.add("NOTIFICATION");
        types.add("REPORT_GENERATION");

        ObjectNode response = Json.newObject();
        response.set("taskTypes", types);

        return ok(response);
    }

    /**
     * 获取优先级列表
     * GET /api/tasks/priorities
     */
    public Result getPriorities() {
        ArrayNode priorities = Json.newArray();
        for (Task.TaskPriority priority : Task.TaskPriority.values()) {
            ObjectNode p = Json.newObject();
            p.put("name", priority.name());
            p.put("value", priority.getValue());
            priorities.add(p);
        }

        ObjectNode response = Json.newObject();
        response.set("priorities", priorities);

        return ok(response);
    }

    private ObjectNode createErrorResponse(String message) {
        ObjectNode error = Json.newObject();
        error.put("error", message);
        return error;
    }
}
