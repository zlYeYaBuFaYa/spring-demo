package com.example.springdemo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建用户请求DTO
 * 用于接收客户端提交的创建用户数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 30, message = "用户名长度必须在3-30个字符之间")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
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
     * 0-禁用，1-正常
     * 默认为1（正常）
     */
    private Integer status;

    /**
     * 用户角色（可选）
     * admin-管理员，user-普通用户
     * 默认为user
     */
    @Size(max = 20, message = "角色长度不能超过20个字符")
    private String role;
}
