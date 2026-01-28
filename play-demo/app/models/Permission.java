package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

/**
 * 权限实体类
 */
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "权限名称不能为空")
    @Size(min = 2, max = 50, message = "权限名称长度必须在2-50之间")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @NotBlank(message = "权限编码不能为空")
    @Size(min = 2, max = 100, message = "权限编码长度必须在2-100之间")
    @Pattern(regexp = "^[A-Z_:]+$", message = "权限编码只能包含大写字母、下划线和冒号")
    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Size(max = 200, message = "描述长度不能超过200")
    @Column(length = 200)
    private String description;

    @NotBlank(message = "资源不能为空")
    @Size(max = 50, message = "资源长度不能超过50")
    @Column(nullable = false, length = 50)
    private String resource;

    @NotBlank(message = "操作不能为空")
    @Size(max = 20, message = "操作长度不能超过20")
    @Pattern(regexp = "^(CREATE|READ|UPDATE|DELETE|ALL)$", message = "操作必须是CREATE、READ、UPDATE、DELETE或ALL")
    @Column(nullable = false, length = 20)
    private String action;

    @Column(nullable = false)
    private Boolean enabled = true;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    // Constructors
    public Permission() {}

    public Permission(String name, String code, String resource, String action) {
        this.name = name;
        this.code = code;
        this.resource = resource;
        this.action = action;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
