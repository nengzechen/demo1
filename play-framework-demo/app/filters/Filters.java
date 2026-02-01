package filters;

import play.http.DefaultHttpFilters;
import play.filters.cors.CORSFilter;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 全局过滤器配置类
 * 注册所有需要的过滤器，执行顺序按照构造函数中的顺序
 */
@Singleton
public class Filters extends DefaultHttpFilters {

    @Inject
    public Filters(
            CORSFilter corsFilter,
            RequestValidationFilter requestValidationFilter,
            LoggingFilter loggingFilter,
            PerformanceFilter performanceFilter
    ) {
        super(
                corsFilter,                    // CORS过滤器（优先级最高）
                requestValidationFilter,       // 请求验证过滤器
                loggingFilter,                 // 日志过滤器
                performanceFilter              // 性能监控过滤器
        );
    }
}
