# 第八周 - 刷题记录

---

## 2026-09-06

---

### Q1 — Bean 生命周期中资源释放所在的阶段

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** 假设某个 bean 要使用某种类型的资源，那么一般情况下应该把资源的释放放到 bean 的生命周期中_____阶段。

**选项:**
1. 定义
2. 初始化 ❌
3. 使用
4. 销毁 ✅

**我的答案:** 选项2 ❌
**正确答案:** 选项4 ✅

**解析:**
- 答案是**销毁（Destroy）阶段**：Bean 生命周期中资源的获取（连接、文件句柄、线程池等）一般在**初始化阶段**完成，对应的**释放必须放在销毁阶段**，保证容器关闭/Bean 销毁时资源被回收，避免泄漏 ✅
- Spring 提供了三种声明销毁逻辑的方式：
  - `@Bean(destroyMethod = "close")` / XML `<bean destroy-method="...">`
  - `DisposableBean` 接口的 `destroy()` 方法
  - `@PreDestroy` 注解（JSR-250）
- 用户选「初始化」的误区：**初始化（Initialization）阶段是做资源准备/装配的**（创建连接、打开文件、启动线程），把资源释放放这里 = 刚建好就销毁，逻辑颠倒 ❌
- 生命周期口诀记忆：**实例化 → 属性填充 → 初始化（拿资源）→ 使用 → 销毁（放资源）**；拿在初始化，放必在销毁
- 记忆点：`@PostConstruct`（初始化后拿资源）配 `@PreDestroy`（销毁前放资源）；init = 拿，destroy = 放

---

### Q2 — @SpringBootApplication 复合注解包含的三件套

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** `@SpringBootApplication` 引入了3个重要的注解，除了下面哪个注解？

**选项:**
1. @SpringBootConfiguration
2. @EnableAutoConfiguration
3. @ComponentScan
4. @Controller ✅

**我的答案:** 选项4 ✅
**正确答案:** 选项4 ✅

**解析:**
- `@SpringBootApplication` 是**复合注解**，由三件套组合而成：
  - **@SpringBootConfiguration**：启动类本身是配置类（@Configuration 的派生）
  - **@EnableAutoConfiguration**：开启自动配置（根据 classpath 依赖自动装配 Bean）
  - **@ComponentScan**：扫描启动类所在包及其子包的 @Component/@Service/@Controller 等组件
- **@Controller 不在其中** —— 它是被扫**描**的对象（MVC 控制器），而不是 @SpringBootApplication 的组成部分 ✅
- 记忆点：三件套口诀 = **配置（@SpringBootConfiguration）+ 自动装配（@EnableAutoConfiguration）+ 扫描（@ComponentScan）**；@Controller 是“被扫的”，不是“组成的”

---

### Q3 — ThreadPoolExecutor 构造参数说明（找不正确的）

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** Spring Boot 一般使用下面的函数创建线程池，选项中对参数说明不正确的是？

```java
public ThreadPoolExecutor(
    int corePoolSize,
    int maximumPoolSize,
    long keepAliveTime,
    TimeUnit unit,
    BlockingQueue workQueue,
    ThreadFactory threadFactory,
    RejectedExecutionHandler handle
)
```

**选项:**
1. corePoolSize 就是线程池中的核心线程数量
2. maximumPoolSize 就是线程池中可以容纳的最大线程的数量
3. workQueue 就是等待队列，任务可以储存在任务队列中等待被执行，执行的是 FIFO 原则（先进先出）❌
4. Handler 储存被拒绝的队列列表，执行的也是 FIFO 原则，即超过一定 keepalive 时… ✅

**我的答案:** 选项3 ❌（选了 workQueue 的描述）
**正确答案:** 选项4 ✅

**解析:**
- 选项4 错误最根本：**Handler（RejectedExecutionHandler）不是“储存被拒绝任务的队列列表”**，而是**拒绝策略**（处理器），当核心线程满 + 队列满 + 最大线程数满时，对新任务执行的**处理策略**，与 FIFO、keepAliveTime 全都无关 ✅
- Spring/JDK 内置四种拒绝策略：
  - **AbortPolicy**（默认）：直接抛 `RejectedExecutionException`
  - **CallerRunsPolicy**：由调用者线程自己执行任务
  - **DiscardPolicy**：静默丢弃任务
  - **DiscardOldestPolicy**：丢弃队列中等待最久的任务（这个是 FIFO 式的“丢最老”，但跟 Handler 本身是策略无关）
