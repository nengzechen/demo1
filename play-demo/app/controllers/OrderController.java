package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.ApiResponse;
import models.Order;
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
 * 订单控制器
 * 演示复杂对象的 JSON 处理、状态更新 (PATCH) 和可选参数筛选
 */
@Singleton
public class OrderController extends Controller {
    
    // 模拟数据库存储
    private final Map<Long, Order> orderStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @Inject
    public OrderController() {
        // 初始化一些测试数据
        initTestData();
    }
    
    private void initTestData() {
        // 创建测试订单
        createOrder(1L, "北京市朝阳区xxx街道", Arrays.asList(
                new Order.OrderItem(1L, "iPhone 15 Pro", new BigDecimal("8999.00"), 1),
                new Order.OrderItem(3L, "AirPods Pro 2", new BigDecimal("1899.00"), 2)
        ), Order.STATUS_COMPLETED);
        
        createOrder(2L, "上海市浦东新区xxx路", Arrays.asList(
                new Order.OrderItem(2L, "MacBook Pro 14", new BigDecimal("16999.00"), 1)
        ), Order.STATUS_SHIPPED);
        
        createOrder(3L, "广州市天河区xxx大道", Arrays.asList(
                new Order.OrderItem(6L, "小米14", new BigDecimal("4299.00"), 2),
                new Order.OrderItem(5L, "Apple Watch S9", new BigDecimal("3199.00"), 1)
        ), Order.STATUS_PAID);
        
        createOrder(1L, "北京市海淀区xxx号", Arrays.asList(
                new Order.OrderItem(4L, "iPad Air", new BigDecimal("4799.00"), 1)
        ), Order.STATUS_PENDING);
    }
    
    private Order createOrder(Long userId, String address, List<Order.OrderItem> items, String status) {
        Long id = idGenerator.getAndIncrement();
        Order order = new Order();
        order.setId(id);
        order.setUserId(userId);
        order.setShippingAddress(address);
        order.setItems(items);
        order.setStatus(status);
        order.generateOrderNo();
        order.calculateTotalAmount();
        orderStore.put(id, order);
        return order;
    }
    
    /**
     * 获取订单列表（支持状态筛选）
     * GET /api/orders
     * GET /api/orders?status=PENDING
     */
    public Result list(Optional<String> status) {
        List<Order> orders;
        
        if (status.isPresent() && !status.get().isEmpty()) {
            // 按状态筛选
            String statusValue = status.get().toUpperCase();
            orders = orderStore.values().stream()
                    .filter(o -> o.getStatus().equals(statusValue))
                    .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                    .collect(Collectors.toList());
        } else {
            // 返回所有订单
            orders = new ArrayList<>(orderStore.values());
            orders.sort((a, b) -> Long.compare(b.getId(), a.getId()));
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("total", orders.size());
        data.put("orders", orders);
        
        // 添加状态统计
        Map<String, Long> statusStats = orderStore.values().stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
        data.put("statusStats", statusStats);
        
        return ok(Json.toJson(ApiResponse.success(data)));
    }
    
    /**
     * 根据 ID 获取订单详情
     * GET /api/orders/:id
     */
    public Result getById(Long id) {
        Order order = orderStore.get(id);
        
        if (order == null) {
            return notFound(Json.toJson(ApiResponse.notFound("订单不存在: ID=" + id)));
        }
        
        return ok(Json.toJson(ApiResponse.success(order)));
    }
    
    /**
     * 创建订单
     * POST /api/orders
     * Body: {
     *   "userId": 1,
     *   "shippingAddress": "xxx",
     *   "remark": "备注",
     *   "items": [
     *     { "productId": 1, "productName": "xxx", "price": 99.99, "quantity": 2 }
     *   ]
     * }
     */
    public Result create(Http.Request request) {
        JsonNode json = request.body().asJson();
        
        if (json == null) {
            return badRequest(Json.toJson(ApiResponse.badRequest("请求体必须是 JSON 格式")));
        }
        
        // 解析 JSON 为 Order 对象
        Order order;
        try {
            order = Json.fromJson(json, Order.class);
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.badRequest("JSON 解析失败: " + e.getMessage())));
        }
        
