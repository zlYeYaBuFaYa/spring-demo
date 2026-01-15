package com.example.springdemo.dto;

import com.example.springdemo.entity.Good;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品响应DTO
 * 用于向客户端返回商品数据
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
 * - 可以添加计算字段（如折扣价）
 */
@Data  // Lombok：生成getter/setter
@NoArgsConstructor  // 无参构造
@AllArgsConstructor  // 全参构造
public class GoodResponse {

    /**
     * 商品ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 库存数量
     */
    private Integer stock;

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
     * Good good = goodMapper.selectById(id);
     * GoodResponse response = GoodResponse.fromEntity(good);
     * return Result.success(response);
     *
     * 为什么不直接返回Entity？
     * - Entity包含deleted字段（前端不需要）
     * - Entity结构变化会影响API（不灵活）
     * - 可以在Response中添加格式化逻辑
     */
    public static GoodResponse fromEntity(Good good) {
        if (good == null) {
            return null;
        }

        GoodResponse response = new GoodResponse();
        response.setId(good.getId());
        response.setName(good.getName());
        response.setPrice(good.getPrice());
        response.setDescription(good.getDescription());
        response.setStock(good.getStock());
        response.setCreateTime(good.getCreateTime());
        response.setUpdateTime(good.getUpdateTime());

        return response;
    }

    /**
     * 批量转换
     *
     * 使用场景：
     * List<Good> goods = goodMapper.selectList(null);
     * List<GoodResponse> responses = GoodResponse.fromEntityList(goods);
     */
    public static java.util.List<GoodResponse> fromEntityList(java.util.List<Good> goods) {
        if (goods == null) {
            return new java.util.ArrayList<>();
        }

        return goods.stream()
                .map(GoodResponse::fromEntity)
                .collect(java.util.stream.Collectors.toList());
    }
}
