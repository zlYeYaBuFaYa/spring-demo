package com.example.springdemo.exception;

import lombok.Getter;

/**
 * 业务逻辑异常
 * 当业务逻辑验证失败时抛出此异常
 *
 * 使用场景：
 * 1. 参数验证失败（如库存不足、价格不合理）
 * 2. 业务规则校验失败（如商品已下架、用户权限不足）
 * 3. 数据状态不符（如商品已售罄）
 *
 * 使用示例：
 * if (good.getStock() < quantity) {
 *     throw new BusinessException("库存不足，当前库存: " + good.getStock());
 * }
 *
 * 全局异常处理器会捕获此异常，返回400状态码
 */
@Getter  // Lombok：生成getter方法
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     * 可用于区分不同的业务错误类型
     */
    private final Integer code;

    /**
     * 构造函数（默认400错误码）
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    /**
     * 构造函数（自定义错误码）
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 静态工厂方法：创建400业务异常
     *
     * @param message 错误消息
     * @return 异常对象
     */
    public static BusinessException of(String message) {
        return new BusinessException(400, message);
    }

    /**
     * 静态工厂方法：创建自定义错误码的业务异常
     *
     * @param code    错误码
     * @param message 错误消息
     * @return 异常对象
     */
    public static BusinessException of(Integer code, String message) {
        return new BusinessException(code, message);
    }
}
