package com.demo.dto.response;

import com.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色响应DTO
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<PermissionResponse> permissions;

    public static RoleResponse from(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setCode(role.getCode());
        response.setDescription(role.getDescription());
        response.setEnabled(role.getEnabled());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedAt(role.getUpdatedAt());
        if (role.getPermissions() != null) {
            response.setPermissions(role.getPermissions().stream()
                    .map(PermissionResponse::from)
                    .collect(Collectors.toSet()));
        }
        return response;
    }
}
