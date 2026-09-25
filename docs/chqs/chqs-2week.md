# 第二周 - 刷题记录

---

## 2026-07-14

---

### Q1 — HyperLogLog 基数估算准确率影响因素

**来源:** 每日一练 App

**题目:** 影响 HyperLogLog 基数估算准确率的因素？

**选项:**
1. 集合中元素的 Hash 函数
2. 集合中元素的数据类型
3. HyperLogLog 的空间大小 ✅
4. 集合中元素的数据量

**我的答案:** 集合中元素的数据类型 ❌
**正确答案:** HyperLogLog 的空间大小

**解析:**
- HyperLogLog 的误差率取决于寄存器数量，而寄存器数量由它占用的空间大小决定
- 标准 Redis HyperLogLog 占用 12KB（2^14 个寄存器），误差率约 0.81%
- 空间越大 → 寄存器越多 → 精度越高，这是唯一影响因素
- 元素数据类型、Hash 函数、数据量均不影响固定误差率
- HLL 的核心特性是"用空间换精度"，与数据本身无关

---

### Q2 — HyperLogLog 统计每天访问量

**来源:** 每日一练 App

**题目:** 如何使用 HyperLogLog 统计电商网站每天访问量？

**选项:**
1. 按页面分别创建 HyperLogLog，分别统计各页面访问量
2. 每天创建一个 HyperLogLog，使用 PFADD 记录访问者 IP，用 PFCOUNT 统计各天访问量
3. 每日创建一个 HyperLogLog，用 PFADD 记录访问者 IP，用 PFMERGE 合并所有天数的数据，再用 PFCOUNT 统计总访问量
4. 按每天创建一个 HyperLogLog，使用 PFADD 命令记录访问者的 IP 地址，使用 PFCOUNT 命令统计每天的访问量 ✅

**我的答案:** 选项3 ❌
**正确答案:** 选项4（按每天创建一个 HyperLogLog，使用 PFADD 命令记录访问者的 IP 地址，使用 PFCOUNT 命令统计每天的访问量）

**解析:**
- 题目问的是“**每天**的访问量（当日 UV）”——直接**每天创建一个 HyperLogLog**，用 `PFADD` 加入访客 IP，当天用 `PFCOUNT` 就能得到该日 UV
- 选项3 里用 PFMERGE 合并所有天数再统计，得到的是“**多日合计的总 UV**”，回答的不是“每天访问量”，属于答非所问
- 陷阱：PFMERGE 是另一道场景题（统计累计总 UV 时去重合并）的正确工具，别在这题里干扰你
- 关键考点：PFADD 记录 → PFCOUNT 单日统计；PFMERGE 只在“合并多日去重总数”场景下用

---

### Q3 — Redis 查看状态信息的命令

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 查看Redis使用情况及状态信息的命令是？

**选项:**
1. `info` ✅
2. `informa`
3. `get`
4. `set`

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- 看Redis使用情况及状态信息用info

---
### Q4 — Redis 命令执行过程分析

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Git 生成 ssh 公钥的正确步骤是？

**选项:**
1. 1. 下载git 
    2. 安装
    3. 点击Git Base Here   输入`cd ~`
    4. 创建文件夹`mkdir ~/.ssh`
    5. 进入文件夹`cd .ssh`
    6. 配置全局的name和email
    7. 生成key
    8. 回车
2. 1. 下载git 
    2. 安装
    3. 配置全局的name和email
    4. 创建文件夹`mkdir ~/.ssh`
    5. 进入文件夹`cd .ssh`
    6. 点击Git Base Here   输入`cd ~`
    7. 生成key
    8. 回车
3. 1. 下载git 
    2. 安装
    3. 配置全局的name和email
    4. 点击Git Base Here   输入`cd ~`
    5. 创建文件夹`mkdir ~/.ssh`
    6. 进入文件夹`cd .ssh`
    7. 生成key
    8. 回车 ✅
4. 1. 下载git 
    2. 安装
    3. 创建文件夹`mkdir ~/.ssh`
    4. 点击Git Base Here   输入`cd ~`
    5. 进入文件夹`cd .ssh`
    6. 配置全局的name和email
    7. 生成key
    8. 回车

**我的答案:** 选项1 ❌

**正确答案:** 选项3

