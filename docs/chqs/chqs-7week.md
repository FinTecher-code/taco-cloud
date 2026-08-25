# 第七周 - 刷题记录

---

## 2026-08-25

---

### Q1 — 修复 SQL 注入漏洞的方法

**来源:** 每日一练 App
**分类:** 代码安全审计

**题目:** 开发人员为修复SQL注入漏洞，建议采取哪个方法？

**选项:**
1. 在前端代码中对输入参数进行过滤
2. 删除所有涉及数据库操作的代码
3. 使用预编译语句，绑定变量 ✅
4. 使用存储过程时使用动态的SQL语句

**我的答案:** 选项3 ✅
**正确答案:** 选项3 ✅

**解析:**
- 选项3 正确：使用**预编译语句（PreparedStatement）+ 绑定变量（参数化查询）**是修复 SQL 注入最标准、最有效的方法 —— SQL 结构（模板）与参数分离，用户输入只作为**数据**传递，永远不会被拼进 SQL 语法，攻击者构造的恶意片段无法改变查询结构
- 选项1 错误：前端过滤**不可靠**，攻击者可以直接构造 HTTP 请求绕过前端校验；过滤必须放在服务端才有意义，且规则容易遗漏，只能作为辅助手段，不能当唯一防线
- 选项2 错误：删除所有涉及数据库操作的代码 = 放弃业务功能，不现实也不可取，属于因噎废食
- 选项4 错误：存储过程里如果还是**拼接动态 SQL**（EXEC 拼字符串），注入风险依然存在；正确做法是存储过程**内部也使用参数化绑定**
- 记忆点：防注入首选 = 预编译 + 绑定变量；前端过滤是纸糊的盾（可绕过）；动态拼接 SQL 到哪里都是坑

---

### Q2 — 搜索框注入恶意代码导致重定向

**来源:** 每日一练 App
**分类:** 代码安全审计

**题目:** 在一个网站的搜索框中，攻击者成功注入恶意代码，使得用户在搜索时被重定向到一个恶意网站，这是以下哪种漏洞？

**选项:**
1. SQL注入
2. XSS ✅
3. 命令执行
4. CSRF

**我的答案:** 选项4 ❌
**正确答案:** 选项2 ✅

**解析:**
- 用户在搜索时被重定向到恶意网站，说明攻击者把**恶意脚本注入到了搜索框中** —— 这是典型的**跨站脚本攻击（XSS）**，搜索框正是反射型 XSS 的高发注入点：搜索词未经过滤/转义就被回显到页面，脚本随之在用户浏览器里执行
- XSS 的典型危害：重定向到钓鱼站、窃取 Cookie/会话、篡改页面内容、键盘记录
- 逐个排除：
  - **SQL注入**：注入目标是**服务端数据库查询**（改查询逻辑、拖库），发生在后端，不会直接导致浏览器重定向 ❌
  - **命令执行（RCE）**：在**服务端**执行系统命令，属于命令注入类漏洞，与浏览器端重定向无关 ❌
  - **CSRF（跨站请求伪造）**：是**冒用已登录用户的身份**去伪造请求（如改密码、转账），不是往页面注入脚本，也不会重定向用户 ❌ —— 用户选这个是典型误判：看到“恶意网站/重定向”想到了攻击者引导用户，但 CSRF 的本质是“借刀”，XSS 的本质是“种脚本”
- 记忆点：往页面/搜索框**注入脚本** → 优先 XSS（重定向、偷 Cookie、弹窗都是它的活儿）；CSRF = 冒用身份发请求；SQL 注入 = 打数据库；命令注入 = 打系统命令

---

### Q3 — 路径遍历（文件写入校验逻辑反转）

**来源:** 每日一练 App
**分类:** 代码安全审计

**题目:** 以下编码方式存在什么漏洞？

```java
String fileName = request.getParameter("fileName")
File file = new File("/img/" + fileName)
fileName = file.getAbsolutePath();
if (!fileName.startsWith("/img/")) {
 FileOutputStream fis = new FileOutputStream(file)
}
```

