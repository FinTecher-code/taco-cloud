# 第七周 - 刷题记录

---

## 2026-08-25

---

### Q1 — 修复 SQL 注入漏洞的方法

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 开发人员为修复SQL注入漏洞，建议采取哪个方法？

**选项:**
1. 在前端代码中对输入参数进行过滤
2. 删除所有涉及数据库操作的代码
3. 使用预编译语句，绑定变量 ✅
4. 使用存储过程时使用动态的SQL语句

**我的答案:** 选项3 ✅

**正确答案:** 选项3

**解析:**
- 为修复SQL注入漏洞，开发人员建议采取的方法是：使用预编译语句，绑定变量
- 解释如下：
- - 选项：在前端代码中对输入参数进行过滤 - 虽然前端过滤可以减少一些简单的攻击，但它不是一种安全的做法，因为攻击者可能会绕过前端验证直接向服务器发送请求。
- - 选项：删除所有涉及数据库操作的代码 - 这显然不是一个合理的解决方案，因为数据库操作是大多数应用程序的核心功能。
- - 选项：使用预编译语句，绑定变量 - 这是防止SQL注入的最佳实践之一。预编译语句（也称为参数化查询）可以确保输入的参数不会被解释为SQL代码的一部分，从而有效地防止SQL注入攻击。
- - 选项：使用存储过程时使用动态的SQL语句 - 使用动态SQL语句会增加SQL注入的风险，即使是存储过程也不例外。因此，这不是推荐的修复方法。
- 因此，使用预编译语句并绑定变量是修复SQL注入漏洞的正确方法。

---
### Q2 — 搜索框注入恶意代码导致重定向

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 在一个网站的搜索框中，攻击者成功注入恶意代码，使得用户在搜索时被重定向到一个恶意网站，这是以下哪种漏洞？

**选项:**
1. SQL注入
2. XSS ✅
3. 命令执行
4. CSRF

**我的答案:** 选项4 ❌

**正确答案:** 选项2

**解析:**
- 用户在搜索时被重定向到了一个恶意网站，说明攻击者成功将恶意代码注入到了搜索框中。此处存在的是跨站脚本攻击（XSS）漏洞，攻击者通过在搜索框中注入恶意脚本，使得用户在搜索时被重定向到了攻击者指定的网站。SQL注入漏洞、CSRF漏洞和命令注入漏洞均不符合此情况。

---
### Q3 — 路径遍历（文件写入校验逻辑反转）

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 以下编码方式存在什么漏洞？
```
String fileName = request.getParameter("fileName");
File file = new File("/img/" + fileName);
fileName = file.getAbsolutePath();
if (!fileName.startsWith("/img/")) {    // exception, log, return}
FileOutputStream fis = new FileOutputStream(file);
```

**选项:**
1. JSON注入
2. 硬编码用户名
3. 路径遍历 ✅
4. XSS

**我的答案:** 选项3 ✅

**正确答案:** 选项3

**解析:**

---
### Q4 — 存在 SQL 注入的原因（参数化形同虚设）

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 以下代码存在SQL注入的原因是什么？
```
String pwd = request.getParameter("password");
String SQLString = "SELECT * FROM db_user WHERE username = '" + username + "' AND password = '" + pwd + "'";
PreparedStatement pstmt = connection.prepareStatement(SQLString);
ResultSet results = pstmt.executeQuery();
```

**选项:**
1. 未使用占位符构造SQL语句 ✅
2. 使用了prepareStatement，不存在SQL注入
3. 未使用Mybaties框架
4. 未使用Hibernate框架

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**

---
### Q5 — JPA 防范 SQL 注入的方式

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** JPA使用哪些方式可防范SQL注入

**选项:**
1. 位置参数
2. 命名参数
3. 命名查询
4. 以上都对 ✅

**我的答案:** 选项4 ✅

**正确答案:** 选项4

**解析:**

---
### Q6 — RESTful API 中 PUT 请求对应的操作

**来源:** 每日一练 App

**题目:** 在 Spring RESTful API 中，PUT 请求对应什么操作？

**选项:**
1. 创建新资源
2. 完全更新资源（客户端提供整个资源）✅
3. 部分更新资源（客户端提供变更的属性）❌
4. 获取资源

**我的答案:** 选项3 ❌
**正确答案:** 选项2 ✅

**解析:**
- 选项3 是 **PATCH** 的定义，不是 PUT —— 用户把两者搞混了：
  - **PUT**：**全量替换/更新**，客户端提交**完整的资源**，服务端整体覆盖；**幂等**（同一请求发多次结果一致）✅
  - **PATCH**：**部分更新**，客户端只提交**变更的属性**，服务端只改这些字段
- RESTful 方法全家桶：
  - **POST** → 创建新资源（非幂等）
  - **GET** → 获取/查询资源（只读）
  - **PUT** → 完全更新（幂等，整体替换）
  - **PATCH** → 部分更新（只改提交的字段）
  - **DELETE** → 删除资源
- 记忆点：**PUT = 整身换装，PATCH = 局部整容**；看到“提供整个资源”选 PUT，看到“提供变更属性”选 PATCH

---

