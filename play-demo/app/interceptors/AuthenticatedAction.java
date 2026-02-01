package interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * 认证拦截器实现
 * 检查请求是否包含有效的认证信息
 */
public class AuthenticatedAction extends Action.Simple {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticatedAction.class);

    @Override
    public CompletionStage<Result> call(Http.Request request) {
        // 检查Authorization header
        String authHeader = request.header(Http.HeaderNames.AUTHORIZATION).orElse(null);

        if (authHeader == null || authHeader.isEmpty()) {
            logger.warn("Unauthorized access attempt to: {} from {}",
                    request.uri(), request.remoteAddress());
            return CompletableFuture.completedFuture(
                    Results.unauthorized(createErrorJson("Missing authorization header"))
            );
        }

        // 简单的Token验证（实际项目中应该验证JWT或Session）
        if (!authHeader.startsWith("Bearer ")) {
            logger.warn("Invalid authorization header format from: {}", request.remoteAddress());
            return CompletableFuture.completedFuture(
                    Results.unauthorized(createErrorJson("Invalid authorization header format"))
            );
        }

        String token = authHeader.substring(7);

        // 这里简化处理，实际应该验证token的有效性
        if (token.isEmpty() || token.equals("invalid")) {
            logger.warn("Invalid token from: {}", request.remoteAddress());
            return CompletableFuture.completedFuture(
                    Results.unauthorized(createErrorJson("Invalid or expired token"))
            );
        }

        logger.info("Authenticated request to: {} from user token: {}",
                request.uri(), token.substring(0, Math.min(10, token.length())) + "...");

        // 认证通过，继续执行
        return delegate.call(request);
    }

    private String createErrorJson(String message) {
        com.fasterxml.jackson.databind.node.ObjectNode error = Json.newObject();
        error.put("error", message);
        error.put("timestamp", System.currentTimeMillis());
        return Json.stringify(error);
    }
}
