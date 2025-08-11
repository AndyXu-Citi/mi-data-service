# 马拉松报名系统后端服务

## 项目介绍

马拉松报名系统后端服务是一个基于Spring Boot的RESTful API服务，提供马拉松赛事报名、管理等功能的后端支持。

## 技术栈

- Spring Boot 2.7.x：应用框架
- MyBatis Plus：ORM框架
- MySQL：关系型数据库
- Redis：缓存
- MongoDB：非关系型数据库
- MinIO：对象存储
- JWT：认证授权
- Knife4j：API文档

## 项目结构

```
mi-data-service
├── src/main/java/com/marathon
│   ├── MarathonApplication.java            # 应用入口
│   ├── common                              # 公共模块
│   │   ├── api                             # API相关
│   │   │   └── R.java                      # 统一响应对象
│   │   ├── exception                       # 异常处理
│   │   │   ├── GlobalExceptionHandler.java # 全局异常处理器
│   │   │   ├── ServiceException.java       # 业务异常
│   │   │   ├── UnauthorizedException.java  # 未授权异常
│   │   │   └── ForbiddenException.java     # 无权限异常
│   │   └── security                        # 安全相关
│   │       └── JwtTokenUtil.java           # JWT工具类
│   ├── controller                          # 控制器层
│   │   ├── UserController.java             # 用户控制器
│   │   ├── EventController.java            # 赛事控制器
│   │   ├── RegistrationController.java     # 报名控制器
│   │   └── FavoriteController.java         # 收藏控制器
│   ├── service                             # 服务层
│   │   ├── UserService.java                # 用户服务接口
│   │   ├── EventService.java               # 赛事服务接口
│   │   ├── RegistrationService.java        # 报名服务接口
│   │   ├── FavoriteService.java            # 收藏服务接口
│   │   └── impl                            # 服务实现
│   │       ├── UserServiceImpl.java        # 用户服务实现
│   │       ├── EventServiceImpl.java       # 赛事服务实现
│   │       ├── RegistrationServiceImpl.java# 报名服务实现
│   │       └── FavoriteServiceImpl.java    # 收藏服务实现
│   ├── mapper                              # 数据访问层
│   │   ├── UserMapper.java                 # 用户Mapper
│   │   ├── EventMapper.java                # 赛事Mapper
│   │   ├── EventItemMapper.java            # 赛事项目Mapper
│   │   ├── RegistrationMapper.java         # 报名Mapper
│   │   └── FavoriteMapper.java             # 收藏Mapper
│   └── domain                              # 领域模型
│       └── entity                          # 实体类
│           ├── User.java                   # 用户实体
│           ├── Event.java                  # 赛事实体
│           ├── EventItem.java              # 赛事项目实体
│           ├── Registration.java           # 报名实体
│           └── Favorite.java               # 收藏实体
└── src/main/resources
    └── application.yml                     # 应用配置文件
```

## 核心功能

### 用户管理

- 用户注册、登录（含微信小程序登录）
- 用户信息管理
- 密码修改与重置

### 赛事管理

- 赛事信息的增删改查
- 赛事项目管理
- 热门赛事、推荐赛事、即将开始赛事
- 赛事搜索

### 报名管理

- 赛事报名
- 报名信息管理
- 报名审核
- 报名支付与退款
- 参赛号码分配

### 收藏管理

- 赛事收藏与取消
- 用户收藏列表

## 启动说明

1. 确保已安装JDK 1.8+、Maven 3.6+、MySQL 5.7+
2. 创建数据库marathon，并导入SQL脚本
3. 修改application.yml中的数据库连接信息
4. 执行以下命令启动项目：

```bash
mvn spring-boot:run
```

5. 访问API文档：http://localhost:8080/doc.html

## API文档

项目集成了Knife4j，启动后可通过访问 http://localhost:8080/doc.html 查看API文档。