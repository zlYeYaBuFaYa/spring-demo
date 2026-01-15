package com.example.springdemo.exception;

import lombok.Getter;

/**
 * 资源不存在异常
 * 当查询的资源（如商品）不存在时抛出此异常
 *
 * 使用场景：
 * 1. 根据ID查询商品，但数据库中不存在该ID的记录
 * 2. 更新/删除商品时，商品ID不存在
 *
 * 使用示例：
 * Good good = goodMapper.selectById(id);
 * if (good == null) {
 *     throw new ResourceNotFoundException("商品不存在: " + id);
 * }
 *
 * 全局异常处理器会捕获此异常，返回404状态码
 */
@Getter  // Lombok：生成getter方法
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 资源ID
     */
    private final Long resourceId;

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceId = null;
    }

    /**
     * 构造函数（带资源ID）
     *
     * @param message    错误消息
     * @param resourceId 资源ID
     */
    public ResourceNotFoundException(String message, Long resourceId) {
        super(message);
        this.resourceId = resourceId;
    }

    /**
     * 静态工厂方法：根据ID创建异常
     *
     * @param resourceName 资源名称
     * @param id           资源ID
     * @return 异常对象
     *
     * 使用示例：
     * throw ResourceNotFoundException.of("商品", id);
     */
    public static ResourceNotFoundException of(String resourceName, Long id) {
        return new ResourceNotFoundException(resourceName + "不存在: " + id, id);
    }
}
