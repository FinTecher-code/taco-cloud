# 第十一周 - 考前辅导知识点记录

> 来源：考前辅导文档（34 页截图），2026-09-26 整理
> 用途：知识点串讲 + 错题讲解，考前最后冲刺

---

## QDA 性能 / 数据库篇

### K1 — 性能基线：没有尺子，不许说「快了」
- 同一条 SQL 至少跑 **3 次取中位数**；首次跑要预热 Buffer Pool（冷启动单独标注）
- 「优化前 3200ms → 优化后 420ms，提升 7.6 倍」才有说服力；「感觉快了」没有
- 四条基线纪律：①固定条件（同数据量/集群/超时）②区分冷热 ③≥3 次取中位数 ④留原始记录可复核

### K2 — TDSQL：分片键与 Proxy 层
- 架构：应用 → TDSQL Proxy → Shard 1/2/3（主+从）；客户端视角仍是一台 MySQL
- Proxy 职责：解析 SQL、路由到分片、聚合结果、跨分片 JOIN/排序/分页（复杂代价都在这层）
- 分片键选得好（查询条件就是分片键）→ 单机查询速度；选不好 → 广播所有 Shard 再合并，越分越慢

### K3 — MPP · Shared-Nothing
- 协调节点 gcluster（拆 SQL、汇总）→ 数据节点 gnode（各自 CPU/磁盘，只算自己的分片）
- 快 = 大查询拆 N 份并行；慢 = 搬数据（网络开销常比计算大）
- 调优格言：单机问「有没有索引」，MPP 先问「数据在不在同一个节点上」

### K4 — 三种分布策略（DISTRIBUTED BY 只认这个）
| 策略 | 特点 | 用法 |
|---|---|---|
| HASH | 均匀、可控，同键落同节点 | 大事实表，分片键选高频 JOIN/GROUP BY 列 |
| RANDOM | 均匀但不可控 | 没有合适键时；每次关联要重分布 |
| REPLICATED | 每节点全量 | 只给小维表用，表大了浪费存储 |

- **坑（错题率 72.7%）：`KEY ... USING HASH` 建的是索引，不是分布表！** 分布表类型只认 `DISTRIBUTED BY` / `REPLICATED`
- 选键三原则：①常做 JOIN 等值关联的列 ②常做 GROUP BY 的列 ③基数高、分布均匀

### K5 — 数据倾斜：慢得没有道理的元凶
- 并行执行总耗时 = **最慢节点耗时**（节点 1 四千万行 vs 均值 1660 万 → 等 8 倍负载的节点）
- 发现：`SELECT 分片键, COUNT(*) FROM 大表 GROUP BY 1 ORDER BY cnt DESC LIMIT 10`
- 判断标准：Top 值行数 > 平均值 **2 倍** → 已倾斜
- 原因：低基数分布键（省份/状态）、超级热点值（NULL、默认值、大客户）
- 解法：①换高基数分布键 ②复合分布键（城市+用户ID）③加盐拆分（热点值拼 0-9 后缀）

### K6 — 慢查询诊断三件套（顺序不能颠倒）
1. **慢查询日志**：`long_query_time` + `log_queries_not_using_indexes` → 几百条候选
2. **mysqldumpslow**：`-s t` 按总耗时排序，`-t 10` 只看 TOP 10 → 10 条值得修的
3. **EXPLAIN**：看 `type / key / rows / Extra` 四字段 → 为什么慢
- 进阶：Performance Schema 按「摘要」归并，给全局画像
- 坑：`SHOW PROFILE` 在 MySQL 8.0 **已废弃**；慢日志阈值设太小会淹掉大问题

