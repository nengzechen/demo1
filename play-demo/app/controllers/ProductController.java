package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.ApiResponse;
import models.Product;
import play.libs.Json;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 产品控制器
 * 演示完整的 CRUD 操作、批量操作和分类筛选
 */
@Singleton
public class ProductController extends Controller {
    
    // 模拟数据库存储
    private final Map<Long, Product> productStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @Inject
    public ProductController() {
        // 初始化一些测试数据
        initTestData();
    }
    
    private void initTestData() {
        createProduct("iPhone 15 Pro", "苹果最新旗舰手机", new BigDecimal("8999.00"), "手机", 100);
        createProduct("MacBook Pro 14", "M3 Pro 芯片笔记本", new BigDecimal("16999.00"), "电脑", 50);
        createProduct("AirPods Pro 2", "主动降噪无线耳机", new BigDecimal("1899.00"), "配件", 200);
        createProduct("iPad Air", "轻薄平板电脑", new BigDecimal("4799.00"), "平板", 80);
        createProduct("Apple Watch S9", "智能健康手表", new BigDecimal("3199.00"), "穿戴", 120);
        createProduct("小米14", "小米旗舰手机", new BigDecimal("4299.00"), "手机", 150);
        createProduct("华为 Mate60", "华为旗舰手机", new BigDecimal("5999.00"), "手机", 80);
    }
    
    private Product createProduct(String name, String description, BigDecimal price, 
                                  String category, Integer stock) {
        Long id = idGenerator.getAndIncrement();
        Product product = new Product(id, name, description, price, category, stock);
        productStore.put(id, product);
        return product;
    }
    
    /**
     * 获取产品列表
     * GET /api/products
     */
    public Result list() {
        List<Product> products = new ArrayList<>(productStore.values());
        // 按 ID 倒序排列
        products.sort((a, b) -> Long.compare(b.getId(), a.getId()));
        
        Map<String, Object> data = new HashMap<>();
        data.put("total", products.size());
        data.put("products", products);
        
        // 添加分类统计
        Map<String, Long> categoryStats = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
        data.put("categoryStats", categoryStats);
        
        return ok(Json.toJson(ApiResponse.success(data)));
    }
    
    /**
     * 根据 ID 获取产品
     * GET /api/products/:id
     */
    public Result getById(Long id) {
        Product product = productStore.get(id);
        
        if (product == null) {
            return notFound(Json.toJson(ApiResponse.notFound("产品不存在: ID=" + id)));
        }
        
        return ok(Json.toJson(ApiResponse.success(product)));
    }
    
    /**
     * 根据类别筛选产品
     * GET /api/products/category/:category
     */
    public Result getByCategory(String category) {
        List<Product> products = productStore.values().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().equals(category))
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .collect(Collectors.toList());
        
        Map<String, Object> data = new HashMap<>();
        data.put("category", category);
        data.put("count", products.size());
        data.put("products", products);
        
