package com.example.springdemo.dto;

import com.example.springdemo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户响应DTO
 * 用于向客户端返回用户数据
 *
 * 设计原则：
 * - 不暴露密码字段（敏感信息）
 * - 不暴露deleted字段（内部实现细节）
 * - 只包含客户端需要看到的数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 用户状态
     * 0-禁用，1-正常
     */
    private Integer status;

    /**
     * 用户角色
     */
    private String role;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 从Entity转换为Response DTO
     * 注意：不包含密码字段
     */
    public static UserResponse fromEntity(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setStatus(user.getStatus());
        response.setRole(user.getRole());
        response.setCreateTime(user.getCreateTime());
        response.setUpdateTime(user.getUpdateTime());

        return response;
    }

    /**
     * 批量转换
     */
    public static java.util.List<UserResponse> fromEntityList(java.util.List<User> users) {
        if (users == null) {
            return new java.util.ArrayList<>();
        }

        return users.stream()
                .map(UserResponse::fromEntity)
                .collect(java.util.stream.Collectors.toList());
    }
}