**解析:**
- 1. **下载并安装 Git**：首先需要获取 Git 工具并完成安装，这是基础环境准备。
- 2. **配置全局用户信息**：通过`git config --global user.name`和`user.email`设置用户名和邮箱，这是 Git 提交代码时的身份标识，应在生成密钥前配置。
- 3. **打开 Git 命令行并进入用户目录**：点击`Git Bash Here`打开命令行，输入`cd ~`进入当前用户的家目录（后续操作均基于此目录）。
- 4. **创建.ssh 目录**：在用户家目录下创建`.ssh`文件夹（`mkdir ~/.ssh`），用于存储密钥文件。
- 5. **进入.ssh 目录**：通过`cd .ssh`进入该目录，后续生成的密钥将保存在这里。
- 6. **生成密钥**：执行`ssh-keygen -t rsa -C "邮箱地址"`生成 ssh 密钥，按回车完成操作。

---
### Q5 — Tomcat Coyote 网络协议

**来源:** 每日一练 App

**题目:** Apache Tomcat 的 Coyote 组件支持哪些主要网络协议？

**选项:**
1. 仅支持 HTTP/1.1
2. 支持 HTTP/1.1 和 HTTPS，不支持 AJP ❌
3. 支持 HTTP/1.1、HTTPS 和 AJP ✅
4. 仅支持 AJP 和 WebSocket

**我的答案:** 选项2 ❌
**正确答案:** 选项3

**解析:**
- Apache Tomcat 的 **Coyote 连接器**（Connector）支持的协议：
  | 协议 | 说明 |
  |------|------|
  | **HTTP/1.1** | 直接处理 HTTP 请求（默认 8080 端口） |
  | **HTTPS** | HTTP over SSL/TLS，加密通信（默认 8443） |
  | **AJP** | Apache JServ Protocol，用于与 Apache HTTPD / Nginx 反向代理通信（默认 8009） |
- **核心误解：** AJP 不是过时的协议，而是 **Tomcat 连接器架构的一部分**，专门用于前端 Web 服务器（Apache HTTPD）和后端 Tomcat 之间的二进制通信。它比 HTTP 代理效率更高（二进制协议、少解析开销）
- **Coyote 的角色：**
  - Coyote 是 Tomcat 的连接器组件，负责监听端口、接收请求、解析协议、转发给 Servlet 引擎
  - 它支持 HTTP/1.1、HTTPS、AJP 三种协议的连接器实现
  - 在 `server.xml` 中以 `<Connector>` 元素配置，通过 `protocol` 属性指定
- **干扰项分析：**
  - 选项1：HTTP/1.1 只是其中之一，Coyote 还支持 HTTPS 和 AJP
  - 选项2：错误的是"不支持 AJP"，AJP 是 Coyote 原生支持的
  - 选项4：WebSocket 是后来通过 NIO 连接器支持的，并非 Coyote 的主要/原生协议

---

## 2026-07-17

---

### Q6 — Tomcat DefaultServlet 职责

**来源:** 每日一练 App

**题目:** `DefaultServlet` 主要负责处理什么？

**选项:**
1. 处理静态资源（HTML、CSS、JS、图片等） ✅
2. 处理动态请求（JSP、Servlet）
3. 拦截 404 请求
4. 分发请求到 Servlet

**我的答案:** 未记录（原记录状态：❌ 答错）
**正确答案:** 处理静态资源（HTML、CSS、JS、图片等）

**解析:**
- `DefaultServlet` 是 Tomcat 内置的默认 Servlet，职责是**处理静态资源**的请求
- 动态请求（JSP/Servlet）分别由 `JspServlet` 和普通 Servlet 处理
- 拦截 404 和分发请求都不是它的主要职责

---

### Q7 — Tomcat Connector 与 Container 交互

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Tomcat Connector 和 Container 交互的方式是什么？

**选项:**
1. Connector 直接将请求交给 Context 处理
2. Connector 通过 Engine 传递请求给 Host
3. Connector 解析 HTTP 请求后，将其交给 Engine 处理 ✅
4. Container 解析请求后，传递给 Connector 进行处理

**我的答案:** 选项3 ✅

**正确答案:** 选项3

**解析:**
- 正确答案：
- Connector 解析 HTTP 请求后，将其交给 Engine 处理
- 理由：
- Connector 解析 HTTP 请求后，将其封装成 Request，然后交给 Engine，Engine 进一步分发给 Host -> Context -> Servlet。
- 错误答案：
- - Connector 直接将请求交给 Context 处理：请求需要经过 Engine 和 Host 处理后才会到达 Context。
- - Connector 通过 Engine 传递请求给 Host：不准确，Engine 只是管理 Host，真正分发请求的还是 Connector。
- - Container 解析请求后，传递给 Connector 进行处理：Container 负责请求分发，不负责解析 HTTP 请求。

