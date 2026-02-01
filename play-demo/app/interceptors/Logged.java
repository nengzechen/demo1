package interceptors;

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志拦截器注解
 * 用于需要记录详细日志的Controller方法
 * 
 * 使用示例：
 * @Logged
 * public Result myMethod() {
 *     return ok("Logged!");
 * }
 */
@With(LoggedAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logged {
}
