package com.example.springdemo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springdemo.dto.UserCreateRequest;
import com.example.springdemo.dto.UserResponse;
import com.example.springdemo.dto.UserUpdateRequest;
import com.example.springdemo.entity.User;
import com.example.springdemo.service.UserService;
import com.example.springdemo.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 用户控制器
 * 提供用户管理的RESTful API端点
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 创建用户
     *
     * RESTful API:
     * POST /api/users
     *
     * 请求示例：
     * POST http://localhost:8080/api/users
     * Content-Type: application/json
     * {
     *   "username": "zhangsan",
     *   "password": "123456",
     *   "email": "zhangsan@example.com",
     *   "phone": "13800138000",
     *   "nickname": "张三",
     *   "status": 1,
     *   "role": "user"
     * }
     */
    @PostMapping
    public Result<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("创建用户请求: username={}", request.getUsername());
        User user = userService.createUser(request);
        UserResponse response = UserResponse.fromEntity(user);
        log.info("用户创建成功: id={}", user.getId());
        return Result.success(response);
    }

    /**
     * 根据ID查询用户
     *
     * RESTful API:
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public Result<UserResponse> getUser(@PathVariable Long id) {
        log.info("查询用户请求: id={}", id);
        User user = userService.getUserById(id);
        UserResponse response = UserResponse.fromEntity(user);
        return Result.success(response);
    }

    /**
     * 根据用户名查询用户
     *
     * RESTful API:
     * GET /api/users/username/{username}
     */
    @GetMapping("/username/{username}")
    public Result<UserResponse> getUserByUsername(@PathVariable String username) {
        log.info("查询用户请求: username={}", username);
        User user = userService.getUserByUsername(username);
        UserResponse response = UserResponse.fromEntity(user);
        return Result.success(response);
    }

    /**
     * 分页查询用户列表
     *
     * RESTful API:
     * GET /api/users?page=1&size=10
     */
    @GetMapping
    public Result<Page<UserResponse>> getUsersByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询用户请求: page={}, size={}", page, size);
        Page<User> userPage = userService.getUsersByPage(page, size);
        Page<UserResponse> responsePage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        responsePage.setRecords(UserResponse.fromEntityList(userPage.getRecords()));
        return Result.success(responsePage);
    }

    /**
     * 更新用户
     *
     * RESTful API:
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public Result<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        log.info("更新用户请求: id={}", id);
        User user = userService.updateUser(id, request);
        UserResponse response = UserResponse.fromEntity(user);
        return Result.success(response);
    }

    /**
     * 删除用户（逻辑删除）
     *
     * RESTful API:
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        log.info("删除用户请求: id={}", id);
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 搜索用户（根据用户名模糊搜索）
     *
     * RESTful API:
     * GET /api/users/search?keyword=zhang
     */
    @GetMapping("/search")
    public Result<List<UserResponse>> searchUsers(@RequestParam String keyword) {
        log.info("搜索用户请求: keyword={}", keyword);
        List<User> users = userService.searchUsers(keyword);
        List<UserResponse> responses = UserResponse.fromEntityList(users);
        return Result.success(responses);
    }

    /**
     * 根据角色查询用户
     *
     * RESTful API:
     * GET /api/users/role/{role}
     */
    @GetMapping("/role/{role}")
    public Result<List<UserResponse>> getUsersByRole(@PathVariable String role) {
        log.info("查询角色为 {} 的用户", role);
        List<User> users = userService.getUsersByRole(role);
        List<UserResponse> responses = UserResponse.fromEntityList(users);
        return Result.success(responses);
    }

    /**
     * 根据状态查询用户
     *
     * RESTful API:
     * GET /api/users/status/{status}
     */
    @GetMapping("/status/{status}")
    public Result<List<UserResponse>> getUsersByStatus(@PathVariable Integer status) {
        log.info("查询状态为 {} 的用户", status);
        List<User> users = userService.getUsersByStatus(status);
        List<UserResponse> responses = UserResponse.fromEntityList(users);
        return Result.success(responses);
    }
}
