# Play Framework 用户权限管理 API 文档

## 基础信息

- **服务地址**: `http://localhost:9000`
- **数据格式**: JSON
- **编码**: UTF-8

## 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2026-01-22T10:30:00"
}
```

## API接口列表

### 用户管理 API

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/management/users` | 创建用户 |
| GET | `/api/management/users/{id}` | 获取用户详情 |
| GET | `/api/management/users` | 获取用户列表（分页） |
| GET | `/api/management/users/search` | 搜索用户 |
| GET | `/api/management/users/username/{username}` | 根据用户名获取 |
| GET | `/api/management/users/by-status` | 根据状态查询 |
| PUT | `/api/management/users/{id}` | 更新用户 |
| DELETE | `/api/management/users/{id}` | 删除用户 |
| DELETE | `/api/management/users/batch` | 批量删除 |

### 角色管理 API

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/management/roles` | 创建角色 |
| GET | `/api/management/roles/{id}` | 获取角色详情 |
| GET | `/api/management/roles` | 获取角色列表 |
| GET | `/api/management/roles/search` | 搜索角色 |
| GET | `/api/management/roles/code/{code}` | 根据编码获取 |
| PUT | `/api/management/roles/{id}` | 更新角色 |
| DELETE | `/api/management/roles/{id}` | 删除角色 |
| DELETE | `/api/management/roles/batch` | 批量删除 |

### 权限管理 API

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/management/permissions` | 创建权限 |
| GET | `/api/management/permissions/{id}` | 获取权限详情 |
| GET | `/api/management/permissions` | 获取权限列表 |
| GET | `/api/management/permissions/search` | 搜索权限 |
| GET | `/api/management/permissions/code/{code}` | 根据编码获取 |
| GET | `/api/management/permissions/by-resource` | 根据资源查询 |
| PUT | `/api/management/permissions/{id}` | 更新权限 |
| DELETE | `/api/management/permissions/{id}` | 删除权限 |
| DELETE | `/api/management/permissions/batch` | 批量删除 |

## 请求示例

### 创建用户

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

### 获取用户列表

```bash
curl "http://localhost:9000/api/management/users?page=0&size=10"
```

### 搜索用户

```bash
curl "http://localhost:9000/api/management/users/search?keyword=test&page=0&size=10"
```

## 错误码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 404 | 资源未找到 |
| 409 | 资源已存在 |
| 500 | 服务器错误 |

## 快速启动

```bash
cd play-demo
sbt run
```

访问：http://localhost:9000
