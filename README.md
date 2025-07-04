# V2EX Clone - 小埋主题论坛

一个模仿 V2EX 风格的论坛应用，使用 Spring Boot + React 技术栈开发，所有头像都使用了可爱的小埋图片。

## 技术栈

### 后端
- Spring Boot 3.2.0
- Java 21
- Spring Data JPA
- H2 数据库
- Maven

### 前端
- React 18
- React Router
- Axios
- 原生 CSS

## 快速开始

### 启动后端
```bash
cd backend
mvn spring-boot:run
```

### 启动前端
```bash
cd frontend
npm install
npm start
```

## 访问地址

- 前端应用: http://localhost:3000
- 后端 API: http://localhost:8080
- H2 数据库控制台: http://localhost:8080/h2-console

## 功能特性

- ✅ V2EX 风格的界面设计
- ✅ 主题列表展示
- ✅ 主题详情页面
- ✅ 响应式布局
- ✅ 小埋主题头像
- ✅ 热门节点展示
- ✅ 社区统计信息

## 项目结构

```
v2ex-clone/
├── backend/          # Spring Boot 后端
│   ├── src/
│   └── pom.xml
├── frontend/         # React 前端
│   ├── src/
│   └── package.json
└── README.md
```
