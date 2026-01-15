package com.example.springdemo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springdemo.dto.GoodCreateRequest;
import com.example.springdemo.dto.GoodResponse;
import com.example.springdemo.dto.GoodUpdateRequest;
import com.example.springdemo.entity.Good;
import com.example.springdemo.service.GoodService;
import com.example.springdemo.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品控制器
 * 提供商品管理的RESTful API端点
 *
 * Controller层职责：
 * 1. 接收HTTP请求
 * 2. 参数验证（@Valid）
 * 3. 调用Service层处理业务逻辑
 * 4. 转换DTO（Entity ↔ Response DTO）
 * 5. 返回统一格式的响应（Result）
 *
 * 关键注解：
 * - @RestController: 组合注解（@Controller + @ResponseBody）
 * - @RequestMapping: 定义URL路径前缀
 * - @PostMapping: 映射POST请求（创建资源）
 * - @GetMapping: 映射GET请求（查询资源）
 * - @PutMapping: 映射PUT请求（更新资源）
 * - @DeleteMapping: 映射DELETE请求（删除资源）
 */
@Slf4j  // Lombok：自动生成日志对象
@RestController  // 标记为REST控制器（返回JSON）
@RequestMapping("/api/goods")  // 所有路径的前缀：/api/goods
@Validated  // 启用方法级别的参数验证
public class GoodController {

    /**
     * 注入GoodService
     *
     * @Autowired: Spring自动注入Service
     */
    @Autowired
    private GoodService goodService;

    /**
     * 创建商品
     *
     * RESTful API:
     * POST /api/goods
     *
     * 请求示例：
     * POST http://localhost:8080/api/goods
     * Content-Type: application/json
     * {
     *   "name": "iPhone 15 Pro",
     *   "price": 8999.00,
     *   "description": "Apple最新旗舰手机",
     *   "stock": 100
     * }
     *
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "id": 1,
     *     "name": "iPhone 15 Pro",
     *     "price": 8999.00,
     *     "description": "Apple最新旗舰手机",
     *     "stock": 100,
     *     "createTime": "2024-01-15T10:30:00",
     *     "updateTime": "2024-01-15T10:30:00"
     *   }
     * }
     *
     * @param request 创建请求DTO（@Valid自动验证）
     * @return Result<GoodResponse> 包含创建成功的商品信息
     */
    @PostMapping
    public Result<GoodResponse> createGood(@Valid @RequestBody GoodCreateRequest request) {
        log.info("创建商品请求: name={}, price={}", request.getName(), request.getPrice());

        // 1. 调用Service创建商品
        Good good = goodService.createGood(request);

        // 2. Entity → Response DTO转换
        GoodResponse response = GoodResponse.fromEntity(good);

        log.info("商品创建成功: id={}", good.getId());

        // 3. 返回统一格式的响应
        return Result.success(response);
    }

    /**
     * 根据ID查询商品
     *
     * RESTful API:
     * GET /api/goods/{id}
     *
     * 请求示例：
     * GET http://localhost:8080/api/goods/1
     *
     * 成功响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "id": 1,
     *     "name": "iPhone 15 Pro",
     *     "price": 8999.00,
     *     ...
     *   }
     * }
     *
     * 失败响应示例（商品不存在）：
     * {
     *   "code": 404,
     *   "message": "商品不存在: 999",
     *   "data": null
     * }
     *
     * @param id 商品ID（路径参数）
     * @return Result<GoodResponse> 包含商品信息
     */
    @GetMapping("/{id}")
    public Result<GoodResponse> getGood(@PathVariable Long id) {
        log.info("查询商品请求: id={}", id);

        // 1. 调用Service查询商品
        Good good = goodService.getGoodById(id);

        // 2. Entity → Response DTO转换
        GoodResponse response = GoodResponse.fromEntity(good);

        log.info("查询成功: name={}", good.getName());

        // 3. 返回响应
        return Result.success(response);
    }

    /**
     * 分页查询商品列表
     *
     * RESTful API:
     * GET /api/goods?page=1&size=10
     *
     * 请求示例：
     * GET http://localhost:8080/api/goods?page=1&size=10
     *
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "records": [...],      // 商品列表
     *     "total": 45,           // 总记录数
     *     "pages": 5,            // 总页数
     *     "current": 1,          // 当前页码
     *     "size": 10             // 每页数量
     *   }
     * }
     *
     * @param page 当前页码（默认第1页）
     * @param size 每页数量（默认10条）
     * @return Result<Page<GoodResponse>> 包含分页信息
     */
    @GetMapping
    public Result<Page<GoodResponse>> getGoodsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询商品请求: page={}, size={}", page, size);

        // 1. 调用Service分页查询
        Page<Good> goodsPage = goodService.getGoodsByPage(page, size);

        // 2. 转换分页结果（Entity → Response DTO）
        Page<GoodResponse> responsePage = new Page<>(goodsPage.getCurrent(), goodsPage.getSize(), goodsPage.getTotal());
        responsePage.setRecords(GoodResponse.fromEntityList(goodsPage.getRecords()));

