package com.example.springdemo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springdemo.dto.UserCreateRequest;
import com.example.springdemo.dto.UserUpdateRequest;
import com.example.springdemo.entity.User;

/**
 * 用户服务接口
 * 定义用户管理的所有业务操作
 *
 * Service层职责：
 * 1. 业务逻辑处理
 * 2. 事务管理
 * 3. 调用Mapper层进行数据访问
 * 4. 数据转换（DTO ↔ Entity）
 * 5. 业务规则验证
 */
public interface UserService {

    /**
     * 创建用户
     *
     * 业务规则：
     * - 用户名不能重复
     * - 邮箱不能重复（可选）
     * - 密码需要加密存储
     *
     * @param request 创建请求DTO
     * @return 创建成功的用户实体（包含数据库生成的ID）
     */
    User createUser(UserCreateRequest request);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户实体
     * @throws com.example.springdemo.exception.ResourceNotFoundException 用户不存在
     */
    User getUserById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体
     * @throws com.example.springdemo.exception.ResourceNotFoundException 用户不存在
     */
    User getUserByUsername(String username);

    /**
     * 查询所有用户（不分页）
     *
     * @return 所有未删除的用户列表
     */
    java.util.List<User> getAllUsers();

    /**
     * 分页查询用户
     *
     * @param current 当前页码（从1开始）
     * @param size    每页数量
     * @return 分页结果
     */
    Page<User> getUsersByPage(int current, int size);

    /**
     * 更新用户
     *
     * @param id      用户ID
     * @param request 更新请求DTO
     * @return 更新后的用户实体
     * @throws com.example.springdemo.exception.ResourceNotFoundException 用户不存在
     */
    User updateUser(Long id, UserUpdateRequest request);

    /**
     * 删除用户（逻辑删除）
     *
     * @param id 用户ID
     * @throws com.example.springdemo.exception.ResourceNotFoundException 用户不存在
     */
    void deleteUser(Long id);

    /**
     * 根据用户名模糊搜索
     *
     * @param keyword 搜索关键字
     * @return 匹配的用户列表
     */
    java.util.List<User> searchUsers(String keyword);

    /**
     * 根据角色查询用户
     *
     * @param role 用户角色
     * @return 该角色的用户列表
     */
    java.util.List<User> getUsersByRole(String role);

    /**
     * 根据状态查询用户
     *
     * @param status 用户状态（0-禁用，1-正常）
     * @return 该状态的用户列表
     */
    java.util.List<User> getUsersByStatus(Integer status);
}
