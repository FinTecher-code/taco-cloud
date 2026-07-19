# 第二周 - 错题记录

## 2026-07-14

### 题目：影响 HyperLogLog 基数估算准确率的因素

**选项：**
1. 集合中元素的 Hash 函数
2. 集合中元素的数据类型
3. HyperLogLog 的空间大小 ✅
4. 集合中元素的数据量

**我的答案：** 集合中元素的数据类型 ❌
**正确答案：** HyperLogLog 的空间大小 ✅

**解析：**
HyperLogLog 的误差率取决于寄存器数量，而寄存器数量由它占用的空间大小决定。标准 Redis HyperLogLog 占用 12KB（2^14 个寄存器），误差率约 0.81%。

- 空间越大 → 寄存器越多 → 精度越高，这是唯一影响因素
- 元素数据类型、Hash 函数、数据量均不影响固定误差率
- HLL 的核心特性是"用空间换精度"，与数据本身无关

---

### 题目：如何使用 HyperLogLog 统计电商网站每天访问量

**选项：**
1. 按页面分别创建 HyperLogLog，分别统计各页面访问量
2. 每天创建一个 HyperLogLog，使用 PFADD 记录访问者 IP，用 PFCOUNT 统计各天访问量 
3. 每日创建一个 HyperLogLog，用 PFADD 记录访问者 IP，用 PFMERGE 合并所有天数的数据，再用 PFCOUNT 统计总访问量 ✅
4. 按每天创建一个 HyperLogLog，使用PFADD 命令记录访问者的IP 地址，使用 PFMERGE 命令合并所有天数的HyperLogLog，使用 PFCOUNT 命令统计总的访问量 ❌

**我的答案：** 选项4 ❌
**正确答案：** 选项3 ✅

**解析：**
这道题的陷阱在于题目问的是"统计电商网站每天访问量"，而选项2看起来合理（每天一个 HLL + PFADD + PFCOUNT），但它少了一步——

题目真正想考的是 **HyperLogLog 的合并能力（PFMERGE）**。

**完整流程：**
1. 每天一个 HyperLogLog key（如 `uv:2026-07-14`）
2. **PFADD** 记录每次访问的访客标识（IP/用户ID）
3. 需要统计某几天或全部的总 UV 时
4. 用 **PFMERGE** 合并多个天的 HLL 数据到临时 key
5. 再用 **PFCOUNT** 统计合并后的基数

**PFMERGE 的核心价值：**
- HLL 合并具有数学性质，合并后的结果 ≈ 对多天访客做整体去重
- 而简单对各天 PFCOUNT 再相加，不等于总独立访客（同一个人访问多天会被重复计算）

**关键考点：**
- PFADD 记录 → PFCOUNT 单日统计 → PFMERGE 多日合并 → PFCOUNT 总统计
- HLL 的合并运算特性（选项2缺了 PFMERGE 这一步）

---

### 题目：查看 Redis 使用情况及状态信息的命令是？ ✅

**选项：**
1. `info` ✅  （回答正确）
2. `informa`
3. `get`
4. `set`

**我的答案：** `info` ✅
**正确答案：** 选项1 - `info`

**解析：**
`INFO` 命令用于查看 Redis 服务器的各种信息，包括：
- 服务器基本信息（版本、进程 ID 等）
- 内存使用情况
- 客户端连接数
- 持久化状态（RDB/AOF）
- 统计信息（命中率、每秒请求数等）
- 复制/集群状态

**常用变体：**
- `INFO server` — 只查看服务器信息
- `INFO memory` — 只查看内存
- `INFO stats` — 只查看统计
- `INFO ALL` — 查看所有信息

**干扰项分析：**
- `informa` — 不存在此命令
- `get` — 获取键值对
- `set` — 设置键值对

---

### 题目：下列 Redis 命令执行过程描述正确的是？

```
1  SADD        user      "child"    "student"    "worker"
2  SADD        person    "worker"   "farmer"     "child"
3  SREM        person                "child"
4  SDIFFSTORE  diff      user        person
5  SMOVE       diff      person      "farmer"
6  SCARD       person
```

**选项：**
1. `diff` 含有两个元素: `"student"`, `"child"` ✅
2. `SMOVE` 操作成功输出 `1`
3. `SCARD` 输出 `3` ❌
4. `SREM` 操作失败输出 `0`

**我的答案：** 选项3 - `SCARD` 输出 `3` ❌
**正确答案：** 选项1 ✅

**逐步推导：**

