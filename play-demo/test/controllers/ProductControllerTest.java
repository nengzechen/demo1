package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.*;

/**
 * ProductController 单元测试
 */
public class ProductControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testListProducts() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/products");

        Result result = route(app, request);

        assertEquals(OK, result.status());
        assertEquals("application/json", result.contentType().orElse(""));

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.get("success").asBoolean());

        JsonNode data = jsonNode.get("data");
        assertTrue(data.has("total"));
        assertTrue(data.has("products"));
        assertTrue(data.has("categoryStats"));

        // 检查初始化数据
        assertTrue(data.get("total").asInt() >= 7);
        assertTrue(data.get("products").isArray());
    }

    @Test
    public void testGetProductById() {
        // 获取第一个产品
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/products/1");

        Result result = route(app, request);

        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.get("success").asBoolean());

        JsonNode data = jsonNode.get("data");
        assertEquals(1, data.get("id").asLong());
        assertTrue(data.has("name"));
        assertTrue(data.has("price"));
    }

    @Test
    public void testGetProductByIdNotFound() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/products/99999");

        Result result = route(app, request);

        assertEquals(NOT_FOUND, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertFalse(jsonNode.get("success").asBoolean());
        assertTrue(jsonNode.get("message").asText().contains("产品不存在"));
    }

    @Test
    public void testGetProductsByCategory() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/products/category/手机");

        Result result = route(app, request);

        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.get("success").asBoolean());

        JsonNode data = jsonNode.get("data");
        assertEquals("手机", data.get("category").asText());
        assertTrue(data.get("count").asInt() >= 3);
        assertTrue(data.get("products").isArray());
    }

    @Test
    public void testCreateProduct() {
        ObjectNode product = Json.newObject();
        product.put("name", "测试产品");
        product.put("description", "这是一个测试产品");
        product.put("price", "199.99");
        product.put("category", "测试");
        product.put("stock", 50);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .header("Content-Type", "application/json").bodyJson(product)
                .uri("/api/products");

        Result result = route(app, request);

        assertEquals(CREATED, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.get("success").asBoolean());
        assertEquals("产品创建成功", jsonNode.get("message").asText());

        JsonNode data = jsonNode.get("data");
        assertTrue(data.has("id"));
        assertEquals("测试产品", data.get("name").asText());
        assertEquals("199.99", data.get("price").asText());
        assertTrue(data.get("available").asBoolean());
    }

    @Test
    public void testCreateProductWithInvalidData() {
        ObjectNode product = Json.newObject();
        product.put("name", "");
        product.put("price", "0");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .header("Content-Type", "application/json").bodyJson(product)
                .uri("/api/products");

        Result result = route(app, request);

        assertEquals(BAD_REQUEST, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertFalse(jsonNode.get("success").asBoolean());
    }

    @Test
    public void testCreateProductWithoutBody() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/products");

        Result result = route(app, request);

        assertEquals(BAD_REQUEST, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertFalse(jsonNode.get("success").asBoolean());
        assertTrue(jsonNode.get("message").asText().contains("JSON 格式"));
    }

    @Test
    public void testBatchCreateProducts() {
        ArrayNode products = Json.newArray();

        ObjectNode product1 = Json.newObject();
        product1.put("name", "批量产品1");
        product1.put("price", "99.99");
        product1.put("category", "测试");
        products.add(product1);

        ObjectNode product2 = Json.newObject();
        product2.put("name", "批量产品2");
        product2.put("price", "199.99");
        product2.put("category", "测试");
        products.add(product2);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .header("Content-Type", "application/json").bodyJson(products)
                .uri("/api/products/batch");

        Result result = route(app, request);

        assertEquals(CREATED, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.get("success").asBoolean());

        JsonNode data = jsonNode.get("data");
        assertEquals(2, data.get("successCount").asInt());
        assertEquals(0, data.get("failCount").asInt());
        assertTrue(data.get("createdProducts").isArray());
        assertEquals(2, data.get("createdProducts").size());
    }

    @Test
    public void testUpdateProduct() {
        ObjectNode updates = Json.newObject();
        updates.put("name", "更新后的产品名称");
        updates.put("price", "299.99");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .header("Content-Type", "application/json").bodyJson(updates)
                .uri("/api/products/1");

        Result result = route(app, request);

        assertEquals(OK, result.status());

        JsonNode jsonNode = Json.parse(contentAsString(result));
        assertTrue(jsonNode.get("success").asBoolean());
        assertEquals("产品更新成功", jsonNode.get("message").asText());

        JsonNode data = jsonNode.get("data");
        assertEquals("更新后的产品名称", data.get("name").asText());
    }

    @Test
    public void testUpdateNonExistentProduct() {
        ObjectNode updates = Json.newObject();
        updates.put("name", "更新产品");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .header("Content-Type", "application/json").bodyJson(updates)
                .uri("/api/products/99999");

        Result result = route(app, request);

        assertEquals(NOT_FOUND, result.status());
    }

    @Test
    public void testDeleteProduct() {
        // 先创建一个产品
        ObjectNode product = Json.newObject();
        product.put("name", "待删除产品");
        product.put("price", "99.99");
        product.put("category", "测试");

        Http.RequestBuilder createRequest = new Http.RequestBuilder()
                .method(POST)
                .header("Content-Type", "application/json").bodyJson(product)
                .uri("/api/products");

        Result createResult = route(app, createRequest);
        assertNotNull("Create result should not be null", createResult);
        assertEquals(CREATED, createResult.status());

        String createContent = contentAsString(createResult);
        assertNotNull("Create content should not be null", createContent);

        JsonNode createJson = Json.parse(createContent);
        assertNotNull("Create JSON should not be null", createJson);
        assertNotNull("Create data should not be null", createJson.get("data"));

        long productId = createJson.get("data").get("id").asLong();

        // 删除该产品
        Http.RequestBuilder deleteRequest = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/api/products/" + productId);

        Result deleteResult = route(app, deleteRequest);

        assertEquals(OK, deleteResult.status());

        JsonNode jsonNode = Json.parse(contentAsString(deleteResult));
        assertTrue(jsonNode.get("success").asBoolean());
        assertEquals("产品删除成功", jsonNode.get("message").asText());
    }

    @Test
    public void testDeleteNonExistentProduct() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/api/products/99999");

        Result result = route(app, request);

        assertEquals(NOT_FOUND, result.status());
    }

    @Test
    public void testCategoryStats() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/products");

        Result result = route(app, request);
        JsonNode jsonNode = Json.parse(contentAsString(result));

        JsonNode categoryStats = jsonNode.get("data").get("categoryStats");
        assertTrue(categoryStats.has("手机"));
        assertTrue(categoryStats.get("手机").asInt() >= 3);
    }
}
