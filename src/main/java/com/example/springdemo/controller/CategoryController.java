package com.example.springdemo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springdemo.dto.CategoryCreateRequest;
import com.example.springdemo.dto.CategoryResponse;
import com.example.springdemo.dto.CategoryUpdateRequest;
import com.example.springdemo.entity.Category;
import com.example.springdemo.service.CategoryService;
import com.example.springdemo.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 分类控制器
 * 提供分类管理的RESTful API端点
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
@RequestMapping("/api/categories")  // 所有路径的前缀：/api/categories
@Validated  // 启用方法级别的参数验证
public class CategoryController {

    /**
     * 注入CategoryService
     *
     * @Autowired: Spring自动注入Service
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * 创建分类
     *
     * RESTful API:
     * POST /api/categories
     *
     * 请求示例：
     * POST http://localhost:8080/api/categories
     * Content-Type: application/json
     * {
     *   "name": "电子产品",
     *   "description": "手机、电脑等电子设备",
     *   "sortOrder": 1
     * }
     *
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "id": 1,
     *     "name": "电子产品",
     *     "description": "手机、电脑等电子设备",
     *     "sortOrder": 1,
     *     "createTime": "2024-01-15T10:30:00",
     *     "updateTime": "2024-01-15T10:30:00"
     *   }
     * }
     *
     * @param request 创建请求DTO（@Valid自动验证）
     * @return Result<CategoryResponse> 包含创建成功的分类信息
     */
    @PostMapping
    public Result<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        log.info("创建分类请求: name={}, sortOrder={}", request.getName(), request.getSortOrder());

        // 1. 调用Service创建分类
        Category category = categoryService.createCategory(request);

        // 2. Entity → Response DTO转换
        CategoryResponse response = CategoryResponse.fromEntity(category);

        log.info("分类创建成功: id={}", category.getId());

