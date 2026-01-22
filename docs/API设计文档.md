# 用户权限管理系统 API 设计文档

## 目录

1. [概述](#概述)
2. [基础信息](#基础信息)
3. [通用说明](#通用说明)
4. [用户管理API](#用户管理api)
5. [角色管理API](#角色管理api)
6. [权限管理API](#权限管理api)
7. [错误码定义](#错误码定义)

---

## 概述

本文档描述了用户权限管理系统的RESTful API接口规范，包括用户、角色、权限的增删改查操作。

### 版本信息

- **版本**: v1.0.0
- **作者**: Demo Team
- **更新日期**: 2026-01-22

---

## 基础信息

### 服务地址

- **开发环境**: `http://localhost:8080`
- **测试环境**: `http://test-server:8080`
- **生产环境**: `http://api.example.com`

### 协议

- **HTTP协议**: HTTP/1.1
- **编码格式**: UTF-8
- **数据格式**: JSON

---

## 通用说明

### 请求头

所有请求应包含以下请求头：

```
Content-Type: application/json
Accept: application/json
```

### 统一响应格式

所有API响应都遵循以下格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2026-01-22T10:30:00"
}
```

**字段说明**：

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码 |
| message | String | 响应消息 |
| data | Object | 响应数据 |
| timestamp | String | 响应时间戳 |

### 分页响应格式

分页查询的响应格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "content": [],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 100,
    "totalPages": 10,
    "last": false,
    "first": true
  },
  "timestamp": "2026-01-22T10:30:00"
}
```

### 分页参数

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 0 | 页码，从0开始 |
| size | Integer | 否 | 10 | 每页大小 |
| sortBy | String | 否 | id | 排序字段 |
| direction | String | 否 | ASC | 排序方向（ASC/DESC） |

---

## 用户管理API

### 1. 创建用户

**接口描述**: 创建新用户

**请求方式**: `POST`

**接口路径**: `/api/users`

**请求参数**:

```json
{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com",
  "phone": "13800138000",
  "realName": "测试用户",
  "roleIds": [1, 2]
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 | 验证规则 |
|------|------|------|------|----------|
| username | String | 是 | 用户名 | 3-50字符，只能包含字母、数字和下划线 |
| password | String | 是 | 密码 | 6-20字符 |
| email | String | 是 | 邮箱 | 有效的邮箱格式 |
| phone | String | 否 | 手机号 | 11位数字，1开头 |
| realName | String | 否 | 真实姓名 | 最多50字符 |
| roleIds | Array | 否 | 角色ID列表 | Long类型数组 |

**响应示例**:

```json
{
  "code": 200,
  "message": "用户创建成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "phone": "13800138000",
    "realName": "测试用户",
    "enabled": true,
    "locked": false,
    "createdAt": "2026-01-22T10:30:00",
    "updatedAt": "2026-01-22T10:30:00",
    "roles": [
      {
        "id": 1,
        "name": "管理员",
        "code": "ADMIN",
        "description": "系统管理员",
        "enabled": true,
        "createdAt": "2026-01-22T10:00:00",
        "updatedAt": "2026-01-22T10:00:00",
        "permissions": []
      }
    ]
  },
  "timestamp": "2026-01-22T10:30:00"
}
```

---

### 2. 获取用户详情

**接口描述**: 根据ID获取用户详情

**请求方式**: `GET`

**接口路径**: `/api/users/{id}`

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "phone": "13800138000",
    "realName": "测试用户",
    "enabled": true,
    "locked": false,
    "createdAt": "2026-01-22T10:30:00",
    "updatedAt": "2026-01-22T10:30:00",
    "roles": []
  },
  "timestamp": "2026-01-22T10:31:00"
}
```

---

### 3. 获取用户列表

**接口描述**: 分页获取用户列表

**请求方式**: `GET`

**接口路径**: `/api/users`

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 0 | 页码 |
| size | Integer | 否 | 10 | 每页大小 |
| sortBy | String | 否 | id | 排序字段 |
| direction | String | 否 | ASC | 排序方向 |

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "content": [
      {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com",
        "phone": "13800138000",
        "realName": "测试用户",
        "enabled": true,
        "locked": false,
        "createdAt": "2026-01-22T10:30:00",
        "updatedAt": "2026-01-22T10:30:00",
        "roles": []
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "first": true
  },
  "timestamp": "2026-01-22T10:32:00"
}
```

---

### 4. 搜索用户

**接口描述**: 根据关键词搜索用户

**请求方式**: `GET`

**接口路径**: `/api/users/search`

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyword | String | 是 | 搜索关键词 |
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页大小 |

**响应示例**: 与获取用户列表相同

---

### 5. 根据用户名获取用户

**接口描述**: 根据用户名获取用户信息

**请求方式**: `GET`

**接口路径**: `/api/users/username/{username}`

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名 |

**响应示例**: 与获取用户详情相同

---

### 6. 根据状态查询用户

**接口描述**: 根据启用状态查询用户

**请求方式**: `GET`

**接口路径**: `/api/users/by-status`

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| enabled | Boolean | 是 | 启用状态 |
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页大小 |

**响应示例**: 与获取用户列表相同

---

### 7. 更新用户

**接口描述**: 根据ID更新用户信息

**请求方式**: `PUT`

**接口路径**: `/api/users/{id}`

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**请求参数**:

```json
{
  "password": "newpassword123",
  "email": "newemail@example.com",
  "phone": "13900139000",
  "realName": "新名字",
  "enabled": true,
  "locked": false,
  "roleIds": [1, 2, 3]
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| password | String | 否 | 新密码 |
| email | String | 否 | 邮箱 |
| phone | String | 否 | 手机号 |
| realName | String | 否 | 真实姓名 |
| enabled | Boolean | 否 | 启用状态 |
| locked | Boolean | 否 | 锁定状态 |
| roleIds | Array | 否 | 角色ID列表 |

**响应示例**:

```json
{
  "code": 200,
  "message": "用户更新成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "newemail@example.com",
    "phone": "13900139000",
    "realName": "新名字",
    "enabled": true,
    "locked": false,
    "createdAt": "2026-01-22T10:30:00",
    "updatedAt": "2026-01-22T10:35:00",
    "roles": []
  },
  "timestamp": "2026-01-22T10:35:00"
}
```

---

### 8. 删除用户

**接口描述**: 根据ID删除用户

**请求方式**: `DELETE`

**接口路径**: `/api/users/{id}`

**路径参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "用户删除成功",
  "data": null,
  "timestamp": "2026-01-22T10:36:00"
}
```

---

### 9. 批量删除用户

**接口描述**: 根据ID列表批量删除用户

**请求方式**: `DELETE`

**接口路径**: `/api/users/batch`

**请求参数**:

```json
[1, 2, 3, 4, 5]
```

**响应示例**:

```json
{
  "code": 200,
  "message": "批量删除成功",
  "data": null,
  "timestamp": "2026-01-22T10:37:00"
}
```

---

## 角色管理API

### 1. 创建角色

**接口描述**: 创建新角色

**请求方式**: `POST`

**接口路径**: `/api/roles`

**请求参数**:

```json
{
  "name": "管理员",
  "code": "ADMIN",
  "description": "系统管理员角色",
  "permissionIds": [1, 2, 3]
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 | 验证规则 |
|------|------|------|------|----------|
| name | String | 是 | 角色名称 | 2-50字符 |
| code | String | 是 | 角色编码 | 2-50字符，只能包含大写字母和下划线 |
| description | String | 否 | 描述 | 最多200字符 |
| permissionIds | Array | 否 | 权限ID列表 | Long类型数组 |

**响应示例**:

```json
{
  "code": 200,
  "message": "角色创建成功",
  "data": {
    "id": 1,
    "name": "管理员",
    "code": "ADMIN",
    "description": "系统管理员角色",
    "enabled": true,
    "createdAt": "2026-01-22T10:40:00",
    "updatedAt": "2026-01-22T10:40:00",
    "permissions": [
      {
        "id": 1,
        "name": "用户查看",
        "code": "USER:READ",
        "description": "查看用户信息",
        "resource": "user",
        "action": "READ",
        "enabled": true,
        "createdAt": "2026-01-22T10:00:00",
        "updatedAt": "2026-01-22T10:00:00"
      }
    ]
  },
  "timestamp": "2026-01-22T10:40:00"
}
```

---

### 2. 获取角色详情

**接口描述**: 根据ID获取角色详情

**请求方式**: `GET`

**接口路径**: `/api/roles/{id}`

**响应示例**: 与创建角色响应类似

---

### 3. 获取角色列表

**接口描述**: 分页获取角色列表

**请求方式**: `GET`

**接口路径**: `/api/roles`

**查询参数**: 与用户列表相同

**响应示例**: 与用户列表响应格式类似

---

### 4. 搜索角色

**接口描述**: 根据关键词搜索角色

**请求方式**: `GET`

**接口路径**: `/api/roles/search`

**查询参数**: 与搜索用户相同

---

### 5. 根据编码获取角色

**接口描述**: 根据角色编码获取角色信息

**请求方式**: `GET`

**接口路径**: `/api/roles/code/{code}`

---

### 6. 根据状态查询角色

**接口描述**: 根据启用状态查询角色

**请求方式**: `GET`

**接口路径**: `/api/roles/by-status`

---

### 7. 更新角色

**接口描述**: 根据ID更新角色信息

**请求方式**: `PUT`

**接口路径**: `/api/roles/{id}`

**请求参数**:

```json
{
  "name": "高级管理员",
  "description": "高级系统管理员",
  "enabled": true,
  "permissionIds": [1, 2, 3, 4]
}
```

---

### 8. 删除角色

**接口描述**: 根据ID删除角色

**请求方式**: `DELETE`

**接口路径**: `/api/roles/{id}`

---

### 9. 批量删除角色

**接口描述**: 根据ID列表批量删除角色

**请求方式**: `DELETE`

**接口路径**: `/api/roles/batch`

---

## 权限管理API

### 1. 创建权限

**接口描述**: 创建新权限

**请求方式**: `POST`

**接口路径**: `/api/permissions`

**请求参数**:

```json
{
  "name": "用户查看",
  "code": "USER:READ",
  "description": "查看用户信息的权限",
  "resource": "user",
  "action": "READ"
}
```

**参数说明**:

| 参数 | 类型 | 必填 | 说明 | 验证规则 |
|------|------|------|------|----------|
| name | String | 是 | 权限名称 | 2-50字符 |
| code | String | 是 | 权限编码 | 2-100字符，只能包含大写字母、下划线和冒号 |
| description | String | 否 | 描述 | 最多200字符 |
| resource | String | 是 | 资源 | 最多50字符 |
| action | String | 是 | 操作 | CREATE、READ、UPDATE、DELETE或ALL |

**响应示例**:

```json
{
  "code": 200,
  "message": "权限创建成功",
  "data": {
    "id": 1,
    "name": "用户查看",
    "code": "USER:READ",
    "description": "查看用户信息的权限",
    "resource": "user",
    "action": "READ",
    "enabled": true,
    "createdAt": "2026-01-22T10:50:00",
    "updatedAt": "2026-01-22T10:50:00"
  },
  "timestamp": "2026-01-22T10:50:00"
}
```

---

### 2. 获取权限详情

**接口描述**: 根据ID获取权限详情

**请求方式**: `GET`

**接口路径**: `/api/permissions/{id}`

---

### 3. 获取权限列表

**接口描述**: 分页获取权限列表

**请求方式**: `GET`

**接口路径**: `/api/permissions`

---

### 4. 搜索权限

**接口描述**: 根据关键词搜索权限

**请求方式**: `GET`

**接口路径**: `/api/permissions/search`

---

### 5. 根据编码获取权限

**接口描述**: 根据权限编码获取权限信息

**请求方式**: `GET`

**接口路径**: `/api/permissions/code/{code}`

---

### 6. 根据资源查询权限

**接口描述**: 根据资源查询权限列表

**请求方式**: `GET`

**接口路径**: `/api/permissions/by-resource`

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| resource | String | 是 | 资源名称 |
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页大小 |

---

### 7. 根据状态查询权限

**接口描述**: 根据启用状态查询权限

**请求方式**: `GET`

**接口路径**: `/api/permissions/by-status`

---

### 8. 更新权限

**接口描述**: 根据ID更新权限信息

**请求方式**: `PUT`

**接口路径**: `/api/permissions/{id}`

**请求参数**:

```json
{
  "name": "用户完全访问",
  "description": "用户的完全访问权限",
  "resource": "user",
  "action": "ALL",
  "enabled": true
}
```

---

### 9. 删除权限

**接口描述**: 根据ID删除权限

**请求方式**: `DELETE`

**接口路径**: `/api/permissions/{id}`

---

### 10. 批量删除权限

**接口描述**: 根据ID列表批量删除权限

**请求方式**: `DELETE`

**接口路径**: `/api/permissions/batch`

---

## 错误码定义

### HTTP状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 404 | 资源未找到 |
| 409 | 资源已存在（冲突） |
| 500 | 服务器内部错误 |

### 业务错误码

| 错误码 | 说明 | 示例 |
|--------|------|------|
| 200 | 操作成功 | 操作成功 |
| 400 | 参数验证失败 | 用户名长度必须在3-50之间 |
| 404 | 资源未找到 | 用户未找到: id = 123 |
| 409 | 资源已存在 | 用户已存在: username = testuser |
| 500 | 系统内部错误 | 系统内部错误: 数据库连接失败 |

### 错误响应示例

#### 参数验证失败

```json
{
  "code": 400,
  "message": "参数验证失败",
  "data": {
    "username": "用户名长度必须在3-50之间",
    "email": "邮箱格式不正确"
  },
  "timestamp": "2026-01-22T10:55:00"
}
```

#### 资源未找到

```json
{
  "code": 404,
  "message": "用户未找到: id = 999",
  "data": null,
  "timestamp": "2026-01-22T10:56:00"
}
```

#### 资源已存在

```json
{
  "code": 409,
  "message": "用户已存在: username = testuser",
  "data": null,
  "timestamp": "2026-01-22T10:57:00"
}
```

#### 系统内部错误

```json
{
  "code": 500,
  "message": "系统内部错误: 数据库连接失败",
  "data": null,
  "timestamp": "2026-01-22T10:58:00"
}
```

---

## 附录

### 数据库模型

#### 用户表（users）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名（唯一） |
| password | VARCHAR(100) | 密码（加密） |
| email | VARCHAR(100) | 邮箱（唯一） |
| phone | VARCHAR(20) | 手机号 |
| real_name | VARCHAR(50) | 真实姓名 |
| enabled | BOOLEAN | 启用状态 |
| locked | BOOLEAN | 锁定状态 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

#### 角色表（roles）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 角色名称（唯一） |
| code | VARCHAR(50) | 角色编码（唯一） |
| description | VARCHAR(200) | 描述 |
| enabled | BOOLEAN | 启用状态 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

#### 权限表（permissions）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 权限名称（唯一） |
| code | VARCHAR(100) | 权限编码（唯一） |
| description | VARCHAR(200) | 描述 |
| resource | VARCHAR(50) | 资源 |
| action | VARCHAR(20) | 操作 |
| enabled | BOOLEAN | 启用状态 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

### API测试工具

推荐使用以下工具测试API：

- **Postman**: 图形化API测试工具
- **cURL**: 命令行工具
- **Swagger UI**: 在线API文档和测试工具（访问 `/swagger-ui.html`）

---

**文档版本**: v1.0.0  
**最后更新**: 2026-01-22
