package com.example.springdemo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 创建商品请求DTO
 * 用于接收客户端提交的创建商品数据
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
public class GoodCreateRequest {

    /**
     * 商品名称
     *
     * 验证规则：
     * - @NotBlank: 不能为null、空字符串或纯空格
     * - @Size: 长度必须在1-100之间
     *
     * 错误消息示例：
     * "商品名称不能为空"
     * "商品名称长度不能超过100个字符"
     */
    @NotBlank(message = "商品名称不能为空")
    @Size(min = 1, max = 100, message = "商品名称长度必须在1-100个字符之间")
    private String name;

    /**
     * 商品价格
     *
     * 验证规则：
     * - @NotNull: 不能为null
     * - @DecimalMin: 必须大于等于0.00（不能为负数）
     * - @Digits: 整数部分最多8位，小数部分最多2位
     *
     * 错误消息示例：
     * "商品价格不能为空"
     * "商品价格不能为负数"
     */
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.00", message = "商品价格不能为负数")
    @Digits(integer = 8, fraction = 2, message = "商品价格格式不正确（最多8位整数，2位小数）")
    private BigDecimal price;

    /**
     * 商品描述
     *
     * 验证规则：
     * - 可选字段（允许为null）
     * - 如果提供，长度不能超过500个字符
     *
     * 使用示例：
     * "Apple最新旗舰手机，搭载A17芯片"
     */
    @Size(max = 500, message = "商品描述长度不能超过500个字符")
    private String description;

    /**
     * 库存数量
     *
     * 验证规则：
     * - @NotNull: 不能为null
     * - @Min: 必须大于等于0
     *
     * 错误消息示例：
     * "库存不能为空"
     * "库存不能为负数"
     */
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;
}
