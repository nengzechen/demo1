package com.demo.controller;

import com.demo.dto.request.UserCreateRequest;
import com.demo.dto.request.UserUpdateRequest;
import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.PageResponse;
import com.demo.dto.response.UserResponse;
import com.demo.entity.User;
import com.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 用户管理Controller
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Tag(name = "用户管理", description = "用户管理相关API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.createUser(request);
        return ApiResponse.success("用户创建成功", UserResponse.from(user));
    }

    @Operation(summary = "获取用户详情", description = "根据ID获取用户详情")
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        User user = userService.getUserById(id);
        return ApiResponse.success(UserResponse.from(user));
    }

    @Operation(summary = "获取用户列表", description = "分页获取用户列表")
    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> getAllUsers(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "ASC") String direction) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userService.getAllUsers(pageable);
        Page<UserResponse> userResponses = users.map(UserResponse::from);
        
        return ApiResponse.success(PageResponse.of(userResponses));
    }

    @Operation(summary = "搜索用户", description = "根据关键词搜索用户")
    @GetMapping("/search")
    public ApiResponse<PageResponse<UserResponse>> searchUsers(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.searchUsers(keyword, pageable);
        Page<UserResponse> userResponses = users.map(UserResponse::from);
        
        return ApiResponse.success(PageResponse.of(userResponses));
    }

    @Operation(summary = "根据用户名获取用户", description = "根据用户名获取用户信息")
    @GetMapping("/username/{username}")
    public ApiResponse<UserResponse> getUserByUsername(
            @Parameter(description = "用户名") @PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ApiResponse.success(UserResponse.from(user));
    }

    @Operation(summary = "根据状态查询用户", description = "根据启用状态查询用户")
    @GetMapping("/by-status")
    public ApiResponse<PageResponse<UserResponse>> getUsersByEnabled(
            @Parameter(description = "启用状态") @RequestParam Boolean enabled,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getUsersByEnabled(enabled, pageable);
        Page<UserResponse> userResponses = users.map(UserResponse::from);
        
        return ApiResponse.success(PageResponse.of(userResponses));
    }

    @Operation(summary = "更新用户", description = "根据ID更新用户信息")
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        User user = userService.updateUser(id, request);
        return ApiResponse.success("用户更新成功", UserResponse.from(user));
    }

    @Operation(summary = "删除用户", description = "根据ID删除用户")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success("用户删除成功", null);
    }

    @Operation(summary = "批量删除用户", description = "根据ID列表批量删除用户")
    @DeleteMapping("/batch")
    public ApiResponse<Void> deleteUsers(
            @Parameter(description = "用户ID列表") @RequestBody Set<Long> ids) {
        userService.deleteUsers(ids);
        return ApiResponse.success("批量删除成功", null);
    }
}
