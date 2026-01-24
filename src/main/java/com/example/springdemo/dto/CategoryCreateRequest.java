package com.example.springdemo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建分类请求DTO
 * 用于接收客户端提交的创建分类数据
 *
 * 为什么使用DTO而不是直接使用Entity？
 * 1. 安全性：避免接收不应被设置的字段（如id、createTime）
 * 2. 验证：专注验证输入参数
 * 3. 灵活性：DTO可以与Entity有不同的字段结构
 * 4. 解耦：前端不需要知道后端的Entity结构
 *
 * 验证注解说明：
 * - @Validated：在Controller中启用验证
 * - 验证失败会自动抛出MethodArgumentNotValidException
 * - GlobalExceptionHandler会捕获并返回友好的错误消息
 */
@Data  // Lombok：生成getter/setter
@NoArgsConstructor  // 无参构造
@AllArgsConstructor  // 全参构造
public class CategoryCreateRequest {

    /**
     * 分类名称
     *
     * 验证规则：
     * - @NotBlank: 不能为null、空字符串或纯空格
     * - @Size: 长度必须在1-50之间
     *
     * 错误消息示例：
     * "分类名称不能为空"
     * "分类名称长度必须在1-50个字符之间"
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(min = 1, max = 50, message = "分类名称长度必须在1-50个字符之间")
    private String name;

    /**
     * 分类描述
     *
     * 验证规则：
     * - 可选字段（允许为null）
     * - 如果提供，长度不能超过200个字符
     *
     * 使用示例：
     * "电子产品分类"
     */
    @Size(max = 200, message = "分类描述长度不能超过200个字符")
    private String description;

    /**
     * 排序序号
     *
     * 验证规则：
     * - @NotNull: 不能为null
     * - @Min: 必须大于等于0
     *
     * 使用说明：
     * - 数字越小，排序越靠前
     * - 可用于前端显示顺序控制
     */
    @NotNull(message = "排序序号不能为空")
    @Min(value = 0, message = "排序序号不能为负数")
    private Integer sortOrder;
}
