# Spring Boot 3.x + MyBatis-Plus CRUD Demo

基于Spring Boot 3.x、Maven、MyBatis-Plus和MySQL的商品管理系统CRUD示例项目。

## 技术栈

- **Spring Boot**: 3.4.0
- **Java**: 17+
- **构建工具**: Maven
- **数据库**: MySQL / PostgreSQL
- **ORM框架**: MyBatis-Plus 3.5.10
- **工具**: Lombok, Validation

## 项目结构

```
spring-demo/
├── src/main/java/com/example/springdemo/
│   ├── SpringDemoApplication.java    # 主启动类
│   ├── config/                       # 配置类
│   ├── controller/                   # REST控制器
│   ├── service/                      # 业务逻辑层
│   ├── mapper/                       # MyBatis-Plus Mapper
│   ├── entity/                       # 实体类
│   ├── dto/                          # 数据传输对象
│   ├── exception/                    # 异常处理
│   └── util/                         # 工具类
└── src/main/resources/
    ├── application.yml                # 主配置
    ├── application-mysql.yml          # MySQL配置
    └── db/migration.sql               # 建表脚本
```

## 快速开始

### 1. 创建数据库

执行SQL脚本创建数据库和表：

```bash
mysql -u root -p < src/main/resources/db/migration.sql
```

### 2. 修改数据库配置

编辑 `src/main/resources/application-mysql.yml`，修改数据库用户名和密码：

```yaml
spring:
  datasource:
    username: your_username
    password: your_password
```

### 3. 运行项目

```bash
mvn spring-boot:run
```

或使用PostgreSQL：

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

### 4. 访问API

应用启动后，访问 http://localhost:8080/api/goods

## API文档

### 商品管理接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/goods | 创建商品 |
| GET | /api/goods/{id} | 根据ID获取商品 |
| GET | /api/goods | 分页查询商品列表 |
| PUT | /api/goods/{id} | 更新商品 |
| DELETE | /api/goods/{id} | 删除商品（逻辑删除） |
| GET | /api/goods/search | 搜索商品 |

### 请求示例

**创建商品：**
```bash
curl -X POST http://localhost:8080/api/goods \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "price": 8999.00,
    "description": "Apple最新旗舰手机",
    "stock": 50
  }'
```

**查询商品列表：**
```bash
curl http://localhost:8080/api/goods?page=1&size=10
```

## 学习要点

- Spring Boot 3.x项目结构和自动配置
- MyBatis-Plus的BaseMapper接口
- RESTful API设计规范
- 统一异常处理机制
- Bean Validation参数验证
- 分页查询和条件构造

## 许可证

MIT License
