package com.example.springdemo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springdemo.dto.CategoryCreateRequest;
import com.example.springdemo.dto.CategoryUpdateRequest;
import com.example.springdemo.entity.Category;

/**
 * 分类服务接口
 * 定义分类管理的所有业务操作
 *
 * Service层职责：
 * 1. 业务逻辑处理
 * 2. 事务管理
 * 3. 调用Mapper层进行数据访问
 * 4. 数据转换（DTO ↔ Entity）
 * 5. 业务规则验证
 *
 * 为什么需要Service接口？
 * - 面向接口编程，降低耦合
 * - 便于单元测试（可以Mock）
 * - 符合分层架构规范
 * - 便于扩展（可以有多个实现）
 */
public interface CategoryService {

    /**
     * 创建分类
     *
     * 业务规则：
     * - 分类名称不能重复（可选）
     * - 排序序号必须大于等于0（已在DTO层验证）
     *
     * @param request 创建请求DTO
     * @return 创建成功的分类实体（包含数据库生成的ID）
     */
    Category createCategory(CategoryCreateRequest request);

    /**
     * 根据ID查询分类
     *
     * 业务规则：
     * - 如果分类不存在，抛出ResourceNotFoundException
     * - 自动过滤已删除的分类（MyBatis-Plus自动处理）
     *
     * @param id 分类ID
     * @return 分类实体
     * @throws com.example.springdemo.exception.ResourceNotFoundException 分类不存在
     */
    Category getCategoryById(Long id);

    /**
     * 查询所有分类（不分页）
     *
     * 注意：数据量大时建议使用分页查询
     *
     * @return 所有未删除的分类列表
     */
    java.util.List<Category> getAllCategories();

    /**
     * 分页查询分类
     *
     * 使用场景：
     * - 前端表格显示（每页10条、20条、50条）
     * - 避免一次性加载大量数据
     *
     * @param current 当前页码（从1开始）
     * @param size    每页数量
     * @return 分页结果（包含数据列表、总记录数、总页数）
     */
    Page<Category> getCategoriesByPage(int current, int size);

    /**
     * 更新分类
     *
     * 业务规则：
     * - 如果分类不存在，抛出ResourceNotFoundException
     * - 只更新请求中提供的字段（部分更新）
     * - updateTime会自动更新（MyBatis-Plus自动处理）
     *
     * @param id      分类ID
     * @param request 更新请求DTO
     * @return 更新后的分类实体
     * @throws com.example.springdemo.exception.ResourceNotFoundException 分类不存在
     */
    Category updateCategory(Long id, CategoryUpdateRequest request);

    /**
     * 删除分类（逻辑删除）
     *
     * 业务规则：
     * - 如果分类不存在，抛出ResourceNotFoundException
     * - 不会物理删除数据，只将deleted字段设置为1
     * - 删除后的分类不会被查询到（MyBatis-Plus自动处理）
     *
     * @param id 分类ID
     * @throws com.example.springdemo.exception.ResourceNotFoundException 分类不存在
     */
    void deleteCategory(Long id);

    /**
     * 根据分类名称模糊搜索
     *
     * 使用场景：
     * - 前端搜索框输入关键字
     * - 显示匹配的分类列表
     *
     * @param keyword 搜索关键字（支持模糊匹配）
     * @return 匹配的分类列表
     */
    java.util.List<Category> searchCategories(String keyword);

    /**
     * 查询分类列表（按排序序号排序）
     *
     * 使用场景：
     * - 前端显示有序的分类列表
     *
     * @return 按sortOrder升序排列的分类列表
     */
    java.util.List<Category> getCategoriesSorted();
}