**选项:**
1. JSON注入
2. 硬编码用户名
3. 路径遍历 ✅
4. XSS

**我的答案:** 选项3 ✅
**正确答案:** 选项3 ✅

**解析:**
- 漏洞是**路径遍历（目录穿越）**，而且这份代码的校验逻辑整个是**反的**：
  1. `new File("/img/" + fileName)`：用户输入直接拼接进路径，攻击者可传 `../../` 逃出 `/img/` 目录
  2. `fileName.startsWith("/img/")` 判的是绝对路径前缀，看似在做目录校验
  3. 关键：`if (!fileName.startsWith("/img/"))` → **路径不以 /img/ 开头时才执行写入**。正常文件（`/img/foo.jpg`）反而不写入；带 `../` 越界的路径**恰好满足条件被写入** —— 典型的“拦好人、放坏人”
  4. 结果：攻击者可向任意目录写入/覆盖文件（如覆盖配置文件、写入计划任务），危害比单纯读取更严重
- 正确写法：用 `getCanonicalPath()` 规范化后判断是否仍以 `/img/` 开头，**校验不过直接拒绝**（不是反转条件）；再配合服务端白名单
- 排除其他选项：**JSON注入**针对 JSON 解析/序列化，本题无关；**硬编码用户名**是凭据管理问题；**XSS**是往页面注入脚本，均与文件路径无关
- 记忆点：看到“用户输入 + new File 拼路径 + 前缀校验” → 先想路径遍历；`!startsWith` 这种反转校验 = 漏洞放大器

---

### Q4 — 存在 SQL 注入的原因（参数化形同虚设）

**来源:** 每日一练 App
**分类:** 代码安全审计

**题目:** 以下代码存在SQL注入的原因是什么？

```java
String pwd = request.getParameter("password");
String SQLString = "SELET * FROM db_user WHERE username = ''" + username + "' AND password = '" + pwd + "'";
PreparedStatement pstmt = connection.prepareStatement(SQLString);
ResultSet results = pstmt.executeQuery();
```

**选项:**
1. 未使用占位符构造SQL语句 ✅
2. 使用了prepareStatement，不存在SQL注入
3. 未使用Mybaties框架
4. 未使用Hibernate框架

**我的答案:** 选项1 ✅
**正确答案:** 选项1 ✅

**解析:**
- 注入原因 = **字符串拼接用户输入构造 SQL**，且 **PreparedStatement 参数化没有真正生效**：
  1. `username`、`pwd` 直接拼进 SQL 字符串（`"'" + username + "' AND password = '" + pwd + "'"`）→ 用户输入成为 SQL 结构的一部分，可闭合引号改写查询逻辑（如 `' OR '1'='1' --`）
  2. 调用的是 `prepareStatement(SQLString)`，传入的是**拼接好的完整 SQL**，而不是 `?` 占位符 —— 没有 `setString()` 绑定变量，预编译机制完全没起作用，等价于普通 Statement
  3. **用没用 PreparedStatement 不是关键，关键是有没有用占位符 `?` + 绑定变量**
- 排除其他选项：
  - 选项2：用了 prepareStatement 就断定“不存在 SQL 注入”是错的——拼接后传入照样注入 ❌
  - 选项3/4：MyBatis/Hibernate 只是 ORM 框架，用不用框架不是注入原因；即便用了框架，如果还是 `${}` 拼 SQL（MyBatis）一样会注入 ❌
- 正确写法：`"SELECT * FROM db_user WHERE username = ? AND password = ?"` + `pstmt.setString(1, username)` + `setString(2, pwd)`
- 记忆点：**预编译防注入的前提是占位符 + 绑定变量**；看到“拼接字符串传给 prepareStatement” = 披着预编译外衣的普通拼 SQL

---

### Q5 — JPA 防范 SQL 注入的方式