        return ok(Json.toJson(ApiResponse.success(data)));
    }
    
    /**
     * 创建产品
     * POST /api/products
     * Body: { "name": "xxx", "price": 99.99, "category": "xxx", ... }
     */
    public Result create(Http.Request request) {
        JsonNode json = request.body().asJson();
        
        if (json == null) {
            return badRequest(Json.toJson(ApiResponse.badRequest("请求体必须是 JSON 格式")));
        }
        
        // 解析 JSON 为 Product 对象
        Product product;
        try {
            product = Json.fromJson(json, Product.class);
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.badRequest("JSON 解析失败: " + e.getMessage())));
        }
        
        // 数据校验
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            return badRequest(Json.toJson(ApiResponse.badRequest("产品名称不能为空")));
        }
        
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return badRequest(Json.toJson(ApiResponse.badRequest("产品价格必须大于 0")));
        }
        
        // 设置 ID 和时间戳
        Long id = idGenerator.getAndIncrement();
        product.setId(id);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setAvailable(true);
        if (product.getStock() == null) {
            product.setStock(0);
        }
        
        // 保存
        productStore.put(id, product);
        
        return created(Json.toJson(ApiResponse.created("产品创建成功", product)));
    }
    
    /**
     * 批量创建产品
     * POST /api/products/batch
     * Body: [{ "name": "xxx", ... }, { "name": "yyy", ... }]
     */
    public Result batchCreate(Http.Request request) {
        JsonNode json = request.body().asJson();
        
        if (json == null || !json.isArray()) {
            return badRequest(Json.toJson(ApiResponse.badRequest("请求体必须是 JSON 数组格式")));
        }
        
        List<Product> createdProducts = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        int index = 0;
        for (JsonNode node : json) {
            index++;
            try {
                Product product = Json.fromJson(node, Product.class);
                
                // 简单校验
                if (product.getName() == null || product.getName().trim().isEmpty()) {
                    errors.add("第 " + index + " 个产品: 名称不能为空");
                    continue;
                }
                
                if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    errors.add("第 " + index + " 个产品: 价格必须大于 0");
                    continue;
                }
                
                // 设置 ID 和时间戳
                Long id = idGenerator.getAndIncrement();
                product.setId(id);
                product.setCreatedAt(LocalDateTime.now());
                product.setUpdatedAt(LocalDateTime.now());
                product.setAvailable(true);
                if (product.getStock() == null) {
                    product.setStock(0);
                }
                
                productStore.put(id, product);
                createdProducts.add(product);
                
            } catch (Exception e) {
                errors.add("第 " + index + " 个产品解析失败: " + e.getMessage());
            }
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("successCount", createdProducts.size());
        data.put("failCount", errors.size());
        data.put("createdProducts", createdProducts);
        if (!errors.isEmpty()) {
            data.put("errors", errors);
        }
        
        return created(Json.toJson(ApiResponse.created("批量创建完成", data)));
    }
    
    /**
     * 更新产品
     * PUT /api/products/:id
     * Body: { "name": "xxx", "price": 99.99, ... }
     */
    public Result update(Http.Request request, Long id) {
        Product existingProduct = productStore.get(id);
        
        if (existingProduct == null) {
            return notFound(Json.toJson(ApiResponse.notFound("产品不存在: ID=" + id)));
        }
        
        JsonNode json = request.body().asJson();
        
        if (json == null) {
            return badRequest(Json.toJson(ApiResponse.badRequest("请求体必须是 JSON 格式")));
        }
        
        // 更新字段
        if (json.has("name") && !json.get("name").isNull()) {
            existingProduct.setName(json.get("name").asText());
        }
        if (json.has("description") && !json.get("description").isNull()) {
            existingProduct.setDescription(json.get("description").asText());
        }
        if (json.has("price") && !json.get("price").isNull()) {
            existingProduct.setPrice(new BigDecimal(json.get("price").asText()));
        }
        if (json.has("category") && !json.get("category").isNull()) {
            existingProduct.setCategory(json.get("category").asText());
        }
        if (json.has("stock") && !json.get("stock").isNull()) {
            existingProduct.setStock(json.get("stock").asInt());
        }
        if (json.has("imageUrl") && !json.get("imageUrl").isNull()) {
            existingProduct.setImageUrl(json.get("imageUrl").asText());
        }
        if (json.has("available") && !json.get("available").isNull()) {
            existingProduct.setAvailable(json.get("available").asBoolean());
        }
        
        existingProduct.setUpdatedAt(LocalDateTime.now());
        
        return ok(Json.toJson(ApiResponse.success("产品更新成功", existingProduct)));
    }
    
    /**
     * 删除产品
     * DELETE /api/products/:id
     */
    public Result delete(Long id) {
        Product product = productStore.remove(id);
        
        if (product == null) {
            return notFound(Json.toJson(ApiResponse.notFound("产品不存在: ID=" + id)));
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("deletedId", id);
        data.put("deletedProduct", product.getName());
        
        return ok(Json.toJson(ApiResponse.success("产品删除成功", data)));
    }
}

