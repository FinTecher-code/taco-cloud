# 📖 Java 第二周复习笔记

> Apache Tomcat · MyBatis · Redis

---

## 📑 目录

- [一、Tomcat 核心架构](#一tomcat-核心架构)
- [二、MyBatis 基础](#二mybatis-基础)
- [三、Redis 入门与实战](#三redis-入门与实战)

---

## 一、Tomcat 核心架构

### 🏗️ 整体架构分层

```
Server
 └── Service
      ├── Connector（Coyote） ← 负责网络通信
      └── Container（Catalina） ← 负责请求处理
           └── Engine
                └── Host
                     └── Context
                          └── Wrapper
```

| 组件 | 角色 | 数量 |
|:----|:-----|:-----|
| **Server** | 代表整个 Tomcat 实例，最顶层组件 | 1 |
| **Service** | 一组 Connector + 一个 Engine 的集合 | 多个 |
| **Connector** | 监听端口、解析协议（HTTP/AJP/HTTPS） | 多个 |
| **Engine** | Container 顶层，处理所有请求 | 1 个/Service |
| **Host** | 虚拟主机，匹配域名 | 多个 |
| **Context** | 一个 Web 应用 | 多个 |
| **Wrapper** | 一个 Servlet | 多个 |

> **关键理解**：Server 是整个 Tomcat 实例的顶层，Engine 只是 Container 的顶层，两者不是一个层级。

---

### 🔌 Coyote 连接器

Coyote 是 Tomcat 的连接器组件，负责**网络通信层**。

**支持的协议：**

| 协议 | 端口 | 用途 |
|:-----|:-----|:------|
| **HTTP/1.1** | 8080 | 直接处理 HTTP 请求 |
| **HTTPS** | 8443 | 加密通信 |
| **AJP** | 8009 | 与 Apache/Nginx 反向代理通信（二进制，效率更高） |

> AJP 不是过时协议，而是专为前端 Web 服务器 ↔ Tomcat 通信设计的二进制协议，比 HTTP 代理少解析开销。

---

### 🔄 Connector 与 Container 的交互

```
① Connector 监听端口
② 收到 HTTP 请求
③ Connector 解析请求 → 封装为 Request/Response 对象
④ 交给 Engine（Container 入口）
⑤ Engine → Host → Context → Wrapper 逐级分发
⑥ Servlet 处理完 → 响应原路返回
```

**核心流程：**
- Connector 只负责**解析**，不做业务处理
- Container 只负责**处理**，不接触网络
- Connector 不能直接交给 Context，必须经过 Engine

---

### 🧩 DefaultServlet 职责

```
DefaultServlet = Tomcat 内置默认 Servlet
├── 负责：处理静态资源（HTML、CSS、JS、图片等）
├── 不负责：JSP（那是 JspServlet 的事）
├── 不负责：动态请求（那是普通 Servlet 的事）
└── 不负责：拦截 404、分发请求
```

---

### ⚙️ ProtocolHandler 三个核心组件

ProtocolHandler 是 Connector 内部的核心处理器：

| 组件 | 职责 |
|:-----|:------|
| **Endpoint** | 处理底层网络 I/O（Socket 连接、线程管理） |
| **Processor** | 解析 HTTP 请求报文 |
| **Adapter** | 将解析后的请求适配给 Container 处理 |

```
请求到达 → Endpoint（网络 I/O）
         → Processor（解析报文）
         → Adapter（适配给 Container）
         → Engine 开始处理
```

> Request 是 Processor 解析后**产生的对象**，不是 ProtocolHandler 本身的组件。

---

## 二、MyBatis 基础

### 📝 MyBatis 是什么

一个**持久层框架**，封装了 JDBC，让开发者用 SQL 操作数据库，省去手动写 `Connection`、`PreparedStatement`、`ResultSet` 的重复劳动。

```
传统 JDBC：写 SQL → 建连接 → 设参数 → 执行 → 处理结果 → 关连接    😩
MyBatis：  写 SQL → 执行 → 拿结果                                    😊
```

---

### 🏁 快速入门

**1. 引入依赖（Maven）**
```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.x</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.x</version>
</dependency>
```

**2. 全局配置文件 `mybatis-config.xml`**
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/db"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="UserMapper.xml"/>
    </mappers>
</configuration>
```

**3. 使用 Resources 加载配置文件**
```java
// ✅ 正确方法
InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);

// ❌ 错误：没有 loadResource / openResource / readResource 这些方法
```

---

### 🗺️ 映射文件（Mapper XML）

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.UserMapper">

    <!-- 查询 -->
    <select id="selectUser" resultType="User">
        SELECT * FROM user WHERE id = #{id}
    </select>

    <!-- 插入 -->
    <insert id="insertUser">
        INSERT INTO user(name, age) VALUES(#{name}, #{age})
    </insert>

    <!-- 更新 -->
    <update id="updateUser">
        UPDATE user SET name = #{name} WHERE id = #{id}
    </update>

    <!-- 删除 -->
    <delete id="deleteUser">
        DELETE FROM user WHERE id = #{id}
    </delete>

</mapper>
```

---

### 🔧 核心 API

| API | 作用 |
|:----|:-----|
| `Resources` | 从 classpath 加载资源文件 |
| `SqlSessionFactoryBuilder` | 解析 XML 构建工厂 |
| `SqlSessionFactory` | 生产 SqlSession（线程安全，单例） |
| `SqlSession` | 代表一次数据库会话（非线程安全） |

> `SqlSessionFactory` 全局只构建一次，`SqlSession` 每次用完要关闭。

---

### ⚡ MyBatis vs JDBC

| 对比 | JDBC | MyBatis |
|:----|:-----|:--------|
| 代码量 | 多（模板代码重复） | 少（框架封装） |
| SQL 管理 | 散落在 Java 代码中 | 集中在 XML 或注解中 |
| 参数设置 | 手动 `setXxx` | 自动映射 `#{xxx}` |
| 结果映射 | 手动 `getXxx` | 自动映射到 POJO |
| 连接管理 | 手动打开/关闭 | 框架管理 |

---

## 三、Redis 入门与实战

### 🚀 Redis 是什么

**Remote Dictionary Server** — 内存中的键值数据库。

> 数据存在内存里 → 极快（微秒级）
> 支持多种数据结构 → String / Hash / List / Set / Sorted Set / HyperLogLog
> 常用场景：缓存、会话、计数器、排行榜、UV 统计

---

### 📋 基本数据结构

| 类型 | 示例 | 特点 | 场景 |
|:----|:-----|:-----|:-----|
| **String** | `set name "Tom"` | 最基础，存字符串/数字 | 缓存、计数器 |
| **Hash** | `hset user:1 name "Tom"` | 类似 Java 的 `Map` | 对象缓存 |
| **List** | `lpush list a b c` | 有序可重复 | 消息队列、最新消息 |
| **Set** | `sadd set a b c` | 无序不可重复 | 标签、去重 |
| **Sorted Set** | `zadd rank 100 "Tom"` | 带权重的有序集合 | 排行榜 |
| **HyperLogLog** | `pfadd uv user1` | 基数统计，固定 12KB | UV 统计 |

---

### 🔑 核心命令速查

**通用：**
```bash
INFO                  # 查看服务器状态信息
  INFO server        # 服务器信息
  INFO memory        # 内存使用
  INFO stats         # 统计信息
```

**String：**
```bash
SET key value        # 设值
GET key              # 取值
INCR key             # 自增 1
EXPIRE key seconds   # 设置过期时间
```

**Set：**
```bash
SADD key member      # 添加元素
SREM key member      # 移除元素
SCARD key            # 获取元素数量
SMEMBERS key         # 获取所有元素
SISMEMBER key member # 判断是否存在
SDIFF key1 key2      # 差集
SINTER key1 key2     # 交集
SUNION key1 key2     # 并集
SDIFFSTORE dest k1 k2  # 差集存到新 key
SMOVE src dst member   # 移动元素（必须在源集合中才成功）
```

---

### 🧮 HyperLogLog 详解

**特点：**
- 用来做**基数统计**（统计不重复元素的数量）
- **固定占用 12KB**，无论存了多少数据
- 标准误差 **≈ 0.81%**
- 不能反查具体元素（不是集合）

**三个命令：**
```bash
PFADD key element            # 添加元素
PFCOUNT key                  # 统计基数
PFMERGE dest key1 key2 ...   # 合并多个 HyperLogLog
```

**影响因素：**
```
影响 HLL 精度？    不影响
-----------------------------------
✔ 空间大小（寄存器数量）   ✘ 元素数量
                        ✘ 数据类型
                        ✘ Hash 函数
```
> HLL 的误差率完全由寄存器数量（即空间大小）决定，与数据本身无关。

---

### 📊 实战：统计网站 UV

```bash
# 第 1 天
PFADD uv:2026-07-14 "192.168.1.1" "192.168.1.2" "192.168.1.3"

# 第 2 天
PFADD uv:2026-07-15 "192.168.1.1" "192.168.1.4" "192.168.1.5"

# 查看单日 UV
PFCOUNT uv:2026-07-14      # → 3
PFCOUNT uv:2026-07-15      # → 3

# 查看总 UV（合并后去重）
PFMERGE uv:total uv:2026-07-14 uv:2026-07-15
PFCOUNT uv:total           # → 5（不是 3+3=6）
```

> ⚠️ 不能直接用 PFCOUNT 相加，因为有**重复访客**。必须用 PFMERGE 合并后再统计。

---

### 🧪 Set 命令执行推导题

以一道经典题为例，逐步推导集合状态变化：

```bash
1  SADD        user      "child"    "student"    "worker"     # user = {child, student, worker}
2  SADD        person    "worker"   "farmer"     "child"      # person = {worker, farmer, child}
3  SREM        person                "child"                  # person = {worker, farmer}
4  SDIFFSTORE  diff      user        person                    # diff = user - person = {child, student}
5  SMOVE       diff      person      "farmer"                  # farmer 不在 diff 中 → 返回 0（失败）
6  SCARD       person                                           # person 有 2 个元素 → 返回 2
```

> **要点**：一步步推，不要跳步。SMOVE 失败不推荐重试，先检查元素是否在源集合中。

---

### 🆚 Redis 经典面试问题

**1. Redis 为什么快？**
- 纯内存操作，微秒级响应
- 单线程模型（避免上下文切换和锁竞争）
- I/O 多路复用（epoll）
- 数据结构简单高效

**2. Redis 持久化方式？**
| 方式 | 原理 | 优点 | 缺点 |
|:----|:-----|:-----|:-----|
| **RDB** | 定期生成快照 | 文件小，恢复快 | 可能丢数据 |
| **AOF** | 记录每条写命令 | 数据更安全 | 文件大，恢复慢 |

**3. 缓存穿透 / 击穿 / 雪崩？**

| 问题 | 现象 | 解决 |
|:----|:-----|:-----|
| **穿透** | 查不存在的数据，穿过缓存打 DB | 布隆过滤器 / 缓存空值 |
| **击穿** | 热点 key 过期，同时大量请求 | 互斥锁 / 逻辑过期 |
| **雪崩** | 大量 key 同时过期 | 过期时间加随机值 / 多级缓存 |

---

### 🔔 INFO 命令常用

```bash
redis> INFO
# Server
redis_version:7.0.0
redis_mode:standalone
os:Linux

# Memory
used_memory_human:1.5M
maxmemory_human:0

# Stats
total_connections_received:10
total_commands_processed:50
keyspace_hits:100
keyspace_misses:5    # 命中率 ≈ 95%

# Keyspace
db0:keys=5,expires=2
```

> 看命中率：`keyspace_hits / (keyspace_hits + keyspace_misses)`

---

### 🎯 选型总结

| 需求 | 用哪个 |
|:----|:-------|
| 缓存热点数据 | `String` |
| 统计页面 UV | `HyperLogLog` |
| 排行榜 | `Sorted Set` |
| 消息队列 | `List` |
| 去重 / 标签 | `Set` |
| 对象存储 | `Hash` |
| 查看服务器状态 | `INFO` |

---

> 📅 复习日期：2026-07-17