### K7 — 连接池（HikariCP）
- 连接建立贵（握手+认证）→ 池子复用；状态：占用中 / 空闲 / 借了没还（泄漏）
- 数据库侧三指标：`Threads_connected`（连接数）、`Threads_running`（真正干活）、`Max_used_connections`（历史峰值）
- 判断：connected 高 + running 低 → 典型泄漏；两个都高 → 真并发压力
- 坑：借了没还 → 开 `leak-detection-threshold` 打印堆栈；根治用 `try-with-resources`
- 口诀：连接池问题只有两种——**池子太小，和借了不还**

### K8 — 主备延迟：从大事务开始查
- 链条：主库大事务（UPDATE 100 万行）→ Binlog 巨型事件 → 从库**单线程回放**（瓶颈）→ `Seconds_Behind_Master` 飙升
- 排查：先看 `Seconds_Behind_Master`，再看 `replication_applier_status_by_worker`；别急着怪网络
- 治标：并行复制 `slave_parallel_workers = 8` + `slave_parallel_type = LOGICAL_CLOCK`（只加快回放，治不了根）
- 治本：大事务分批 `UPDATE ... LIMIT 1000` + sleep，每批小事件摊平回放压力

### K9 — Redis Set：先问「这条命令改的是哪个键」
- **SDIFFSTORE C A B** → 只有 C 被改写，A、B 不动
- 只读不改：`SINTER / SUNION / SDIFF / SMEMBERS / SCARD / SISMEMBER`
- 会改：`SREM`（改自己）、`SMOVE`(源+目标)、带 `STORE` 后缀 = 结果写进第一个参数
- HyperLogLog：12 KB 换 0.81% 误差，只答「有多少个」拿不到元素；`PFADD/PFCOUNT/PFMERGE`；UV 统计场景

### K10 — Kafka：一个分区在同组内只给一个消费者
- offset 单调递增，**分区内有序**，分区之间不保证全局顺序
- 有 key：`hash(key) % 分区数` 决定落点；无 key：轮询 RoundRobin
- 消费者组：5 消费者 4 分区 → 1 个完全闲置；2 消费者 4 分区 → 各扛 2 个
- 并发上限 = 分区数；想并行更多只能**加分区**；分区只能增不能减（hash 变了，键序被打乱）
- 两组各自保存 offset（`__consumer_offsets`），互不干扰，同一份数据可被多业务重复消费

---

## Java / Spring Boot 工程能力篇

### K11 — 分层与依赖注入：别在类里 new 一个 Service
- 分层：Controller（收参/校验/返回）→ Service（业务规则+事务边界）→ Repository（只管存取）
- 调用方向永远向下；Controller 不许直接写 SQL
- **坑：字段注入**（`@Autowired` 写字段上）→ 改用**构造器注入 + final 字段**
- 在类里 `new` 一个 Service = 破坏 IoC 秩序

### K12 — REST API 与状态码：「请求在哪一步失败，就返回哪个码」
- 四道关：认证(401) → 授权(403) → 校验(400) → 业务(500) → 成功(2xx)
- 2xx：200 查到；201 新建（Location 头给地址）；204 删除无返回体
- 4xx：调用方改（400/401/403/404/429，重试没用）；5xx：服务端改（500/502/504，重试可能成功但要看日志）
- 401 = 不知道你是谁；403 = 知道你是谁但角色不够
- **坑：业务失败也返回 200** → 该拒绝就用 4xx

### K13 — 过滤器链：请求一节一节被传下去
- 顺序：JWT 过滤器（验签→查黑名单→放身份，失败 401）→ 用户名密码过滤器（只管 /login）→ 授权过滤器（@PreAuthorize，不够 403）→ Controller
- `chain.doFilter()` = 放行；不调用 = 拦住；`OncePerRequestFilter` 保证只跑一遍
- 注册顺序：`addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)`
- 坑：「token 正确却一直 401」→ 先查过滤器在不在链上、位置对不对

