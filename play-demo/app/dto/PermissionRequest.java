package dto;

import jakarta.validation.constraints.*;

/**
 * 权限请求DTO
 */
public class PermissionRequest {
    
    @Size(min = 2, max = 50, message = "权限名称长度必须在2-50之间")
    private String name;

    @Size(min = 2, max = 100, message = "权限编码长度必须在2-100之间")
    @Pattern(regexp = "^[A-Z_:]+$", message = "权限编码只能包含大写字母、下划线和冒号")
    private String code;

    @Size(max = 200, message = "描述长度不能超过200")
    private String description;

    @Size(max = 50, message = "资源长度不能超过50")
    private String resource;

    @Size(max = 20, message = "操作长度不能超过20")
    @Pattern(regexp = "^(CREATE|READ|UPDATE|DELETE|ALL)$", message = "操作必须是CREATE、READ、UPDATE、DELETE或ALL")
    private String action;

    private Boolean enabled;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
