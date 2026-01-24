package com.example.springdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springdemo.dto.CategoryCreateRequest;
import com.example.springdemo.dto.CategoryUpdateRequest;
import com.example.springdemo.entity.Category;
import com.example.springdemo.exception.BusinessException;
import com.example.springdemo.exception.ResourceNotFoundException;
import com.example.springdemo.mapper.CategoryMapper;
import com.example.springdemo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类服务实现类
 * 实现分类管理的核心业务逻辑
 *
 * 关键注解：
 * - @Service: 标记为Service组件，Spring自动扫描注册
 * - @Slf4j: Lombok自动生成log变量，用于日志记录
 * - @Transactional: 开启事务管理
 */
@Slf4j  // Lombok：自动生成 private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
@Service  // 标记为Spring Service组件
public class CategoryServiceImpl implements CategoryService {

    /**
     * 注入CategoryMapper
     *
     * @Autowired: Spring自动注入（自动从容器中查找CategoryMapper类型的Bean）
     *
     * 为什么使用接口而不是实现类？
     * - 面向接口编程
     * - 便于测试（可以Mock）
     * - MyBatis-Plus会动态生成代理实现类
     */
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 创建分类
     *
     * 实现步骤：
     * 1. 将DTO转换为Entity
     * 2. 调用Mapper插入数据库
     * 3. 返回插入后的Entity（包含自增ID）
     *
     * @Transactional: 事务管理
     * - 如果方法抛出异常，自动回滚
     * - 如果方法正常结束，自动提交
     */
    @Override
    @Transactional(rollbackFor = Exception.class)  // 任何异常都回滚
    public Category createCategory(CategoryCreateRequest request) {
        log.info("创建分类: name={}, sortOrder={}", request.getName(), request.getSortOrder());

        // 1. DTO → Entity转换
        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        // copyProperties会复制同名属性：
        // - name → name
        // - description → description
        // - sortOrder → sortOrder
        // createTime和updateTime会自动填充（MyBatisPlusConfig配置的MetaObjectHandler）
        // id会自动生成（IdType.AUTO）

        // 2. 插入数据库
        int result = categoryMapper.insert(category);

        if (result <= 0) {
            throw new BusinessException("创建分类失败");
        }

        log.info("分类创建成功: id={}, name={}", category.getId(), category.getName());

        // 3. 返回包含ID的Entity
        return category;
    }

    /**
     * 根据ID查询分类
     *
     * 实现步骤：
     * 1. 调用Mapper查询
     * 2. 如果不存在，抛出ResourceNotFoundException
     * 3. 返回查询结果
     *
     * 注意：
     * - MyBatis-Plus会自动添加 WHERE deleted = 0（逻辑删除）
     * - 如果查询已删除的分类，会返回null
     */
    @Override
    public Category getCategoryById(Long id) {
        log.info("查询分类: id={}", id);

        // 1. 根据ID查询
        Category category = categoryMapper.selectById(id);

        // 2. 判断是否存在
        if (category == null) {
            log.warn("分类不存在: id={}", id);
            throw ResourceNotFoundException.of("分类", id);
        }

        log.info("查询成功: id={}, name={}", category.getId(), category.getName());

        return category;
    }

    /**
     * 查询所有分类
     *
     * 注意：
     * - 数据量大时不要使用此方法
     * - 建议使用分页查询 getCategoriesByPage()
     */
    @Override
    public List<Category> getAllCategories() {
        log.info("查询所有分类");

        // 查询所有未删除的分类
        // MyBatis-Plus自动添加 WHERE deleted = 0
        List<Category> categories = categoryMapper.selectList(null);

        log.info("查询到 {} 个分类", categories.size());

        return categories;
    }

    /**
     * 分页查询分类
     *
     * @param current 当前页码
     * @param size    每页数量
     * @return 分页结果
     */
    @Override
    public Page<Category> getCategoriesByPage(int current, int size) {
        log.info("查询分类列表: current={}, size={}", current, size);

        // 暂时返回所有分类
        List<Category> categories = categoryMapper.selectList(null);

        log.info("查询成功: 共 {} 个分类", categories.size());

        // 创建一个简单的Page对象返回（兼容接口）
        Page<Category> page = new Page<>(current, size);
        page.setRecords(categories);
        page.setTotal(categories.size());

        return page;
    }

