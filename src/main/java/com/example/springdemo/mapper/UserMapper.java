package com.example.springdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * User Mapper接口
 * MyBatis-Plus的数据访问层
 *
 * 继承BaseMapper后，自动获得以下CRUD方法：
 * - insert(User entity): 插入一条记录
 * - deleteById(Serializable id): 根据ID删除
 * - updateById(User entity): 根据ID更新
 * - selectById(Serializable id): 根据ID查询
 * - selectList(Wrapper wrapper): 条件查询列表
 * - selectPage(Page page, Wrapper wrapper): 分页查询
 * ... 等等
 *
 * 无需编写XML文件，MyBatis-Plus自动生成SQL
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承BaseMapper<User>后，已经拥有所有基础CRUD方法

    // 如果需要自定义SQL，可以在这里添加方法定义
    // 例如：
    // User selectByUsername(@Param("username") String username);
    // List<User> selectByRole(@Param("role") String role);

    // 复杂查询建议使用MyBatis-Plus的QueryWrapper（在Service层实现）
}