- 各参数正确含义：
  - **corePoolSize**：核心线程数（即使空闲也保留）
  - **maximumPoolSize**：最大线程数（核心 + 临时非核心）
  - **keepAliveTime**：非核心线程的空闲存活时间（而不是 Handler 相关）
  - **workQueue**：任务等待队列（选项3 的描述基本正确——等待队列存储任务，常见实现如 LinkedBlockingQueue/ArrayBlockingQueue 是 FIFO；严格说 FIFO 与否取决于队列实现，但核心描述没错）
- 用户选 C 的误区：把“等待队列 FIFO”当成了错点，但**真正错得离谱的是 D**——Handler 压根不是队列，是拒绝策略；看到“被拒绝队列/储存列表 + keepalive”这类拼接描述，基本可以锁定是错的
- 记忆点：**Handler = 拒绝策略（4 种）**；keepAliveTime 只管非核心线程空闲存活；拒绝链 = 核心满 → 队列满 → 最大线程满 → 拒绝策略出手

---

### Q4 — @Transactional 使用说法（找错误的）

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** 在Spring Boot中，关于 `@Transactional` 的使用，下面说法错误的是？

**选项:**
1. 在接口上声明 `@Transactional` 时，注解可能无效
2. 将 `@Transactional` 放置在类级的声明中，会使得所有 `public` 方法都有事务
3. 使用了 `@Transactional` 的方法，被同一个类里面的方法调用，`@Transactional` 无效
4. 使用了 `@Transactional` 的方法，可以是 `public` 或 `protected` ✅（错误说法）

**我的答案:** 选项3 ❌
**正确答案:** 选项4 ✅

**解析:**
- 选项4 错误：**@Transactional 只能作用于 public 方法**。Spring 事务基于 AOP 代理实现，默认只拦截 public 方法，放在 `protected`/`private`/包级方法上**不会生效**（不会报错，但静默失效）✅
- 其他三个选项**说的都是对的**：
  - 选项1 正确：**接口上声明 @Transactional 可能无效**——Spring 官方建议把注解放在**实现类/方法**上；接口注解依赖 JDK 动态代理才可能读到，若用 CGLIB 代理则读不到，所以“可能无效”成立
  - 选项2 正确：**类级别声明 = 该类的所有 public 方法都开启事务**（默认行为）
  - 选项3 正确：**同类内部调用（自调用）事务失效**——`this.method()` 直接走本类实例，不经过代理对象，@Transactional 不会触发（经典坑：事务方法内调同类另一 @Transactional 方法，第二个方法不启用新事务）
- 用户选选项3 的误区：选项3 描述的是**真实存在的自调用坑**（说法正确），它不是“错误说法”；题目问的是哪个**说法错**，要去抓“public 或 protected”这个错误点 ❌
- 事务失效全家桶记忆：**非 public 方法**（protected/private）、**同类自调用**（不走代理）、**异常被 catch 吞掉**（事务感知不到 RuntimeException）、**接口/代理方式不匹配**
- 记忆点：看到“可以是 public 或 protected”→ 必错，@Transactional 只认 public；自调用失效是坑但说法本身是对的

---

