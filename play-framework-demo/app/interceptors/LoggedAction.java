package interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * 日志拦截器实现
 * 记录方法调用的详细信息
 */
public class LoggedAction extends Action.Simple {

    private static final Logger logger = LoggerFactory.getLogger(LoggedAction.class);

    @Override
    public CompletionStage<Result> call(Http.Request request) {
        long startTime = System.currentTimeMillis();

        logger.info("╔══════════════════════════════════════════════════════════");
        logger.info("║ Action Call Started");
        logger.info("║ Method: {}", request.method());
        logger.info("║ URI: {}", request.uri());
        logger.info("║ Remote Address: {}", request.remoteAddress());
        logger.info("║ Content-Type: {}", request.contentType().orElse("N/A"));

        // 记录请求头（可选）
        if (logger.isDebugEnabled()) {
            logger.debug("║ Headers:");
            request.getHeaders().toMap().forEach((key, values) ->
                    logger.debug("║   {}: {}", key, String.join(", ", values))
            );
        }

        logger.info("╚══════════════════════════════════════════════════════════");

        return delegate.call(request).thenApply(result -> {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            logger.info("╔══════════════════════════════════════════════════════════");
            logger.info("║ Action Call Completed");
            logger.info("║ URI: {}", request.uri());
            logger.info("║ Status: {}", result.status());
            logger.info("║ Duration: {}ms", duration);
            logger.info("╚══════════════════════════════════════════════════════════");

            return result;
        });
    }
}
