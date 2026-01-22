package com.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * 角色创建请求DTO
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Data
public class RoleCreateRequest {

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 50, message = "角色名称长度必须在2-50之间")
    private String name;

    @NotBlank(message = "角色编码不能为空")
    @Size(min = 2, max = 50, message = "角色编码长度必须在2-50之间")
    @Pattern(regexp = "^[A-Z_]+$", message = "角色编码只能包含大写字母和下划线")
    private String code;

    @Size(max = 200, message = "描述长度不能超过200")
    private String description;

    private Set<Long> permissionIds;
}
