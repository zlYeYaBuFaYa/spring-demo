package com.example.springdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springdemo.dto.UserCreateRequest;
import com.example.springdemo.dto.UserUpdateRequest;
import com.example.springdemo.entity.User;
import com.example.springdemo.exception.BusinessException;
import com.example.springdemo.exception.ResourceNotFoundException;
import com.example.springdemo.mapper.UserMapper;
import com.example.springdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类
 * 实现用户管理的核心业务逻辑
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 创建用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User createUser(UserCreateRequest request) {
        log.info("创建用户: username={}", request.getUsername());

        // 1. 检查用户名是否已存在
        QueryWrapper<User> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("username", request.getUsername());
        if (userMapper.selectCount(checkWrapper) > 0) {
            throw new BusinessException("用户名已存在: " + request.getUsername());
        }

        // 2. DTO → Entity转换
        User user = new User();
        BeanUtils.copyProperties(request, user);

        // TODO: 密码需要加密（建议使用 BCrypt）
        // user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. 插入数据库
        int result = userMapper.insert(user);
        if (result <= 0) {
            throw new BusinessException("创建用户失败");
        }

        log.info("用户创建成功: id={}, username={}", user.getId(), user.getUsername());
        return user;
    }

    /**
     * 根据ID查询用户
     */
    @Override
    public User getUserById(Long id) {
        log.info("查询用户: id={}", id);
        User user = userMapper.selectById(id);
        if (user == null) {
            log.warn("用户不存在: id={}", id);
            throw ResourceNotFoundException.of("用户", id);
        }
        return user;
    }

    /**
     * 根据用户名查询用户
     */
    @Override
    public User getUserByUsername(String username) {
        log.info("查询用户: username={}", username);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            log.warn("用户不存在: username={}", username);
            throw new ResourceNotFoundException("用户不存在: " + username);
        }
        return user;
    }

    /**
     * 查询所有用户
     */
    @Override
    public List<User> getAllUsers() {
        log.info("查询所有用户");
        List<User> users = userMapper.selectList(null);
        log.info("查询到 {} 个用户", users.size());
        return users;
    }

    /**
     * 分页查询用户
     */
    @Override
    public Page<User> getUsersByPage(int current, int size) {
        log.info("查询用户列表: current={}, size={}", current, size);
        List<User> users = userMapper.selectList(null);
        log.info("查询成功: 共 {} 个用户", users.size());

        Page<User> page = new Page<>(current, size);
        page.setRecords(users);
        page.setTotal(users.size());
        return page;
    }

    /**
     * 更新用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(Long id, UserUpdateRequest request) {
        log.info("更新用户: id={}", id);

        // 1. 先查询用户是否存在
        User user = getUserById(id);

        // 2. 部分更新：只更新请求中提供的字段
        if (request.hasUsername()) {
            // 检查新用户名是否已被其他用户使用
            QueryWrapper<User> checkWrapper = new QueryWrapper<>();
            checkWrapper.eq("username", request.getUsername());
            checkWrapper.ne("id", id);
            if (userMapper.selectCount(checkWrapper) > 0) {
                throw new BusinessException("用户名已被使用: " + request.getUsername());
            }
            user.setUsername(request.getUsername());
            log.info("  更新用户名: {}", request.getUsername());
        }

        if (request.hasPassword()) {
            user.setPassword(request.getPassword());
            // TODO: 密码需要加密
            log.info("  更新密码: ***");
        }

        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
            log.info("  更新邮箱: {}", request.getEmail());
        }

        if (request.hasPhone()) {
            user.setPhone(request.getPhone());
            log.info("  更新手机号: {}", request.getPhone());
        }

        if (request.hasNickname()) {
            user.setNickname(request.getNickname());
            log.info("  更新昵称: {}", request.getNickname());
        }

        if (request.hasAvatar()) {
            user.setAvatar(request.getAvatar());
            log.info("  更新头像: {}", request.getAvatar());
        }

        if (request.hasStatus()) {
            user.setStatus(request.getStatus());
            log.info("  更新状态: {}", request.getStatus());
        }

        if (request.hasRole()) {
            user.setRole(request.getRole());
            log.info("  更新角色: {}", request.getRole());
        }

        // 3. 执行更新
        int result = userMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException("更新用户失败");
        }

        log.info("用户更新成功: id={}", id);
        return user;
    }

    /**
     * 删除用户（逻辑删除）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        log.info("删除用户: id={}", id);
        getUserById(id); // 检查是否存在
        int result = userMapper.deleteById(id);
        if (result <= 0) {
            throw new BusinessException("删除用户失败");
        }
        log.info("用户删除成功: id={}", id);
    }

    /**
     * 根据用户名模糊搜索
     */
    @Override
    public List<User> searchUsers(String keyword) {
        log.info("搜索用户: keyword={}", keyword);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", keyword);
        queryWrapper.orderByDesc("create_time");
        List<User> users = userMapper.selectList(queryWrapper);
        log.info("搜索到 {} 个用户", users.size());
        return users;
    }

    /**
     * 根据角色查询用户
     */
    @Override
    public List<User> getUsersByRole(String role) {
        log.info("查询角色为 {} 的用户", role);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", role);
        queryWrapper.orderByDesc("create_time");
        List<User> users = userMapper.selectList(queryWrapper);
        log.info("查询到 {} 个用户", users.size());
        return users;
    }

    /**
     * 根据状态查询用户
     */
    @Override
    public List<User> getUsersByStatus(Integer status) {
        log.info("查询状态为 {} 的用户", status);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        queryWrapper.orderByDesc("create_time");
        List<User> users = userMapper.selectList(queryWrapper);
        log.info("查询到 {} 个用户", users.size());
        return users;
    }
}