### Q5 — @PostMapping + @ResponseStatus(CREATED) 的 void 方法行为

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** 给定下面的代码，会发生什么场景？

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public void add(@RequestBody Person person)
```

**选项:**
1. add方法接收POST请求，并将 Person 对象映射到请求，并以 CREATED 状态响应 ✅
2. 代码不会运行，因为没有对请求应用背压 ❌
3. add方法接收所有请求，并映射一个包含传入值的 String
4. 代码将无法编译，因为不能有 @ResponseStatus 和一个空返回类型

**我的答案:** 选项2 ❌
**正确答案:** 选项1 ✅

**解析:**
- 选项1 正确：`@PostMapping` 把 add 方法映射到 **POST 请求**；`@RequestBody Person person` 让 Spring MVC 用 Jackson 把**请求体 JSON 反序列化为 Person 对象**；`@ResponseStatus(HttpStatus.CREATED)` 把响应状态码设为 **201 Created**——即使方法返回 `void`（无响应体）也完全合法 ✅
- 用户选选项2「背压」的误区：**背压（Backpressure）是响应式编程（Reactive Streams）的概念**（如 WebFlux/Project Reactor），跟传统 Spring MVC 的 @PostMapping 毫不相干——选项2 是拿响应式术语硬凑的干扰项 ❌
- 排除其他选项：
  - 选项3：@PostMapping 只接收 **POST** 请求，不是“所有请求”（所有请求是 @RequestMapping 不限方法的默认行为），而且返回类型是 void 不是 String ❌
  - 选项4：`void` 返回 + @ResponseStatus 完全合法，代码**能编译**——@ResponseStatus 的意义恰恰是“我不用返回 ResponseEntity，也能通过注解指定状态码” ❌
- 记忆点：**void 方法 + @ResponseStatus = 只定状态码，不返回体**；看到“背压”出现在 Spring MVC 题里 → 响应式术语串台了，直接排除

---

### Q6 — Zuul 过滤器 pre 类型的使用场景

**来源:** 每日一练 App
**分类:** Spring Cloud

**题目:** Zuul 过滤器类型中，pre类型的使用场景是？

**选项:**
1. 在请求被路由之前调用 ✅
2. 路由请求时被调用
3. route 和 error 过滤器之后被调用 ❌
4. 处理请求时发生错误时被调用

**我的答案:** 选项3 ❌
**正确答案:** 选项1 ✅

**解析:**
- 答案是 **pre（前置）过滤器：在请求被路由（route）之前调用** ✅——Zuul 四类过滤器按执行顺序：
  1. **pre**：路由**之前**（鉴权、限流、参数校验、请求头加工都在这做）
  2. **route**：路由请求时（转发到后端服务，如 Ribbon 负载均衡选实例）
  3. **post**：路由**之后**（响应加工、日志、加响应头）
  4. **error**：处理请求出错时（统一异常兜底）
- 用户选选项3 的误区：**“route 和 error 之后调用”既不是 pre 的作用时机，也不符合任何一类**——pre 在 route 之前，post 才在 route 之后，error 也不是“之后才跑”的常规顺序，选项3 是错位拼接 ❌
- 对照表记忆：**pre=前（进门前办事）、route=中（出门办事）、post=后（办完再收拾）、error=出事故（兜底）**；看到 pre 就锁定“路由之前/请求到达后端之前”
- 记忆点：pre → 请求被路由**之前**调用（认证/限流/改请求）；四大类型顺序 pre → route → post → error

---

### Q7 — Zuul 能做什么（多选组合）

**来源:** 每日一练 App
**分类:** Spring Cloud

**题目:** 下列关于“zuul 能做什么”描述正确的有：

**小项:**
1. 验证与安全保障，识别面向各类资源的验证要求并拒绝那些与要求不符的请求
2. 动态路由，以动态方式根据需要将请求路由至不同后端集群处
3. 静态响应处理：在边缘位置直接建立部分响应，从而避免其流入内部集群

**选项:**
1. 1、2、3 ✅
2. 1、2
3. 1、3
4. 2、3

**我的答案:** 选项1（1、2、3）✅
**正确答案:** 选项1 ✅

**解析:**
- **三项全对**——Zuul 作为 Spring Cloud 的 API 网关，三大核心能力就是：
  - **① 验证与安全**：网关层做统一的鉴权/认证，拦截不符要求的请求（pre 过滤器典型场景）✅
  - **② 动态路由**：按规则把请求路由到不同后端集群（route 过滤器，配合 Ribbon 负载均衡）✅
  - **③ 静态响应处理**：在边缘（网关层）直接返回部分静态响应，不必进入内部集群——这是 Zuul 官方文档列出的能力之一（如直接回静态内容/缓存响应，减少穿透）✅
- 这道题答对的关键是**敢选 1、2、3 全选**：常见陷阱是认为“静态响应处理”不是网关职责，但 Zuul 官方三大场景恰恰包含它（前端静态资源聚合/边缘响应）；只要三个小项都描述得合理，没有互相矛盾，就全选
- 对照上题记忆：Zuul 的四大过滤器（pre/route/post/error）正是实现“验证（pre）、路由（route）、响应处理（post）、错误兜底（error）”的机制
- 记忆点：Zuul 三能力 = **验证安全 + 动态路由 + 边缘静态响应**；小项都成立时放心全选

---

### Q8 — Consul 注册中心优势（组合选择）

**来源:** 每日一练 App
**分类:** Spring Cloud

**题目:** Consul注册中心有什么优势：

**小项:**
1. Consul提供健康检查
2. Consul提供多种包括http、dns协议
3. Consul支持多数据中心

**选项:**
1. 1、2、3 ✅
2. 1、2 ❌
3. 1、3
4. 2、3

**我的答案:** 选项2（1、2）❌
**正确答案:** 选项1（1、2、3）✅

**解析:**
- **三项全对**：Consul 作为服务注册中心，三大优势全部成立：
  - **① 健康检查**：Consul 原生提供健康检查（HTTP/TCP/脚本探活），自动剔除不健康实例——这是它对比 Eureka 的核心优势之一 ✅
  - **② 多种协议（HTTP + DNS）**：Consul 同时支持 HTTP API 和 **DNS 查询**做服务发现（`name.service.consul` 直接解析），比 Eureka 只支持 HTTP 更灵活 ✅
  - **③ 多数据中心**：Consul 原生支持**多数据中心（Multi-Datacenter）联邦**，跨机房复制服务状态——Eureka 不支持，这也是选 Consul 的经典理由 ✅
- 用户选「1、2」的误区：漏掉了**多数据中心**——“Consul 支持多数据中心”是官方宣传的真特性，不是干扰项；看到“存储/复制/联邦/跨机房”这类描述往 Consul 的分布式能力上想
- 对比记忆：**Eureka 三大短板 = 无健康检查（靠心跳）、仅 HTTP、不支持多数据中心** → Consul 恰好三项都补齐，所以 1、2、3 全选
- 记忆点：Consul 三优势 = **健康检查 + HTTP/DNS + 多数据中心**；遇到“哪些是 Consul 优势”的题，三项全选基本不会错

---

### Q9 — Spring Cloud Consul 优势描述（找错误的）

**来源:** 每日一练 App
**分类:** Spring Cloud

**题目:** Spring Cloud Consul组件的优势，下面描述错误的选项是哪一个？

**选项:**
1. Consul支持多数据中心，内外网的服务采用不同的端口进行监听
2. Consul支持健康检查，etcd不提供此功能 ❌
3. Consul使用Paxos算法来保证一致性 ✅（错误说法）
4. Consul官方提供web管理界面，etcd无此功能

**我的答案:** 选项2 ❌
**正确答案:** 选项3 ✅

**解析:**
- 选项3 错误：**Consul 用的不是 Paxos，而是 Raft 算法**来保证一致性。Paxos 是 Zookeeper（Chubby）系的经典选择，Consul 用的是更易工程实现的 **Raft（共识算法）**——把 Paxos 安到 Consul 头上就是这题的坑 ✅
- 用户选选项2 的误区：认为“etcd 也支持健康检查”所以选项2 描述有误——但**对比维度错了**：
  - etcd 提供的是**租约（Lease）+ TTL 心跳机制**，本质上也是健康检查机制，但**它没有内置服务级的健康检查**（如 HTTP 探活、依赖检查），Consul 的 Health Checks 是**一等公民特性**，所以“Consul 支持健康检查，etcd 不提供此功能”在 Spring Cloud 语境下**是成立的、描述正确** ❌
- 逐个排除其他正确描述：
  - 选项1 正确：Consul 支持**多数据中心**，且内外网可通过**不同端口监听**（默认 8300/8301/8500/8600 等按用途区分）
  - 选项4 正确：Consul **官方提供 Web UI**（`/ui` 管理界面），etcd 没有官方可视化面板
- 记忆点：**Consul = Raft**（一致性），**Zookeeper = Paxos（ZAB 基于 Paxos 思想）**，**etcd = Raft**；看到“Consul 用 Paxos”→ 直接锁定错误项

---

### Q10 — Spring Cloud Nacos 原理说明（找错误的）

**来源:** 每日一练 App
**分类:** Spring Cloud

**题目:** 关于下面Spring Cloud Nacos组件的原理说明，错误的选项是哪一个？

**选项:**
1. 在 Nacos 中，客户端进行服务注册时会轮询注册中心集群节点地址，Nacos Server 端采用 Map 结构保存实例信息
2. Nacos 只提供了主动 push 机制，由服务端主动告知客户端服务列表 ✅（错误说法，本题答案）
3. Nacos 服务端提供了 InstanceController 类，其中包含了服务注册相关的 API 接口
4. 客户端发起注册时，调用的接口是 POST `/nacos/v1/ns/instance`，其中 serviceName 表示服务名称，namespace 表示 Nacos 的命名空间
5. Nacos 通过 namespace 来隔离服务，每个 namespace 下有多个 group，每个 group 下有多个 service，再通过 serviceName 确定服务实例

**我的答案:** 选项2 ✅
**正确答案:** 选项2 ✅

**解析:**
- 选项2 错误：**Nacos 不是“只提供主动 push 机制”**，而是 **push + pull 双机制并存**：
  - **pull（默认主力）**：客户端**定时轮询**服务端拉取服务列表（默认约 10s 一次）
  - **push（辅助，UDP）**：服务端有变更时通过 **UDP push** 主动通知客户端，客户端收到后再去拉最新列表兜底——**push 只为减少延迟，不是唯一机制**，且 UDP 不可靠，所以核心还是 pull
  - 说“只提供主动 push” = 以偏概全，错误 ✅
- 选项1 正确：客户端注册时会**轮询集群节点地址**（失败则换节点），Server 端用 **Map 结构（serviceName → List<Instance>）** 保存实例信息
- 选项3 正确：Nacos 服务端确实有 **InstanceController**（naming 模块），提供注册/注销/查询实例的 REST API
- 选项4 正确：客户端注册走 **POST `/nacos/v1/ns/instance`**，参数 `serviceName`（服务名）、`namespace`（命名空间）等——REST API 描述与实际一致
- 选项5 正确：Nacos 隔离层级 = **namespace（租户隔离）→ group → service → instance**，通过 serviceName 定位实例，描述准确
- 对比记忆：**Eureka = 纯 pull（客户端每 30s 拉）+ 心跳；Nacos = pull 为主 + UDP push 为辅；Consul = 服务端主动推送变更（watch）**
- 记忆点：看到“Nacos 只提供 push / 全靠服务端推送”→ 必错，Nacos 是**轮询为主、push 为辅**

---

### Q11 — RetryConfiguration 代码的目的

**来源:** 每日一练 App
**分类:** Spring Cloud

**题目:** 下面这段代码的目的是什么

```java
package practicedir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

