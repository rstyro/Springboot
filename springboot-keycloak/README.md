# Spring Boot Keycloak 集成示例

## 一、Keycloak 是什么

Keycloak 是一个开源的身份和访问管理（IAM）解决方案，专为现代应用程序和服务设计。它由 Red Hat 开发和维护，提供了完整的身份管理功能，使开发者能够轻松实现安全的用户认证和授权。

### 1.1 Keycloak 的核心功能

- **单点登录（SSO）**：用户只需登录一次即可访问所有集成的应用，无需在每个应用中重复登录
- **身份代理和社交登录**：支持与 Google、Facebook、Twitter 等第三方身份提供商集成，用户可以使用现有社交账号登录
- **用户联盟**：可以连接到现有的用户存储，如 LDAP、Active Directory 等
- **客户端适配器**：提供与各种应用框架的集成，包括 Spring Boot、Node.js、Angular 等
- **管理控制台**：直观的管理界面，用于配置和管理用户、角色、客户端等
- **账户管理**：用户可以管理自己的个人资料、密码和授权
- **细粒度授权**：基于角色的访问控制（RBAC）和基于资源的权限管理
- **多因素认证**：支持短信、电子邮件、TOTP 等多因素认证方式
- **会话管理**：管理员可以查看和管理用户会话
- **事件记录和审计**：记录用户登录、登出等事件，便于审计和监控

### 1.2 Keycloak 的应用场景

Keycloak 适用于以下场景：

1. **企业应用集成**：在企业内部，多个应用系统需要统一的身份认证和授权管理
2. **SaaS 应用**：为 SaaS 应用提供多租户的身份管理解决方案
3. **微服务架构**：在微服务架构中，为各个服务提供统一的身份验证和授权
4. **移动应用**：为移动应用提供安全的身份认证机制
5. **API 安全**：保护 API 接口，确保只有授权用户才能访问
6. **B2B 集成**：实现企业间的安全身份验证和授权
7. **客户门户**：为客户提供安全的自助服务门户
8. **合规要求**：满足 GDPR、HIPAA 等合规要求的身份管理解决方案

### 1.3 Keycloak 的优势

- **开源免费**：基于 Apache 2.0 许可证，完全免费使用
- **功能完整**：提供了企业级身份管理所需的所有核心功能
- **易于集成**：提供了丰富的客户端适配器和 API
- **可扩展性**：支持集群部署，可处理高并发场景
- **安全性**：内置多种安全特性，如多因素认证、密码策略等
- **标准化**：实现了 OAuth 2.0、OpenID Connect 等标准协议

本文使用 Keycloak 的管理控制台，通过 Spring Security OAuth2.0 与 Spring Boot 应用集成。

## 二、设置 Keycloak 服务器

### 2.1、下载和安装 Keycloak

