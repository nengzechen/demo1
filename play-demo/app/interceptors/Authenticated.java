package interceptors;

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 认证拦截器注解
 * 用于需要认证的Controller方法
 * 
 * 使用示例：
 * @Authenticated
 * public Result mySecureMethod() {
 *     return ok("Authenticated!");
 * }
 */
@With(AuthenticatedAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticated {
}