| 步骤 | 命令 | 执行结果 | 当前状态 |
|------|------|---------|---------|
| 1 | `SADD user "child" "student" "worker"` | 返回 3（新加 3 个） | user = {child, student, worker} |
| 2 | `SADD person "worker" "farmer" "child"` | 返回 2（farmer 为新加） | person = {worker, farmer, child} |
| 3 | `SREM person "child"` | 返回 1（成功移除） | person = {worker, farmer} |
| 4 | `SDIFFSTORE diff user person` | 返回 2（存了 2 个元素） | diff = {child, student}（差集） |
| 5 | `SMOVE diff person "farmer"` | 返回 **0**（farmer 不在 diff 中） | diff/person 不变 ❗ |
| 6 | `SCARD person` | 返回 **2**（person 有 2 个元素） | person = {worker, farmer} |

**逐项验证选项：**
1. ✅ diff 确实为 {child, student}，两个元素
2. ❌ SMOVE 失败返回 0，不是 1
3. ❌ SCARD person 输出 2，不是 3（用户选的②
4. ❌ SREM 成功返回 1，不是 0

**关键考点：**
- `SDIFFSTORE` 计算差集并存储（user - person）
- `SMOVE` 元素必须在源集合中才能移动，否则返回 0
- `SREM` 移除存在的元素返回 1，不存在返回 0
- 一步步推算集合状态，不要凭感觉跳步

---

### 题目：Apache Tomcat 的 Coyote 组件支持哪些主要网络协议？

**选项：**
1. 仅支持 HTTP/1.1
2. 支持 HTTP/1.1 和 HTTPS，不支持 AJP ❌
3. 支持 HTTP/1.1、HTTPS 和 AJP ✅
4. 仅支持 AJP 和 WebSocket

**我的答案：** 选项2 ❌
**正确答案：** 选项3 ✅

**解析：**
Apache Tomcat 的 **Coyote 连接器**（Connector）支持以下协议：

| 协议 | 说明 |
|------|------|
| **HTTP/1.1** | 直接处理 HTTP 请求（默认 8080 端口） |
| **HTTPS** | HTTP over SSL/TLS，加密通信（默认 8443） |
| **AJP** | Apache JServ Protocol，用于与 Apache HTTPD / Nginx 反向代理通信（默认 8009） |

**核心误解：** AJP 不是过时的协议，而是 **Tomcat 连接器架构的一部分**，专门用于前端 Web 服务器（Apache HTTPD）和后端 Tomcat 之间的二进制通信。它比 HTTP 代理效率更高（二进制协议、少解析开销）。

**Coyote 的角色：**
- Coyote 是 Tomcat 的连接器组件，负责监听端口、接收请求、解析协议、转发给 Servlet 引擎
- 它支持 HTTP/1.1、HTTPS、AJP 三种协议的连接器实现
- 在 `server.xml` 中以 `<Connector>` 元素配置，通过 `protocol` 属性指定

**干扰项分析：**
- 选项1：HTTP/1.1 只是其中之一，Coyote 还支持 HTTPS 和 AJP
- 选项2：错误的是"不支持 AJP"，AJP 是 Coyote 原生支持的 ✅
- 选项4：WebSocket 是后来通过 NIO 连接器支持的，并非 Coyote 的主要/原生协议

---

## 2026-07-17

### Tomcat — DefaultServlet 职责

**题目**：`DefaultServlet` 主要负责处理什么？

- [ ] 处理静态资源（HTML、CSS、JS、图片等）
- [ ] 处理动态请求（JSP、Servlet）
- [ ] 拦截 404 请求
- [ ] 分发请求到 Servlet

**正确答案**：处理静态资源（HTML、CSS、JS、图片等）

**解析**：
- `DefaultServlet` 是 Tomcat 内置的默认 Servlet，职责是**处理静态资源**的请求
- 动态请求（JSP/Servlet）分别由 `JspServlet` 和普通 Servlet 处理
- 拦截 404 和分发请求都不是它的主要职责

**状态**：❌ 答错

---

### Tomcat — Connector 与 Container 交互方式

**题目**：Tomcat 中 Connector 和 Container 是怎样交互的？

- [ ] Connector 直接将请求交给 Context 处理
- [ ] Connector 将请求交给 Engine，再由 Engine 交给 Host
- [x] Connector 解析 HTTP 请求，然后交给 Engine
- [ ] Container 取代 Connector 解析 HTTP 请求

**正确答案**：Connector 解析 HTTP 请求，然后交给 Engine

**解析**：
- **Connector** 负责接收并**解析 HTTP 请求**，将其封装为 `Request`/`Response` 对象
- 解析后交给**Container 容器顶层（Engine）** 处理
- Engine → Host → Context → Wrapper 逐级向下分发
- Connector 不直接跟 Context 打交道，Container 也不干解析的活