public class RetryConfiguration {
    private static Logger log = LoggerFactory.getLogger(RetryConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(name = "configServerRetryInterceptor")
    public RetryOperationsInterceptor configServerRetryInterceptor() {
        log.info(String.format(
            "configServerRetryInterceptor: Changing backOffOptions " +
            "to initial: %s, multiplier: %s, maxInterval: %s",
            1000, 1.2, 5000));
        return RetryInterceptorBuilder.stateless()
            .backOffOptions(1000,1.2,5000)
            .maxAttempts(10)
            .build();
    }
}
```

**选项:**
1. 这个代码有语法错误，没有意义
2. 这个是数据库连接池，复用数据库连接 ❌
3. 应用启动的异常捕获
4. Consul作为注册中心时，实现控制重试，解决配置中心config server响应超时问题 ✅

**我的答案:** 选项2 ❌
**正确答案:** 选项4 ✅

**解析:**
- 选项4 正确：这段代码是 **Spring Retry 的重试拦截器配置**，用于 **Spring Cloud Config（配置中心）客户端**在启动时加载配置的场景——当 **config server 响应超时**时，客户端按照配置的**退避策略（backOff）重试**获取配置，这是 Spring Cloud Config 官方推荐的 `configServerRetryInterceptor` 模式（配合 `spring.cloud.config.fail-fast=true` + Consul 作为注册中心的环境）✅
- 代码关键点拆解：
  - `@ConditionalOnMissingBean(name = "configServerRetryInterceptor")`：**若容器已有同名 Bean 则不重复注册**——允许用户自定义覆盖默认重试配置，且避免重复定义
  - `RetryInterceptorBuilder.stateless()`：构建**无状态重试**拦截器
  - `.backOffOptions(1000, 1.2, 5000)`：**退避策略**——初始间隔 1000ms、每次乘 1.2 倍、最大间隔 5000ms（第一次失败等 1s，之后 1.2× 递增，封顶 5s）
  - `.maxAttempts(10)`：**最多重试 10 次**
- 用户选选项2 的误区：看到 `Interceptor`/`backOff` 联想到“连接池复用”？其实**数据库连接池**是 HikariCP/Druid 这类组件（管理 Connection 复用），和这里的 **RetryOperationsInterceptor（方法调用重试）** 完全是两回事 ❌
- 排除其他选项：
  - 选项1：代码**语法正确**、能编译能运行，不是语法错误 ❌
  - 选项3：**不是对启动异常的捕获**——try/catch 才是捕获，这里是**重试机制**（失败不立即报错，而是按策略再试），语义不同 ❌
- 记忆点：`RetryInterceptorBuilder` + `backOffOptions` + `maxAttempts` = **Spring Retry 重试拦截器**；看到 `configServerRetryInterceptor` 这个名字就锁定“Config 配置中心失败重试”

---

### Q12 — Sentinel 限流规则中表示调用关系限流策略的字段

**来源:** 每日一练 App
**分类:** Spring Cloud

**题目:** Spring Cloud中，Sentinel组件的限流规则中表示调用关系限流策略的是？

**选项:**
1. resource
2. count
3. strategy ✅
4. controlBehavior ❌

**我的答案:** 选项4 ❌
**正确答案:** 选项3 ✅

**解析:**
- 答案是 **strategy（策略字段）**：Sentinel 的 `FlowRule` 中 `strategy` 字段用于设置**调用关系限流策略**（`FlowRuleStrategy`），取值如：
  - `STRATEGY_DIRECT`（0）：**直接限流**——针对当前资源本身
  - `STRATEGY_RELATE`（1）：**关联限流**——当关联资源达到阈值时限制当前资源（如支付接口限流连带限制下单接口）
  - `STRATEGY_CHAIN`（2）：**链路限流**——针对调用链路入口限流（从指定入口资源进入的请求才计数）
- 用户选选项4 的误区：**controlBehavior 是“流控效果/行为”字段**（直接拒绝、Warm Up 预热、匀速排队），管的是**到达阈值后怎么处理**，不是“调用关系策略”——把“策略”和“行为”搞混了 ❌
- 其他字段对照：
  - **resource**：限流的**资源名**（被保护的对象）
  - **count**：**限流阈值**（QPS/线程数上限）
  - **strategy**：**调用关系限流策略**（直接/关联/链路）← 本题答案
  - **controlBehavior**：**流控效果**（直接拒绝/预热/排队等待）
- 记忆口诀：**资源（resource）→ 阈值（count）→ 策略（strategy）→ 效果（controlBehavior）**；问“调用关系”= strategy（直接/关联/链路），问“超出后怎样”= controlBehavior（拒绝/预热/排队）

---

### Q13 — Spring Cloud Gateway 的作用

**来源:** 每日一练 App
**分类:** Spring Cloud

**题目:** Spring Cloud Gateway的作用是

**选项:**
A. 作为服务的注册中心提供服务的注册、发现与配置的功能
B. 为各种环境下运行的服务的外部配置提供一个统一的管理中心 ❌
C. 旨在为微服务架构提供一种简单有效的统一的 API 路由管理方式 ✅
D. 通过轻量消息代理连接各个分布的节点，可用来广播状态的变化或者其他的消息指令

**我的答案:** 选项B ❌

**正确答案:** 选项C ✅

**解析:**
- 选项C 正确：**Spring Cloud Gateway = API 网关**，核心职责是**统一的 API 路由管理**——基于 Spring WebFlux（响应式）实现，通过 Route（路由）+ Predicate（断言）+ Filter（过滤器）把请求按规则转发到下游微服务，同时可做**统一鉴权、限流、日志、跨域**等横切处理 ✅
- 用户选选项B 的误区：把 Gateway 当成**配置中心**了——**“外部配置统一管理”是 Spring Cloud Config（配置中心）的职责**，配套组件是 Config Server/Client，跟 Gateway 无关 ❌
- 逐个排除：
  - 选项A：服务注册/发现是 **Eureka / Consul / Nacos** 的活；配置功能是 Config 的活——这是把“注册中心+配置中心”混合描述，不是 Gateway ❌
  - 选项B：**Spring Cloud Config** 的职责（统一外部配置管理），不是 Gateway ❌
  - 选项D：**“轻量消息代理广播状态变化”是 Spring Cloud Bus** 的职责（配合 Config 做配置动态刷新，基于 MQ）❌
- 组件职责全家桶记忆：
  - **Gateway** = 统一 API 路由/网关
  - **Config** = 外部配置统一管理
  - **Eureka/Consul/Nacos** = 服务注册与发现
  - **Bus** = 轻量消息代理，广播配置变更/状态变化
- 记忆点：看到“路由管理”→ Gateway；看到“外部配置管理”→ Config；看到“消息代理广播”→ Bus；看到“注册发现”→ 注册中心

---

### Q14 — application.properties 与 application.yml 并存时的优先级

**来源:** 每日一练 App
**分类:** Spring Boot

**题目:** 如果SpringBoot中在项目的相同位置存在application.properties和application.yml，以下描述正确的是？

**选项:**
1. 以.properties为准，不再读取.yml
2. 相同的key以.yml为准，某个key不在.yml且在.properties则以.properties为准
3. 以.yml为准，不再读取.properties
4. 相同的key以.properties为准，某个key不在.properties且在.yml则以.yml为准 ✅

**我的答案:** 选项4 ✅
**正确答案:** 选项4 ✅

**解析:**
- 选项4 正确：Spring Boot 加载配置时，**同位置的 application.properties 优先级高于 application.yml**：
  - **相同 key**：以 **.properties 的值**为准，覆盖 .yml 中的同名配置
  - **key 只在 .yml 中**：使用 .yml 中的值（两者不是二选一，而是**合并加载**，properties 覆盖 yml）
- 即：**两个文件都会读取，properties 优先覆盖同名项，yml 只填补 properties 没有的键** ✅
- 原理：Spring Boot 的 `ConfigDataEnvironmentPostProcessor` 按**优先级顺序**加载配置源（同一目录下 properties 排在 yml 前面），后加载的高优先级覆盖先加载的；配置源还能通过 `spring.config.import`、`spring.config.additional-location` 扩展
- 完整优先级梯子记忆（从高到低）：命令行参数 > Java 系统属性 / 环境变量 > application-{profile}.properties(yml) > **application.properties > application.yml** > 随机数/默认值……（同名规则同理：properties 压 yml 一头）
- 记忆点：**同目录下 properties ＞ yml（同名 key 前者赢）；两份文件都生效（互补不互斥）**；选“以 properties 为准、yml 仅补缺”的选项

---

### Q15 — 线程池隔离 vs 信号量隔离对比（找错误的）

**来源:** 每日一练 App
**分类:** Spring Cloud

**题目:** 关于线程池隔离和信号量隔离的对比说法错误的是？

**选项:**
1. 信号量隔离请求处理线程和调用服务的线程是同一个线程，而线程池隔离请求处理线程和调用服务的线程不是同一个线程
2. 线程池隔离资源开销大，而信号量隔离无线程切换故开销小 ❌
3. 线程池隔离支持并发、支持异步处理，而信号隔离支持并发、不支持异步处理
4. 线程池隔离不支持超时处理，而信号量隔离支持超时处理 ✅（错误说法，本题答案）

**我的答案:** 选项2 ❌
**正确答案:** 选项4 ✅

**解析:**
- 选项4 说反了：**线程池隔离支持超时，信号量隔离不支持超时**——这正是两者的关键差异之一 ✅
  - **线程池隔离**：调用跑在独立线程池里，可通过 Future/超时机制**中断超时调用**，**支持超时处理**
  - **信号量隔离**：调用跑在**调用方自己的线程**里，无法从外部中断正在执行的线程，**不支持超时**（只能靠自身代码逻辑）
- 用户选选项2 的误区：选项2 描述**其实是正确的**——线程池隔离确实**开销大**（线程创建、上下文切换、队列），信号量隔离**无线程切换故开销小**（同一线程直跑）；它是“对的陈述”，不是本题要找的“错误说法” ❌
- 逐一确认其他正确说法：
  - 选项1 正确：信号量 = **同线程**执行；线程池 = **请求线程与执行业务的线程分离**（请求线程把任务丢进线程池）
  - 选项3 正确：线程池隔离基于线程池，**支持并发 + 支持异步**；信号量只控制并发数（许可证），**不支持异步**
- 完整对照表记忆：
  | 维度 | 线程池隔离 | 信号量隔离 |
  |------|-----------|-----------|
  | 线程 | 独立线程池（非同线程） | 调用方同线程 |
  | 开销 | 大（线程切换） | 小（无线程切换） |
  | 异步 | 支持 | 不支持 |
  | 超时 | **支持** | **不支持** |
- 记忆点：问“错误说法”时，看到**“线程池不支持超时”**→ 锁定，正确版本是“信号量不支持超时”

---

## 📊 第八周错题汇总

| 日期 | 题数 | 答对 | 答错 |
|:----:|:----:|:----:|:----:|
| 09-06 | 15 | 4 | 11 |
| **合计** | **15** | **4** | **11** |