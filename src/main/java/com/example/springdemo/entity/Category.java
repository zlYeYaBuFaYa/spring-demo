package com.example.springdemo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 分类实体类
 * 使用MyBatis-Plus注解实现ORM映射
 */
@Data  // Lombok：自动生成getter、setter、toString、equals、hashCode
@NoArgsConstructor  // Lombok：生成无参构造函数
@AllArgsConstructor  // Lombok：生成全参构造函数
@TableName("category")  // MyBatis-Plus：指定对应的数据库表名
public class Category {

    /**
     * 主键ID - 自增策略
     * @TableId: 指定主键字段
     * type = IdType.AUTO: 数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     * @NotBlank: 不能为null或空字符串
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(min = 1, max = 50, message = "分类名称长度必须在1-50个字符之间")
    private String name;

    /**
     * 分类描述
     * 可选字段，允许为空
     */
    @Size(max = 200, message = "分类描述长度不能超过200个字符")
    private String description;

    /**
     * 排序序号
     * 用于分类列表的排序显示，数字越小越靠前
     */
    @NotNull(message = "排序序号不能为空")
    @Min(value = 0, message = "排序序号不能为负数")
    private Integer sortOrder;

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