**来源:** 每日一练 App
**分类:** 代码安全审计

**题目:** JPA使用哪些方式可防范SQL注入

**选项:**
1. 位置参数
2. 命名参数
3. 命名查询
4. 以上都对 ✅

**我的答案:** 选项4 ✅
**正确答案:** 选项4 ✅

**解析:**
- JPA/JPQL 防 SQL 注入的三种方式全都成立：
  - **位置参数**：JPQL 中用 `?1`、`?2` 占位，`setParameter(1, value)` 绑定 —— 参数化查询，用户输入只当数据 ❌不直接拼接 ✅
  - **命名参数**：JPQL 中用 `:name` 占位，`setParameter("name", value)` 绑定 —— 比位置参数可读性更好，同样参数化防注入 ✅
  - **命名查询**：`@NamedQuery` 在**应用启动时就把 JPQL 编译好**，语句结构固定，运行时不支持拼字符串，天然免疫注入 ✅
- 三者核心都是“**参数绑定**”，把用户输入和 SQL 结构分开，因此选项4「以上都对」✅
- 对比记忆：MyBatis 用 `#{}`（预编译）防注入、`${}`（拼接）会注入；JPA 对应三件套 = 位置参数 `?1` / 命名参数 `:name` / 命名查询 `@NamedQuery`
- 记忆点：看到 JPQL 里的 `?1` / `:name` / @NamedQuery → 都是参数化防注入的手段，全选才完整

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

**来源:** 每日一练 App
**分类:** Spring

**题目:** Spring Security中对请求进行拦截，支持以下哪种拦截方式？

**选项:**
1. AntMatchers
2. RegexMatchers
3. MvcMatchers
4. 其他选项都支持 ✅

**我的答案:** 选项4 ✅
**正确答案:** 选项4 ✅

**解析:**
- 三种匹配方式**全都受支持**，各有适用场景：
  - **AntMatchers**：Ant 风格路径匹配（如 `/admin/**`、`/api/*.json`），基于 AntPathMatcher，最常用、最直观
  - **RegexMatchers**：正则表达式匹配（如 `\\.*(save|delete)$`），基于 RegexRequestMatcher，适合复杂/动态路径规则
  - **MvcMatchers**：Spring MVC 风格匹配，基于 MvcRequestMatcher，会**结合 MVC 的路径映射规则**（后缀匹配、矩阵变量、路径变量等），与 Controller 路由行为最一致，Spring Boot 项目首选
- 因此选项4「其他选项都支持」✅
- 补充：Spring Security **6.x 起推荐统一用 `requestMatchers()`**，antMatchers/mvcMatchers 等旧方法已标记弃用（但仍是受支持的匹配方式，老项目大量在用）
- 记忆点：ant=通配符路径、regex=正则、mvc=贴合 MVC 路由；三者都可 → 选“都支持”；新写法一律 requestMatchers()

---

### Q8 — 创建根 ApplicationContext 的官方推荐类

**来源:** 每日一练 App
**分类:** Spring

**题目:** Spring Web 应用中，为了声明式地创建根 ApplicationContext（即加载 Spring 配置文件），通常会使用 ContextLoaderListener。当前官方推荐的实现类是以下哪一个？

**选项:**
1. ContextLoaderListener ✅
2. ContextWebListener
3. ContextLoaderServlet
4. ContextWebServlet

**我的答案:** 选项1 ✅
**正确答案:** 选项1 ✅

**解析:**
- **ContextLoaderListener** 是 Spring 官方推荐的、在 Web 应用中声明式创建**根 ApplicationContext（Root WebApplicationContext）**的类：在 `web.xml` 里声明一个 `<listener>`，应用启动时自动加载 Spring 配置文件（如 `applicationContext.xml`），创建根容器，作为整个应用的 Bean 工厂 ✅
- 工作原理：Servlet 容器启动 → 触发 ContextLoaderListener → 创建根容器并放入 ServletContext（attribute 名为 `WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE`）→ 子容器（DispatcherServlet 的容器）可继承根容器的 Bean（如 service、dao）
- 排除其他选项：
  - **ContextLoaderServlet**：是**早期（Servlet 2.3 之前）**的加载方式，通过 servlet 加载根容器，后来被 listener 方式取代——已过时，不是“当前官方推荐” ❌
  - ContextWebListener / ContextWebServlet：**不存在**的干扰类名 ❌