**状态**：✅ 答对

---

### Tomcat — 顶层容器（整个实例）

**题目**：哪个组件是顶层容器，管理多个 Service，代表整个 Tomcat 实例？

- [ ] Engine
- [ ] Host
- [x] Server
- [ ] Connector

**正确答案**：Server

**解析**：
- **Server** 是 Tomcat 最顶层组件，代表**整个 Tomcat 实例**
- 一个 Server 可以包含**多个 Service**
- 每个 Service 包含若干 Connector + 一个 Engine
- Engine → Host → Context → Wrapper 都在 Server 管理之下
- Engine 是 Container 的顶层，但不是整个 Tomcat 实例的顶层

**状态**：✅ 答对

---

### Tomcat — ProtocolHandler 三个重要组件

**题目**：以下哪个**不是** ProtocolHandler 的三个重要组件之一？

- [ ] Endpoint
- [ ] Processor
- [ ] Adapter
- [x] Request

**正确答案**：Request

**解析**：
- ProtocolHandler 的**三个核心组件**是：**Endpoint**、**Processor**、**Adapter**
- **Endpoint**：处理底层网络 I/O（Socket 连接）
- **Processor**：解析 HTTP 请求报文
- **Adapter**：将解析后的请求适配给 Container 处理
- **Request** 是 Processor 解析后产生的对象，不是 ProtocolHandler 的组件

**状态**：❌ 答错

---

### MyBatis — Resources 加载配置文件

**题目**：使用 `Resources` 类的哪个方法从 classpath 加载 `mybatis-config.xml` 来构建 `SqlSessionFactory`？

- [x] `getResourceAsStream`
- [ ] `loadResource` ← 我选的
- [ ] `openResource`
- [ ] `readResource`

**正确答案**：`Resources.getResourceAsStream()`

**解析**：
- MyBatis 的 `org.apache.ibatis.io.Resources` 工具类，从 classpath 加载资源用的是 **`getResourceAsStream()`**
- 用法：`Resources.getResourceAsStream("mybatis-config.xml")`
- `loadResource`、`openResource`、`readResource` 都不是 Resources 类的方法

**状态**：❌ 答错

---

## 2026-07-19

### 题目：MyBatis `<foreach>` 标签不包含哪个属性？

**选项：**
1. `collection`
2. `itif` ✅（正确答案）
3. `index`
4. `separator`

**我的答案：** `itif` ✅
**正确答案：** 选项2 - `itif`

**解析：**
MyBatis `<foreach>` 标签的有效属性共 6 个：

| 属性 | 说明 |
|------|------|
| `collection` | 必填，要遍历的集合/数组名 |
| `item` | 每次迭代的元素变量名 |
| `index` | 当前索引（从0开始）|
| `open` | 开头字符串，如 `(` |
| `close` | 结尾字符串，如 `)` |
| `separator` | 元素之间的分隔符，如 `,` |

- **`itif`** 不是任何合法属性名，纯干扰项
- `collection` 是必填属性，`index` 和 `separator` 都是可选合法属性

**状态**：✅ 答对

---

### 题目：执行以下 Redis 命令后，`SCARD cd` 输出是什么？

```
SADD ca "php" "java" "go" "c" "ruby" "julia"
SMOVE ca cb "julia"
SADD cb "ruby"
SDIFFSTORE cc ca cb
SMOVE cc ca "php"
SREM ca "go" "ruby" "julia"
SUNIONSTORE cd ca cc
SCARD cd
```

**选项：**
1. `4` ✅ (正确答案)
2. `3`
3. `6` ❌ (平台标注的答案，有误)
4. `2`

**我的答案：** `6` ❌（按平台错误答案选的）
**正确答案：** `4` ✅

**逐步推导：**

| 步骤 | 命令 | ca | cb | cc | cd |
|------|------|:--:|:--:|:--:|:--:|
| ① | `SADD ca 6个元素` | {php,java,go,c,ruby,julia} | ∅ | ∅ | ∅ |
| ② | `SMOVE ca→cb julia` | {php,java,go,c,ruby} | {julia} | ∅ | ∅ |
| ③ | `SADD cb ruby` | {php,java,go,c,ruby} | {julia,ruby} | ∅ | ∅ |
| ④ | `SDIFFSTORE cc ca⊖cb` | {php,java,go,c,ruby} | {julia,ruby} | **{php,java,go,c}** | ∅ |
| ⑤ | `SMOVE cc→ca php` | {php,java,go,c,ruby} | {julia,ruby} | {java,go,c} | ∅ |
| ⑥ | `SREM ca go ruby julia` | **{php,java,c}** | {julia,ruby} | {java,go,c} | ∅ |
| ⑦ | `SUNIONSTORE cd ca∪cc` | {php,java,c} | {julia,ruby} | {java,go,c} | **{php,java,c,go}** |
| ⑧ | `SCARD cd` | | | | **→ 4** |