        // 数据校验
        if (order.getUserId() == null) {
            return badRequest(Json.toJson(ApiResponse.badRequest("用户ID不能为空")));
        }
        
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return badRequest(Json.toJson(ApiResponse.badRequest("订单项不能为空")));
        }
        
        if (order.getShippingAddress() == null || order.getShippingAddress().trim().isEmpty()) {
            return badRequest(Json.toJson(ApiResponse.badRequest("收货地址不能为空")));
        }
        
        // 设置订单属性
        Long id = idGenerator.getAndIncrement();
        order.setId(id);
        order.generateOrderNo();
        order.setStatus(Order.STATUS_PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        // 计算每个订单项的小计
        for (Order.OrderItem item : order.getItems()) {
            if (item.getPrice() != null && item.getQuantity() != null) {
                item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        
        // 计算总金额
        order.calculateTotalAmount();
        
        // 保存
        orderStore.put(id, order);
        
        return created(Json.toJson(ApiResponse.created("订单创建成功", order)));
    }
    
    /**
     * 更新订单状态 (PATCH 请求)
     * PATCH /api/orders/:id/status
     * Body: { "status": "PAID" }
     */
    public Result updateStatus(Http.Request request, Long id) {
        Order order = orderStore.get(id);
        
        if (order == null) {
            return notFound(Json.toJson(ApiResponse.notFound("订单不存在: ID=" + id)));
        }
        
        JsonNode json = request.body().asJson();
        
        if (json == null || !json.has("status")) {
            return badRequest(Json.toJson(ApiResponse.badRequest("请提供新的状态值")));
        }
        
        String newStatus = json.get("status").asText().toUpperCase();
        
        // 验证状态值
        List<String> validStatuses = Arrays.asList(
                Order.STATUS_PENDING,
                Order.STATUS_PAID,
                Order.STATUS_SHIPPED,
                Order.STATUS_COMPLETED,
                Order.STATUS_CANCELLED
        );
        
        if (!validStatuses.contains(newStatus)) {
            return badRequest(Json.toJson(ApiResponse.badRequest(
                    "无效的状态值，有效值: " + String.join(", ", validStatuses))));
        }
        
        // 检查状态转换是否合法
        String currentStatus = order.getStatus();
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            return badRequest(Json.toJson(ApiResponse.badRequest(
                    "状态转换不合法: " + currentStatus + " -> " + newStatus)));
        }
        
        String oldStatus = order.getStatus();
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", id);
        data.put("orderNo", order.getOrderNo());
        data.put("oldStatus", oldStatus);
        data.put("newStatus", newStatus);
        data.put("updatedAt", order.getUpdatedAt());
        
        return ok(Json.toJson(ApiResponse.success("订单状态更新成功", data)));
    }
    
    /**
     * 检查状态转换是否合法
     */
    private boolean isValidStatusTransition(String from, String to) {
        // 已取消或已完成的订单不能再更改状态
        if (from.equals(Order.STATUS_CANCELLED) || from.equals(Order.STATUS_COMPLETED)) {
            return false;
        }
        
        // 定义合法的状态转换
        Map<String, List<String>> validTransitions = new HashMap<>();
        validTransitions.put(Order.STATUS_PENDING, Arrays.asList(Order.STATUS_PAID, Order.STATUS_CANCELLED));
        validTransitions.put(Order.STATUS_PAID, Arrays.asList(Order.STATUS_SHIPPED, Order.STATUS_CANCELLED));
        validTransitions.put(Order.STATUS_SHIPPED, Arrays.asList(Order.STATUS_COMPLETED));
        
        List<String> allowedNext = validTransitions.get(from);
        return allowedNext != null && allowedNext.contains(to);
    }
}

