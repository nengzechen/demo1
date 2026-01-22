package com.demo.repository;

import com.demo.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色Repository接口
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 根据角色编码查找角色
     */
    Optional<Role> findByCode(String code);

    /**
     * 根据角色名称查找角色
     */
    Optional<Role> findByName(String name);

    /**
     * 检查角色编码是否存在
     */
    boolean existsByCode(String code);

    /**
     * 检查角色名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 根据关键词模糊查询
     */
    @Query("SELECT r FROM Role r WHERE r.name LIKE %:keyword% OR r.code LIKE %:keyword% OR r.description LIKE %:keyword%")
    Page<Role> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 查询启用状态的角色
     */
    Page<Role> findByEnabled(Boolean enabled, Pageable pageable);
}