### Q7 — Spring Security 支持的请求拦截方式

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Spring Security中对请求进行拦截，支持以下哪种拦截方式？

**选项:**
1. AntMatchers
2. RegexMatchers
3. MvcMatchers
4. 其他选项都支持 ✅

**我的答案:** 选项4 ✅

**正确答案:** 选项4

**解析:**
- Spring Security支持AntMatchers、RegexMatchers、MvcMatchers的请求拦截方式。

---
### Q8 — 创建根 ApplicationContext 的官方推荐类

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Spring Web 应用中，为了声明式地创建根 ApplicationContext（即加载 Spring 配置文件），通常会使用 ContextLoaderListener。当前官方推荐的实现类是以下哪一个？

**选项:**
1. ContextLoaderListener ✅
2. ContextWebListener
3. ContextLoaderServlet
4. ContextWebServlet

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- ContextLoaderListener 实现了 ServletContextListener，是 当前唯一推荐 的方式。
- ContextLoaderServlet 是早期版本的实现，从 Spring 3.0 开始已被移除，不应用于新项目。

---
### Q9 — Servlet 转向且地址栏不变的实现方式

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 基于Servlet API如何实现转向时不在地址栏中显示转向后的地址？

**选项:**
1. `redirect ( )`
2. `sendRedirect ( )`
3. `forward ( )` ✅
4. `transform ( )`

**我的答案:** 选项1 ❌

**正确答案:** 选项3

**解析:**
- `forward ( )`

---
### Q10 — Spring Bean 生命周期核心类说法

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 关于Spring Bean的生命周期中应用的核心类说法正确的是

**选项:**
1. InstantiationAwareBeanPostProcessor继承了BeanPostProcessor接口 ✅
2. InstantiationAwareBeanPostProcessor作用于初始化阶段的前后
3. BeanPostProcessor作用于实例化阶段的前后
4. 其他都不对

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- InstantiationAwareBeanPostProcessor继承了BeanPostProcessor接口
- InstantiationAwareBeanPostProcessor作用于实例化阶段的前后
- BeanPostProcessor作用于初始化阶段的前后

---
### Q11 — 不是 HikariCP 成为 Spring Boot 2 默认连接池的原因

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下面哪项不是Spring Boot 2默认数据库连接池选择HikariCP的原因？

**选项:**
1. HikariCP的性能高
2. HikariCP代码量低
3. HikariCP稳定性高
4. HikariCP监控全面 ✅

**我的答案:** 选项4 ✅

**正确答案:** 选项4

**解析:**
- HikariCP注重于性能，监控上稍差

---
### Q12 — Spring Web 上下文中 Bean 的作用域

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Spring中Web上下文中beans的作用域有哪些？

**选项:**
1. 单例，原型，请求，会话，globalSession ✅
2. 代理、适配器、工厂
3. bean、组件、服务
4. 控制器、服务、存储库

**我的答案:** 选项2 ❌

**正确答案:** 选项1

**解析:**
- Spring Web 上下文中 Bean 的核心作用域是：单例、原型、请求、会话、globalSession。

---
### Q13 — @Configuration + @Bean 代码的说法

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 关于下面这段代码说法正确的是？
```
@Configuration
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }
}
```

**选项:**
1. 不编译，bean需要一个名字
2. 不编译，需要扫描
3. 表示一个配置类并创建一个bean ✅
4. 应该命名为`Config`而不是`AppConfig`

**我的答案:** 选项3 ✅

**正确答案:** 选项3

**解析:**
- - `@Configuration` 表示这是一个 Spring 配置类。
- - `@Bean` 注解的方法 `myService()` 会在 Spring 容器中注册一个名为 `myService` 的 Bean，类型为 `MyServiceImpl`（或其接口 `MyService` 类型）。

---
### Q14 — 不是 Spring Boot 默认支持的数据源类型

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** SpringBoot默认支持的数据源类型不包括下列哪项？

**选项:**
1. org.apache.tomcat.jdbc.pool.DataSource
2. com.zaxxer.hikari.HikariDataSource
3. org.apache.commons.dbcp2.BasicDataSource
4. org.drools ✅

**我的答案:** 选项3 ❌

**正确答案:** 选项4

**解析:**
- Springboot默认支持4种数据源类型，定义在 org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration 中，分别是：
- org.apache.tomcat.jdbc.pool.DataSource
- com.zaxxer.hikari.HikariDataSource
- org.apache.commons.dbcp.BasicDataSource
- org.apache.commons.dbcp2.BasicDataSource

---
### Q15 — @RestController 标注类的特点

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 用`@RestController`标注的类特点是？

**选项:**
1. 他们只能接受获取和发布请求
2. 每个方法都是一个`@ResponseBody`，默认情况下会处理JSON或XML的序列化 ✅
3. 它们只能返回JSON响应，序列化是通过Jackson处理的
4. 一个项目中只有一个类可以被注释为`@RestController`

**我的答案:** 选项3 ❌

**正确答案:** 选项2

**解析:**
- @RestController 是 @Controller + @ResponseBody 的组合注解，类内所有请求处理方法默认带 @ResponseBody，支持 JSON/XML 序列化。

