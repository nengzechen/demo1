package com.demo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * 角色更新请求DTO
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Data
public class RoleUpdateRequest {

    @Size(min = 2, max = 50, message = "角色名称长度必须在2-50之间")
    private String name;

    @Size(max = 200, message = "描述长度不能超过200")
    private String description;

    private Boolean enabled;

    private Set<Long> permissionIds;
}