- 记忆点：加载根容器 → 认准 **ContextLoaderListener**（listener 声明式加载）；看到 LoaderServlet 要能识别它是老古董

---

### Q9 — Servlet 转向且地址栏不变的实现方式

**来源:** 每日一练 App
**分类:** Spring

**题目:** 基于Servlet API如何实现转向时不在地址栏中显示转向后的地址？

**选项:**
1. redirect ❌
2. sendRedirect
3. forward ✅
4. transform

**我的答案:** 选项1 ❌
**正确答案:** 选项3 ✅

**解析:**
- 答案是 **forward（请求转发/服务端跳转）**：`RequestDispatcher.forward()` 由**服务端内部**完成转发，浏览器地址栏**不变**（始终是原始 URL），客户端全程只发**一次请求**，跳转对用户完全透明 ✅
- 排除其他选项：
  - **sendRedirect**：`response.sendRedirect()` 是**客户端重定向**，服务端返回 302 + Location 头，**浏览器收到后重新发起新请求**，地址栏会**变成新地址** —— 正好不符合题目要求 ❌
  - **redirect()**：Servlet API 中**不存在**这个直接调用的 redirect 方法，属于干扰项 ❌
  - **transform**：与请求跳转无关的干扰概念 ❌
- 对比记忆：
  - **forward**：服务端转发 → 地址栏不变、一次请求、可带 request 域数据（同应用内）
  - **sendRedirect**：客户端重定向 → 地址栏变、两次请求（第一次 302）、可跨应用/跨域
- 记忆点：**“地址栏不变”= forward**；看到 sendRedirect/302/地址栏变 → 反向选

---

### Q10 — Spring Bean 生命周期核心类说法

**来源:** 每日一练 App
**分类:** Spring

**题目:** 关于Spring Bean的生命周期中应用的核心类说法正确的是

**选项:**
1. InstantiationAwareBeanPostProcessor继承了BeanPostProcessor接口 ✅
2. InstantiationAwareBeanPostProcessor作用于初始化阶段的前后
3. BeanPostProcessor作用于实例化阶段的前后
4. 其他都不对

**我的答案:** 选项1 ✅
**正确答案:** 选项1 ✅

**解析:**
- 选项1 正确：**InstantiationAwareBeanPostProcessor 确实继承了 BeanPostProcessor 接口**，并在此基础上扩展了 `postProcessBeforeInstantiation` / `postProcessAfterInstantiation` / `postProcessProperties` 等**实例化阶段**的回调方法 ✅
- 关键易混点是把“实例化”和“初始化”两个阶段搞反：
  - **Instantiation（实例化）**：创建 Bean 对象本身（new 出来），属 `InstantiationAwareBeanPostProcessor` 的管辖区（before/afterInstantiation + 属性填充）
  - **Initialization（初始化）**：实例化之后的初始加工（属性赋值、`@PostConstruct`、init-method 等），属 **BeanPostProcessor** 的管辖区（`postProcessBeforeInitialization` / `postProcessAfterInitialization`）
- 因此：
  - 选项2 说 InstantiationAwareBeanPostProcessor 作用于**初始化**阶段前后 ❌ —— 它作用的是**实例化**阶段
  - 选项3 说 BeanPostProcessor 作用于**实例化**阶段前后 ❌ —— 它作用的是**初始化**阶段前后
- 记忆点：**名字即职责**：Instantiation(实例化)Aware → 管实例化；BeanPostProcessor → 管初始化；前者 `extends` 后者

---

