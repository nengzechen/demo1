package interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流拦截器实现
 * 基于IP地址的简单限流策略
 */
public class RateLimitedAction extends Action<RateLimited> {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitedAction.class);

    // 存储每个IP的请求计数
    private static final Map<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

    // 清理过期计数器的时间间隔（60秒）
    private static final long CLEANUP_INTERVAL = 60_000;
    private static long lastCleanupTime = System.currentTimeMillis();

    @Override
    public CompletionStage<Result> call(Http.Request request) {
        String clientIp = request.remoteAddress();
        int maxRequests = configuration.requestsPerMinute();

        // 定期清理过期的计数器
        cleanup();

        // 获取或创建请求计数器
        RequestCounter counter = requestCounters.computeIfAbsent(clientIp,
                k -> new RequestCounter());

        // 重置计数器（如果已经过了1分钟）
        if (counter.shouldReset()) {
            counter.reset();
        }

        // 检查是否超过限制
        int currentCount = counter.increment();
        if (currentCount > maxRequests) {
            logger.warn("Rate limit exceeded for IP: {} (Requests: {}/{})",
                    clientIp, currentCount, maxRequests);

            return CompletableFuture.completedFuture(
                    Results.status(Http.Status.TOO_MANY_REQUESTS,
                            createErrorJson("Rate limit exceeded. Max " + maxRequests + " requests per minute"))
                            .withHeader("X-RateLimit-Limit", String.valueOf(maxRequests))
                            .withHeader("X-RateLimit-Remaining", "0")
                            .withHeader("Retry-After", "60")
            );
        }

        logger.debug("Rate limit check passed for IP: {} (Requests: {}/{})",
                clientIp, currentCount, maxRequests);

        // 添加限流信息到响应头
        return delegate.call(request).thenApply(result ->
                result.withHeader("X-RateLimit-Limit", String.valueOf(maxRequests))
                        .withHeader("X-RateLimit-Remaining", String.valueOf(maxRequests - currentCount))
        );
    }

    /**
     * 清理过期的计数器
     */
    private void cleanup() {
        long now = System.currentTimeMillis();
        if (now - lastCleanupTime > CLEANUP_INTERVAL) {
            requestCounters.entrySet().removeIf(entry ->
                    entry.getValue().isExpired()
            );
            lastCleanupTime = now;
            logger.debug("Cleaned up expired rate limit counters");
        }
    }

    private String createErrorJson(String message) {
        com.fasterxml.jackson.databind.node.ObjectNode error = Json.newObject();
        error.put("error", message);
        error.put("timestamp", System.currentTimeMillis());
        return Json.stringify(error);
    }

    /**
     * 请求计数器内部类
     */
    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile long windowStart = System.currentTimeMillis();
        private static final long WINDOW_SIZE = 60_000; // 1分钟

        public int increment() {
            return count.incrementAndGet();
        }

        public boolean shouldReset() {
            return System.currentTimeMillis() - windowStart > WINDOW_SIZE;
        }

        public void reset() {
            count.set(0);
            windowStart = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - windowStart > WINDOW_SIZE * 2;
        }
    }
}
