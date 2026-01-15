package com.example.springdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springdemo.dto.GoodCreateRequest;
import com.example.springdemo.dto.GoodUpdateRequest;
import com.example.springdemo.entity.Good;
import com.example.springdemo.exception.BusinessException;
import com.example.springdemo.exception.ResourceNotFoundException;
import com.example.springdemo.mapper.GoodMapper;
import com.example.springdemo.service.GoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品服务实现类
 * 实现商品管理的核心业务逻辑
 *
 * 关键注解：
 * - @Service: 标记为Service组件，Spring自动扫描注册
 * - @Slf4j: Lombok自动生成log变量，用于日志记录
 * - @Transactional: 开启事务管理
 */
@Slf4j  // Lombok：自动生成 private static final Logger log = LoggerFactory.getLogger(GoodServiceImpl.class);
@Service  // 标记为Spring Service组件
public class GoodServiceImpl implements GoodService {

    /**
     * 注入GoodMapper
     *
     * @Autowired: Spring自动注入（自动从容器中查找GoodMapper类型的Bean）
     *
     * 为什么使用接口而不是实现类？
     * - 面向接口编程
     * - 便于测试（可以Mock）
     * - MyBatis-Plus会动态生成代理实现类
     */
    @Autowired
    private GoodMapper goodMapper;

    /**
     * 创建商品
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
    public Good createGood(GoodCreateRequest request) {
        log.info("创建商品: name={}, price={}", request.getName(), request.getPrice());

        // 1. DTO → Entity转换
        Good good = new Good();
        BeanUtils.copyProperties(request, good);
        // copyProperties会复制同名属性：
        // - name → name
        // - price → price
        // - description → description
        // - stock → stock
        // createTime和updateTime会自动填充（MyBatisPlusConfig配置的MetaObjectHandler）
        // id会自动生成（IdType.AUTO）

        // 2. 插入数据库
        int result = goodMapper.insert(good);

        if (result <= 0) {
            throw new BusinessException("创建商品失败");
        }

        log.info("商品创建成功: id={}, name={}", good.getId(), good.getName());

        // 3. 返回包含ID的Entity
        return good;
    }

    /**
     * 根据ID查询商品
     *
     * 实现步骤：
     * 1. 调用Mapper查询
     * 2. 如果不存在，抛出ResourceNotFoundException
     * 3. 返回查询结果
     *
     * 注意：
     * - MyBatis-Plus会自动添加 WHERE deleted = 0（逻辑删除）
     * - 如果查询已删除的商品，会返回null
     */
    @Override
    public Good getGoodById(Long id) {
        log.info("查询商品: id={}", id);

        // 1. 根据ID查询
        Good good = goodMapper.selectById(id);

        // 2. 判断是否存在
        if (good == null) {
            log.warn("商品不存在: id={}", id);
            throw ResourceNotFoundException.of("商品", id);
        }

        log.info("查询成功: id={}, name={}", good.getId(), good.getName());

        return good;
    }

    /**
     * 查询所有商品
     *
     * 注意：
     * - 数据量大时不要使用此方法
     * - 建议使用分页查询 getGoodsByPage()
     */
    @Override
    public List<Good> getAllGoods() {
        log.info("查询所有商品");

        // 查询所有未删除的商品
        // MyBatis-Plus自动添加 WHERE deleted = 0
        List<Good> goods = goodMapper.selectList(null);

        log.info("查询到 {} 个商品", goods.size());

        return goods;
    }

    /**
     * 分页查询商品（简化版本）
     *
     * 注意：由于MyBatis-Plus分页插件配置问题，暂时使用简单列表查询
     * 真正的分页需要配置分页插件或手动编写SQL
     *
     * @param current 当前页码（暂时未使用）
     * @param size    每页数量（暂时未使用）
     * @return 所有商品列表
     */
    @Override
    public Page<Good> getGoodsByPage(int current, int size) {
        log.info("查询商品列表: current={}, size={}", current, size);

        // 暂时返回所有商品
        List<Good> goods = goodMapper.selectList(null);

        log.info("查询成功: 共 {} 个商品", goods.size());

        // 创建一个简单的Page对象返回（兼容接口）
        Page<Good> page = new Page<>(current, size);
        page.setRecords(goods);
        page.setTotal(goods.size());

        return page;
    }