1. 从Keycloak 官方网站：[https://www.keycloak.org/downloads](https://www.keycloak.org/downloads) 下载最新版本的 Keycloak
2. 解压下载的文件
3. 进入解压目录，运行以下命令启动 Keycloak：
   ```bash
   # Linux/Mac
   bin/kc.sh start-dev
   
   # Windows
   bin\kc.bat start-dev
   
   # Windows 指定8880端口启动
   bin\kc.bat start-dev --http-port=8880
   ```
   
4. Keycloak 服务器默认将在 http://localhost:8080 上运行

### 2.2、创建 Realm

1. 打开浏览器，访问 http://localhost:8080
2. 首次访问会提示创建管理员账户，按照提示设置用户名和密码
3. 登录管理控制台后，点击左侧菜单中的 "Realm" 下拉菜单
4. 点击 "Create Realm" 按钮
5. 输入 Realm 名称为 `my-realm`，点击 "Create" 按钮

### 2.3、创建客户端

1. 在左侧菜单中点击 "Clients"，然后点击 "Create client" 按钮
2. 输入以下信息：
   - Client type: OpenID Connect
   - Client ID: my-java-app
   - Name: My Java Application
3. 点击 "Next" 按钮
4. 在 "Access settings" 页面，设置：
   - Root URL: http://localhost:8081
   - Valid redirect URIs: http://localhost:8081/login/oauth2/code/keycloak
   - Web origins: http://localhost:8081
5. 点击 "Save" 按钮
6. 在客户端详情页面，点击 "Credentials" 选项卡，复制生成的 "Client secret"

### 2.4、创建角色和用户

1. 在左侧菜单中点击 "Roles"，然后点击 "Create role" 按钮
2. 输入角色名称，例如 `user`，点击 "Save" 按钮
3. 在左侧菜单中点击 "Users"，然后点击 "Add user" 按钮
4. 输入用户名，例如 `test`，点击 "Create" 按钮
5. 在用户详情页面，点击 "Credentials" 选项卡，设置密码并禁用 "Temporary" 选项
6. 点击 "Set password" 按钮确认
7. 点击 "Role mapping" 选项卡，将之前创建的角色分配给该用户

## 三、Spring Boot 应用配置

### 3.1、项目依赖

在 `pom.xml` 文件中添加以下依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-client</artifactId>
    </dependency>
</dependencies>
```

### 3.2、应用配置

在 `application.yml` 文件中添加以下配置：

```yaml
server:
  port: 8081  # Java 应用端口，和 Keycloak 客户端配置的 Redirect URIs 对应

spring:
  application:
    name: keycloak-demo
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: my-java-app
            client-secret: {your-client-secret}  # 从 Keycloak 客户端详情页面复制
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/keycloak"
        provider:
          keycloak:
            issuer-uri: http://localhost:8080/realms/my-realm
            user-name-attribute: preferred_username
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/my-realm
```

### 3.3、安全配置

创建 `SecurityConfig.java` 文件，配置 Spring Security：

```java
package top.lrshuai.keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorize -> authorize
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/home", true)
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .issuerUri("http://localhost:8080/realms/my-realm")
                )
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/public/logout-success")
            );
        return http.build();
    }
}
```

### 3.4、控制器实现

创建 `HomeController.java` 文件，实现基本的端点：

```java
package top.lrshuai.keycloak.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello from public endpoint!";
    }

    @GetMapping("/home")
    public Map<String, Object> home(@AuthenticationPrincipal OidcUser user) {
        return Map.of(
            "message", "Hello from secured endpoint!",
            "userName", user.getPreferredUsername(),
            "name", user.getFullName(),
            "email", user.getEmail(),
            "claims", user.getClaims()
        );
    }

    @GetMapping("/public/logout-success")
    public String logoutSuccess() {
        return "Logout successful!";
    }
}
```

## 四、Keycloak 与 OAuth2.0 的关系

### 4.1、OAuth2.0 简介

OAuth 2.0 是一个授权框架，允许应用程序在用户授权的情况下访问用户的资源。它定义了以下角色：

- **资源所有者**：用户，拥有受保护的资源
- **客户端**：需要访问资源的应用程序
- **资源服务器**：存储受保护资源的服务器
- **授权服务器**：验证用户身份并颁发访问令牌

### 4.2、Keycloak 作为 OAuth2.0 授权服务器

Keycloak 实现了 OAuth 2.0 规范，可以作为授权服务器使用。它提供了以下 OAuth 2.0 流程：

- **授权码流程**：最常用的流程，适用于有服务器端的应用
- **隐式流程**：适用于纯前端应用
- **客户端凭证流程**：适用于服务器间通信
- **密码流程**：适用于受信任的应用

在本示例中，我们使用的是授权码流程。

## 五、OpenID Connect (OIDC) 协议详解

### 5.1、OIDC 简介

OpenID Connect (OIDC) 是建立在 OAuth 2.0 协议之上的身份认证层，它扩展了 OAuth 2.0，添加了身份认证功能。OIDC 允许客户端验证用户的身份，并获取用户的基本配置信息。

OIDC 定义了以下核心概念：

- **ID Token**：包含用户身份信息的 JWT (JSON Web Token)，由授权服务器签名
- **UserInfo Endpoint**：客户端可以通过此端点获取用户的详细信息
- **Discovery Endpoint**：提供 OIDC 提供商的配置信息
- **Client Registration**：客户端注册机制

### 5.2、OIDC 与 OAuth 2.0 的关系

OIDC 是 OAuth 2.0 的超集，它在 OAuth 2.0 的基础上添加了身份认证功能。OAuth 2.0 主要解决授权问题，而 OIDC 主要解决身份认证问题。

- **OAuth 2.0**：关注"用户是否允许客户端访问其资源"
- **OIDC**：关注"用户是谁"以及"用户是否已认证"

### 5.3、OIDC 的工作流程

OIDC 的工作流程基于 OAuth 2.0 的授权码流程，主要步骤如下：

1. **客户端重定向**：客户端将用户重定向到 OIDC 提供商的授权端点
2. **用户认证**：用户在 OIDC 提供商处进行认证
3. **授权同意**：用户同意客户端请求的权限
4. **颁发授权码**：OIDC 提供商向客户端颁发授权码
5. **交换令牌**：客户端使用授权码向 OIDC 提供商请求令牌（包括 ID Token 和 Access Token）
6. **验证 ID Token**：客户端验证 ID Token 的签名和内容
7. **获取用户信息**：客户端可以使用 Access Token 从 UserInfo 端点获取用户详细信息

### 5.4、OIDC 的应用场景

OIDC 适用于以下场景：

1. **单点登录（SSO）**：用户只需登录一次，即可访问多个应用
2. **跨域身份认证**：在不同域名的应用之间实现统一的身份认证
3. **移动应用和原生应用**：为移动应用和原生应用提供安全的身份认证机制
4. **API 网关认证**：在 API 网关中使用 OIDC 进行身份验证和授权
5. **微服务架构**：在微服务架构中，使用 OIDC 实现服务间的安全通信
6. **联合身份**：实现不同组织之间的身份联合

### 5.5、OIDC 的优势

- **标准化**：基于开放标准，由 IETF 标准化
- **简单易用**：使用 JSON 和 RESTful API，易于实现和集成
- **安全可靠**：使用 JWT 进行身份令牌的传输和验证
- **灵活可扩展**：支持多种认证方式和扩展声明
- **广泛支持**：被众多身份提供商和客户端框架支持

### 5.6、Keycloak 与 OIDC

Keycloak 是一个功能完整的 OIDC 提供商，它实现了 OIDC 1.0 规范，提供了以下 OIDC 功能：

- **ID Token 颁发**：生成和签名 ID Token
- **UserInfo 端点**：提供用户信息
- **Discovery 端点**：提供 OIDC 配置信息
- **客户端注册**：支持动态客户端注册
- **多因素认证**：支持基于 OIDC 的多因素认证
- **会话管理**：基于 OIDC 的会话管理

在本示例中，我们使用 Keycloak 作为 OIDC 提供商，通过 Spring Security OAuth2 客户端集成，实现了基于 OIDC 的身份认证。

## 六、Spring Boot Security 与 OAuth2.0 集成

### 6.1、Spring Security OAuth2 支持

Spring Security 提供了对 OAuth2.0 的全面支持，包括：

- **OAuth2 客户端**：用于实现授权码流程，处理用户登录
- **OAuth2 资源服务器**：用于验证访问令牌，保护 API 端点

### 6.2、集成流程

1. **客户端注册**：在 Keycloak 中注册客户端，获取 client-id 和 client-secret
2. **配置 OAuth2 客户端**：在 Spring Boot 应用中配置 OAuth2 客户端信息
3. **配置 OAuth2 资源服务器**：在 Spring Boot 应用中配置 JWT 验证
4. **配置安全规则**：使用 Spring Security 配置访问控制规则
5. **实现控制器**：创建需要保护的端点和公开端点

## 七、测试步骤

1. **启动 Keycloak 服务器**：确保 Keycloak 在 http://localhost:8080 上运行
2. **启动 Spring Boot 应用**：运行 `mvn spring-boot:run` 命令
3. **访问公开端点**：打开浏览器，访问 http://localhost:8081/public/hello，应该直接返回 "Hello from public endpoint!"
4. **访问受保护端点**：访问 http://localhost:8081/home，会跳转到 Keycloak 登录页面
5. **登录**：使用之前创建的用户凭据登录
6. **验证**：登录成功后，会跳转到 /home 端点，显示用户信息
7. **退出登录**：访问 http://localhost:8081/logout，然后访问 /public/logout-success 查看退出成功信息

## 八、常见问题及解决方案

### 8.1、客户端配置错误

**问题**：登录时出现 "Invalid redirect URI" 错误
**解决方案**：确保 Keycloak 客户端配置中的 "Valid redirect URIs" 包含 `http://localhost:8081/login/oauth2/code/keycloak`