        log.info("查询成功: 总记录数={}, 当前页数据={}", goodsPage.getTotal(), goodsPage.getRecords().size());

        // 3. 返回响应
        return Result.success(responsePage);
    }

    /**
     * 更新商品
     *
     * RESTful API:
     * PUT /api/goods/{id}
     *
     * 请求示例：
     * PUT http://localhost:8080/api/goods/1
     * Content-Type: application/json
     * {
     *   "name": "iPhone 15 Pro Max",
     *   "price": 9999.00
     * }
     *
     * 注意：只更新提供的字段，未提供的字段保持原值
     *
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "id": 1,
     *     "name": "iPhone 15 Pro Max",  // 已更新
     *     "price": 9999.00,              // 已更新
     *     "description": "...",          // 保持原值
     *     "stock": 100,                  // 保持原值
     *     ...
     *   }
     * }
     *
     * @param id      商品ID（路径参数）
     * @param request 更新请求DTO（@Valid自动验证）
     * @return Result<GoodResponse> 包含更新后的商品信息
     */
    @PutMapping("/{id}")
    public Result<GoodResponse> updateGood(
            @PathVariable Long id,
            @Valid @RequestBody GoodUpdateRequest request) {
        log.info("更新商品请求: id={}", id);

        // 1. 调用Service更新商品
        Good good = goodService.updateGood(id, request);

        // 2. Entity → Response DTO转换
        GoodResponse response = GoodResponse.fromEntity(good);

        log.info("商品更新成功: id={}", id);

        // 3. 返回响应
        return Result.success(response);
    }

    /**
     * 删除商品（逻辑删除）
     *
     * RESTful API:
     * DELETE /api/goods/{id}
     *
     * 请求示例：
     * DELETE http://localhost:8080/api/goods/1
     *
     * 成功响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": null
     * }
     *
     * 失败响应示例（商品不存在）：
     * {
     *   "code": 404,
     *   "message": "商品不存在: 999",
     *   "data": null
     * }
     *
     * 注意：这是逻辑删除，数据不会真正从数据库删除
     * - deleted字段会被设置为1
     * - 删除后的商品不会被查询到
     *
     * @param id 商品ID（路径参数）
     * @return Result<Void> 删除成功（无数据返回）
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteGood(@PathVariable Long id) {
        log.info("删除商品请求: id={}", id);

        // 1. 调用Service删除商品
        goodService.deleteGood(id);

        log.info("商品删除成功: id={}", id);

        // 2. 返回成功响应（无数据）
        return Result.success();
    }

    /**
     * 搜索商品（根据名称模糊搜索）
     *
     * RESTful API:
     * GET /api/goods/search?keyword=iPhone
     *
     * 请求示例：
     * GET http://localhost:8080/api/goods/search?keyword=iPhone
     *
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": [
     *     {
     *       "id": 1,
     *       "name": "iPhone 15 Pro",
     *       "price": 8999.00,
     *       ...
     *     },
     *     {
     *       "id": 2,
     *       "name": "iPhone 15 Pro Max",
     *       "price": 9999.00,
     *       ...
     *     }
     *   ]
     * }
     *
     * @param keyword 搜索关键字（查询参数）
     * @return Result<List<GoodResponse>> 包含匹配的商品列表
     */
    @GetMapping("/search")
    public Result<List<GoodResponse>> searchGoods(@RequestParam String keyword) {
        log.info("搜索商品请求: keyword={}", keyword);

        // 1. 调用Service搜索商品
        List<Good> goods = goodService.searchGoods(keyword);

        // 2. Entity → Response DTO转换
        List<GoodResponse> responses = GoodResponse.fromEntityList(goods);

        log.info("搜索成功: 找到{}个商品", responses.size());

        // 3. 返回响应
        return Result.success(responses);
    }

    /**
     * 根据价格区间查询商品
     *
     * RESTful API:
     * GET /api/goods/byPrice?minPrice=1000&maxPrice=5000
     *
     * 请求示例：
     * GET http://localhost:8080/api/goods/byPrice?minPrice=1000&maxPrice=5000
     *
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": [
     *     {
     *       "id": 1,
     *       "name": "MacBook Air",
     *       "price": 4500.00,
     *       ...
     *     }
     *   ]
     * }
     *
     * @param minPrice 最低价格（查询参数）
     * @param maxPrice 最高价格（查询参数）
     * @return Result<List<GoodResponse>> 包含符合价格区间的商品列表
     */
    @GetMapping("/byPrice")
    public Result<List<GoodResponse>> getGoodsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("按价格区间查询商品请求: minPrice={}, maxPrice={}", minPrice, maxPrice);

        // 1. 调用Service查询商品
        List<Good> goods = goodService.getGoodsByPriceRange(minPrice, maxPrice);

        // 2. Entity → Response DTO转换
        List<GoodResponse> responses = GoodResponse.fromEntityList(goods);

        log.info("查询成功: 找到{}个商品", responses.size());

        // 3. 返回响应
        return Result.success(responses);
    }
}