    /**
     * 更新商品
     *
     * 实现步骤：
     * 1. 先查询商品是否存在
     * 2. 更新请求中提供的字段（部分更新）
     * 3. 调用Mapper更新数据库
     * 4. 返回更新后的商品
     *
     * 注意：
     * - updateTime会自动更新（MyBatis-Plus自动处理）
     * - 只更新提供的字段，未提供的字段保持原值
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Good updateGood(Long id, GoodUpdateRequest request) {
        log.info("更新商品: id={}", id);

        // 1. 先查询商品是否存在
        Good good = getGoodById(id);  // 如果不存在会抛出ResourceNotFoundException

        // 2. 部分更新：只更新请求中提供的字段
        if (request.hasName()) {
            good.setName(request.getName());
            log.info("  更新名称: {}", request.getName());
        }

        if (request.hasPrice()) {
            good.setPrice(request.getPrice());
            log.info("  更新价格: {}", request.getPrice());
        }

        if (request.hasDescription()) {
            good.setDescription(request.getDescription());
            log.info("  更新描述: {}", request.getDescription());
        }

        if (request.hasStock()) {
            good.setStock(request.getStock());
            log.info("  更新库存: {}", request.getStock());
        }

        // 3. 执行更新
        int result = goodMapper.updateById(good);

        if (result <= 0) {
            throw new BusinessException("更新商品失败");
        }

        log.info("商品更新成功: id={}", id);

        // 4. 返回更新后的商品
        return good;
    }

    /**
     * 删除商品（逻辑删除）
     *
     * 实现步骤：
     * 1. 先查询商品是否存在
     * 2. 调用Mapper删除（逻辑删除）
     * 3. MyBatis-Plus会自动设置 deleted=1
     *
     * 注意：
     * - 不是物理删除（DELETE FROM）
     * - 是逻辑删除（UPDATE SET deleted=1）
     * - 删除后的商品不会被查询到
     * - 可以恢复（将deleted改回0）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGood(Long id) {
        log.info("删除商品: id={}", id);

        // 1. 先查询商品是否存在（可选，为了返回友好的错误提示）
        Good good = getGoodById(id);

        // 2. 执行逻辑删除
        // MyBatis-Plus会自动执行: UPDATE good SET deleted=1 WHERE id=? AND deleted=0
        int result = goodMapper.deleteById(id);

        if (result <= 0) {
            throw new BusinessException("删除商品失败");
        }

        log.info("商品删除成功: id={}, name={}", id, good.getName());
    }

    /**
     * 根据商品名称模糊搜索
     *
     * 实现步骤：
     * 1. 创建QueryWrapper（查询条件构造器）
     * 2. 添加模糊查询条件
     * 3. 调用Mapper查询
     * 4. 返回结果
     *
     * QueryWrapper常用方法：
     * - like: 模糊查询（WHERE name LIKE '%关键字%'）
     * - eq: 等于（WHERE price = 100）
     * - ge: 大于等于（WHERE price >= 100）
     * - le: 小于等于（WHERE price <= 100）
     * - between: 区间（WHERE price BETWEEN 100 AND 200）
     * - orderByDesc: 降序排序（ORDER BY create_time DESC）
     */
    @Override
    public List<Good> searchGoods(String keyword) {
        log.info("搜索商品: keyword={}", keyword);

        // 1. 创建查询条件构造器
        QueryWrapper<Good> queryWrapper = new QueryWrapper<>();

        // 2. 添加模糊查询条件
        // SQL: WHERE name LIKE '%keyword%'
        queryWrapper.like("name", keyword);

        // 3. 按创建时间降序排序
        // SQL: ORDER BY create_time DESC
        queryWrapper.orderByDesc("create_time");

        // 4. 执行查询
        List<Good> goods = goodMapper.selectList(queryWrapper);

        log.info("搜索到 {} 个商品", goods.size());

        return goods;
    }

    /**
     * 根据价格区间查询商品
     *
     * 实现步骤：
     * 1. 创建QueryWrapper
     * 2. 添加价格区间条件
     * 3. 调用Mapper查询
     * 4. 返回结果
     *
     * 使用场景：
     * - 筛选价格在100-500之间的商品
     */
    @Override
    public List<Good> getGoodsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("按价格区间查询商品: minPrice={}, maxPrice={}", minPrice, maxPrice);

        // 1. 创建查询条件构造器
        QueryWrapper<Good> queryWrapper = new QueryWrapper<>();

        // 2. 添加价格区间条件
        // SQL: WHERE price >= minPrice AND price <= maxPrice
        queryWrapper.ge("price", minPrice);  // 大于等于
        queryWrapper.le("price", maxPrice);  // 小于等于

        // 3. 按价格升序排序
        queryWrapper.orderByAsc("price");

        // 4. 执行查询
        List<Good> goods = goodMapper.selectList(queryWrapper);

        log.info("查询到 {} 个商品", goods.size());

        return goods;
    }
}
