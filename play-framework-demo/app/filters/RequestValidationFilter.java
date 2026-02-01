package filters;

import akka.stream.Materializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * 请求验证过滤器 - 验证请求合法性
 * 演示Filter进行请求拦截和验证
 */
@Singleton
public class RequestValidationFilter extends Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestValidationFilter.class);
    private static final int MAX_CONTENT_LENGTH = 10 * 1024 * 1024; // 10MB

    @Inject
    public RequestValidationFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(
            Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
            Http.RequestHeader requestHeader) {

        // 检查请求方法是否合法
        String method = requestHeader.method();
        if (!isValidMethod(method)) {
            logger.warn("Invalid HTTP method: {} for URI: {}", method, requestHeader.uri());
            return CompletableFuture.completedFuture(
                    Results.status(Http.Status.METHOD_NOT_ALLOWED,
                            createErrorJson("Invalid HTTP method: " + method))
            );
        }

        // 检查Content-Length
        requestHeader.header(Http.HeaderNames.CONTENT_LENGTH).ifPresent(contentLength -> {
            try {
                long length = Long.parseLong(contentLength);
                if (length > MAX_CONTENT_LENGTH) {
                    logger.warn("Request content too large: {} bytes for URI: {}",
                            length, requestHeader.uri());
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid Content-Length header: {}", contentLength);
            }
        });

        // 检查User-Agent（可选，用于识别客户端）
        String userAgent = requestHeader.header(Http.HeaderNames.USER_AGENT).orElse("Unknown");
        logger.debug("User-Agent: {} for URI: {}", userAgent, requestHeader.uri());

        // 继续处理请求
        return nextFilter.apply(requestHeader);
    }

    /**
     * 检查HTTP方法是否合法
     */
    private boolean isValidMethod(String method) {
        return method.equals("GET") ||
                method.equals("POST") ||
                method.equals("PUT") ||
                method.equals("DELETE") ||
                method.equals("PATCH") ||
                method.equals("OPTIONS") ||
                method.equals("HEAD");
    }

    /**
     * 创建错误JSON响应
     */
    private String createErrorJson(String message) {
        com.fasterxml.jackson.databind.node.ObjectNode error = Json.newObject();
        error.put("error", message);
        error.put("timestamp", System.currentTimeMillis());
        return Json.stringify(error);
    }
}
