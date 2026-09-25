# 第八周 - 刷题记录

---

## 2026-09-06

---

### Q1 — Bean 生命周期中资源释放所在的阶段

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 假设某个bean要使用某种类型的资源，那么一般情况下应该把资源的释放放到bean的生命周期中____阶段。

**选项:**
1. 定义
2. 初始化
3. 使用
4. 销毁 ✅

**我的答案:** 选项2 ❌

**正确答案:** 选项4

**解析:**
- 在 Spring Bean 的生命周期中，资源的释放（如关闭文件、数据库连接、网络连接等清理操作）通常应放在 销毁阶段，以确保资源被正确回收，避免资源泄漏。

---
### Q2 — @SpringBootApplication 复合注解包含的三件套

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** `@SpringBootApplication `引入了3个重要的注解，除了下面哪个注解？

**选项:**
1. `@SpringBootConfiguration`
2. `@EnableAutoConfiguration`
3. `@ComponentScan`
4. `@Controller` ✅

**我的答案:** 选项4 ✅

**正确答案:** 选项4

**解析:**
- `@SpringBootApplication`是一个复合注解，包括@ComponentScan，和`@SpringBootConfiguration`，`@EnableAutoConfiguration`。其他不是主要的注解。

---
### Q3 — ThreadPoolExecutor 构造参数说明（找不正确的）

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Spring Boot一般使用下面的函数创建线程池，选项中对参数说明不正确的是？
```
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
1. 线程池中的corePoolSize就是线程池中的核心线程数量
2. maximumPoolSize就是线程池中可以容纳的最大线程的数量
3. workQueue，就是等待队列，任务可以储存在任务队列中等待被执行，执行的是FIFO原则（先进先出）
4. Hanlder储存被拒绝的队列列表，执行的也是FIFO原则，即超过一定keepalive时间之后的队列被删除 ✅

**我的答案:** 选项3 ❌

**正确答案:** 选项4

**解析:**
- handler,是一种拒绝策略，我们可以在任务满了之后，拒绝执行某些任务。
- handler的拒绝策略有四种：
- ```
- 第一种AbortPolicy:不执行新任务，直接抛出异常，提示线程池已满
- 第二种DisCardPolicy:不执行新任务，也不抛出异常
- 第三种DisCardOldSetPolicy:将消息队列中的第一个任务替换为当前新进来的任务执行
- 第四种CallerRunsPolicy:直接调用execute来执行当前任务
- ```

---
### Q4 — @Transactional 使用说法（找错误的）

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 在Spring Boot中，关于`@Transactional`的使用，下面说法错误的是？

**选项:**
1. 在接口上声明`@Transactional`时，注解可能无效
2. 将`@Transactional`放置在类级的声明中，会使得所有`public`方法都有事务
3. 使用了`@Transactional`的方法，被同一个类里面的方法调用，`@Transactional`无效
4. 使用了`@Transactional`的方法，可以是`public`或`protected` ✅

**我的答案:** 选项3 ❌

**正确答案:** 选项4

**解析:**
- 使用了@Transactional的方法，只能是public，@Transactional注解的方法都是被外部其他类调用才有效，故只能是public。道理和上面的有关联。故在 protected、private 或者 package-visible 的方法上使用 @Transactional 注解，它也不会报错，但事务无效

---
### Q5 — @PostMapping + @ResponseStatus(CREATED) 的 void 方法行为

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 给定下面的代码，会发生的场景是？
```
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public void add (@RequestBody Person person) {
    // ...
}
```

**选项:**
1. add方法接收POST请求，并将 `Person`对象映射到请求，并以 `CREATED`状态响应 ✅
2. 代码不会运行，因为没有对请求应用背压
3. add方法接收所有请求，并映射一个包含传入值的`String`
4. 代码将无法编译，因为不能有`@ResponseStatus` 和一个空返回类型

**我的答案:** 选项2 ❌

**正确答案:** 选项1

**解析:**
- 1. `@PostMapping` 表示处理 HTTP POST 请求。
- 2. `@ResponseStatus(HttpStatus.CREATED)` 设置成功响应时的 HTTP 状态码为 201（CREATED）。
- 3. `@RequestBody Person person` 将请求体 JSON/XML 反序列化为 Person 对象。
- 4. 方法返回类型 `void` 表示没有响应体，但状态码已经指定为 201。

---
### Q6 — Zuul 过滤器 pre 类型的使用场景

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Zuul 过滤器类型中，pre类型的使用场景是？

**选项:**
1. 在请求被路由之前调用 ✅
2. 路由请求时被调用
3. route 和 error 过滤器之后被调用
4. 处理请求时发生错误时被调用

**我的答案:** 选项3 ❌

**正确答案:** 选项1

**解析:**
- Zuul 中的过滤器总共有 4 种类型，且每种类型都有对应的使用场景。
- 1）pre：
- 可以在请求被路由之前调用。适用于身份认证的场景，认证通过后再继续执行下面的流程。
- 2）route：
- 在路由请求时被调用。适用于灰度发布场景，在将要路由的时候可以做一些自定义的逻辑。
- 3）post：
- 在 route 和 error 过滤器之后被调用。这种过滤器将请求路由到达具体的服务之后执行。适用于需要添加响应头，记录响应日志等应用场景。
- 4）error：
- 处理请求时发生错误时被调用。在执行过程中发送错误时会进入 error 过滤器，可以用来统一记录错误信息。

---
### Q7 — Zuul 能做什么（多选组合）

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下列关于“zuul 能做什么”描述正确的有：
1. 验证与安全保障，识别面向各类资源的验证要求并拒绝那些与要求不符的请求。
2. 动态路由，以动态方式根据需要将请求路由至不同后端集群处。
3. 静态响应处理: 在边缘位置直接建立部分响应，从而避免其流入内部集群。

**选项:**
1. 1、2、3 ✅
2. 1、2
3. 1、3
4. 2、3

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- 正确答案是“1、2、3

---
### Q8 — Consul 注册中心优势（组合选择）

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Consul注册中心有什么优势：
1. Consul提供健康检查
2. Consul提供多种包括http、dns协议
3. Consul支持多数据中心

**选项:**
1. 1、2、3 ✅
2. 1、2
3. 1、3
4. 2、3

**我的答案:** 选项2 ❌

**正确答案:** 选项1

**解析:**
- 正确答案是：1、2、3

---
### Q9 — Spring Cloud Consul 优势描述（找错误的）

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Spring Cloud Consul组件的优势，下面描述错误的选项是哪一个？

**选项:**
1. Consul支持多数据中心，内外网的服务采用不同的端口进行监听
2. Consul支持健康检查，etcd不提供此功能
3. Consul使用Paxos算法来保证一致性 ✅
4. Consul官方提供web管理界面，etcd无此功能

**我的答案:** 选项2 ❌

**正确答案:** 选项3

**解析:**
- Consul使用 Raft 算法来保证一致性, 比复杂的 Paxos 算法更直接. 相比较而言, zookeeper 采用的是 Paxos, 而 etcd 使用的则是 Raft。

---
### Q10 — Spring Cloud Nacos 原理说明（找错误的）

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 关于下面Spring Cloud Nacos组件的原理说明，错误的选项是哪一个？

**选项:**
1. 在 Nacos 中，客户端进行服务注册时会轮询注册中心集群节点地址，Nacos Server 端采用 Map 结构保存实例信息
2. Nacos 只提供了主动 push 机制，由服务端主动告知客户端服务列表 ✅
3. Nacos 服务端提供了 InstanceController 类，其中包含了服务注册相关的 API 接口
4. 客户端发起注册时，调用的接口是：`[post]: /nacos/v1/ns/instance`，其中 `serviceName` 表示服务名称，`namespace` 表示 Nacos 的命名空间

**我的答案:** 选项2 ✅

**正确答案:** 选项2

**解析:**
- 在服务的调用方，为了保证本地服务实例列表的动态感知，Nacos与其他注册中心不同的是，采用了 Pull/Push同时运作的方式

---
### Q11 — RetryConfiguration 代码的目的

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下面的代码的目的是什么？

```
package com.louis.mango.consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

