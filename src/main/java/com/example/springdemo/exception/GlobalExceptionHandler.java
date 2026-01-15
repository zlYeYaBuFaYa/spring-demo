package com.example.springdemo.exception;

import com.example.springdemo.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理应用中的所有异常，返回标准格式的错误响应
 *
 * @RestControllerAdvice: 组合注解
 * - @ControllerAdvice: 标记为全局异常处理器
 * - @ResponseBody: 将返回值序列化为JSON
 *
 * 工作原理：
 * 1. 当Controller中抛出异常时，Spring会自动捕获
 * 2. 根据异常类型，匹配对应的@ExceptionHandler方法
 * 3. 执行处理方法，返回统一的Result格式
 *
 * 优势：
 * - 统一错误格式，前端便于处理
 * - 避免重复的try-catch代码
 * - 可以记录异常日志，便于排查问题
 */
@Slf4j  // Lombok：自动生成logger变量
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理资源不存在异常（404）
     *
     * 场景：查询的商品不存在
     * 返回：404状态码
     *
     * 使用示例：
     * throw new ResourceNotFoundException("商品不存在: 999");
     *
     * 响应：
     * {
     *   "code": 404,
     *   "message": "商品不存在: 999",
     *   "data": null
     * }
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public Result<Void> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("资源不存在: {}", ex.getMessage());

        // 返回404状态码和错误消息
        return Result.notFound(ex.getMessage());
    }

    /**
     * 处理业务逻辑异常（400）
     *
     * 场景：业务规则校验失败（库存不足、商品已下架等）
     * 返回：400状态码
     *
     * 使用示例：
     * throw new BusinessException("库存不足");
     *
     * 响应：
     * {
     *   "code": 400,
     *   "message": "库存不足",
     *   "data": null
     * }
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException ex) {
        log.warn("业务异常: {}", ex.getMessage());

        // 返回400状态码和错误消息
        return Result.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理参数验证异常（400）
     *
     * 场景：@Valid验证失败（如@NotBlank、@Min等）
     * 返回：400状态码
     *
     * 使用示例：
     * public Result<Good> createGood(@Valid @RequestBody GoodCreateRequest request) {
     *     // 如果request.name为空，会触发此异常
     * }
     *
     * 响应：
     * {
     *   "code": 400,
     *   "message": "参数验证失败: 商品名称不能为空, 价格不能小于0",
     *   "data": null
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException ex) {
        // 提取所有验证错误消息
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("参数验证失败: {}", errors);

        // 返回400状态码和所有错误消息
        return Result.error("参数验证失败: " + errors);
    }

    /**
     * 处理非法参数异常（400）
     *
     * 场景：方法参数绑定失败（如类型转换错误）
     * 返回：400状态码
     *
     * 使用示例：
     * GET /api/goods/abc
     * 如果abc不能转换为Long，会触发此异常
     *
     * 响应：
     * {
     *   "code": 400,
     *   "message": "参数错误: ...",
     *   "data": null
     * }
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("非法参数: {}", ex.getMessage());

        return Result.error("参数错误: " + ex.getMessage());
    }

    /**
     * 处理通用异常（500）
     *
     * 场景：所有未被上述方法捕获的异常
     * 返回：500状态码
     *
     * 注意：这是最后的兜底处理，避免直接暴露堆栈信息给用户
     *
     * 响应：
     * {
     *   "code": 500,
     *   "message": "服务器内部错误",
     *   "data": null
     * }
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleGenericException(Exception ex) {
        // 记录完整的异常堆栈（ERROR级别）
        log.error("未处理的异常", ex);

        // 返回通用的错误消息（不暴露技术细节给用户）
        return Result.serverError("服务器内部错误，请稍后重试");
    }
}
