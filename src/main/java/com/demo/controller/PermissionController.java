package com.demo.controller;

import com.demo.dto.request.PermissionCreateRequest;
import com.demo.dto.request.PermissionUpdateRequest;
import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.PageResponse;
import com.demo.dto.response.PermissionResponse;
import com.demo.entity.Permission;
import com.demo.service.PermissionService;
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
 * 权限管理Controller
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Tag(name = "权限管理", description = "权限管理相关API")
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "创建权限", description = "创建新权限")
    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@Valid @RequestBody PermissionCreateRequest request) {
        Permission permission = permissionService.createPermission(request);
        return ApiResponse.success("权限创建成功", PermissionResponse.from(permission));
    }

    @Operation(summary = "获取权限详情", description = "根据ID获取权限详情")
    @GetMapping("/{id}")
    public ApiResponse<PermissionResponse> getPermissionById(
            @Parameter(description = "权限ID") @PathVariable Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return ApiResponse.success(PermissionResponse.from(permission));
    }

    @Operation(summary = "获取权限列表", description = "分页获取权限列表")
    @GetMapping
    public ApiResponse<PageResponse<PermissionResponse>> getAllPermissions(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "ASC") String direction) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Permission> permissions = permissionService.getAllPermissions(pageable);
        Page<PermissionResponse> permissionResponses = permissions.map(PermissionResponse::from);
        
        return ApiResponse.success(PageResponse.of(permissionResponses));
    }

    @Operation(summary = "搜索权限", description = "根据关键词搜索权限")
    @GetMapping("/search")
    public ApiResponse<PageResponse<PermissionResponse>> searchPermissions(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Permission> permissions = permissionService.searchPermissions(keyword, pageable);
        Page<PermissionResponse> permissionResponses = permissions.map(PermissionResponse::from);
        
        return ApiResponse.success(PageResponse.of(permissionResponses));
    }

    @Operation(summary = "根据编码获取权限", description = "根据权限编码获取权限信息")
    @GetMapping("/code/{code}")
    public ApiResponse<PermissionResponse> getPermissionByCode(
            @Parameter(description = "权限编码") @PathVariable String code) {
        Permission permission = permissionService.getPermissionByCode(code);
        return ApiResponse.success(PermissionResponse.from(permission));
    }

    @Operation(summary = "根据资源查询权限", description = "根据资源查询权限列表")
    @GetMapping("/by-resource")
    public ApiResponse<PageResponse<PermissionResponse>> getPermissionsByResource(
            @Parameter(description = "资源名称") @RequestParam String resource,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Permission> permissions = permissionService.getPermissionsByResource(resource, pageable);
        Page<PermissionResponse> permissionResponses = permissions.map(PermissionResponse::from);
        
        return ApiResponse.success(PageResponse.of(permissionResponses));
    }

    @Operation(summary = "根据状态查询权限", description = "根据启用状态查询权限")
    @GetMapping("/by-status")
    public ApiResponse<PageResponse<PermissionResponse>> getPermissionsByEnabled(
            @Parameter(description = "启用状态") @RequestParam Boolean enabled,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Permission> permissions = permissionService.getPermissionsByEnabled(enabled, pageable);
        Page<PermissionResponse> permissionResponses = permissions.map(PermissionResponse::from);
        
        return ApiResponse.success(PageResponse.of(permissionResponses));
    }

    @Operation(summary = "更新权限", description = "根据ID更新权限信息")
    @PutMapping("/{id}")
    public ApiResponse<PermissionResponse> updatePermission(
            @Parameter(description = "权限ID") @PathVariable Long id,
            @Valid @RequestBody PermissionUpdateRequest request) {
        Permission permission = permissionService.updatePermission(id, request);
        return ApiResponse.success("权限更新成功", PermissionResponse.from(permission));
    }

    @Operation(summary = "删除权限", description = "根据ID删除权限")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(
            @Parameter(description = "权限ID") @PathVariable Long id) {
        permissionService.deletePermission(id);
        return ApiResponse.success("权限删除成功", null);
    }

    @Operation(summary = "批量删除权限", description = "根据ID列表批量删除权限")
    @DeleteMapping("/batch")
    public ApiResponse<Void> deletePermissions(
            @Parameter(description = "权限ID列表") @RequestBody Set<Long> ids) {
        permissionService.deletePermissions(ids);
        return ApiResponse.success("批量删除成功", null);
    }
}
