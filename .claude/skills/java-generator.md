# Java Spring Boot CRUD Generator

为 spring-demo 项目快速生成符合规范的 CRUD 功能代码。

## 使用方法

调用此 skill 时，请提供以下信息：
- 实体类名称（如：Product、Order）
- 需要的字段及类型（如：name:String, price:BigDecimal）
- 可选：额外需求（如：分页、搜索、软删除）

示例：
```
/java-generator 实体：Product，字段：name(String), price(BigDecimal), stock(Integer), description(String)
```

## 项目规范

### 技术栈
- Spring Boot 3.4.0
- MyBatis-Plus 3.5.10
- Lombok
- Java 17
- MySQL/PostgreSQL

### 包结构
```
com.example.springdemo/
├── controller/      # REST API 控制器
├── service/         # 业务逻辑层
│   └── impl/       # 业务实现
├── mapper/         # 数据访问层
├── entity/         # 实体类（数据库表映射）
├── dto/            # 数据传输对象
├── exception/      # 异常处理
├── config/         # 配置类
└── util/           # 工具类
```

### 命名规范

#### 类命名
- **实体类**：名词（如 `Product`）
- **Controller**：`{实体名}Controller`（如 `ProductController`）
- **Service 接口**：`{实体名}Service`（如 `ProductService`）
- **Service 实现**：`{实体名}ServiceImpl`（如 `ProductServiceImpl`）
- **Mapper**：`{实体名}Mapper`（如 `ProductMapper`）
- **DTO Request**：`{实体名}{Create/Update}Request`（如 `ProductCreateRequest`）
- **DTO Response**：`{实体名}Response`（如 `ProductResponse`）

#### 方法命名
- **Controller/Service**：
  - `create{实体名}` - 创建
  - `get{实体名}ById` - 根据ID查询
  - `update{实体名}` - 更新
  - `delete{实体名}` - 删除
  - `get{实体名}List` - 分页查询列表
  - `search{实体名}` - 搜索

### 代码规范

#### Entity（实体类）
```java
@Data
@TableName("{表名}")
public class {实体名} {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    // 业务字段...
}
```

#### Controller（控制器）
```java
@RestController
@RequestMapping("/api/{资源名小写}")
@Tag(name = "{实体名}管理", description = "{实体名}CRUD接口")
public class {实体名}Controller {

    @Autowired
    private {实体名}Service {实体名小写}Service;

    @PostMapping
    @Operation(summary = "创建{实体名}")
    public Result<{实体名}Response> create{实体名}(
        @Valid @RequestBody {实体名}CreateRequest request) {
        // ...
    }
}
```

#### Service（业务层）
```java
public interface {实体名}Service {
    {实体名}Response create{实体名}({实体名}CreateRequest request);
    {实体名}Response get{实体名}ById(Long id);
    {实体名}Response update{实体名}(Long id, {实体名}UpdateRequest request);
    void delete{实体名}(Long id);
    Page<{实体名}Response> get{实体名}List(PageDTO pageDTO);
}
```

#### Mapper（数据访问层）
```java
@Mapper
public interface {实体名}Mapper extends BaseMapper<{实体名}> {
    // 继承 BaseMapper 自动获得 CRUD 方法
}
```

#### DTO（数据传输对象）
```java
@Data
public class {实体名}CreateRequest {
    @NotBlank(message = "{字段名}不能为空")
    private String field1;

    @NotNull(message = "{字段名}不能为空")
    private BigDecimal field2;
}

@Data
public class {实体名}Response {
    private Long id;
    private String field1;
    private BigDecimal field2;
    private LocalDateTime createTime;
}
```

### 统一响应格式

使用项目中的 `Result<T>` 类：
```java
Result.success(data)        // 成功响应
Result.error(message)       // 错误响应
Result.notFound(message)    // 404响应
Result.validateFailed()     // 验证失败
```

### 异常处理

使用项目中的自定义异常：
```java
throw new ResourceNotFoundException("{资源名}不存在", id);
throw new BusinessException("{业务错误描述}");
```

### 验证注解

常用验证注解：
- `@NotNull` - 不能为 null
- `@NotBlank` - 字符串不能为空
- `@Min(value)` - 最小值
- `@Max(value)` - 最大值
- `@Email` - 邮箱格式
- `@Pattern(regexp)` - 正则表达式

## 生成内容

调用此 skill 后，将为你生成以下完整的代码文件：

1. **Entity** - 数据库实体类
2. **Mapper** - MyBatis-Plus Mapper 接口
3. **Service 接口** - 业务逻辑接口
4. **Service 实现** - 业务逻辑实现
5. **Controller** - REST API 控制器
6. **DTO** - Request 和 Response 对象
7. **数据库迁移脚本** - SQL 建表语句

所有代码将遵循项目现有的风格和规范，包括：
- Lombok 注解简化代码
- MyBatis-Plus 自动填充
- 逻辑删除支持
- 统一异常处理
- 统一响应格式
- 参数验证

## 示例调用

```
/java-generator 实体：Category，字段：name(String, 必填), description(String), sort(Integer), parentId(Long)
```

这将生成一个分类管理模块，包含完整的 CRUD 功能。
