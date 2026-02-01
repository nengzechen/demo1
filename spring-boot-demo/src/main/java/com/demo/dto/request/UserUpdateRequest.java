package com.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * 用户更新请求DTO
 *
 * @author Demo
 * @version 1.0
 * @since 2026-01-22
 */
@Data
public class UserUpdateRequest {

    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Size(max = 50, message = "真实姓名长度不能超过50")
    private String realName;

    private Boolean enabled;

    private Boolean locked;

    private Set<Long> roleIds;
}
