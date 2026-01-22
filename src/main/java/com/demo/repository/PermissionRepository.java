package com.demo.repository;

import com.demo.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 权限Repository接口
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * 根据权限编码查找权限
     */
    Optional<Permission> findByCode(String code);

    /**
     * 检查权限编码是否存在
     */
    boolean existsByCode(String code);

    /**
     * 根据资源和操作查询
     */
    Optional<Permission> findByResourceAndAction(String resource, String action);

    /**
     * 根据关键词模糊查询
     */
    @Query("SELECT p FROM Permission p WHERE p.name LIKE %:keyword% OR p.code LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Permission> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 查询启用状态的权限
     */
    Page<Permission> findByEnabled(Boolean enabled, Pageable pageable);

    /**
     * 根据资源查询权限
     */
    Page<Permission> findByResource(String resource, Pageable pageable);
}