### K14 — Java 语法与并发：先判断再写入会被插队
- 坑：`if (!map.containsKey(k)) put(...)` 两步之间被插队 → 计数丢失
- 对：`map.compute(k, (key, old) -> old == null ? 1 : old + 1)` 一个原子动作
- record/sealed 做数据契约；Optional 别不判断就 get()；金额一律 BigDecimal
- try-with-resources 自动关闭；Pattern 预编译复用；SimpleDateFormat 非线程安全

### K15 — 三个并发工具
- **CountDownLatch**：一次性倒计时门闩，`countDown()` 归零放行，**归零后不可重置**
- **CyclicBarrier**：可复用循环栅栏，N 线程互相等 `await()` 到齐一起走，`barrierAction` 由最后到达的线程执行
- **Semaphore**：限量凭证，acquire/release，接口限流、连接池容量；permits=1 等价锁
- 口诀：Latch=等人齐，Barrier=人齐了一起走，Semaphore=名额先到先得

### K16 — 反射：找得到 ≠ 能访问
- `getField`：只找 public（含继承）；`getDeclaredField`：本类声明全部（含 private，不含父类）
- 必须 `setAccessible(true)` 才能访问，否则 `IllegalAccessException`
- 坑：JDK 9+ 模块化未 opens → `InaccessibleObjectException`；错误推迟到运行期才炸
- 记法：**get 看公开（含继承），getDeclared 看自家（含私有）**

### K17 — @Transactional 事务失效四件套
1. **同类自调用**：`this.b()` 走的是原始对象不是代理 → 注解形同虚设
2. **非 public 方法**
3. **异常被 catch 吞掉**（必须继续 throw）
4. **异常类型不在 rollbackFor 里**
- 修法：①注入代理后的自己 ②`AopContext.currentProxy()` ③拆到另一个 Service（推荐）
- 传播行为记 3 种：
  - **REQUIRED**（默认）同舟共济：有就加入，没有新建，同生共死
  - **REQUIRES_NEW** 另起炉灶：挂起当前，互不影响（写审计日志）
  - **NESTED** 留个后路：savepoint，内层可单独回滚

---

## 安全与合规篇（把边界处的失败当常态设计）

### K18 — 认证 vs 授权
- 认证 Authentication = 你是谁（落点：SecurityFilterChain / JWT 过滤器）→ 失败 **401**
- 授权 Authorization = 你配吗（落点：@PreAuthorize / SecurityFilterChain）→ 失败 **403**

### K19 — 密码存储：绝不明文落库
- 明文：拖库全曝光；MD5 确定性哈希 → 彩虹表穷举
- BCrypt：随机盐写进哈希串 + 成本因子（默认 10，故意慢），验证用 `matches()`，不是自己比字符串
- 例：`$2a$10$[22字符盐][哈希]` —— 同一密码两次哈希不同但都能验证
- 别自己发明加密；用 `BCryptPasswordEncoder`；密码绝不进日志/返回值/异常

### K20 — JWT 三段：前两段没加密，第三段是封条
- Header（算法元数据）. Payload（claims：sub/authorities/jti/iat/exp）. Signature（HMACSHA256 防伪）
- 前两段只是 Base64 **编码不是加密** → 别放密码、身份证号、银行卡
- 密钥 ≥256bit，别硬编码；exp 几小时足够
- jti = 令牌编号，登出拉黑用；黑名单记 exp，过期后惰性清理

### K21 — OAuth 2.0 授权码模式
- 密码只在授权服务器输入；code 只在后端与授权服务器之间换 token（back channel，最安全）
- **state 防 CSRF 一步都不能省**：发起时生成存起来，回调时比对，不一致拒绝
- code 只能兑换一次、有效期很短
- 口诀：浏览器只拿一次性 code，真正的 token 只在后端之间传递

### K22 — RBAC 权限模型
- 三层映射：用户 → 角色 → 权限；改权限只动中间层
- `hasAuthority("code:review")` 精确比对（推荐）；`hasRole("DEV")` 自动补 `ROLE_` 前缀
- **坑：库里存 `ROLE_DEV` 又写 `hasRole("ROLE_DEV")` → 实际比 `ROLE_ROLE_DEV`，永远 403**（头号原因）
- 光写 `@PreAuthorize` 不够，还要 `@EnableMethodSecurity`（漏了注解被静默忽略）
- 权限签进 JWT claims → 改权限要等 token 过期

