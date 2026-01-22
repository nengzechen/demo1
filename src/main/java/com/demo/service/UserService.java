package com.demo.service;

import com.demo.dto.request.UserCreateRequest;
import com.demo.dto.request.UserUpdateRequest;
import com.demo.entity.Role;
import com.demo.entity.User;
import com.demo.exception.ResourceAlreadyExistsException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户服务类
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 创建用户
     */
    @Transactional
    public User createUser(UserCreateRequest request) {
        log.info("创建用户: {}", request.getUsername());

        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("用户", "username", request.getUsername());
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("用户", "email", request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRealName(request.getRealName());

        // 设置角色
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : request.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("角色", "id", roleId));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    /**
     * 根据ID获取用户
     */
    public User getUserById(Long id) {
        log.info("获取用户: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户", "id", id));
    }

    /**
     * 根据用户名获取用户
     */
    public User getUserByUsername(String username) {
        log.info("根据用户名获取用户: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户", "username", username));
    }

    /**
     * 获取所有用户（分页）
     */
    public Page<User> getAllUsers(Pageable pageable) {
        log.info("获取所有用户，页码: {}, 页大小: {}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable);
    }

    /**
     * 搜索用户
     */
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        log.info("搜索用户: {}", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.searchByKeyword(keyword, pageable);
    }

    /**
     * 根据状态查询用户
     */
    public Page<User> getUsersByEnabled(Boolean enabled, Pageable pageable) {
        log.info("根据启用状态查询用户: {}", enabled);
        return userRepository.findByEnabled(enabled, pageable);
    }

    /**
     * 更新用户
     */
    @Transactional
    public User updateUser(Long id, UserUpdateRequest request) {
        log.info("更新用户: {}", id);

        User user = getUserById(id);

        // 更新密码
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // 更新邮箱（检查是否重复）
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ResourceAlreadyExistsException("用户", "email", request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        // 更新其他字段
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getRealName() != null) {
            user.setRealName(request.getRealName());
        }
        if (request.getEnabled() != null) {
            user.setEnabled(request.getEnabled());
        }
        if (request.getLocked() != null) {
            user.setLocked(request.getLocked());
        }

        // 更新角色
        if (request.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : request.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("角色", "id", roleId));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("删除用户: {}", id);
        User user = getUserById(id);
        userRepository.delete(user);
    }

    /**
     * 批量删除用户
     */
    @Transactional
    public void deleteUsers(Set<Long> ids) {
        log.info("批量删除用户: {}", ids);
        for (Long id : ids) {
            deleteUser(id);
        }
    }
}
