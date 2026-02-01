package com.demo.controller;

import com.demo.dto.request.RoleCreateRequest;
import com.demo.dto.request.RoleUpdateRequest;
import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.PageResponse;
import com.demo.dto.response.RoleResponse;
import com.demo.entity.Role;
import com.demo.service.RoleService;
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
 * 角色管理Controller
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Tag(name = "角色管理", description = "角色管理相关API")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "创建角色", description = "创建新角色")
    @PostMapping
    public ApiResponse<RoleResponse> createRole(@Valid @RequestBody RoleCreateRequest request) {
        Role role = roleService.createRole(request);
        return ApiResponse.success("角色创建成功", RoleResponse.from(role));
    }

    @Operation(summary = "获取角色详情", description = "根据ID获取角色详情")
    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRoleById(
            @Parameter(description = "角色ID") @PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return ApiResponse.success(RoleResponse.from(role));
    }

    @Operation(summary = "获取角色列表", description = "分页获取角色列表")
    @GetMapping
    public ApiResponse<PageResponse<RoleResponse>> getAllRoles(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "ASC") String direction) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Role> roles = roleService.getAllRoles(pageable);
        Page<RoleResponse> roleResponses = roles.map(RoleResponse::from);
        
        return ApiResponse.success(PageResponse.of(roleResponses));
    }

    @Operation(summary = "搜索角色", description = "根据关键词搜索角色")
    @GetMapping("/search")
    public ApiResponse<PageResponse<RoleResponse>> searchRoles(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> roles = roleService.searchRoles(keyword, pageable);
        Page<RoleResponse> roleResponses = roles.map(RoleResponse::from);
        
        return ApiResponse.success(PageResponse.of(roleResponses));
    }

    @Operation(summary = "根据编码获取角色", description = "根据角色编码获取角色信息")
    @GetMapping("/code/{code}")
    public ApiResponse<RoleResponse> getRoleByCode(
            @Parameter(description = "角色编码") @PathVariable String code) {
        Role role = roleService.getRoleByCode(code);
        return ApiResponse.success(RoleResponse.from(role));
    }

    @Operation(summary = "根据状态查询角色", description = "根据启用状态查询角色")
    @GetMapping("/by-status")
    public ApiResponse<PageResponse<RoleResponse>> getRolesByEnabled(
            @Parameter(description = "启用状态") @RequestParam Boolean enabled,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> roles = roleService.getRolesByEnabled(enabled, pageable);
        Page<RoleResponse> roleResponses = roles.map(RoleResponse::from);
        
        return ApiResponse.success(PageResponse.of(roleResponses));
    }

    @Operation(summary = "更新角色", description = "根据ID更新角色信息")
    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> updateRole(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest request) {
        Role role = roleService.updateRole(id, request);
        return ApiResponse.success("角色更新成功", RoleResponse.from(role));
    }

    @Operation(summary = "删除角色", description = "根据ID删除角色")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(
            @Parameter(description = "角色ID") @PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.success("角色删除成功", null);
    }

    @Operation(summary = "批量删除角色", description = "根据ID列表批量删除角色")
    @DeleteMapping("/batch")
    public ApiResponse<Void> deleteRoles(
            @Parameter(description = "角色ID列表") @RequestBody Set<Long> ids) {
        roleService.deleteRoles(ids);
        return ApiResponse.success("批量删除成功", null);
    }
}
