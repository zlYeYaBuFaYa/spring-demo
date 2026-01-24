package com.example.springdemo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 使用MyBatis-Plus注解实现ORM映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {

    /**
     * 主键ID - 自增策略
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（登录账号）
     * 唯一标识，不能重复
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 30, message = "用户名长度必须在3-30个字符之间")
    private String username;

    /**
     * 密码（加密存储）
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    private String password;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    /**
     * 头像URL
     */
    @Size(max = 500, message = "头像URL长度不能超过500个字符")
    private String avatar;

    /**
     * 用户状态
     * 0-禁用，1-正常
     */
    @NotNull(message = "用户状态不能为空")
    private Integer status;

    /**
     * 用户角色
     * admin-管理员，user-普通用户
     */
    @Size(max = 20, message = "角色长度不能超过20个字符")
    private String role;

    /**
     * 逻辑删除标志
     * 0-未删除，1-已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
