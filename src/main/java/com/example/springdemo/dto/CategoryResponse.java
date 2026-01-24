package com.example.springdemo.dto;

import com.example.springdemo.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 分类响应DTO
 * 用于向客户端返回分类数据
 *
 * 为什么使用Response DTO而不是直接返回Entity？
 * 1. 安全性：不暴露敏感字段（如deleted逻辑删除标志）
 * 2. 灵活性：可以格式化数据（如日期格式）
 * 3. 解耦：Entity结构变化不会影响API响应
 * 4. 性能：可以只包含客户端需要的字段
 *
 * 设计原则：
 * - 只包含客户端需要看到的数据
 * - 不包含内部实现细节（如deleted字段）
 * - 可以添加计算字段（如商品数量）
 */
@Data  // Lombok：生成getter/setter
@NoArgsConstructor  // 无参构造
@AllArgsConstructor  // 全参构造
public class CategoryResponse {

    /**
     * 分类ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 排序序号
     */
    private Integer sortOrder;

    /**
     * 创建时间
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime updateTime;

    /**
     * 从Entity转换为Response DTO
     *
     * 使用场景：
     * Category category = categoryMapper.selectById(id);
     * CategoryResponse response = CategoryResponse.fromEntity(category);
     * return Result.success(response);
     *
     * 为什么不直接返回Entity？
     * - Entity包含deleted字段（前端不需要）
     * - Entity结构变化会影响API（不灵活）
     * - 可以在Response中添加格式化逻辑
     */
    public static CategoryResponse fromEntity(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setSortOrder(category.getSortOrder());
        response.setCreateTime(category.getCreateTime());
        response.setUpdateTime(category.getUpdateTime());

        return response;
    }

    /**
     * 批量转换
     *
     * 使用场景：
     * List<Category> categories = categoryMapper.selectList(null);
     * List<CategoryResponse> responses = CategoryResponse.fromEntityList(categories);
     */
    public static java.util.List<CategoryResponse> fromEntityList(java.util.List<Category> categories) {
        if (categories == null) {
            return new java.util.ArrayList<>();
        }

        return categories.stream()
                .map(CategoryResponse::fromEntity)
                .collect(java.util.stream.Collectors.toList());
    }
}
