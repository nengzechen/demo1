package controllers;

import models.ApiResponse;
import play.http.HttpErrorHandler;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.*;

import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * 自定义错误处理器
 * 统一处理 HTTP 错误并返回 JSON 格式
 */
@Singleton
public class ErrorHandler implements HttpErrorHandler {
    
    @Override
    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        ApiResponse<Object> response;
        
        switch (statusCode) {
            case 400:
                response = ApiResponse.badRequest(message.isEmpty() ? "请求参数错误" : message);
                break;
            case 404:
                response = ApiResponse.notFound("请求的资源不存在: " + request.uri());
                break;
            case 403:
                response = ApiResponse.error(403, "禁止访问");
                break;
            case 401:
                response = ApiResponse.error(401, "未授权访问");
                break;
            default:
                response = ApiResponse.error(statusCode, message.isEmpty() ? "客户端错误" : message);
        }
        
        return CompletableFuture.completedFuture(
                Results.status(statusCode, Json.toJson(response))
        );
    }
    
    @Override
    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        // 记录错误日志
        exception.printStackTrace();
        
        ApiResponse<Object> response = ApiResponse.serverError(
                "服务器内部错误: " + exception.getMessage()
        );
        
        return CompletableFuture.completedFuture(
                Results.internalServerError(Json.toJson(response))
        );
    }
}

