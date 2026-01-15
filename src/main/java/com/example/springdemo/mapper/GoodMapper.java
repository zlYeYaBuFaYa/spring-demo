package com.example.springdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springdemo.entity.Good;
import org.apache.ibatis.annotations.Mapper;

/**
 * Good Mapper接口
 * MyBatis-Plus的数据访问层
 *
 * 继承BaseMapper后，自动获得以下CRUD方法：
 * - insert(Good entity): 插入一条记录
 * - deleteById(Serializable id): 根据ID删除
 * - updateById(Good entity): 根据ID更新
 * - selectById(Serializable id): 根据ID查询
 * - selectList(Wrapper wrapper): 条件查询列表
 * - selectPage(Page page, Wrapper wrapper): 分页查询
 * ... 等等
 *
 * 无需编写XML文件，MyBatis-Plus自动生成SQL
 */
@Mapper  // MyBatis注解：标记为Mapper接口，Spring会自动扫描注册
public interface GoodMapper extends BaseMapper<Good> {
    // 继承BaseMapper<Good>后，已经拥有所有基础CRUD方法

    // 如果需要自定义SQL，可以在这里添加方法定义
    // 例如：
    // List<Good> selectByPriceRange(@Param("minPrice") BigDecimal minPrice,
    //                               @Param("maxPrice") BigDecimal maxPrice);

    // 复杂查询建议使用MyBatis-Plus的QueryWrapper（在Service层实现）
}
