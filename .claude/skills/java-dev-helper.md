# Java Spring Boot 开发助手

为 spring-demo 项目提供专业的 Java 开发辅助。

## 功能特性

### 1. 代码生成
生成符合项目规范的各类代码：
- Entity 实体类
- Controller 控制器
- Service 业务层
- Mapper 数据访问层
- DTO 数据传输对象
- Config 配置类

### 2. 代码审查
检查代码是否符合项目规范：
- 命名规范检查
- 分层架构检查
- 异常处理检查
- 注解使用检查
- 代码风格检查

### 3. 重构建议
提供代码重构建议：
- 简化复杂方法
- 提取重复代码
- 优化性能
- 改进设计模式

### 4. 问题诊断
帮助解决常见问题：
- MyBatis-Plus 配置问题
- Spring Boot 启动问题
- 数据库连接问题
- 依赖冲突问题
- 热更新不生效

### 5. 最佳实践
提供项目相关的最佳实践：
- 事务管理
- 异常处理
- 参数验证
- 日志记录
- 性能优化

## 使用方法

### 生成代码
```
生成一个 User 实体类，包含 username, email, password 字段
```

### 审查代码
```
检查 GoodController 是否符合项目规范
```

### 添加功能
```
为 Product 实体添加批量删除功能
```

### 修复问题
```
修复 MyBatis-Plus 逻辑删除不生效的问题
```

### 优化代码
```
优化 GoodService 中的查询方法性能
```

## 项目速查

### 常用注解
- `@Data` - Lombok 生成 getter/setter
- `@TableName` - 指定数据库表名
- `@TableId` - 主键标识
- `@TableLogic` - 逻辑删除字段
- `@TableField(fill = FieldFill.INSERT)` - 自动填充
- `@RestController` - REST 控制器
- `@RequestMapping` - 路径映射
- `@Valid` - 参数验证
- `@Service` - 服务层
- `@Mapper` - Mapper 接口
- `@Transactional` - 事务管理

### 依赖注入方式
```java
@Autowired
private SomeService someService;
```

### 返回响应
```java
// 成功
return Result.success(data);

// 错误
return Result.error("错误信息");

// 404
return Result.notFound("资源不存在");
```

### 抛出异常
```java
// 资源不存在
throw new ResourceNotFoundException("资源名", id);

// 业务异常
throw new BusinessException("业务错误描述");
```

### 分页查询
```java
Page<Good> page = new Page<>(pageNum, pageSize);
Page<Good> result = mapper.selectPage(page, wrapper);
```

### 条件构造器
```java
LambdaQueryWrapper<Good> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(Good::getName, name)
       .ge(Good::getPrice, minPrice)
       .orderByDesc(Good::getCreateTime);
```

### 验证注解
```java
@NotNull(message = "不能为空")
@NotBlank(message = "不能为空字符串")
@Min(value = 0, message = "最小值为0")
@Max(value = 100, message = "最大值为100")
@Email(message = "邮箱格式不正确")
@Pattern(regexp = "正则", message = "格式不正确")
```

### 配置文件
- 主配置：`application.yml`
- MySQL配置：`application-mysql.yml`
- PostgreSQL配置：`application-postgresql.yml`

### 数据库迁移
脚本位置：`src/main/resources/db/migration.sql`

### 热更新
已配置 Spring Boot DevTools，修改代码后自动重启。

## 技术栈详情

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.4.0 | 核心框架 |
| MyBatis-Plus | 3.5.10 | ORM框架 |
| Lombok | - | 代码简化 |
| Validation | - | 参数验证 |
| DevTools | - | 热更新 |
| MySQL Connector | - | MySQL驱动 |
| PostgreSQL | - | PostgreSQL驱动 |
| HikariCP | - | 连接池 |

## 常见问题

### Q: 如何添加新模块？
A: 按照 entity → mapper → service → controller 的顺序创建各层代码。

### Q: 如何处理事务？
A: 在 Service 方法上添加 `@Transactional` 注解。

### Q: 如何自定义查询？
A: 在 Mapper 接口中定义方法，使用 MyBatis-Plus 的条件构造器。

### Q: 如何添加全局异常？
A: 在 exception 包下创建异常类，在 GlobalExceptionHandler 中处理。

### Q: 如何启用参数验证？
A: 在 Request DTO 中添加验证注解，在 Controller 方法参数上添加 `@Valid`。

### Q: 如何配置多数据源？
A: 在 application-{profile}.yml 中配置，使用 `spring.profiles.active` 切换。

## 示例场景

### 场景1：添加新功能模块
```
我需要添加一个订单管理模块，包含订单创建、查询、更新状态、删除功能
```

### 场景2：性能优化
```
GoodService 中的列表查询很慢，帮我优化
```

### 场景3：添加验证规则
```
为 GoodCreateRequest 添加更严格的验证规则
```

### 场景4：异常处理增强
```
添加一个自定义异常类，处理库存不足的情况
```

### 场景5：配置修改
```
修改数据库配置，使用连接池优化性能
```

## 最佳实践提醒

1. **始终使用 DTO**：不要直接暴露 Entity 给前端
2. **统一异常处理**：使用全局异常处理器，不要在 Controller 中捕获
3. **事务在 Service 层**：不要在 Controller 或 Mapper 层管理事务
4. **参数验证**：使用 Validation 注解，不要手动验证
5. **逻辑删除**：使用 `@TableLogic`，不要物理删除数据
6. **自动填充**：使用 `@TableField(fill = ...)` 自动设置时间字段
7. **使用 Lambda**：使用 LambdaQueryWrapper 避免字符串硬编码
8. **分页查询**：使用 MyBatis-Plus 的 Page 对象
9. **日志记录**：使用 SLF4J，不要使用 System.out
10. **代码简洁**：充分利用 Lombok 减少样板代码

## 获取帮助

直接描述你的需求，例如：
- "帮我创建一个..."
- "检查这个文件..."
- "优化这个方法..."
- "修复这个bug..."
- "解释这段代码..."
