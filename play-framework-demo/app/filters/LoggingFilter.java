package filters;

import akka.stream.Materializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * 日志过滤器 - 记录所有HTTP请求和响应
 * 演示Play Framework Filter的使用
 */
@Singleton
public class LoggingFilter extends Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Inject
    public LoggingFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(
            Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
            Http.RequestHeader requestHeader) {

        long startTime = System.currentTimeMillis();

        // 记录请求信息
        logger.info("==> Request: {} {} from {}",
                requestHeader.method(),
                requestHeader.uri(),
                requestHeader.remoteAddress());

        return nextFilter.apply(requestHeader).thenApply(result -> {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // 记录响应信息
            logger.info("<== Response: {} {} - Status: {} - Duration: {}ms",
                    requestHeader.method(),
                    requestHeader.uri(),
                    result.status(),
                    duration);

            // 如果响应时间过长，记录警告
            if (duration > 1000) {
                logger.warn("Slow request detected: {} {} took {}ms",
                        requestHeader.method(),
                        requestHeader.uri(),
                        duration);
            }

            return result;
        });
    }
}
