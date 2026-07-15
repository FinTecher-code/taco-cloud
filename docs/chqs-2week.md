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
2. 每天创建一个 HyperLogLog，使用 PFADD 记录访问者 IP，用 PFCOUNT 统计各天访问量 ❌
3. 每日创建一个 HyperLogLog，用 PFADD 记录访问者 IP，用 PFMERGE 合并所有天数的数据，再用 PFCOUNT 统计总访问量 ✅

**我的答案：** 选项2 ❌
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