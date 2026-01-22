package com.demo.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 权限更新请求DTO
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Data
public class PermissionUpdateRequest {

    @Size(min = 2, max = 50, message = "权限名称长度必须在2-50之间")
    private String name;

    @Size(max = 200, message = "描述长度不能超过200")
    private String description;

    @Size(max = 50, message = "资源长度不能超过50")
    private String resource;

    @Size(max = 20, message = "操作长度不能超过20")
    @Pattern(regexp = "^(CREATE|READ|UPDATE|DELETE|ALL)$", message = "操作必须是CREATE、READ、UPDATE、DELETE或ALL")
    private String action;

    private Boolean enabled;
}