public class RetryConfiguration {
    private static Logger log = LoggerFactory.getLogger(RetryConfiguration.class);

    @ConditionalOnMissingBean(name = "configServerRetryInterceptor")
    @Bean
    public RetryOperationsInterceptor configServerRetryInterceptor(){

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
2. 这个是数据库连接池，复用数据库连接
3. 应用启动的异常捕获
4. Consul作为注册中心时，实现控制重试，解决配置中心config server响应超时问题 ✅

**我的答案:** 选项2 ❌

**正确答案:** 选项4

**解析:**
- 客户端需要在 config server 无响应时进行重试，以给 config server 时间进行恢复。
- 利用 spring 提供的重试组件，可以方便的配置重试机制，包括重试间隔，重试次数等。

---
### Q12 — Sentinel 限流规则中表示调用关系限流策略的字段

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Spring Cloud中，Sentinel组件的限流规则中表示调用关系限流策略的是？

**选项:**
1. resource
2. count
3. strategy ✅
4. controlBehavior

**我的答案:** 选项4 ❌

**正确答案:** 选项3

**解析:**
- strategy: 调用关系限流策略，直连，链路等

---
### Q13 — Spring Cloud Gateway 的作用

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Spring Cloud Gateway的作用是

**选项:**
1. 作为服务的注册中心提供服务的注册、发现与配置的功能
2. 为各种环境下运行的服务的外部配置提供一个统一的管理中心
3. 旨在为微服务架构提供一种简单有效的统一的 API 路由管理方式 ✅
4. 通过轻量消息代理连接各个分布的节点，可用来广播状态的变化或者其他的消息指令

**我的答案:** 选项2 ❌

**正确答案:** 选项3

**解析:**

---
### Q14 — application.properties 与 application.yml 并存时的优先级

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 如果SpringBoot中在项目的相同位置存在application.properties和application.yml，以下描述正确的是？

**选项:**
1. 以.properties为准，不再读取.yml
2. 相同的key以.yml为准，某个key不在.yml且在.properties则以.properties为准
3. 以.yml为准，不再读取.properties
4. 相同的key以.properties为准，某个key不在.properties且在.yml则以.yml为准 ✅

**我的答案:** 选项4 ✅

**正确答案:** 选项4

**解析:**
- 根据Spring Boot的官方文档和常规行为，当两个文件都存在时，Spring Boot会优先加载application.properties文件。
- - 如果两个文件定义了相同的键，application.properties中的值将覆盖application.yml中的值（假设application.properties先被加载）。
- - 如果一个键只存在于其中一个文件中，则该文件中的值将被使用。

---
### Q15 — 线程池隔离 vs 信号量隔离对比（找错误的）

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 关于线程池隔离和信号量隔离的对比说法错误的是?

**选项:**
1. 信号量隔离请求处理线程和调用服务的线程是同一个线程，而线程池隔离请求处理线程和调用服务的线程不是同一个线程
2. 线程池隔离资源开销大，而信号量隔离无线程切换故开销小
3. 线程池隔离支持并发、支持异步处理，而信号隔离支持并发、不支持异步处理
4. 线程池隔离不支持超时处理，而信号量隔离支持超时处理 ✅

**我的答案:** 选项2 ❌

**正确答案:** 选项4

**解析:**
- 线程池隔离支持超时处理，而信号量隔离不支持超时处理

