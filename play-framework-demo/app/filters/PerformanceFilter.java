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
 * 性能监控过滤器 - 监控请求性能并添加响应头
 * 演示Filter添加自定义响应头
 */
@Singleton
public class PerformanceFilter extends Filter {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceFilter.class);

    @Inject
    public PerformanceFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(
            Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
            Http.RequestHeader requestHeader) {

        long startTime = System.nanoTime();

        return nextFilter.apply(requestHeader).thenApply(result -> {
            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;

            // 添加性能相关的响应头
            Result resultWithHeaders = result
                    .withHeader("X-Response-Time", durationMs + "ms")
                    .withHeader("X-Server-Name", "Play-Demo-Server");

            // 记录性能指标
            if (durationMs > 500) {
                logger.warn("Performance Warning: {} {} took {}ms (threshold: 500ms)",
                        requestHeader.method(),
                        requestHeader.uri(),
                        durationMs);
            } else {
                logger.debug("Performance: {} {} took {}ms",
                        requestHeader.method(),
                        requestHeader.uri(),
                        durationMs);
            }

            return resultWithHeaders;
        });
    }
}
