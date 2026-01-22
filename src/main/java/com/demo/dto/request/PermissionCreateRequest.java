package com.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 权限创建请求DTO
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Data
public class PermissionCreateRequest {

    @NotBlank(message = "权限名称不能为空")
    @Size(min = 2, max = 50, message = "权限名称长度必须在2-50之间")
    private String name;

    @NotBlank(message = "权限编码不能为空")
    @Size(min = 2, max = 100, message = "权限编码长度必须在2-100之间")
    @Pattern(regexp = "^[A-Z_:]+$", message = "权限编码只能包含大写字母、下划线和冒号")
    private String code;

    @Size(max = 200, message = "描述长度不能超过200")
    private String description;

    @NotBlank(message = "资源不能为空")
    @Size(max = 50, message = "资源长度不能超过50")
    private String resource;

    @NotBlank(message = "操作不能为空")
    @Size(max = 20, message = "操作长度不能超过20")
    @Pattern(regexp = "^(CREATE|READ|UPDATE|DELETE|ALL)$", message = "操作必须是CREATE、READ、UPDATE、DELETE或ALL")
    private String action;
}
