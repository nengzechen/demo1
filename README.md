# 用户权限管理系统 RESTful API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 项目简介

这是一个完整的用户权限管理系统RESTful API，实现了用户、角色、权限的增删改查操作，包含参数验证、异常处理、分页查询、请求日志记录、CORS支持等功能。

### 主要特性

✅ **完整的CRUD操作** - 用户、角色、权限的增删改查  
✅ **参数验证** - 使用Jakarta Validation进行参数校验  
✅ **统一异常处理** - 全局异常处理，统一响应格式  
✅ **分页查询** - 支持灵活的分页和排序功能  
✅ **模糊搜索** - 支持关键词模糊搜索  
✅ **请求日志记录** - 自动记录所有API请求日志  
✅ **CORS支持** - 配置跨域资源共享  
✅ **API文档** - 集成Swagger UI在线文档  
✅ **单元测试** - 完整的单元测试覆盖  
✅ **密码加密** - 使用BCrypt加密用户密码  

## 🏗️ 技术栈

- **Java**: 17
- **Spring Boot**: 3.2.1
- **Spring Data JPA**: 数据持久化
- **H2 Database**: 内存数据库（开发环境）
- **MySQL**: 生产环境数据库（可选）
- **Lombok**: 简化Java代码
- **SpringDoc OpenAPI**: API文档生成
- **JUnit 5**: 单元测试框架
- **Maven**: 项目构建工具

## 📁 项目结构

```
demo1/
├── src/
│   ├── main/
│   │   ├── java/com/demo/
│   │   │   ├── Application.java                 # 启动类
│   │   │   ├── config/                          # 配置类
│   │   │   │   ├── LoggingInterceptor.java      # 日志拦截器
│   │   │   │   └── WebConfig.java               # Web配置（CORS等）
│   │   │   ├── controller/                      # 控制器层
│   │   │   │   ├── UserController.java          # 用户管理API
│   │   │   │   ├── RoleController.java          # 角色管理API
│   │   │   │   └── PermissionController.java    # 权限管理API
│   │   │   ├── dto/                             # 数据传输对象
│   │   │   │   ├── request/                     # 请求DTO
│   │   │   │   │   ├── UserCreateRequest.java
│   │   │   │   │   ├── UserUpdateRequest.java
│   │   │   │   │   ├── RoleCreateRequest.java
│   │   │   │   │   ├── RoleUpdateRequest.java
│   │   │   │   │   ├── PermissionCreateRequest.java
│   │   │   │   │   └── PermissionUpdateRequest.java
│   │   │   │   └── response/                    # 响应DTO
│   │   │   │       ├── ApiResponse.java         # 统一响应格式
│   │   │   │       ├── PageResponse.java        # 分页响应
│   │   │   │       ├── UserResponse.java
│   │   │   │       ├── RoleResponse.java
│   │   │   │       └── PermissionResponse.java
│   │   │   ├── entity/                          # 实体类
│   │   │   │   ├── User.java                    # 用户实体
│   │   │   │   ├── Role.java                    # 角色实体
│   │   │   │   └── Permission.java              # 权限实体
│   │   │   ├── exception/                       # 异常处理
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── ResourceAlreadyExistsException.java
│   │   │   │   └── GlobalExceptionHandler.java  # 全局异常处理器
│   │   │   ├── repository/                      # 数据访问层
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   └── PermissionRepository.java
│   │   │   ├── service/                         # 业务逻辑层
│   │   │   │   ├── UserService.java
│   │   │   │   ├── RoleService.java
│   │   │   │   └── PermissionService.java
│   │   │   └── util/                            # 工具类
│   │   │       └── StringUtils.java
│   │   └── resources/
│   │       ├── application.properties            # 应用配置
│   │       └── application-mysql.properties      # MySQL配置
│   └── test/                                     # 测试代码
│       └── java/com/demo/
│           ├── ApplicationTests.java
│           ├── controller/
│           │   └── UserControllerTest.java
│           └── service/
│               └── UserServiceTest.java
├── docs/                                         # 文档目录
│   ├── API设计文档.md                            # API设计文档
│   ├── 测试报告.md                               # 测试报告
│   ├── Postman测试用例.json                      # Postman测试集合
│   └── Postman使用说明.md                        # Postman使用指南
├── pom.xml                                       # Maven配置
└── README_API.md                                 # 项目说明文档
```

## 🚀 快速开始

### 前置要求

- JDK 17+
- Maven 3.6+
- （可选）MySQL 8.0+

### 安装步骤

1. **克隆项目**

```bash
git clone <repository-url>
cd demo1
```

2. **编译项目**

```bash
mvn clean install
```

3. **运行项目**

