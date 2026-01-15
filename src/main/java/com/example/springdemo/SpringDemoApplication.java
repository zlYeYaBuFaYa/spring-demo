package com.example.springdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring Boot应用主启动类
 *
 * @SpringBootApplication: 是一个组合注解，包含：
 * - @Configuration: 标记为配置类
 * - @EnableAutoConfiguration: 启用自动配置（根据依赖自动配置Spring应用）
 * - @ComponentScan: 自动扫描当前包及其子包下的所有组件
 *
 * 启动流程：
 * 1. 初始化Spring应用上下文
 * 2. 扫描并注册所有Bean（@Controller, @Service, @Mapper等）
 * 3. 自动配置嵌入式Tomcat服务器
 * 4. 启动应用监听8080端口
 */
@SpringBootApplication
@EnableTransactionManagement  // 启用事务管理
@MapperScan("com.example.springdemo.mapper")  // 扫描Mapper接口
public class SpringDemoApplication {

    public static void main(String[] args) {
        /**
         * 启动Spring Boot应用
         * SpringApplication.run()会：
         * 1. 创建Spring应用上下文
         * 2. 加载所有配置和Bean
         * 3. 启动嵌入式Web服务器
         * 4. 应用开始运行
         */
        SpringApplication.run(SpringDemoApplication.class, args);

        System.out.println("========================================");
        System.out.println("   Spring Boot应用启动成功！");
        System.out.println("   访问地址: http://localhost:8080");
        System.out.println("   API文档: http://localhost:8080/api/goods");
        System.out.println("========================================");
    }
}