### K23 — 登录限流与 Token 黑名单
- 限流：10 分钟窗口 5 次失败 → 锁定 10 分钟；ConcurrentHashMap + compute 原子累加；成功清零
- 黑名单：登出 → jti 入黑名单（连 exp 存）→ 过滤器每请求查 → 命中 401

---

## AI 应用篇（把 AI 关进工程纪律）

### K24 — Harness Engineering：先给 AI 写项目说明书
- Harness 层给 AI 看：Agent.md（Role/Capabilities/Knowledge Base/Constraints）、decisions/（ADR）、requirements/、context/、skills/
- **ADR 三段式：Context → Decision → Consequences**
- 模型能力是公共的，**上下文才是你独有的资产**——先写文档再写代码
- 先探能力边界（指令遵循率、可解析率、上下文容量）再上生产

### K25 — Prompt 四要素：写 Prompt = 下订单
1. **角色设定**（"资深 Java 安全审计员"）
2. **任务与输入**
3. **输出约束**：枚举值 + JSON Schema（最关键，否则今天 high 明天「严重」）
4. **示例与边界**（不确定标 confidence 低，别编行号；找不到就返回空数组）
- `temperature = 0.1` 要可复现；短 Prompt 分步链比大而全稳定
- 坑：只说「帮我看看」→ 散文输出，程序没法解析

### K26 — 解析·校验·降级：LLM 输出永远先当不可信输入
- 流程：去 Markdown 围栏 → JSON 解析 → Schema 校验 → 不过则有限重试(≤2 次，**带报错原因**) → 降级 `degraded + rawResponse` 交人工
- **铁律：绝不静默返回空列表。**「没解析出来」≠「没问题」，必须能区分

### K27 — 单测生成与覆盖率门禁
- 三步闭环：分析(正则提签名) → 生成(正常/边界/异常三路径，JUnit5+Mockito) → 验证(ProcessBuilder 真跑 mvn test + 解析 jacoco.xml)
- 门禁：LINE COVEREDRATIO ≥ 0.60，不达标带报错回去重生成
- 坑：把覆盖率当质量目标——没断言的测试是废测试

### K28 — 受控编排：让 AI 走确定的路
- 意图枚举：REVIEW / DEBUG_ISSUE / PROPOSE_PATCH / CONFIRM_APPLY / REREVIEW / EXTRACT / GENERATE_TEST，不确定→澄清
- 上下文裁剪：早期压缩成摘要，最近 4 条保留原文；Token 估算 = 字符数 × 3 × 1.1
- 三纪律：①受控工具调用（程序决定，留痕可审计）②不确定即澄清 ③人工确认才落变更（先看 diff 再写文件）
- 状态机：PROPOSED / CONFIRMED / REJECTED + 稳定 issueId
- **AI 负责分析与建议，人负责确认与落地**

---

## 考前速记口诀汇总
- 性能基线：3 次取中位，冷热要分开，记录要归档
- 分布表只认 `DISTRIBUTED BY`；`USING HASH` 是索引
- 倾斜判断：Top 值 > 均值 2 倍
- 连接池两问题：池子太小、借了不还
- 主备延迟：先找一次改几十万行的大事务
- 并发：先看再动手 → 换成一个原子动作（compute）
- 反射：get 看公开，getDeclared 看自家
- 事务失效四件套：自调用 · 非 public · catch 吞异常 · rollbackFor 没配
- 401 你没登录，403 你权限不够
- 4xx 请你改，5xx 我来改
- ROLE_ 前缀只能出现一次
- JWT 是签名凭据不是保险箱；OAuth 的 state 不能省
