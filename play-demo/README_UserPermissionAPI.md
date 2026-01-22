# Play Framework - 用户权限管理系统 RESTful API

[![Play Framework](https://img.shields.io/badge/Play-2.9.x-green.svg)](https://www.playframework.com/)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 项目简介

这是一个基于**Play Framework**开发的完整的用户权限管理系统RESTful API，实现了用户、角色、权限的增删改查操作，包含参数验证、异常处理、分页查询、请求日志记录等功能。

### 主要特性

✅ **完整的CRUD操作** - 用户、角色、权限的增删改查  
✅ **参数验证** - 使用Jakarta Validation进行参数校验  
✅ **统一异常处理** - 全局异常处理，统一响应格式  
✅ **分页查询** - 支持灵活的分页功能  
✅ **模糊搜索** - 支持关键词模糊搜索  
✅ **CORS支持** - 配置跨域资源共享  
✅ **密码加密** - 使用BCrypt加密用户密码  
✅ **JPA持久化** - 使用Hibernate JPA进行数据持久化  
✅ **依赖注入** - 基于Guice的依赖注入  

## 🏗️ 技术栈

- **Play Framework**: 2.9.x
- **Java**: 17
- **Hibernate JPA**: 6.2.x
- **H2 Database**: 内存数据库（开发环境）
- **MySQL**: 生产环境数据库（可选）
- **BCrypt**: 密码加密
- **Guice**: 依赖注入
- **SBT**: 项目构建工具

## 📁 项目结构

```
play-demo/
├── app/
│   ├── controllers/                      # 控制器层
│   │   ├── UserManagementController.java
│   │   ├── RoleManagementController.java
│   │   └── PermissionManagementController.java
│   ├── dto/                              # 数据传输对象
│   │   ├── ApiResponse.java
│   │   ├── PageResponse.java
│   │   ├── UserRequest.java
│   │   ├── RoleRequest.java
│   │   └── PermissionRequest.java
│   ├── exceptions/                       # 异常类
│   │   ├── ResourceNotFoundException.java
│   │   └── ResourceAlreadyExistsException.java
│   ├── models/                           # 实体模型
│   │   ├── BaseEntity.java
│   │   ├── User.java
│   │   ├── Role.java
│   │   └── Permission.java
│   ├── repositories/                     # 数据访问层
│   │   ├── UserRepository.java
│   │   ├── RoleRepository.java
│   │   └── PermissionRepository.java
│   └── services/                         # 业务逻辑层
│       ├── UserService.java
│       ├── RoleService.java
│       └── PermissionService.java
├── conf/
│   ├── application.conf                  # 应用配置
│   ├── routes                            # 路由配置
│   └── META-INF/
│       └── persistence.xml               # JPA配置
├── docs/
│   └── API设计文档-PlayFramework.md      # API文档
├── postman/
│   └── UserPermissionAPI.postman_collection.json  # Postman测试集合
├── build.sbt                             # SBT构建配置
└── README_UserPermissionAPI.md           # 项目说明
```

## 🚀 快速开始

### 前置要求

- JDK 17+
- SBT 1.9+
- （可选）MySQL 8.0+

### 安装步骤

1. **克隆项目**

```bash
cd play-demo
```

2. **编译项目**

```bash
sbt compile
```

3. **运行项目**

```bash
sbt run
```

4. **访问应用**

- **API基础地址**: http://localhost:9000
- **健康检查**: http://localhost:9000/health

## 📖 API文档

### API概览

#### 用户管理 `/api/management/users`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/management/users` | 创建用户 |
| GET | `/api/management/users/{id}` | 获取用户详情 |
| GET | `/api/management/users` | 获取用户列表（分页） |
| GET | `/api/management/users/search` | 搜索用户 |
| GET | `/api/management/users/username/{username}` | 根据用户名获取用户 |
| GET | `/api/management/users/by-status` | 根据状态查询用户 |
| PUT | `/api/management/users/{id}` | 更新用户 |
| DELETE | `/api/management/users/{id}` | 删除用户 |
| DELETE | `/api/management/users/batch` | 批量删除用户 |

#### 角色管理 `/api/management/roles`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/management/roles` | 创建角色 |
| GET | `/api/management/roles/{id}` | 获取角色详情 |
| GET | `/api/management/roles` | 获取角色列表（分页） |
| GET | `/api/management/roles/search` | 搜索角色 |
| GET | `/api/management/roles/code/{code}` | 根据编码获取角色 |
| PUT | `/api/management/roles/{id}` | 更新角色 |
| DELETE | `/api/management/roles/{id}` | 删除角色 |
| DELETE | `/api/management/roles/batch` | 批量删除角色 |

#### 权限管理 `/api/management/permissions`

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/management/permissions` | 创建权限 |
| GET | `/api/management/permissions/{id}` | 获取权限详情 |
| GET | `/api/management/permissions` | 获取权限列表（分页） |
| GET | `/api/management/permissions/search` | 搜索权限 |
| GET | `/api/management/permissions/code/{code}` | 根据编码获取权限 |
| GET | `/api/management/permissions/by-resource` | 根据资源查询权限 |
| PUT | `/api/management/permissions/{id}` | 更新权限 |
| DELETE | `/api/management/permissions/{id}` | 删除权限 |
| DELETE | `/api/management/permissions/batch` | 批量删除权限 |

### 请求示例

#### 创建用户

```bash
curl -X POST http://localhost:9000/api/management/users \
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
curl http://localhost:9000/api/management/users?page=0&size=10
```

## 📚 文档

- [API设计文档](docs/API设计文档-PlayFramework.md) - 完整的API接口说明
- [Postman测试集合](postman/UserPermissionAPI.postman_collection.json) - Postman测试用例

## ⚙️ 配置说明

### 数据库配置

#### H2内存数据库（默认）

```properties
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL"
db.default.username=sa
db.default.password=""
```

#### MySQL数据库（可选）

修改 `conf/application.conf`：

```properties
db.default.driver=com.mysql.cj.jdbc.Driver
db.default.url="jdbc:mysql://localhost:3306/user_permission_db?useSSL=false"
db.default.username=root
db.default.password=your_password
```

## 📊 项目统计

| 项目 | 数量 |
|------|------|
| **Java文件** | 23个 |
| **API接口** | 26个 |
| **实体类** | 4个 |
| **Repository** | 3个 |
| **Service** | 3个 |
| **Controller** | 3个 |
| **DTO类** | 6个 |

## 💎 核心亮点

1. **Play Framework** - 轻量级、高性能的Web框架
2. **完整的CRUD** - 用户、角色、权限三层管理
3. **JPA持久化** - 使用Hibernate JPA进行数据持久化
4. **参数验证** - Jakarta Validation注解验证
5. **统一异常处理** - 全局异常捕获和处理
6. **密码加密** - BCrypt安全加密
7. **分页查询** - 支持灵活分页
8. **依赖注入** - Guice依赖注入

## 🧪 测试

### 使用Postman测试

1. 导入 `postman/UserPermissionAPI.postman_collection.json`
2. 设置环境变量 `baseUrl` 为 `http://localhost:9000`
3. 执行测试用例

### 使用cURL测试

参考API文档中的cURL示例

## 🔒 安全说明

### 当前实现

- ✅ 密码使用BCrypt加密存储
- ✅ 参数验证防止非法输入
- ✅ 统一异常处理防止信息泄露
- ✅ CORS配置控制跨域访问

### 生产环境建议

- 🔐 添加JWT认证机制
- 🔐 实现基于角色的访问控制（RBAC）
- 🔐 添加请求频率限制
- 🔐 启用HTTPS
- 🔐 配置更严格的CORS策略

## 📄 许可证

本项目采用 MIT 许可证。

## 👥 作者

Demo Team

---

**项目版本**: v1.0.0  
**最后更新**: 2026-01-22