### 8.2、客户端密钥错误

**问题**：登录时出现 "Bad credentials" 错误
**解决方案**：确保 `application.yml` 文件中的 `client-secret` 与 Keycloak 客户端详情页面中的 "Client secret" 一致

### 8.3、令牌验证失败

**问题**：访问受保护端点时出现 "Invalid token" 错误
**解决方案**：确保 `application.yml` 文件中的 `issuer-uri` 正确，格式为 `http://localhost:8080/realms/my-realm`

## 九、API接口文档

### 3. 用户身份验证

#### 3.1 授权码流程

授权码流程是最常用的 OAuth 2.0/OIDC 流程，适用于有服务器端的应用。流程如下：

1. 客户端构造认证请求，将用户重定向到 Keycloak 的授权端点
2. 用户在 Keycloak 登录页面输入凭据进行认证
3. 认证成功后，Keycloak 生成授权码并将用户重定向回客户端
4. 客户端使用授权码向 Keycloak 的令牌端点请求令牌
5. Keycloak 验证授权码并颁发访问令牌、刷新令牌和 ID 令牌

#### 3.2 接口定义

##### 3.2.1 用户身份认证

###### 3.2.1.1 构造并发送认证请求

**接口路径**：`/auth/login`

**方法**：GET

**功能**：构造并发送认证请求，将用户重定向到 Keycloak 登录页面

