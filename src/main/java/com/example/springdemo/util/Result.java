package com.example.springdemo.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一API响应结果封装类
 * 所有API接口都返回这个格式，保证前后端交互的一致性
 *
 * @param <T> 响应数据的类型
 *
 * 响应格式示例：
 * {
 *   "code": 200,
 *   "message": "操作成功",
 *   "data": { "id": 1, "name": "iPhone", ... }
 * }
 */
@Data  // Lombok：生成getter/setter
@NoArgsConstructor  // 无参构造
@AllArgsConstructor  // 全参构造
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     * 200: 成功
     * 400: 请求参数错误
     * 404: 资源不存在
     * 500: 服务器内部错误
     */
    private Integer code;

    /**
     * 响应消息
     * 例如："操作成功"、"商品不存在"
     */
    private String message;

    /**
     * 响应数据
     * 可以是任意类型：对象、集合、字符串等
     */
    private T data;

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return Result对象
     *
     * 使用示例：
     * Good good = goodService.getById(1L);
     * return Result.success(good);
     *
     * 响应JSON：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": { "id": 1, "name": "iPhone", ... }
     * }
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 成功响应（无数据）
     *
     * 使用示例：
     * goodService.deleteById(1L);
     * return Result.success();
     *
     * 响应JSON：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": null
     * }
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /**
     * 成功响应（自定义消息）
     *
     * @param message 自定义消息
     * @param data    响应数据
     * 使用示例：
     * return Result.success("创建成功", good);
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    /**
     * 失败响应（默认400错误码）
     *
     * @param message 错误消息
     * 使用示例：
     * return Result.error("商品名称不能为空");
     *
     * 响应JSON：
     * {
     *   "code": 400,
     *   "message": "商品名称不能为空",
     *   "data": null
     * }
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(400, message, null);
    }

    /**
     * 失败响应（自定义状态码）
     *
     * @param code    状态码
     * @param message 错误消息
     * 使用示例：
     * return Result.error(404, "商品不存在");
     *
     * 响应JSON：
     * {
     *   "code": 404,
     *   "message": "商品不存在",
     *   "data": null
     * }
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 404资源不存在
     *
     * @param message 错误消息
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }

    /**
     * 500服务器错误
     *
     * @param message 错误消息
     */
    public static <T> Result<T> serverError(String message) {
        return new Result<>(500, message, null);
    }
}
