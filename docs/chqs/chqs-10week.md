# 第十周 - 刷题记录

---

## 2026-09-15

---

### Q1 — 线程安全的容器

**来源:** 每日一练 App（岛屿主页在线答题）
**分类:** 数据安全 / Java 集合

**题目:** 下列哪个是线程安全的容器？

**选项:**
1. java.util.Vector
2. java.util.ArrayList
3. java.util.LinkedList
4. java.util.HashSet

**我的答案:** 选项4（HashSet）❌
**正确答案:** 选项1（Vector）✅

**解析:**
- **Vector** 的方法内部使用 `synchronized` 关键字，是**线程安全**的容器
- ArrayList、LinkedList、HashSet 均为**非线程安全**容器
- 记忆点：
  - 线程安全容器（面试常考）：`Vector`（古老，全方法加锁）、`HashTable`（对应 HashMap 的线程安全版）、`ConcurrentHashMap`（分段锁/乐观锁，性能好）、`CopyOnWriteArrayList`、`Collections.synchronizedXxx()` 包装
  - HashMap vs HashTable vs ConcurrentHashMap 的区别必须会答：HashMap 非线程安全；HashTable 全表加锁；ConcurrentHashMap 锁粒度更细（JDK1.8 CAS + synchronized 锁桶）

### Q2 — 基于 synchronized 保证线程安全的容器

**来源:** 每日一练 App（岛屿主页在线答题）
**分类:** 数据安全 / Java 集合

**题目:** 下列哪个容器使用 synchronized 关键字保证线程安全？

**选项:**
1. CopyOnWriteArrayList
2. HashTable
3. ConcurrentHashMap
4. CopyOnWriteArraySet

**我的答案:** 选项3（ConcurrentHashMap）❌
**正确答案:** 选项2（HashTable）✅

**解析:**
- **HashTable** 基于 `synchronized`（方法级全表锁）保证线程安全——本题考点
- CopyOnWriteArrayList/CopyOnWriteArraySet：基于**写时复制**（COW），读不加锁、写时复制新数组
- ConcurrentHashMap：基于 **ReentrantLock + CAS + 分段思想**（JDK1.7 分段锁 Segment 继承 ReentrantLock；JDK1.8 改为 CAS + synchronized 锁桶）
- 记忆点：
  - 线程安全容器三兄弟的锁机制：**HashTable = synchronized 全表锁**｜**ConcurrentHashMap = 分段锁/CAS**｜**CopyOnWrite 系列 = 写时复制**
  - 区分“用 synchronized 的”和“不用 synchronized 的”：Vector/HashTable 是 synchronized；ConcurrentHashMap 早期靠分段锁（Segment 本身是 ReentrantLock），JDK1.8 用 CAS+synchronized，都不是简单的方法级锁

### Q3 — 对称密码算法的适用性（加密）

**来源:** 每日一练 App（岛屿主页在线答题）
**分类:** 数据安全 / 密码学

**题目:** 为了保障数据传输和存储安全，需要对一些重要的数据进行加密。由于对称密码算法____，所以特别适合于对数据进行加解密。

**选项:**
1. 比非对称密码算法更安全
2. 比非对称密码算法效率高
3. 比非对称密码算法密钥长度更长
4. 能同时用于数据签名

**我的答案:** 选项1 ❌
**正确答案:** 选项2 ✅

**解析:**
- 对称密码算法（AES、DES）相对非对称（RSA、ECC）的核心优势：**加解密速度快、计算开销小、效率高** → 适合加密大量数据
- 非对称算法虽然安全性不弱（安全性不取决于对称/非对称，而取决于密钥长度和算法强度），但计算复杂度高，通常用于**加密会话密钥**或**数字签名**，不直接加密大量数据
- ✗ 选项1 错：对称并不“比非对称更安全”，安全性取决于算法本身和密钥长度
- ✗ 选项3 错：非对称密钥通常更长（RSA 2048+），对称密钥较短（AES-128/256）
- ✗ 选项4 错：对称加密**不能直接用于数字签名**（签名要用非对称私钥）
- 记忆点：**对称 = 快 → 加密数据本体；非对称 = 慢 → 加密会话密钥/签名**；实际方案常为混合加密（非对称协商密钥 + 对称加密数据）

### Q4 — Session 生命周期描述错误的选项

**来源:** 每日一练 App（岛屿主页在线答题）
**分类:** 数据安全 / Session

**题目:** 关于 session 的生命周期，下面选项中描述错误的是？

**选项:**
1. Session 在用户第一次访问服务器的时候自动创建
2. 只有访问 JSP、Servlet 等程序时才会创建 Session，只访问 HTML、IMAGE 等静态资源并不会创建 Session
3. Session 生成后，只要用户继续访问，服务器就会更新 Session 的最后访问时间，并维护该 Session
4. 为了获得更高的存取速度，服务器一般把 Session 放在内存里，而且所有用户共有一个 Session

**我的答案:** 选项4 ✅
**正确答案:** 选项4 ✅

**解析:**
- 选项4 描述**错误**（题目选错误的）：Session 确实是**每个用户独享一份**（通过 JSESSIONID 区分），不是“所有用户共用一个”；放在内存里是对的（速度快的代价是重启丢失）
- 选项1 正确：首次访问服务器（请求带 cookie/session）时自动创建
- 选项2 正确：只有访问动态资源（JSP/Servlet）才创建 Session，静态资源（HTML/图片）不触发
- 选项3 正确：持续访问会更新最后访问时间（滑动过期），保持 Session 存活
- 记忆点：
  - **Session 是“一人一份”**，靠 Cookie 里的 JSESSIONID 标识（URL 重写是兜底方案）
  - Session 默认放内存 → 快但重启丢失；集群部署要解决 Session 共享（粘性会话 / Redis 集中存储）

### Q5 — SSH 支持的认证方式（不属于的选项）

**来源:** 每日一练 App（岛屿主页在线答题）
**分类:** 数据安全 / SSH

**题目:** SSH 支持多种认证方式，下面哪个选项不属于 SSH 支持的认证方式？

**选项:**
1. 用户密码
2. 公钥认证
3. CA
4. 私钥认证

**我的答案:** 选项2（公钥认证）❌
**正确答案:** 选项4（私钥认证）✅

**解析:**
- SSH 支持的认证方式：**用户密码、公钥认证、CA** 等；可以单独用，也可以多种组合使用
- “私钥认证”不是独立认证方式——私钥**从不传输**，认证时是用**公钥**验证（客户端拿私钥签名，服务器用公钥验签），所以不存在“私钥认证”这种认证方式
- 记忆点：
  - **私钥永不上传/不传输**：公钥放服务器 `~/.ssh/authorized_keys`，私钥留在客户端
  - SSH 常见认证：密码认证、公钥认证（keyboard-interactive）、证书（CA 签名）认证；企业中常用**跳板机 + CA 证书**管理

<!-- 后续题目贴这里，格式同上 -->