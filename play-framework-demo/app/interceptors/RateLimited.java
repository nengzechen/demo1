package interceptors;

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流拦截器注解
 * 用于需要限流的Controller方法
 * 
 * 使用示例：
 * @RateLimited(requestsPerMinute = 10)
 * public Result myMethod() {
 *     return ok("Rate limited!");
 * }
 */
@With(RateLimitedAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
    /**
     * 每分钟允许的请求数
     */
    int requestsPerMinute() default 60;
}