### Q11 — 不是 HikariCP 成为 Spring Boot 2 默认连接池的原因

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** 下面哪项不是Spring Boot 2默认数据库连接池选择HikariCP的原因？

**选项:**
1. HikariCP的性能高
2. HikariCP代码量低
3. HikariCP稳定性高
4. HikariCP监控全面 ✅

**我的答案:** 选项4 ✅
**正确答案:** 选项4 ✅

**解析:**
- Spring Boot 2 默认选 HikariCP 的三大官方卖点：
  - **性能高**：基准测试中吞吐量高、延迟低，号称“最快的连接池”（快于 Tomcat JDBC、DBCP2、C3P0）✅
  - **代码量低**：体量轻（约 130KB），类数量少、实现简洁，内存占用小 ✅
  - **稳定性高**：作者 Bret Woolley 的孤品神作，bug 少、经大规模生产验证 ✅
- **“监控全面”不是选它的原因**：HikariCP 的监控能力并不突出（仅有基础的 JMX/Micrometer 指标），而**内置监控面板/全面监控恰恰是 Druid 的招牌卖点**（阿里 Druid 自带可视化监控台、SQL 审计等）——把 Druid 的优点安到 HikariCP 头上就是这题的坑 ❌
- 记忆点：Hikari 三字诀 = **快（性能）/ 小（代码量）/ 稳（稳定性）**；看到“监控全面/监控台” → 那是 Druid 的活

---

### Q12 — Spring Web 上下文中 Bean 的作用域

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** Spring中Web上下文中beans的作用域有哪些？

**选项:**
1. 单例，原型，请求，会话，globalSession ✅
2. 代理、适配器、工厂 ❌
3. bean、组件、服务
4. 控制器、服务、存储库

**我的答案:** 选项2 ❌
**正确答案:** 选项1 ✅

**解析:**
- 选项1 完整列全了 Spring 在**Web 上下文**中的五种 Bean 作用域：
  - **singleton（单例）**：整个容器一个实例，默认作用域
  - **prototype（原型）**：每次获取都新建实例
  - **request（请求）**：每个 HTTP 请求一个实例，请求结束销毁（Web 容器才有）
  - **session（会话）**：每个 HTTP Session 一个实例（Web 容器才有）
  - **globalSession（全局会话）**：全局 HTTP Session 一个实例，主要用在 **Portlet** 场景（4.x 后基本并入 session/application）
- 排除其他选项：
  - 选项2「代理、适配器、工厂」是**设计模式三兄弟**（Proxy / Adapter / Factory），不是 Bean 作用域 —— 用户把设计模式和对象作用域概念混淆了 ❌
  - 选项3「bean、组件、服务」、选项4「控制器、服务、存储库」是 @Component/@Service/@Controller 之类的**注解分层体系**，跟作用域无关 ❌
- 补充：Spring 4.2+ 还多了 **application**（ServletContext 级）作用域，但经典五作用域按题目选项来
- 记忆点：作用域 = **单例/原型 + 请求/会话/globalSession**（后面三个只有 Web 环境才有）；代理/适配器/工厂 → 设计模式，别串台

---

### Q13 — @Configuration + @Bean 代码的说法

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** 关于下面这段代码说法正确的是？