**请求参数**：无

**响应**：302 重定向到 Keycloak 授权端点，包含以下参数：
- `client_id`：客户端 ID
- `redirect_uri`：重定向 URI
- `response_type`：响应类型，固定为 `code`
- `scope`：请求的权限范围，如 `openid profile email`
- `state`：随机生成的状态参数，用于防止 CSRF 攻击

###### 3.2.1.2 认证响应

认证成功后，Keycloak 将用户重定向回客户端的重定向 URI，包含以下参数：

- `code`：授权码
- `state`：之前发送的状态参数

**示例重定向 URL**：
```
http://localhost:8081/login/oauth2/code/keycloak?code=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...&state=abc123
```

##### 3.2.2 令牌获取

###### 3.2.2.1 基本信息

**接口路径**：`/auth/token`

**方法**：POST

**功能**：使用授权码获取访问令牌、刷新令牌和 ID 令牌

###### 3.2.2.2 请求参数

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| `code` | string | 是 | 授权码 |
| `redirect_uri` | string | 是 | 重定向 URI，必须与认证请求中的一致 |

###### 3.2.2.3 响应参数

**成功响应**：

| 参数名 | 类型 | 描述 |
|--------|------|------|
| `access_token` | string | 访问令牌 |
| `refresh_token` | string | 刷新令牌 |
| `id_token` | string | ID 令牌 |
| `token_type` | string | 令牌类型，固定为 `Bearer` |
| `expires_in` | integer | 访问令牌过期时间（秒） |

**示例响应**：
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "id_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

###### 3.2.2.4 ID Token

ID Token 是一个 JWT（JSON Web Token），包含用户的身份信息，由 Keycloak 签名。主要包含以下声明：

| 声明 | 描述 |
|------|------|
| `sub` | 用户的唯一标识符 |
| `name` | 用户的全名 |
| `preferred_username` | 用户的首选用户名 |
| `email` | 用户的电子邮件 |
| `email_verified` | 电子邮件是否已验证 |
| `given_name` | 用户的名字 |
| `family_name` | 用户的姓氏 |
| `iss` | 令牌的签发者（Keycloak 服务器 URL） |
| `aud` | 令牌的受众（客户端 ID） |
| `exp` | 令牌的过期时间 |
| `iat` | 令牌的签发时间 |
| `nonce` | 随机值，用于防止重放攻击 |

##### 3.2.3 用户信息获取

###### 3.2.3.1 基本信息

**接口路径**：`/auth/userinfo`

**方法**：GET

**功能**：使用访问令牌获取用户的详细信息

**请求头**：
- `Authorization`: `Bearer <access_token>`

###### 3.2.3.2 请求参数

无

###### 3.2.3.3 响应参数

**成功响应**：

| 参数名 | 类型 | 描述 |
|--------|------|------|
| `sub` | string | 用户的唯一标识符 |
| `name` | string | 用户的全名 |
| `preferred_username` | string | 用户的首选用户名 |
| `email` | string | 用户的电子邮件 |
| `email_verified` | boolean | 电子邮件是否已验证 |
| `given_name` | string | 用户的名字 |
| `family_name` | string | 用户的姓氏 |

