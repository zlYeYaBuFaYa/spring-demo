package com.example.springdemo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 使用MyBatis-Plus注解实现ORM映射
 */
@Data  // Lombok：自动生成getter、setter、toString、equals、hashCode
@NoArgsConstructor  // Lombok：生成无参构造函数
@AllArgsConstructor  // Lombok：生成全参构造函数
@TableName("good")  // MyBatis-Plus：指定对应的数据库表名
public class Good {

    /**
     * 主键ID - 自增策略
     * @TableId: 指定主键字段
     * type = IdType.AUTO: 数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     * @NotBlank: 不能为null或空字符串
     */
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称长度不能超过100个字符")
    private String name;

    /**
     * 商品价格
     * @NotNull: 不能为null
     * @DecimalMin: 最小值为0.00（不能为负数）
     */
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.00", message = "商品价格不能为负数")
    private BigDecimal price;

    /**
     * 商品描述
     * 可选字段，允许为空
     */
    @Size(max = 500, message = "商品描述长度不能超过500个字符")
    private String description;

    /**
     * 库存数量
     * 默认值为0
     */
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    /**
     * 逻辑删除标志
     * 0-未删除，1-已删除
     * @TableLogic: 标记逻辑删除字段，删除时自动设置为1
     * 查询时自动添加 WHERE deleted = 0
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     * @TableField: 指定字段填充策略
     * FieldFill.INSERT: 插入时自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     * FieldFill.INSERT_UPDATE: 插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
