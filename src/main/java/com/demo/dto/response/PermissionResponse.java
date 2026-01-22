package com.demo.dto.response;

import com.demo.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 权限响应DTO
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {

    private Long id;
    private String name;
    private String code;
    private String description;
    private String resource;
    private String action;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PermissionResponse from(Permission permission) {
        PermissionResponse response = new PermissionResponse();
        response.setId(permission.getId());
        response.setName(permission.getName());
        response.setCode(permission.getCode());
        response.setDescription(permission.getDescription());
        response.setResource(permission.getResource());
        response.setAction(permission.getAction());
        response.setEnabled(permission.getEnabled());
        response.setCreatedAt(permission.getCreatedAt());
        response.setUpdatedAt(permission.getUpdatedAt());
        return response;
    }
}
