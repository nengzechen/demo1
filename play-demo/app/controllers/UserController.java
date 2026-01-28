package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.ApiResponse;
import models.PageResult;
import models.User;
import play.libs.Json;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 用户控制器
 * 演示完整的 CRUD 操作和 JSON 数据处理
 */
@Singleton
public class UserController extends Controller {
    
    // 模拟数据库存储
    private final Map<Long, User> userStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @Inject
    public UserController() {
        // 初始化一些测试数据
        initTestData();
    }
    
    private void initTestData() {
        createUser("张三", "zhangsan@example.com", "13800138001", 25, "男");
        createUser("李四", "lisi@example.com", "13800138002", 30, "男");
        createUser("王芳", "wangfang@example.com", "13800138003", 28, "女");
        createUser("赵琳", "zhaolin@example.com", "13800138004", 26, "女");
        createUser("刘伟", "liuwei@example.com", "13800138005", 35, "男");
    }
    
    private User createUser(String username, String email, String phone, Integer age, String gender) {
        Long id = idGenerator.getAndIncrement();
        User user = new User(username, "password123", email);
        user.setId(id);
        user.setPhone(phone);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userStore.put(id, user);
        return user;
    }
    
    /**
     * 获取用户列表（支持分页）
     * GET /api/users?page=1&size=10
     */
    public Result list(int page, int size) {
        // 参数校验
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 10;
        
        List<User> allUsers = new ArrayList<>(userStore.values());
        // 按 ID 倒序排列
        allUsers.sort((a, b) -> Long.compare(b.getId(), a.getId()));
        
        long total = allUsers.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, allUsers.size());
        
        List<User> pageList = fromIndex < allUsers.size() 
                ? allUsers.subList(fromIndex, toIndex) 
                : Collections.emptyList();
        
        PageResult<User> pageResult = PageResult.of(pageList, page, size, total);
        
        return ok(Json.toJson(ApiResponse.success(pageResult)));
    }
    
    /**
     * 根据 ID 获取用户
     * GET /api/users/:id
     */
    public Result getById(Long id) {
        User user = userStore.get(id);
        
        if (user == null) {
            return notFound(Json.toJson(ApiResponse.notFound("用户不存在: ID=" + id)));
        }
        
        return ok(Json.toJson(ApiResponse.success(user)));
    }
    
    /**
     * 根据用户名搜索
     * GET /api/users/search?name=张
     */
    public Result searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return badRequest(Json.toJson(ApiResponse.badRequest("搜索关键词不能为空")));
        }
        
        List<User> results = userStore.values().stream()
                .filter(u -> u.getUsername() != null && u.getUsername().contains(name))
                .collect(Collectors.toList());
        
        Map<String, Object> data = new HashMap<>();
        data.put("keyword", name);
        data.put("count", results.size());
        data.put("users", results);
        
        return ok(Json.toJson(ApiResponse.success(data)));
    }
    
    /**
     * 创建用户
     * POST /api/users
     * Body: { "username": "xxx", "email": "xxx@example.com", ... }
     */
    public Result create(Http.Request request) {
        JsonNode json = request.body().asJson();
        
        if (json == null) {
            return badRequest(Json.toJson(ApiResponse.badRequest("请求体必须是 JSON 格式")));
        }
        
        // 解析 JSON 为 User 对象
        User user;
        try {
            user = Json.fromJson(json, User.class);
        } catch (Exception e) {
            return badRequest(Json.toJson(ApiResponse.badRequest("JSON 解析失败: " + e.getMessage())));
        }
        
        // 数据校验
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return badRequest(Json.toJson(ApiResponse.badRequest("用户名不能为空")));
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return badRequest(Json.toJson(ApiResponse.badRequest("邮箱不能为空")));
        }
        
        // 检查用户名是否已存在
        boolean usernameExists = userStore.values().stream()
                .anyMatch(u -> u.getUsername().equals(user.getUsername()));
        if (usernameExists) {
            return badRequest(Json.toJson(ApiResponse.badRequest("用户名已存在")));
        }
        
        // 设置 ID 和时间戳
        Long id = idGenerator.getAndIncrement();
        user.setId(id);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setEnabled(true);
        
        // 保存
        userStore.put(id, user);
        
        return created(Json.toJson(ApiResponse.created("用户创建成功", user)));
    }
    
    /**
     * 更新用户
     * PUT /api/users/:id
     * Body: { "username": "xxx", "email": "xxx@example.com", ... }
     */
    public Result update(Http.Request request, Long id) {
        User existingUser = userStore.get(id);
        
        if (existingUser == null) {
            return notFound(Json.toJson(ApiResponse.notFound("用户不存在: ID=" + id)));
        }
        
        JsonNode json = request.body().asJson();
        
        if (json == null) {
            return badRequest(Json.toJson(ApiResponse.badRequest("请求体必须是 JSON 格式")));
        }
        
        // 更新字段
        if (json.has("username") && !json.get("username").isNull()) {
            existingUser.setUsername(json.get("username").asText());
        }
        if (json.has("email") && !json.get("email").isNull()) {
            existingUser.setEmail(json.get("email").asText());
        }
        if (json.has("phone") && !json.get("phone").isNull()) {
            existingUser.setPhone(json.get("phone").asText());
        }
        if (json.has("realName") && !json.get("realName").isNull()) {
            existingUser.setRealName(json.get("realName").asText());
        }
        if (json.has("enabled") && !json.get("enabled").isNull()) {
            existingUser.setEnabled(json.get("enabled").asBoolean());
        }
        
        existingUser.setUpdatedAt(LocalDateTime.now());
        
        return ok(Json.toJson(ApiResponse.success("用户更新成功", existingUser)));
    }
    
    /**
     * 删除用户
     * DELETE /api/users/:id
     */
    public Result delete(Long id) {
        User user = userStore.remove(id);
        
        if (user == null) {
            return notFound(Json.toJson(ApiResponse.notFound("用户不存在: ID=" + id)));
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("deletedId", id);
        data.put("deletedUser", user.getUsername());
        
        return ok(Json.toJson(ApiResponse.success("用户删除成功", data)));
    }
}