**示例响应**：
```json
{
  "sub": "f8b0b61a-4f9a-4b3c-9c8d-1e2f3a4b5c6d",
  "name": "Test User",
  "preferred_username": "testuser",
  "email": "test@example.com",
  "email_verified": true,
  "given_name": "Test",
  "family_name": "User"
}
```

### 4. 用户退出

#### 4.1 后通道退出流程

后通道退出是 OIDC 中的一种退出机制，允许 Keycloak 通知所有已登录的客户端用户已退出。流程如下：

1. 用户在一个客户端发起退出请求
2. 客户端向 Keycloak 发送退出请求
3. Keycloak 处理退出请求，使用户的会话失效
4. Keycloak 向后通道退出 URL 发送注销请求
5. 客户端处理注销请求，清理用户会话

#### 4.2 接口定义

##### 4.2.1 后通道客户端注销

###### 4.2.1.1 基本信息

**接口路径**：`/auth/backchannel-logout`

**方法**：POST

**功能**：处理 Keycloak 发送的后通道注销请求

###### 4.2.1.2 请求参数

**请求体**：

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| `logout_token` | string | 是 | 包含注销信息的 JWT |

**示例请求体**：
```json
{
  "logout_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

###### 4.2.1.3 响应参数

**成功响应**：204 No Content

**失败响应**：400 Bad Request 或 401 Unauthorized

###### 4.2.1.4 Logout Token

Logout Token 是一个 JWT，包含注销信息，由 Keycloak 签名。主要包含以下声明：

| 声明 | 描述 |
|------|------|
| `sub` | 用户的唯一标识符 |
| `iss` | 令牌的签发者（Keycloak 服务器 URL） |
| `aud` | 令牌的受众（客户端 ID） |
| `exp` | 令牌的过期时间 |
| `iat` | 令牌的签发时间 |
| `jti` | 令牌的唯一标识符 |
| `events` | 包含注销事件信息 |

## 十、多租户多客户端实现

### 10.1、多租户概念

多租户是指一个应用系统可以同时为多个独立的组织（租户）提供服务，每个租户的数据和配置相互隔离。在 Keycloak 中，多租户通常通过以下方式实现：

- **使用多个 Realm**：每个租户使用一个独立的 Realm，完全隔离租户数据
- **使用单一 Realm + 客户端隔离**：在一个 Realm 中创建多个客户端，通过客户端权限控制实现隔离
- **使用单一 Realm + 组织/群组**：在一个 Realm 中使用组织或群组来管理不同租户的用户

### 10.2、多客户端配置

在 Keycloak 中，每个应用都应该创建一个独立的客户端。以下是多客户端配置的步骤：

1. **创建多个客户端**：
   - 在 Keycloak 管理控制台中，为每个应用创建一个独立的客户端
   - 为每个客户端设置不同的 Client ID 和配置

2. **客户端配置示例**：
   - 客户端 1：`app-client-1`，用于应用 1
   - 客户端 2：`app-client-2`，用于应用 2

3. **设置客户端权限**：
   - 为每个客户端设置独立的角色和权限
   - 配置客户端之间的访问控制

### 10.3、Spring Boot 应用中的多租户多客户端实现

#### 10.3.1、配置多个 OAuth2 客户端

在 `application.yml` 文件中配置多个 OAuth2 客户端：

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak-app1:
            client-id: app-client-1
            client-secret: {client-secret-1}
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/keycloak-app1"
            scope: openid,profile,email
          keycloak-app2:
            client-id: app-client-2
            client-secret: {client-secret-2}
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/keycloak-app2"
            scope: openid,profile,email
        provider:
          keycloak-app1:
            issuer-uri: http://localhost:8080/realms/my-realm
            user-name-attribute: preferred_username
          keycloak-app2:
            issuer-uri: http://localhost:8080/realms/my-realm
            user-name-attribute: preferred_username
```

#### 10.3.2、多租户安全配置

创建 `SecurityConfig.java` 文件，配置多个客户端的安全规则：

```java
package top.lrshuai.keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorize -> authorize
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/app1/**").authenticated()
                .requestMatchers("/app2/**").authenticated()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .issuerUri("http://localhost:8080/realms/my-realm")
                )
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/public/logout-success")
            );
        return http.build();
    }
}
```

#### 10.3.3、多租户控制器实现