        // 3. 返回统一格式的响应
        return Result.success(response);
    }

    /**
     * 根据ID查询分类
     *
     * RESTful API:
     * GET /api/categories/{id}
     *
     * 请求示例：
     * GET http://localhost:8080/api/categories/1
     *
     * 成功响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "id": 1,
     *     "name": "电子产品",
     *     "description": "手机、电脑等电子设备",
     *     "sortOrder": 1,
     *     "createTime": "2024-01-15T10:30:00",
     *     "updateTime": "2024-01-15T10:30:00"
     *   }
     * }
     *
     * 失败响应示例（分类不存在）：
     * {
     *   "code": 404,
     *   "message": "分类不存在: 999",
     *   "data": null
     * }
     *
     * @param id 分类ID（路径参数）
     * @return Result<CategoryResponse> 包含分类信息
     */
    @GetMapping("/{id}")
    public Result<CategoryResponse> getCategory(@PathVariable Long id) {
        log.info("查询分类请求: id={}", id);

        // 1. 调用Service查询分类
        Category category = categoryService.getCategoryById(id);

        // 2. Entity → Response DTO转换
        CategoryResponse response = CategoryResponse.fromEntity(category);

        log.info("查询成功: name={}", category.getName());

        // 3. 返回响应
        return Result.success(response);
    }

    /**
     * 分页查询分类列表
     *
     * RESTful API:
     * GET /api/categories?page=1&size=10
     *
     * 请求示例：
     * GET http://localhost:8080/api/categories?page=1&size=10
     *
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "records": [...],      // 分类列表
     *     "total": 15,           // 总记录数
     *     "pages": 2,            // 总页数
     *     "current": 1,          // 当前页码
     *     "size": 10             // 每页数量
     *   }
     * }
     *
     * @param page 当前页码（默认第1页）
     * @param size 每页数量（默认10条）
     * @return Result<Page<CategoryResponse>> 包含分页信息
     */
    @GetMapping
    public Result<Page<CategoryResponse>> getCategoriesByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询分类请求: page={}, size={}", page, size);

        // 1. 调用Service分页查询
        Page<Category> categoryPage = categoryService.getCategoriesByPage(page, size);

        // 2. 转换分页结果（Entity → Response DTO）
        Page<CategoryResponse> responsePage = new Page<>(categoryPage.getCurrent(), categoryPage.getSize(), categoryPage.getTotal());
        responsePage.setRecords(CategoryResponse.fromEntityList(categoryPage.getRecords()));

        log.info("查询成功: 总记录数={}, 当前页数据={}", categoryPage.getTotal(), categoryPage.getRecords().size());

        // 3. 返回响应
        return Result.success(responsePage);
    }

    /**
     * 查询排序后的分类列表
     *
     * RESTful API:
     * GET /api/categories/sorted
     *
     * 请求示例：
     * GET http://localhost:8080/api/categories/sorted
     *
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": [
     *     {
     *       "id": 1,
     *       "name": "电子产品",
     *       "sortOrder": 1,
     *       ...
     *     },
     *     {
     *       "id": 2,
     *       "name": "服装鞋帽",
     *       "sortOrder": 2,
     *       ...
     *     }
     *   ]
     * }
     *
     * @return Result<List<CategoryResponse>> 按sortOrder排序的分类列表
     */
    @GetMapping("/sorted")
    public Result<List<CategoryResponse>> getCategoriesSorted() {
        log.info("查询排序后的分类列表");

        // 1. 调用Service查询排序后的分类
        List<Category> categories = categoryService.getCategoriesSorted();

        // 2. Entity → Response DTO转换
        List<CategoryResponse> responses = CategoryResponse.fromEntityList(categories);

        log.info("查询成功: 找到{}个分类", responses.size());

        // 3. 返回响应
        return Result.success(responses);
    }

    /**
     * 更新分类
     *
     * RESTful API:
     * PUT /api/categories/{id}
     *
     * 请求示例：
     * PUT http://localhost:8080/api/categories/1
     * Content-Type: application/json
     * {
     *   "name": "数码产品",
     *   "sortOrder": 10
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
     *     "name": "数码产品",      // 已更新
     *     "description": "...",   // 保持原值
     *     "sortOrder": 10,        // 已更新
     *     ...
     *   }
     * }
     *
     * @param id      分类ID（路径参数）
     * @param request 更新请求DTO（@Valid自动验证）
     * @return Result<CategoryResponse> 包含更新后的分类信息
     */
    @PutMapping("/{id}")
    public Result<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        log.info("更新分类请求: id={}", id);

        // 1. 调用Service更新分类
        Category category = categoryService.updateCategory(id, request);

        // 2. Entity → Response DTO转换
        CategoryResponse response = CategoryResponse.fromEntity(category);

        log.info("分类更新成功: id={}", id);

        // 3. 返回响应
        return Result.success(response);
    }

    /**
     * 删除分类（逻辑删除）
     *
     * RESTful API:
     * DELETE /api/categories/{id}
     *
     * 请求示例：
     * DELETE http://localhost:8080/api/categories/1
     *
     * 成功响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": null
     * }
     *
     * 失败响应示例（分类不存在）：
     * {
     *   "code": 404,
     *   "message": "分类不存在: 999",
     *   "data": null
     * }
     *
     * 注意：这是逻辑删除，数据不会真正从数据库删除
     * - deleted字段会被设置为1
     * - 删除后的分类不会被查询到
     *
     * @param id 分类ID（路径参数）
     * @return Result<Void> 删除成功（无数据返回）
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        log.info("删除分类请求: id={}", id);

        // 1. 调用Service删除分类
        categoryService.deleteCategory(id);

        log.info("分类删除成功: id={}", id);

        // 2. 返回成功响应（无数据）
        return Result.success();
    }

    /**
     * 搜索分类（根据名称模糊搜索）
     *
     * RESTful API:
     * GET /api/categories/search?keyword=电子
     *
     * 请求示例：
     * GET http://localhost:8080/api/categories/search?keyword=电子
     *
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": [
     *     {
     *       "id": 1,
     *       "name": "电子产品",
     *       "description": "手机、电脑等电子设备",
     *       ...
     *     },
     *     {
     *       "id": 5,
     *       "name": "电子配件",
     *       "description": "充电器、数据线等",
     *       ...
     *     }
     *   ]
     * }
     *
     * @param keyword 搜索关键字（查询参数）
     * @return Result<List<CategoryResponse>> 包含匹配的分类列表
     */
    @GetMapping("/search")
    public Result<List<CategoryResponse>> searchCategories(@RequestParam String keyword) {
        log.info("搜索分类请求: keyword={}", keyword);

        // 1. 调用Service搜索分类
        List<Category> categories = categoryService.searchCategories(keyword);

        // 2. Entity → Response DTO转换
        List<CategoryResponse> responses = CategoryResponse.fromEntityList(categories);

        log.info("搜索成功: 找到{}个分类", responses.size());

        // 3. 返回响应
        return Result.success(responses);
    }
}
