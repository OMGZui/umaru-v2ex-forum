# GitHub OAuth 设置指南

## 1. 创建GitHub OAuth应用

1. 访问 [GitHub Developer Settings](https://github.com/settings/developers)
2. 点击 "New OAuth App"
3. 填写应用信息：
   - **Application name**: V2EX Clone
   - **Homepage URL**: http://localhost:3000
   - **Application description**: V2EX克隆应用
   - **Authorization callback URL**: http://localhost:8080/oauth2/callback/github

4. 点击 "Register application"
5. 记录下 `Client ID` 和 `Client Secret`

## 2. 配置环境变量

### 方法1: 直接修改application.yml
在 `backend/src/main/resources/application.yml` 中替换：
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: 你的GitHub客户端ID
            client-secret: 你的GitHub客户端密钥
```

### 方法2: 使用环境变量（推荐）
设置环境变量：
```bash
export GITHUB_CLIENT_ID=你的GitHub客户端ID
export GITHUB_CLIENT_SECRET=你的GitHub客户端密钥
```

或者在启动应用时指定：
```bash
GITHUB_CLIENT_ID=你的ID GITHUB_CLIENT_SECRET=你的密钥 mvn spring-boot:run
```

## 3. 启动应用

1. 启动后端：
```bash
cd backend
mvn spring-boot:run
```

2. 启动前端：
```bash
cd frontend
npm start
```

## 4. 测试OAuth登录

1. 访问 http://localhost:3000
2. 点击 "登录" 按钮
3. 点击 "使用 GitHub 登录"
4. 授权应用访问你的GitHub账户
5. 登录成功后会重定向回应用

## 5. 故障排除

### 常见问题：

1. **回调URL不匹配**
   - 确保GitHub应用的回调URL设置为: `http://localhost:8080/oauth2/callback/github`

2. **CORS错误**
   - 确保后端CORS配置正确
   - 检查前端请求的URL是否正确

3. **JWT解析错误**
   - 检查JWT密钥配置
   - 确保token格式正确

4. **数据库连接错误**
   - 确保PostgreSQL正在运行
   - 检查数据库连接配置

### 调试技巧：

1. 查看浏览器开发者工具的网络请求
2. 检查后端控制台日志
3. 验证GitHub OAuth应用配置
4. 测试API端点：`GET http://localhost:8080/api/auth/user`

## 6. 生产环境配置

在生产环境中：
1. 创建新的GitHub OAuth应用
2. 设置正确的生产环境URL
3. 使用环境变量管理敏感信息
4. 配置HTTPS
5. 更新CORS设置
