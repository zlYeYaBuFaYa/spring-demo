package com.example.springdemo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 更新商品请求DTO
 * 用于接收客户端提交的更新商品数据
 *
 * 设计特点：
 * - 所有字段都是可选的（支持部分更新）
 * - 只更新客户端提供的字段
 * - 未提供的字段保持原值
 *
 * 使用场景：
 * 1. 只更新商品名称：{"name": "iPhone 15 Pro"}
 * 2. 只更新价格：{"price": 8999.00}
 * 3. 更新多个字段：{"name": "新名称", "price": 7999.00, "stock": 100}
 */
@Data  // Lombok：生成getter/setter
@NoArgsConstructor  // 无参构造
@AllArgsConstructor  // 全参构造
public class GoodUpdateRequest {

    /**
     * 商品名称（可选）
     *
     * 如果为null，表示不更新该字段
     * 如果为空字符串""，会清空商品名称（通常不允许）
     *
     * 使用示例：
     * - {"name": "新名称"} → 更新名称
     * - 不提供name字段 → 保持原名称
     */
    @Size(min = 1, max = 100, message = "商品名称长度必须在1-100个字符之间")
    private String name;

    /**
     * 商品价格（可选）
     *
     * 如果为null，表示不更新该字段
     *
     * 使用示例：
     * - {"price": 8999.00} → 更新价格
     * - 不提供price字段 → 保持原价格
     */
    @DecimalMin(value = "0.00", message = "商品价格不能为负数")
    @Digits(integer = 8, fraction = 2, message = "商品价格格式不正确（最多8位整数，2位小数）")
    private BigDecimal price;

    /**
     * 商品描述（可选）
     *
     * 如果为null，表示不更新该字段
     * 如果为空字符串""，会清空描述（允许）
     *
     * 使用示例：
     * - {"description": "新描述"} → 更新描述
     * - {"description": ""} → 清空描述
     * - 不提供description字段 → 保持原描述
     */
    @Size(max = 500, message = "商品描述长度不能超过500个字符")
    private String description;

    /**
     * 库存数量（可选）
     *
     * 如果为null，表示不更新该字段
     *
     * 使用示例：
     * - {"stock": 100} → 更新库存
     * - 不提供stock字段 → 保持原库存
     */
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    /**
     * 判断字段是否有值
     * 用于Service层判断是否需要更新该字段
     *
     * 使用示例：
     * if (request.getName() != null) {
     *     good.setName(request.getName());
     * }
     */
    public boolean hasName() {
        return name != null;
    }

    public boolean hasPrice() {
        return price != null;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public boolean hasStock() {
        return stock != null;
    }

    /**
     * 判断是否至少有一个字段有值
     * 用于验证：至少要更新一个字段
     */
    public boolean hasAnyField() {
        return hasName() || hasPrice() || hasDescription() || hasStock();
    }
}