创建 `MultiTenantController.java` 文件，实现多租户支持：

```java
package top.lrshuai.keycloak.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MultiTenantController {

    @GetMapping("/app1/home")
    public Map<String, Object> app1Home(@AuthenticationPrincipal OidcUser user) {
        return Map.of(
            "message", "Hello from App 1 secured endpoint!",
            "userName", user.getPreferredUsername(),
            "name", user.getFullName(),
            "email", user.getEmail(),
            "claims", user.getClaims()
        );
    }

    @GetMapping("/app2/home")
    public Map<String, Object> app2Home(@AuthenticationPrincipal OidcUser user) {
        return Map.of(
            "message", "Hello from App 2 secured endpoint!",
            "userName", user.getPreferredUsername(),
            "name", user.getFullName(),
            "email", user.getEmail(),
            "claims", user.getClaims()
        );
    }
}
```

### 10.4、多 Realm 实现（高级多租户）

对于需要完全隔离的多租户场景，可以使用多个 Realm：

1. **创建多个 Realm**：
   - 在 Keycloak 管理控制台中创建多个 Realm，每个租户一个
   - 例如：`tenant1-realm`、`tenant2-realm`

2. **配置多个 Realm 的客户端**：
   - 在每个 Realm 中创建相应的客户端
   - 为每个 Realm 配置独立的用户和角色

3. **Spring Boot 应用配置**：

   #### 10.4.1、application.yml 配置
   
   ```yaml
   spring:
     security:
       oauth2:
         client:
           registration:
             keycloak-tenant1:
               client-id: tenant1-client
               client-secret: {tenant1-client-secret}
               authorization-grant-type: authorization_code
               redirect-uri: "{baseUrl}/login/oauth2/code/keycloak-tenant1"
               scope: openid,profile,email
             keycloak-tenant2:
               client-id: tenant2-client
               client-secret: {tenant2-client-secret}
               authorization-grant-type: authorization_code
               redirect-uri: "{baseUrl}/login/oauth2/code/keycloak-tenant2"
               scope: openid,profile,email
           provider:
             keycloak-tenant1:
               issuer-uri: http://localhost:8080/realms/tenant1-realm
               user-name-attribute: preferred_username
             keycloak-tenant2:
               issuer-uri: http://localhost:8080/realms/tenant2-realm
               user-name-attribute: preferred_username
         resourceserver:
           jwt:
             issuer-uri: http://localhost:8080/realms/tenant1-realm  # 默认 Realm
   ```

   #### 10.4.2、SecurityConfig 配置
   
   ```java
   package top.lrshuai.keycloak.config;

   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
   import org.springframework.security.config.annotation.web.builders.HttpSecurity;
   import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
   import org.springframework.security.web.SecurityFilterChain;

   @Configuration
   @EnableWebSecurity
   @EnableMethodSecurity
   public class SecurityConfig {

       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http
               .authorizeRequests(authorize -> authorize
                   .requestMatchers("/public/**").permitAll()
                   .requestMatchers("/tenant1/**").authenticated()
                   .requestMatchers("/tenant2/**").authenticated()
                   .anyRequest().authenticated()
               )
               .oauth2Login(oauth2 -> oauth2
                   .loginPage("/login")
                   .defaultSuccessUrl("/home", true)
               )
               .oauth2ResourceServer(oauth2 -> oauth2
                   .jwt(jwt -> jwt
                       .issuerUri("http://localhost:8080/realms/tenant1-realm")
                   )
               )
               .logout(logout -> logout
                   .logoutSuccessUrl("/public/logout-success")
               );
           return http.build();
       }
   }
   ```

   #### 10.4.3、基于请求的 Realm 选择机制
   
   对于多 Realm 场景，通常需要实现基于请求的 Realm 选择机制，例如通过子域名、路径参数或请求头来判断使用哪个 Realm：
   
   ```java
   package top.lrshuai.keycloak.config;

   import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
   import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
   import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
   import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
   import org.springframework.web.context.request.RequestContextHolder;
   import org.springframework.web.context.request.ServletRequestAttributes;

   import javax.servlet.http.HttpServletRequest;
   import java.util.HashMap;
   import java.util.Map;

   public class TenantOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

       private final Map<String, OAuth2AuthorizationRequestResolver> resolvers = new HashMap<>();

       public TenantOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
           // 为每个租户创建一个解析器
           resolvers.put("tenant1", new DefaultOAuth2AuthorizationRequestResolver(
               clientRegistrationRepository, "/login/oauth2/code/keycloak-tenant1"));
           resolvers.put("tenant2", new DefaultOAuth2AuthorizationRequestResolver(
               clientRegistrationRepository, "/login/oauth2/code/keycloak-tenant2"));
       }

       @Override
       public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
           String tenantId = resolveTenantId(request);
           OAuth2AuthorizationRequestResolver resolver = resolvers.get(tenantId);
           return resolver != null ? resolver.resolve(request) : null;
       }

       @Override
       public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
           String tenantId = resolveTenantId(request);
           OAuth2AuthorizationRequestResolver resolver = resolvers.get(tenantId);
           return resolver != null ? resolver.resolve(request, clientRegistrationId) : null;
       }

       private String resolveTenantId(HttpServletRequest request) {
           // 从请求中解析租户 ID，例如从子域名、路径或请求头
           // 这里简单示例，从路径中解析
           String path = request.getRequestURI();
           if (path.startsWith("/tenant1/")) {
               return "tenant1";
           } else if (path.startsWith("/tenant2/")) {
               return "tenant2";
           }
           return "tenant1"; // 默认租户
       }
   }
   ```

   #### 10.4.4、注册自定义的 OAuth2AuthorizationRequestResolver
   
   ```java
   package top.lrshuai.keycloak.config;

   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
   import org.springframework.security.config.annotation.web.builders.HttpSecurity;
   import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
   import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
   import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
   import org.springframework.security.web.SecurityFilterChain;

   @Configuration
   @EnableWebSecurity
   @EnableMethodSecurity
   public class SecurityConfig {

       @Bean
       public OAuth2AuthorizationRequestResolver authorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
           return new TenantOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
       }

       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2AuthorizationRequestResolver authorizationRequestResolver) throws Exception {
           http
               .authorizeRequests(authorize -> authorize
                   .requestMatchers("/public/**").permitAll()
                   .requestMatchers("/tenant1/**").authenticated()
                   .requestMatchers("/tenant2/**").authenticated()
                   .anyRequest().authenticated()
               )
               .oauth2Login(oauth2 -> oauth2
                   .loginPage("/login")
                   .authorizationEndpoint(authorization -> authorization
                       .authorizationRequestResolver(authorizationRequestResolver)
                   )
                   .defaultSuccessUrl("/home", true)
               )
               .oauth2ResourceServer(oauth2 -> oauth2
                   .jwt(jwt -> jwt
                       .issuerUri("http://localhost:8080/realms/tenant1-realm")
                   )
               )
               .logout(logout -> logout
                   .logoutSuccessUrl("/public/logout-success")
               );
           return http.build();
       }
   }
   ```

