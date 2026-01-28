package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

/**
 * HomeController 单元测试
 */
public class HomeControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        Map<String, Object> config = new HashMap<>();
        config.put("play.application.name", "play-demo");

        return new GuiceApplicationBuilder()
                .configure(config)
                .build();
    }

    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);

        assertEquals(OK, result.status());
        assertEquals("application/json", result.contentType().orElse(""));

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.has("success"));
        assertTrue(jsonNode.get("success").asBoolean());
        assertTrue(jsonNode.has("data"));

        JsonNode data = jsonNode.get("data");
        assertTrue(data.has("message"));
        assertTrue(data.has("version"));
        assertTrue(data.has("endpoints"));

        assertEquals("欢迎使用 Play Framework Demo API", data.get("message").asText());
        assertEquals("1.0.0", data.get("version").asText());
    }

    @Test
    public void testHealth() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/health");

        Result result = route(app, request);

        assertEquals(OK, result.status());
        assertEquals("application/json", result.contentType().orElse(""));

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.get("success").asBoolean());

        JsonNode data = jsonNode.get("data");
        assertTrue(data.has("status"));
        assertTrue(data.has("timestamp"));
        assertTrue(data.has("checks"));

        assertEquals("UP", data.get("status").asText());

        JsonNode checks = data.get("checks");
        assertEquals("healthy", checks.get("application").asText());
        assertEquals("healthy", checks.get("database").asText());
    }

    @Test
    public void testAppInfo() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/info");

        Result result = route(app, request);

        assertEquals(OK, result.status());
        assertEquals("application/json", result.contentType().orElse(""));

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.get("success").asBoolean());

        JsonNode data = jsonNode.get("data");
        assertTrue(data.has("name"));
        assertTrue(data.has("environment"));
        assertTrue(data.has("javaVersion"));
        assertTrue(data.has("playVersion"));
        assertTrue(data.has("scalaVersion"));
        assertTrue(data.has("runtime"));

        assertEquals("play-demo", data.get("name").asText());
        assertEquals("development", data.get("environment").asText());

        JsonNode runtime = data.get("runtime");
        assertTrue(runtime.has("processors"));
        assertTrue(runtime.has("maxMemory"));
        assertTrue(runtime.has("freeMemory"));
    }

    @Test
    public void testIndexResponseStructure() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        JsonNode jsonNode = Json.parse(contentAsString(result));
        JsonNode endpoints = jsonNode.get("data").get("endpoints");

        assertTrue(endpoints.has("users"));
        assertTrue(endpoints.has("products"));
        assertTrue(endpoints.has("orders"));
        assertTrue(endpoints.has("health"));
        assertTrue(endpoints.has("info"));

        assertEquals("/api/users", endpoints.get("users").asText());
        assertEquals("/api/products", endpoints.get("products").asText());
        assertEquals("/api/orders", endpoints.get("orders").asText());
    }
}