---
### Q8 — Tomcat 顶层容器

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 取出 client 分支，找出处于 client 分支和 server 分支的共同祖先之后的修改，然后把它们在 master 分支上重放一遍。以下哪个命令能达到上述效果？

**选项:**
1. git rebase --onto master server client ✅
2. git rebase --onto server client master
3. git rebase --onto master client server
4. git rebase --onto client server master

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- onto参数后面是新的基

---
### Q9 — Tomcat ProtocolHandler 组件

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** `ProtocolHandler`包含了三个非常重要的组件，下列选项中错误的是？

**选项:**
1. `Endpoint`
2. `Processor`
3. `Adapter`
4. `Request` ✅

**我的答案:** （当时未记录具体选项，原记录标 ❌ 答错）

**正确答案:** 选项4

**解析:**
- ProtocolHandler包含了三个非常重要的组件：Endpoint、Processor、Adapter

---
### Q10 — MyBatis Resources 加载配置

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 在构建SqlSessionFactory时，如果配置文件mybatis-config.xml位于类路径下，使用Resources类加载该文件的正确方式是？

**选项:**
1. ```
    Resources.getResourceAsStream("mybatis-config.xml")
    ``` ✅
2. ```
    Resources.loadResource("mybatis-config.xml")
    ```
3. ```
    Resources.openResource("mybatis-config.xml")
    ```
4. ```
    Resources.readResource("mybatis-config.xml")
    ```

**我的答案:** 选项2 ❌

**正确答案:** 选项1

**解析:**
- Resources类是 MyBatis 提供的用于加载类路径下资源的工具类，getResourceAsStream()方法可以将类路径下的资源文件以输入流的形式返回，这在构建SqlSessionFactory时，方便将配置文件传递给SqlSessionFactoryBuilder的build()方法。loadResource、openResource和readResource都不是Resources类中用于获取类路径下资源文件输入流的正确方法。

---
### Q11 — MyBatis foreach 标签属性

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** `<foreach>`标签不包含以下哪个属性?

**选项:**
1. collection
2. itif ✅
3. index
4. separator

**我的答案:** 选项2 ✅

**正确答案:** 选项2

**解析:**
- itif 不是 MyBatis 的标签或属性

---
### Q12 — Redis SCARD 集合运算

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Git 生成 ssh 公钥的正确步骤是？

**选项:**
1. 1. 下载git 
    2. 安装
    3. 点击Git Base Here   输入`cd ~`
    4. 创建文件夹`mkdir ~/.ssh`
    5. 进入文件夹`cd .ssh`
    6. 配置全局的name和email
    7. 生成key
    8. 回车
2. 1. 下载git 
    2. 安装
    3. 配置全局的name和email
    4. 创建文件夹`mkdir ~/.ssh`
    5. 进入文件夹`cd .ssh`
    6. 点击Git Base Here   输入`cd ~`
    7. 生成key
    8. 回车
3. 1. 下载git 
    2. 安装
    3. 配置全局的name和email
    4. 点击Git Base Here   输入`cd ~`
    5. 创建文件夹`mkdir ~/.ssh`
    6. 进入文件夹`cd .ssh`
    7. 生成key
    8. 回车 ✅
4. 1. 下载git 
    2. 安装
    3. 创建文件夹`mkdir ~/.ssh`
    4. 点击Git Base Here   输入`cd ~`
    5. 进入文件夹`cd .ssh`
    6. 配置全局的name和email
    7. 生成key
    8. 回车

**我的答案:** 选项1 ❌

**正确答案:** 选项3

**解析:**
- 1. **下载并安装 Git**：首先需要获取 Git 工具并完成安装，这是基础环境准备。
- 2. **配置全局用户信息**：通过`git config --global user.name`和`user.email`设置用户名和邮箱，这是 Git 提交代码时的身份标识，应在生成密钥前配置。
- 3. **打开 Git 命令行并进入用户目录**：点击`Git Bash Here`打开命令行，输入`cd ~`进入当前用户的家目录（后续操作均基于此目录）。
- 4. **创建.ssh 目录**：在用户家目录下创建`.ssh`文件夹（`mkdir ~/.ssh`），用于存储密钥文件。
- 5. **进入.ssh 目录**：通过`cd .ssh`进入该目录，后续生成的密钥将保存在这里。
- 6. **生成密钥**：执行`ssh-keygen -t rsa -C "邮箱地址"`生成 ssh 密钥，按回车完成操作。