```bash
# 使用H2内存数据库（默认）
mvn spring-boot:run

# 或使用MySQL数据库
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

4. **访问应用**

- **API文档**: http://localhost:8080/swagger-ui.html
- **H2控制台**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - 用户名: `sa`
  - 密码: （空）

## 📖 API文档

### 在线文档

启动应用后，访问 http://localhost:8080/swagger-ui.html 查看完整的API文档。

### API概览

#### 用户管理 `/api/users`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/users` | 创建用户 |
| GET | `/api/users/{id}` | 获取用户详情 |
| GET | `/api/users` | 获取用户列表（分页） |
| GET | `/api/users/search` | 搜索用户 |
| GET | `/api/users/username/{username}` | 根据用户名获取用户 |
| GET | `/api/users/by-status` | 根据状态查询用户 |
| PUT | `/api/users/{id}` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |
| DELETE | `/api/users/batch` | 批量删除用户 |

#### 角色管理 `/api/roles`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/roles` | 创建角色 |
| GET | `/api/roles/{id}` | 获取角色详情 |
| GET | `/api/roles` | 获取角色列表（分页） |
| GET | `/api/roles/search` | 搜索角色 |
| GET | `/api/roles/code/{code}` | 根据编码获取角色 |
| GET | `/api/roles/by-status` | 根据状态查询角色 |
| PUT | `/api/roles/{id}` | 更新角色 |
| DELETE | `/api/roles/{id}` | 删除角色 |
| DELETE | `/api/roles/batch` | 批量删除角色 |

#### 权限管理 `/api/permissions`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/permissions` | 创建权限 |
| GET | `/api/permissions/{id}` | 获取权限详情 |
| GET | `/api/permissions` | 获取权限列表（分页） |
| GET | `/api/permissions/search` | 搜索权限 |
| GET | `/api/permissions/code/{code}` | 根据编码获取权限 |
| GET | `/api/permissions/by-resource` | 根据资源查询权限 |
| GET | `/api/permissions/by-status` | 根据状态查询权限 |
| PUT | `/api/permissions/{id}` | 更新权限 |
| DELETE | `/api/permissions/{id}` | 删除权限 |
| DELETE | `/api/permissions/batch` | 批量删除权限 |

### 请求示例

#### 创建用户

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "phone": "13800138000",
    "realName": "测试用户"
  }'
```

#### 获取用户列表

```bash
curl -X GET "http://localhost:8080/api/users?page=0&size=10&sortBy=id&direction=ASC"
```

#### 搜索用户

```bash
curl -X GET "http://localhost:8080/api/users/search?keyword=test&page=0&size=10"
```

## 🧪 测试

### 运行单元测试

```bash
mvn test
```

### 使用Postman测试

1. 导入 `docs/Postman测试用例.json` 到Postman
2. 参考 `docs/Postman使用说明.md` 进行测试

## 📚 文档

- [API设计文档](docs/API设计文档.md) - 完整的API接口说明
- [测试报告](docs/测试报告.md) - 测试用例和测试结果
- [Postman使用说明](docs/Postman使用说明.md) - Postman测试指南

## ⚙️ 配置说明

### 数据库配置

#### H2内存数据库（默认）

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
```

#### MySQL数据库

1. 创建数据库：

```sql
CREATE DATABASE user_permission_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件或使用MySQL profile：

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

### 应用配置

主要配置项（`application.properties`）：

```properties
# 服务端口
server.port=8080

# 分页配置
spring.data.web.pageable.default-page-size=10
spring.data.web.pageable.max-page-size=100

# 日志级别
logging.level.com.demo=DEBUG
```

## 🔒 安全说明

### 当前实现

- ✅ 密码使用BCrypt加密存储
- ✅ 参数验证防止非法输入
- ✅ 统一异常处理防止信息泄露
- ✅ CORS配置控制跨域访问

### 生产环境建议

在生产环境中，建议添加以下安全措施：

- 🔐 添加JWT认证机制
- 🔐 实现基于角色的访问控制（RBAC）
- 🔐 添加请求频率限制
- 🔐 启用HTTPS
- 🔐 配置更严格的CORS策略
- 🔐 添加SQL注入防护
- 🔐 配置安全HTTP头

## 🐛 常见问题

### Q1: 端口被占用

**错误**: `Port 8080 is already in use`

**解决方案**: 修改 `application.properties` 中的端口配置：
```properties
server.port=8081
```

### Q2: 数据库连接失败

**错误**: `Cannot connect to database`

**解决方案**: 
1. 检查MySQL是否已启动
2. 确认数据库配置信息是否正确
3. 确保数据库已创建

### Q3: 参数验证失败

**错误**: HTTP 400，参数验证错误

**解决方案**: 检查请求参数是否符合验证规则，参考API文档中的参数说明

## 📈 性能优化建议

1. **缓存**: 对频繁查询的数据添加Redis缓存
2. **索引**: 为常用查询字段添加数据库索引
3. **分页**: 大数据量查询使用分页
4. **异步**: 耗时操作使用异步处理
5. **连接池**: 优化数据库连接池配置

## 🤝 贡献

欢迎提交Issue和Pull Request！

## 📄 许可证

本项目采用 MIT 许可证。

## 👥 作者

Demo Team

---

**项目版本**: v1.0.0  
**最后更新**: 2026-01-22