```java
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
4. 应该命名为 `Config` 而不是 `AppConfig`

**我的答案:** 选项3 ✅
**正确答案:** 选项3 ✅

**解析:**
- 选项3 正确：`@Configuration` 标注的类是**配置类**，其中 `@Bean` 修饰的方法 `myService()` 会向容器**创建并注册一个 Bean**（bean 名默认 = 方法名 `myService`，实例是 `MyServiceImpl`，默认单例）✅
- 排除其他选项：
  - 选项1：代码**完全能编译**；`@Bean` 方法的 bean 名**默认就是方法名**，不需要额外起名 ❌
  - 选项2：代码**能编译**；`@Configuration` + `@Bean` 不依赖组件扫描也能注册 Bean（只要配置类被加载进容器），编译层面更没有“需要扫描”的问题 ❌
  - 选项4：类名没有强制约束，`AppConfig` 是合法命名，不存在必须叫 `Config` 的规则 ❌
- 补充：`@Configuration` 本身是 `@Component` 的派生注解，如果配置了 `@ComponentScan` 也会被自动发现；`@Bean` 方法还可以显式指定 `name` 属性改名、用 `@Scope` 改作用域
- 记忆点：**@Configuration = 配置类、@Bean 方法 = 手动造 Bean**；方法名即 Bean 名；看到“不编译/必须叫 Config”这类说法直接排除

---

### Q14 — 不是 Spring Boot 默认支持的数据源类型

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** 下列哪种数据源类型不是 Spring Boot 默认支持的？

**选项:**
1. Tomcat JDBC Pool
2. HikariCP
3. Apache Commons DBCP2 ❌
4. org.drools ✅

**我的答案:** 选项3 ❌
**正确答案:** 选项4 ✅

**解析:**
- **org.drools（Drools）是业务规则引擎**（BRMS，做规则匹配/决策表），**根本不是数据库连接池/数据源** → 它就是“不是默认支持的数据源类型” ✅
- Spring Boot 实际自动支持的三大连接池（按 classpath 自动检测，优先级从高到低）：
  - **HikariCP**：默认首选，classpath 有它就用它
  - **Tomcat JDBC Pool**：Hikari 不在时自动退化使用（Spring Boot 内置依赖）
  - **Apache Commons DBCP2**：显式引入 `commons-dbcp2` 依赖后也可自动配置
- 用户选 DBCP2 的误区：DBCP2 是 Spring Boot **支持**的数据源（只是优先级在 Hikari 之后、需要额外引依赖），不是“不支持”的那个 ❌
- 记忆点：连接池三大候选 = **Hikari / Tomcat JDBC / DBCP2**；看到 **drools** → 那是以规则引擎出名的 Drools，跟数据库连接池八竿子打不着

---

### Q15 — @RestController 标注类的特点

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** 用 @RestController 标注的类特点是？

**选项:**
1. 他们只能接受获取和发布请求
2. 每个方法都是一个 @ResponseBody，默认情况下会处理JSON或XML的序列化 ✅
3. 它们只能返回JSON响应，序列化是通过Jackson处理的 ❌
4. 一个项目中只有一个类可以被注释为 @RestController

**我的答案:** 选项3 ❌
**正确答案:** 选项2 ✅

**解析:**
- 选项2 正确：`@RestController` 是 `@Controller` + `@ResponseBody` 的组合注解，**类里每个方法的返回值都自动当作响应体**（方法等效带 @ResponseBody），由 HttpMessageConverter 完成序列化——默认情况下**支持 JSON（Jackson）和 XML** 等格式 ✅
- 选项3 的坑：说“**只能**返回 JSON”太绝对——输出格式由**返回值类型 + 内容协商（Content Negotiation）**决定，XML 等格式同样支持；Jackson 只是默认的 JSON 转换器，不是唯一序列化方式 ❌（用户就是栽在“只能 JSON”这个绝对化表述上）
- 排除其他选项：
  - 选项1：@RestController **不限制 HTTP 方法**，GET/POST/PUT/DELETE 都行，方法级用 @GetMapping/@PostMapping 等指定 ❌
  - 选项4：一个项目可以**有多个** @RestController 类，没有数量限制 ❌
- 记忆点：@RestController = @Controller + @ResponseBody；序列化 = JSON（Jackson）**或 XML**；看到“只能 JSON”“只能 GET/POST”“只有一个”这类绝对化说法 → 大概率是坑

---

## 📊 第七周错题汇总

| 日期 | 题数 | 答对 | 答错 |
|:----:|:----:|:----:|:----:|
| 08-25 | 15 | 9 | 6 |
| **合计** | **15** | **9** | **6** |