-- ================================================
-- 商品表（Good）创建脚本
-- ================================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS spring_demo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE spring_demo;

-- 删除已存在的表（谨慎使用！仅用于开发环境）
-- DROP TABLE IF EXISTS `good`;

-- 创建商品表
CREATE TABLE IF NOT EXISTS `good` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    `price` DECIMAL(10, 2) NOT NULL COMMENT '商品价格',
    `description` VARCHAR(500) COMMENT '商品描述',
    `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0-未删除，1-已删除）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_name` (`name`) COMMENT '商品名称索引',
    INDEX `idx_create_time` (`create_time`) COMMENT '创建时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 插入测试数据（可选）
INSERT INTO `good` (`name`, `price`, `description`, `stock`) VALUES
('iPhone 15 Pro', 8999.00, 'Apple最新旗舰手机', 50),
('MacBook Pro 14寸', 14999.00, 'M3 Pro芯片专业笔记本', 30),
('AirPods Pro 2', 1899.00, '主动降噪无线耳机', 100);

-- 查看表结构
DESC `good`;

-- 查询测试数据
SELECT * FROM `good`;
