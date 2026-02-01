package controllers;

import models.ApiResponse;
import play.libs.Json;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.typesafe.config.Config;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 * 演示基本的 GET 请求处理和 JSON 返回
 */
@Singleton
public class HomeController extends Controller {
    
    private final Config config;
    
    @Inject
    public HomeController(Config config) {
        this.config = config;
    }
    
    /**
     * 首页
     * GET /
     */
    public Result index() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "欢迎使用 Play Framework Demo API");
        data.put("version", "1.0.0");
        data.put("documentation", "/api-docs");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("users", "/api/users");
        endpoints.put("products", "/api/products");
        endpoints.put("orders", "/api/orders");
        endpoints.put("health", "/health");
        endpoints.put("info", "/info");
        data.put("endpoints", endpoints);
        
        return ok(Json.toJson(ApiResponse.success(data)));
    }
    
    /**
     * 健康检查接口
     * GET /health
     */
    public Result health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", System.currentTimeMillis());
        
        Map<String, String> checks = new HashMap<>();
        checks.put("application", "healthy");
        checks.put("database", "healthy");
        healthInfo.put("checks", checks);
        
        return ok(Json.toJson(ApiResponse.success(healthInfo)));
    }
    
    /**
     * 应用信息接口
     * GET /info
     */
    public Result appInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", config.getString("play.application.name"));
        info.put("environment", "development");
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("playVersion", "2.9.0");
        info.put("scalaVersion", "2.13.12");
        
        Map<String, Object> runtime = new HashMap<>();
        runtime.put("processors", Runtime.getRuntime().availableProcessors());
        runtime.put("maxMemory", Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");
        runtime.put("freeMemory", Runtime.getRuntime().freeMemory() / (1024 * 1024) + " MB");
        info.put("runtime", runtime);
        
        return ok(Json.toJson(ApiResponse.success(info)));
    }
}

