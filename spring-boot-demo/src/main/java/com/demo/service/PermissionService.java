package com.demo.service;

import com.demo.dto.request.PermissionCreateRequest;
import com.demo.dto.request.PermissionUpdateRequest;
import com.demo.entity.Permission;
import com.demo.exception.ResourceAlreadyExistsException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 权限服务类
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    /**
     * 创建权限
     */
    @Transactional
    public Permission createPermission(PermissionCreateRequest request) {
        log.info("创建权限: {}", request.getName());

        // 检查权限编码是否已存在
        if (permissionRepository.existsByCode(request.getCode())) {
            throw new ResourceAlreadyExistsException("权限", "code", request.getCode());
        }

        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setCode(request.getCode());
        permission.setDescription(request.getDescription());
        permission.setResource(request.getResource());
        permission.setAction(request.getAction());

        return permissionRepository.save(permission);
    }

    /**
     * 根据ID获取权限
     */
    public Permission getPermissionById(Long id) {
        log.info("获取权限: {}", id);
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("权限", "id", id));
    }

    /**
     * 根据编码获取权限
     */
    public Permission getPermissionByCode(String code) {
        log.info("根据编码获取权限: {}", code);
        return permissionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("权限", "code", code));
    }

    /**
     * 获取所有权限（分页）
     */
    public Page<Permission> getAllPermissions(Pageable pageable) {
        log.info("获取所有权限，页码: {}, 页大小: {}", pageable.getPageNumber(), pageable.getPageSize());
        return permissionRepository.findAll(pageable);
    }

    /**
     * 搜索权限
     */
    public Page<Permission> searchPermissions(String keyword, Pageable pageable) {
        log.info("搜索权限: {}", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return permissionRepository.findAll(pageable);
        }
        return permissionRepository.searchByKeyword(keyword, pageable);
    }

    /**
     * 根据资源查询权限
     */
    public Page<Permission> getPermissionsByResource(String resource, Pageable pageable) {
        log.info("根据资源查询权限: {}", resource);
        return permissionRepository.findByResource(resource, pageable);
    }

    /**
     * 根据状态查询权限
     */
    public Page<Permission> getPermissionsByEnabled(Boolean enabled, Pageable pageable) {
        log.info("根据启用状态查询权限: {}", enabled);
        return permissionRepository.findByEnabled(enabled, pageable);
    }

    /**
     * 更新权限
     */
    @Transactional
    public Permission updatePermission(Long id, PermissionUpdateRequest request) {
        log.info("更新权限: {}", id);

        Permission permission = getPermissionById(id);

        // 更新字段
        if (request.getName() != null) {
            permission.setName(request.getName());
        }
        if (request.getDescription() != null) {
            permission.setDescription(request.getDescription());
        }
        if (request.getResource() != null) {
            permission.setResource(request.getResource());
        }
        if (request.getAction() != null) {
            permission.setAction(request.getAction());
        }
        if (request.getEnabled() != null) {
            permission.setEnabled(request.getEnabled());
        }

        return permissionRepository.save(permission);
    }

    /**
     * 删除权限
     */
    @Transactional
    public void deletePermission(Long id) {
        log.info("删除权限: {}", id);
        Permission permission = getPermissionById(id);
        permissionRepository.delete(permission);
    }

    /**
     * 批量删除权限
     */
    @Transactional
    public void deletePermissions(Set<Long> ids) {
        log.info("批量删除权限: {}", ids);
        for (Long id : ids) {
            deletePermission(id);
        }
    }
}