---
### Q13 — MyBatis 动态 SQL 说法

**来源:** 每日一练 App

**题目:** 关于以下 MyBatis 动态 SQL，说法不正确的是？

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

**选项:**
1. `where 1=1` 防止所有 if 都不满足时 SQL 拼接出错 ✅
2. `property != null` 适用于任意字段类型 ✅
3. `property != null`（不判空串）只适用于 int 类型，不能检查空串 ❌（**不正确的说法**）
4. 用 `and`/`or` 连接多个 SQL 条件 ✅

**我的答案:** 选项3 ✅
**正确答案:** 选项3

**解析:**
- 选项③的错误在于：`int` 是基本类型，**永远不可能为 null**，判 `!= null` 永远返回 true，反而会出问题
- 反而是 `Integer`（包装类型）可以判 null，适用于所有引用类型
- 所以这句话**前后说反了**——判 null 适合引用类型，对 `int` 基本类型无意义

---

### Q14 — MyBatis bind 模糊搜索

**来源:** 每日一练 App

**题目:** MyBatis `<bind>` 模糊搜索 — 选出正确的填充代码

**场景:** 社交平台用户查询，按 `searchKey`（用户名模糊匹配）和 `status`（可选）筛选。

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

**选项:**
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

**我的答案:** 选项A ✅
**正确答案:** 选项A

**解析:**
- | 选项 | 问题 |
  |:----:|------|
  | **A** ✅ | `<bind>` 用 `'%' + searchKey + '%'` 拼装通配符；判空完整（null + 空串）；`#{pattern}` 引用绑定变量；`<where>` 自动处理 AND |
  | **B** ❌ | `<bind value="searchKey">` 没加 `%` 通配符；`OR` 拼接在 `<where>` 内错误；没判 null 可能 NPE；`#{searchKey}` 未引用绑定变量 |
  | **C** ❌ | `<bind name="searchKey">` 覆盖了原参数（命名冲突）；`test="searchKey == null"` 条件写反了；`OR` 错误 |
  | **D** ❌ | 完全没用 `<bind>`（题目明确要求用）；`username = CONCAT...` 用 `=` 不是 `LIKE`；花括号 `'%'}` 写反，`}` 应为 `)` |

---

### Q15 — MyBatis choose 多条件筛选

**来源:** 每日一练 App

**题目:** `<choose>` 实现多条件筛选的问题

**场景:** 根据用户名、邮箱、手机号三者至少一个参数筛选用户。

**原代码：**
```xml
<select id="selectUsers">
    SELECT * FROM users WHERE
    <choose>
        <when test="username != null">username = #{username}</when>
        <when test="email != null">email = #{email}</when>
        <when test="phone != null">phone = #{phone}</when>
        <otherwise>1=1</otherwise>
    </choose>
</select>
```

**问题：** 同时传入 `username` 和 `email` 时，仅根据 `username` 筛选，email 条件被忽略。

**原因：** `<choose>` 相当于 Java 的 `switch-case`，只执行第一个匹配的 `<when>`，不继续匹配后续条件。

**正确修改：** 改用 `<if>`，每个条件独立判断：
```xml
<select id="selectUsers">
    SELECT * FROM users
    <where>
        <if test="username != null">and username = #{username}</if>
        <if test="email != null">and email = #{email}</if>
        <if test="phone != null">and phone = #{phone}</if>
    </where>
</select>
```

**我的答案:** 未记录具体选项（原记录状态：✅ 答对）
**正确答案:** 改用 `<if>` 实现多条件组合查询

**解析:**
- `<choose>/<when>` 用于 **多选一** 场景（如按不同字段排序）
- `<if>` 用于 **多选多** 场景（多条件组合查询）
- 题目要求"至少一个参数"，传几个筛几个，必须用 `<if>`

---

## 📊 第二周错题汇总

| 日期 | 题数 | 答对 | 答错 |
|:----:|:----:|:----:|:----:|
| 07-14 | 5 | 1 | 4 |
| 07-17 | 5 | 2 | 3 |
| 07-19 | 5 | 4 | 1 |
| **合计** | **15** | **7** | **8** |
