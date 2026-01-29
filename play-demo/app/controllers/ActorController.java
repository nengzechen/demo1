package controllers;

import actors.ActorSystemManager;
import actors.TaskMessage;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.concurrent.duration.Duration;
import akka.util.Timeout;
import scala.compat.java8.FutureConverters;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Actor功能测试控制器
 * 提供API接口来测试Actor的功能
 */
public class ActorController extends Controller {

    private final ActorSystemManager actorSystemManager;

    @Inject
    public ActorController(ActorSystemManager actorSystemManager) {
        this.actorSystemManager = actorSystemManager;
    }

    /**
     * 提交异步任务
     * POST /api/actor/task
     */
    public CompletableFuture<Result> submitTask(Http.Request request) {
        JsonNode json = request.body().asJson();

        if (json == null) {
            return CompletableFuture.completedFuture(
                    badRequest(createErrorResponse("Missing request body"))
            );
        }

        String taskName = json.path("taskName").asText("default-task");
        String taskData = json.path("taskData").asText("");

        // 生成任务ID
        String taskId = UUID.randomUUID().toString();

        // 创建任务消息
        TaskMessage.ProcessTask processTask = new TaskMessage.ProcessTask(taskId, taskName, taskData);

        // 发送消息给Actor
        ActorRef taskProcessor = actorSystemManager.getTaskProcessorActor();

        Timeout timeout = Timeout.apply(5, TimeUnit.SECONDS);
        return FutureConverters.toJava(Patterns
                .ask(taskProcessor, processTask, timeout))
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
                    return internalServerError(createErrorResponse("Task submission failed: " + throwable.getMessage()));
                });
    }

    /**
     * 查询任务状态
     * GET /api/actor/task/:taskId
     */
    public CompletableFuture<Result> getTaskStatus(String taskId) {
        ActorRef taskProcessor = actorSystemManager.getTaskProcessorActor();

        TaskMessage.QueryTaskStatus query = new TaskMessage.QueryTaskStatus(taskId);

        Timeout timeout = Timeout.apply(5, TimeUnit.SECONDS);
        return FutureConverters.toJava(Patterns
                .ask(taskProcessor, query, timeout))
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
     * 获取Actor系统信息
     * GET /api/actor/info
     */
    public Result getActorInfo() {
        ObjectNode result = Json.newObject();
        result.put("actorSystem", actorSystemManager.getActorSystem().name());
        result.put("taskProcessorActor", actorSystemManager.getTaskProcessorActor().path().toString());
        result.put("scheduledTaskActor", actorSystemManager.getScheduledTaskActor().path().toString());
        return ok(result);
    }

    private ObjectNode createErrorResponse(String message) {
        ObjectNode error = Json.newObject();
        error.put("error", message);
        return error;
    }
}