**关键细节：**
- 步骤⑥：`julia` 早已不在 ca 中（步骤②已移到 cb），所以只移除了 `go` 和 `ruby`，ca 剩下 {php, java, c}
- 步骤⑦：并集 {php, java, c} ∪ {java, go, c} = {php, java, c, go}，共 4 个元素
- ⚠️ 该题平台本身的答案标注有误（平台标 6），实际正确答案为 4

**状态**：❌ 答错（平台答案错误导致）

---

### 题目：关于以下 MyBatis 动态 SQL，说法不正确的是？

```xml
<select id="selectByUser" parameterType="com.tgb.mybatis.entity.SysUser"
        resultType="com.tgb.mybaits.entity.Sysuser">
  select
    user_name "userName",
    user_password "userPassword",
    user_info "userInfo",
    head_img "headImg",
    create_time "createTime"
  from sys_user
  where 1=1
  <if test="userName != null and userName != ''">
    and user_name like CONCAT('%',#{userName},'%')
  </if>
  <if test="userEmail != null and userEmail != ''">
    and user_email = #{userEmail}
  </if>
</select>
```

**选项：**
1. `where 1=1` 防止所有 if 都不满足时 SQL 拼接出错 ✅
2. `property != null` 适用于任意字段类型 ✅
3. `property != null`（不判空串）只适用于 int 类型，不能检查空串 ❌（**不正确的说法**）
4. 用 `and`/`or` 连接多个 SQL 条件 ✅

**我的答案：** 选项3 ✅
**正确答案：** 选项3

**解析：**
- 选项③的错误在于：`int` 是基本类型，**永远不可能为 null**，判 `!= null` 永远返回 true，反而会出问题
- 反而是 `Integer`（包装类型）可以判 null，适用于所有引用类型
- 所以这句话**前后说反了**——判 null 适合引用类型，对 `int` 基本类型无意义

**状态**：✅ 答对

---

### 题目：MyBatis `<bind>` 模糊搜索 — 选出正确的填充代码

**场景：** 社交平台用户查询，按 `searchKey`（用户名模糊匹配）和 `status`（可选）筛选。

```xml
<mapper namespace="com.example.mapper.UserMapper">
  <select id="searchUsers" parameterType="java.util.Map" resultType="com.example.model.User">
    select id, username, status, created_at
    from t_user
    <where>
      /* 代码缺失 */
    </where>
  </select>
</mapper>
```

**选项：**

**A.** ✅
```xml
<bind name="pattern" value="'%' + searchKey + '%'"/>
<if test="searchKey != null and searchKey != ''">
    AND username LIKE #{pattern}
</if>
<if test="status != null and status != ''">
    AND status = #{status}
</if>
```

**B.** ❌
```xml
<bind name="pattern" value="searchKey" />
<if test="pattern != ''">
    OR username LIKE #{searchKey}
</if>
<if test="status != ''">
    AND status = #{status}
</if>
```

**C.** ❌
```xml
<bind name="searchKey" value="'%' + searchKey + '%'"/>
<if test="searchKey == null">
    AND username LIKE #{searchKey}
</if>
<if test="status != null">
    OR status = #{status}
</if>
```

**D.** ❌
```xml
<if test="searchKey != null">
    username = CONCAT('%', #{searchKey}, '%'}
</if>
<if test="status != ''">
    status = #{status}
</if>
<bind name="extra" value="'unused'" />
```

**我的答案：** 选项A ✅
**正确答案：** 选项A

**解析：**

| 选项 | 问题 |
|:----:|------|
| **A** ✅ | `<bind>` 用 `'%' + searchKey + '%'` 拼装通配符；判空完整（null + 空串）；`#{pattern}` 引用绑定变量；`<where>` 自动处理 AND |
| **B** ❌ | `<bind value="searchKey">` 没加 `%` 通配符；`OR` 拼接在 `<where>` 内错误；没判 null 可能 NPE；`#{searchKey}` 未引用绑定变量 |
| **C** ❌ | `<bind name="searchKey">` 覆盖了原参数（命名冲突）；`test="searchKey == null"` 条件写反了；`OR` 错误 |
| **D** ❌ | 完全没用 `<bind>`（题目明确要求用）；`username = CONCAT...` 用 `=` 不是 `LIKE`；花括号 `'%'}` 写反成 `}` 应为 `)` |

**状态**：✅ 答对