package com.demo.service;

import com.demo.dto.request.RoleCreateRequest;
import com.demo.dto.request.RoleUpdateRequest;
import com.demo.entity.Permission;
import com.demo.entity.Role;
import com.demo.exception.ResourceAlreadyExistsException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.repository.PermissionRepository;
import com.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色服务类
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    /**
     * 创建角色
     */
    @Transactional
    public Role createRole(RoleCreateRequest request) {
        log.info("创建角色: {}", request.getName());

        // 检查角色名称是否已存在
        if (roleRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("角色", "name", request.getName());
        }

        // 检查角色编码是否已存在
        if (roleRepository.existsByCode(request.getCode())) {
            throw new ResourceAlreadyExistsException("角色", "code", request.getCode());
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setCode(request.getCode());
        role.setDescription(request.getDescription());

        // 设置权限
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>();
            for (Long permissionId : request.getPermissionIds()) {
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new ResourceNotFoundException("权限", "id", permissionId));
                permissions.add(permission);
            }
            role.setPermissions(permissions);
        }

        return roleRepository.save(role);
    }

    /**
     * 根据ID获取角色
     */
    public Role getRoleById(Long id) {
        log.info("获取角色: {}", id);
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("角色", "id", id));
    }

    /**
     * 根据编码获取角色
     */
    public Role getRoleByCode(String code) {
        log.info("根据编码获取角色: {}", code);
        return roleRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("角色", "code", code));
    }

    /**
     * 获取所有角色（分页）
     */
    public Page<Role> getAllRoles(Pageable pageable) {
        log.info("获取所有角色，页码: {}, 页大小: {}", pageable.getPageNumber(), pageable.getPageSize());
        return roleRepository.findAll(pageable);
    }

    /**
     * 搜索角色
     */
    public Page<Role> searchRoles(String keyword, Pageable pageable) {
        log.info("搜索角色: {}", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return roleRepository.findAll(pageable);
        }
        return roleRepository.searchByKeyword(keyword, pageable);
    }

    /**
     * 根据状态查询角色
     */
    public Page<Role> getRolesByEnabled(Boolean enabled, Pageable pageable) {
        log.info("根据启用状态查询角色: {}", enabled);
        return roleRepository.findByEnabled(enabled, pageable);
    }

    /**
     * 更新角色
     */
    @Transactional
    public Role updateRole(Long id, RoleUpdateRequest request) {
        log.info("更新角色: {}", id);

        Role role = getRoleById(id);

        // 更新名称（检查是否重复）
        if (request.getName() != null && !request.getName().equals(role.getName())) {
            if (roleRepository.existsByName(request.getName())) {
                throw new ResourceAlreadyExistsException("角色", "name", request.getName());
            }
            role.setName(request.getName());
        }

        // 更新其他字段
        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }
        if (request.getEnabled() != null) {
            role.setEnabled(request.getEnabled());
        }

        // 更新权限
        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = new HashSet<>();
            for (Long permissionId : request.getPermissionIds()) {
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new ResourceNotFoundException("权限", "id", permissionId));
                permissions.add(permission);
            }
            role.setPermissions(permissions);
        }

        return roleRepository.save(role);
    }

    /**
     * 删除角色
     */
    @Transactional
    public void deleteRole(Long id) {
        log.info("删除角色: {}", id);
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }

    /**
     * 批量删除角色
     */
    @Transactional
    public void deleteRoles(Set<Long> ids) {
        log.info("批量删除角色: {}", ids);
        for (Long id : ids) {
            deleteRole(id);
        }
    }
}
