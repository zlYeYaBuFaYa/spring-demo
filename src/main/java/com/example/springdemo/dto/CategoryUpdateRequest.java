package com.example.springdemo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新分类请求DTO
 * 用于接收客户端提交的更新分类数据
 *
 * 设计特点：
 * - 所有字段都是可选的（支持部分更新）
 * - 只更新客户端提供的字段
 * - 未提供的字段保持原值
 *
 * 使用场景：
 * 1. 只更新分类名称：{"name": "新名称"}
 * 2. 只更新排序序号：{"sortOrder": 10}
 * 3. 更新多个字段：{"name": "新名称", "description": "新描述", "sortOrder": 5}
 */
@Data  // Lombok：生成getter/setter
@NoArgsConstructor  // 无参构造
@AllArgsConstructor  // 全参构造
public class CategoryUpdateRequest {

    /**
     * 分类名称（可选）
     *
     * 如果为null，表示不更新该字段
     * 如果为空字符串""，会清空分类名称（通常不允许）
     *
     * 使用示例：
     * - {"name": "新名称"} → 更新名称
     * - 不提供name字段 → 保持原名称
     */
    @Size(min = 1, max = 50, message = "分类名称长度必须在1-50个字符之间")
    private String name;

    /**
     * 分类描述（可选）
     *
     * 如果为null，表示不更新该字段
     * 如果为空字符串""，会清空描述（允许）
     *
     * 使用示例：
     * - {"description": "新描述"} → 更新描述
     * - {"description": ""} → 清空描述
     * - 不提供description字段 → 保持原描述
     */
    @Size(max = 200, message = "分类描述长度不能超过200个字符")
    private String description;

    /**
     * 排序序号（可选）
     *
     * 如果为null，表示不更新该字段
     *
     * 使用示例：
     * - {"sortOrder": 10} → 更新排序序号
     * - 不提供sortOrder字段 → 保持原排序序号
     */
    @Min(value = 0, message = "排序序号不能为负数")
    private Integer sortOrder;

    /**
     * 判断字段是否有值
     * 用于Service层判断是否需要更新该字段
     *
     * 使用示例：
     * if (request.getName() != null) {
     *     category.setName(request.getName());
     * }
     */
    public boolean hasName() {
        return name != null;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public boolean hasSortOrder() {
        return sortOrder != null;
    }

    /**
     * 判断是否至少有一个字段有值
     * 用于验证：至少要更新一个字段
     */
    public boolean hasAnyField() {
        return hasName() || hasDescription() || hasSortOrder();
    }
}
