package com.example.springdemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新用户请求DTO
 * 用于接收客户端提交的更新用户数据
 *
 * 设计特点：
 * - 所有字段都是可选的（支持部分更新）
 * - 只更新客户端提供的字段
 * - 未提供的字段保持原值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    /**
     * 用户名（可选）
     */
    @Size(min = 3, max = 30, message = "用户名长度必须在3-30个字符之间")
    private String username;

    /**
     * 密码（可选）
     */
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    private String password;

    /**
     * 邮箱（可选）
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 手机号（可选）
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 昵称（可选）
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    /**
     * 头像URL（可选）
     */
    @Size(max = 500, message = "头像URL长度不能超过500个字符")
    private String avatar;

    /**
     * 用户状态（可选）
     */
    private Integer status;

    /**
     * 用户角色（可选）
     */
    @Size(max = 20, message = "角色长度不能超过20个字符")
    private String role;

    // 判断字段是否提供的方法
    public boolean hasUsername() {
        return username != null;
    }

    public boolean hasPassword() {
        return password != null;
    }

    public boolean hasEmail() {
        return email != null;
    }

    public boolean hasPhone() {
        return phone != null;
    }

    public boolean hasNickname() {
        return nickname != null;
    }

    public boolean hasAvatar() {
        return avatar != null;
    }

    public boolean hasStatus() {
        return status != null;
    }

    public boolean hasRole() {
        return role != null;
    }

    public boolean hasAnyField() {
        return hasUsername() || hasPassword() || hasEmail() || hasPhone()
                || hasNickname() || hasAvatar() || hasStatus() || hasRole();
    }
}
