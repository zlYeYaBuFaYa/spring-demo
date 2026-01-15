package com.example.springdemo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus配置类
 * 功能：
 * 1. 配置自动填充处理器
 * 2. 扫描Mapper接口
 */
@Configuration  // 标记为配置类
@MapperScan("com.example.springdemo.mapper")  // 扫描Mapper接口所在的包
public class MyBatisPlusConfig {

    /**
     * 自动填充处理器
     * 在插入和更新时自动填充create_time和update_time字段
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            /**
             * 插入时自动填充
             * 在执行insert操作时，自动设置createTime和updateTime
             */
            @Override
            public void insertFill(MetaObject metaObject) {
                LocalDateTime now = LocalDateTime.now();

                // 自动填充创建时间
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);

                // 自动填充更新时间
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
            }

            /**
             * 更新时自动填充
             * 在执行update操作时，自动更新updateTime
             */
            @Override
            public void updateFill(MetaObject metaObject) {
                LocalDateTime now = LocalDateTime.now();

                // 自动填充更新时间
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
            }
        };
    }
}