    /**
     * 更新分类
     *
     * 实现步骤：
     * 1. 先查询分类是否存在
     * 2. 更新请求中提供的字段（部分更新）
     * 3. 调用Mapper更新数据库
     * 4. 返回更新后的分类
     *
     * 注意：
     * - updateTime会自动更新（MyBatis-Plus自动处理）
     * - 只更新提供的字段，未提供的字段保持原值
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category updateCategory(Long id, CategoryUpdateRequest request) {
        log.info("更新分类: id={}", id);

        // 1. 先查询分类是否存在
        Category category = getCategoryById(id);  // 如果不存在会抛出ResourceNotFoundException

        // 2. 部分更新：只更新请求中提供的字段
        if (request.hasName()) {
            category.setName(request.getName());
            log.info("  更新名称: {}", request.getName());
        }

        if (request.hasDescription()) {
            category.setDescription(request.getDescription());
            log.info("  更新描述: {}", request.getDescription());
        }

        if (request.hasSortOrder()) {
            category.setSortOrder(request.getSortOrder());
            log.info("  更新排序序号: {}", request.getSortOrder());
        }

        // 3. 执行更新
        int result = categoryMapper.updateById(category);

        if (result <= 0) {
            throw new BusinessException("更新分类失败");
        }

        log.info("分类更新成功: id={}", id);

        // 4. 返回更新后的分类
        return category;
    }

    /**
     * 删除分类（逻辑删除）
     *
     * 实现步骤：
     * 1. 先查询分类是否存在
     * 2. 调用Mapper删除（逻辑删除）
     * 3. MyBatis-Plus会自动设置 deleted=1
     *
     * 注意：
     * - 不是物理删除（DELETE FROM）
     * - 是逻辑删除（UPDATE SET deleted=1）
     * - 删除后的分类不会被查询到
     * - 可以恢复（将deleted改回0）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        log.info("删除分类: id={}", id);

        // 1. 先查询分类是否存在（可选，为了返回友好的错误提示）
        Category category = getCategoryById(id);

        // 2. 执行逻辑删除
        // MyBatis-Plus会自动执行: UPDATE category SET deleted=1 WHERE id=? AND deleted=0
        int result = categoryMapper.deleteById(id);

        if (result <= 0) {
            throw new BusinessException("删除分类失败");
        }

        log.info("分类删除成功: id={}, name={}", id, category.getName());
    }

    /**
     * 根据分类名称模糊搜索
     *
     * 实现步骤：
     * 1. 创建QueryWrapper（查询条件构造器）
     * 2. 添加模糊查询条件
     * 3. 调用Mapper查询
     * 4. 返回结果
     *
     * QueryWrapper常用方法：
     * - like: 模糊查询（WHERE name LIKE '%关键字%'）
     * - eq: 等于（WHERE status = 1）
     * - orderByAsc: 升序排序（ORDER BY sort_order ASC）
     */
    @Override
    public List<Category> searchCategories(String keyword) {
        log.info("搜索分类: keyword={}", keyword);

        // 1. 创建查询条件构造器
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();

        // 2. 添加模糊查询条件
        // SQL: WHERE name LIKE '%keyword%'
        queryWrapper.like("name", keyword);

        // 3. 按排序序号升序排序
        // SQL: ORDER BY sort_order ASC
        queryWrapper.orderByAsc("sort_order");

        // 4. 执行查询
        List<Category> categories = categoryMapper.selectList(queryWrapper);

        log.info("搜索到 {} 个分类", categories.size());

        return categories;
    }

    /**
     * 查询分类列表（按排序序号排序）
     *
     * 使用场景：
     * - 前端显示有序的分类列表（如导航栏、侧边栏）
     */
    @Override
    public List<Category> getCategoriesSorted() {
        log.info("查询排序后的分类列表");

        // 1. 创建查询条件构造器
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();

        // 2. 按排序序号升序排序
        queryWrapper.orderByAsc("sort_order");

        // 3. 执行查询
        List<Category> categories = categoryMapper.selectList(queryWrapper);

        log.info("查询到 {} 个分类", categories.size());

        return categories;
    }
}