### 10.5、多租户多客户端的优势

- **隔离性**：不同租户的数据和配置相互隔离，提高安全性
- **灵活性**：每个租户可以有独立的认证和授权策略
- **可扩展性**：可以轻松添加新租户，无需修改应用代码
- **管理便捷**：通过 Keycloak 管理控制台集中管理所有租户

### 10.6、最佳实践

1. **Realm 设计**：
   - 对于小型应用，使用单一 Realm + 客户端隔离
   - 对于大型企业应用，使用多个 Realm 实现完全隔离

2. **客户端配置**：
   - 为每个应用创建独立的客户端
   - 使用不同的 Client ID 区分不同的应用

3. **权限管理**：
   - 为每个客户端设置独立的角色和权限
   - 使用 Keycloak 的细粒度授权功能

4. **安全配置**：
   - 为每个客户端使用强密码和密钥
   - 配置适当的重定向 URI
   - 启用 HTTPS 保护

## 十一、总结

通过本示例，我们实现了：

1. Keycloak 服务器的搭建和配置
2. Spring Boot 应用与 Keycloak 的集成
3. 使用 Spring Security OAuth2 实现身份验证和授权
4. 理解了 Keycloak 与 OAuth2.0 的关系
5. 了解了 OpenID Connect (OIDC) 协议的工作原理和应用场景
6. 实现了用户身份验证、令牌获取、用户信息获取和退出等接口
7. 实现了多租户多客户端支持

这种集成方式提供了一种安全、标准的身份管理解决方案，适用于各种企业应用场景，特别是需要多租户支持的 SaaS 应用。