# 第三周 - 刷题记录

---

## 2026-07-21

---

### Q1 — Kafka — retries 与 retries.backoff.ms 配置

**来源:** 每日一练 App

**题目:** 关于 Kafka 生产者的 `retries` 和 `retries.backoff.ms` 参数，下列描述**不正确**的是？

**选项:**
1. `retries` 是生产消息的重试次数
2. `retries` 设置重试的时间间隔，单位是毫秒 ✅
3. `retries.backoff.ms` 设置重试的时间间隔，单位是毫秒 ❌
4. `retries` 设置为 0 表示不重试

**我的答案:** 选项3 ❌
**正确答案:** 选项2

**解析:**
这道题的陷阱是把 `retries` 和 `retries.backoff.ms` 的职责搞混了：

| 参数 | 作用 | 默认值 |
|------|------|:------:|
| `retries` | 重试**次数**，不是时间间隔 | `Integer.MAX_VALUE`（新版） |
| `retries.backoff.ms` | 重试之间的**时间间隔**（毫秒） | `100` |

- **选项2** ✅ 错误地把 `retries` 说成是重试间隔，实际上重试间隔由 `retries.backoff.ms` 控制
- **选项3** ❌ 本身描述正确，`retries.backoff.ms` 确实是重试间隔，单位毫秒
- 你选了选项3，可能是因为题目看反了——题目问的是"不正确的"，而选项3的描述本身是对的，应该是选选项2才对

**关键记忆：**
- `retries` → 次数（多少次）
- `retries.backoff.ms` → 间隔（多久一次）
- 记一句口诀：**次数是 retries，间隔找 backoff**

---

### Q2 — Kafka — 增加主题分区

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 某团队正在开发一个实时日志处理系统，使用Kafka作为消息传递的中间件，以下哪个操作可以增加一个主题的分片数量？

**选项:**
1. 删除一个分区
2. 启用压缩
3. 增加副本数
4. 修改分片配置 ✅

**我的答案:** 选项2 ❌

**正确答案:** 选项4

**解析:**
- Kafka中，要增加一个主题的分片数量，需要通过修改分片配置来实现。分片是Kafka中数据的基本存储单位，增加分片数量可以提高系统的并行性和处理能力。通过修改分片配置，可以动态地增加或减少主题的分片数量来满足不同场景下的需求。

---
### Q3 — Kafka — 数据管道概念

**来源:** 每日一练 App

**题目:** Kafka 中的数据管道指的是什么？

**选项:**
1. 不同应用程序之间传输和处理数据的通信机制 ✅
2. 消息的持久化存储机制
3. 集中控制所有生产者和消费者的中心化组件
4. 将消息从一个节点分发到其他节点的机制

**我的答案:** 选项1 ✅
**正确答案:** 选项1

**解析:**
Kafka 数据管道（Data Pipeline）的核心定义是**不同应用之间传输和处理数据的通信机制**：

- Kafka 作为数据管道，核心价值在于**解耦**生产者和消费者
- 支持**多订阅者**模式，一份数据可以被多个消费者独立消费
- 支持**回溯重放**，消费者可以从任意 offset 重新消费
- 不是单纯的存储系统，也不是点对点的消息队列

---

### Q4 — Kafka — 消费者组与分区消费

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Kafka中，一个消费者组可以同时消费一个主题的多个分区吗？

**选项:**
1. 可以，消费者组可以同时消费一个主题的多个分区 ✅
2. 不可以，一个消费者组只能消费一个主题的一个分区
3. 可以，但是需要使用特定的配置参数
4. 不可以，一个消费者组只能消费一个主题的所有分区

**我的答案:** 选项2 ❌

**正确答案:** 选项1

**解析:**
- 在Kafka中，一个消费者组可以同时消费一个主题的多个分区。通过增加消费者实例的数量，每个实例可以独立消费一个或多个分区的消息，从而实现更高的消费吞吐量。

---
### Q5 — Kafka — 过期数据清理策略

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Kafka 过期数据清理中启用删除策略的配置是？

**选项:**
1. `log.cleanup.policy=none`
2. `log.cleanup.policy=all`
3. `log.cleanup.policy=delete` ✅
4. `log.cleanup.policy=compact`

**我的答案:** 选项3 ✅

**正确答案:** 选项3

**解析:**
- 日志清理保存的策略只有delete和compact两种
- 1. log.cleanup.policy=delete启用删除策略
- 2. log.cleanup.policy=compact启用压缩策略

---
### Q6 — Kafka — RoundRobin 策略前提条件

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Kafka 使用 RoundRobin 策略有两个前提条件，其中不包括？

**选项:**
1. 同一个Consumer Group里面的所有消费者的num.streams必须相等
2. 每个消费者订阅的主题必须相同
3. 每个消费者订阅 的Broker必须相同 ✅
4. 其他都不对

**我的答案:** 选项1 ❌

**正确答案:** 选项3

**解析:**
- 使用RoundRobin策略有两个前提条件必须满足：
- 1. 同一个Consumer Group里面的所有消费者的num.streams必须相等；
- 2. 每个消费者订阅的主题必须相同。

---
### Q7 — Kafka — Producer 优化写入速度

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 关于 Kafka producer 优化写入速度的方法，下列哪项是错误的？

**选项:**
1. 增加 isr 数量 ✅
2. 增加 partition 数
3. 提高 batch.size
4. 增加线程

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- kafka producer优化写入速度的方法有：
- 增加线程
- 提高 batch.size
- 增加更多 producer 实例
- 增加 partition 数
- 设置 acks=-1 时，如果延迟增大：可以增大 num.replica.fetchers（follower 同步数据的线程数）来调解；
- 跨数据中心的传输：增加 socket 缓冲区设置以及 OS tcp 缓冲区设置。

---
### Q8 — Kafka — 消费者未提交 offset

**来源:** 每日一练 App

**题目:** Kafka 中如果消费者消费后没有提交 offset，会怎样？

**选项:**
1. 程序崩溃 ❌
2. 强行 kill
3. 重复消费 ✅
4. 自动消费

**我的答案:** 程序崩溃 ❌
**正确答案:** 重复消费

**解析:**
- Kafka 消费者消费消息后，如果没有提交 offset，下次启动或 rebalance 时，会从上次提交的 offset 位置重新消费
- 这导致**重复消费** (Duplicate Consumption)，而不是程序崩溃
- Kafka 消费者有自动提交机制 (enable.auto.commit=true)，但即使关闭自动提交，也只是不提交 offset，不会导致程序崩溃

**关键记忆:**
> 不提交 offset → 重复消费，不是崩溃
> Kafka 保证 at least once 语义，重复消费是常见现象

---

### Q9 — Kafka — 分区键映射

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 关于Kafka分区位置描述错误的是？

**选项:**
1. 主题内添加了新的分区，散列算法是恒定的，键与分区之间的映射一定不会发生改变 ✅
2. 如果键值不为`null`并使用了默认的分区器，消息就会写在特定的分区上，如果该分区不可用就会发生错误
3. 只有在不改变主题分区数量的情况下，键与分区之间的映射就会保持不变
4. 主题内添加了新的分区，键与分区之间的映射可能会发生改变

**我的答案:** 选项3 ❌

**正确答案:** 选项1

**解析:**
- 添加了分区之后，键与分区之间的映射可能会发生改变

---
### Q10 — Kafka — 心跳参数

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下列关于 Kafka 消费者心跳描述错误的是？

**选项:**
1. 一般情况下 heartbeat.interval.ms 的时间会设置得比 session.timeout.ms 要大，session.timeout.ms 的默认值是 30s ✅
2. session.timeout.ms 指定消费者被认定死亡之前可以与服务器断开连接的时间
3. heartbeat.interval.ms 指定了向协调器发送心跳的频率
4. 把 session.timeout.ms 值设置的大一点可以减少意外的再均衡

**我的答案:** 选项2 ❌

**正确答案:** 选项1

**解析:**
- heartbeat.interval.ms的时间一定比session.timeout.ms要小，session.timeout.ms的默认值是10s， heartbeat.interval.ms官方建议为session.timeout.ms的1/3。

---
### Q11 — Git — vs SVN 区别

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下列关于git和svn区别中说法错误的是？

**选项:**
1. git是分布式的，svn是集中式的
2. git把内容按元数据的方式存储，svn按文件存储，git的内容完整性要优于svn
3. git有一个全局的版本号，而svn没有 ✅
4. git不需要联网就可以使用（本地库），svn需要联网

**我的答案:** 选项2 ❌

**正确答案:** 选项3

**解析:**
- git没有全局版本号，svn有全局版本号

---
### Q12 — Git — 生成 SSH 公钥步骤

**来源:** 每日一练 App

**题目:** Git 生成 ssh 公钥的正确步骤是？

**选项:**
1. 下载git → 安装 → mkdir ~/.ssh → cd ~ → cd .ssh → 配置name/email → 生成key → 回车
2. 下载git → 安装 → 配置name/email → mkdir .ssh → cd .ssh → cd ~ → 生成key → 回车
3. 下载git → 安装 → 配置name/email → cd ~ → mkdir .ssh → cd .ssh → 生成key → 回车 ✅
4. 下载git → 安装 → cd ~ → mkdir .ssh → cd .ssh → 配置name/email → 生成key → 回车 ❌

**我的答案:** 选项4 ❌
**正确答案:** 选项3

**解析:**
生成 SSH 公钥的正确流程：
1. 下载并安装 Git
2. 配置全局 name 和 email（`git config --global user.name` / `user.email`）
3. 打开 Git Bash，`cd ~` 进入用户主目录
4. `mkdir .ssh` 创建 .ssh 文件夹
5. `cd .ssh` 进入文件夹
6. `ssh-keygen -t rsa -b 4096 -C "email"` 生成密钥
7. 回车确认默认路径，可选设置密码

- **选项1** 先创建 `.ssh` 再 `cd ~` 然后 `cd .ssh`，用绝对路径创建可以工作但顺序别扭
- **选项2** 在创建 `.ssh` 文件夹和进入目录后才 `cd ~` 回到主目录，逻辑错乱
- **选项3 ✅** 是最合理的顺序：先配置 → `cd ~` 进主目录 → `mkdir .ssh` → `cd .ssh` → 生成密钥
- **选项4 ❌** 把 `cd ~` → `mkdir .ssh` → `cd .ssh` 放在了配置 name/email **之前**，顺序不正确

**关键记忆:**
> SSH 生成流程：**安装 → 配置 → cd ~ → mkdir .ssh → cd .ssh → ssh-keygen → 回车**

---

### Q13 — Git — rebase --onto 用法

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 取出 client 分支，找出处于 client 分支和 server 分支的共同祖先之后的修改，然后把它们在 master 分支上重放一遍。以下哪个命令能达到上述效果？

**选项:**
1. git rebase --onto master server client ✅
2. git rebase --onto server client master
3. git rebase --onto master client server
4. git rebase --onto client server master

**我的答案:** 选项3 ❌

**正确答案:** 选项1

**解析:**
- onto参数后面是新的基

---
### Q14 — Git — merge vs rebase 优势

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 相比于`git rebase`，`git merge`的一个优势是什么？

**选项:**
1. “git merge”保留了分支的完整历史和时间顺序，而“git rebase”则没有 ✅
2. “git merge”不会在项目历史中添加新的提交，但“git rebase”会
3. “git merge”过程中的数据冲突会自动解决，但“git rebase”不会
4. “git merge”上的提交被缓存，变成比“git rebase”更快的操作

**我的答案:** 选项3 ❌

**正确答案:** 选项1

**解析:**
- git rebase 的优点是可以让开发者在提交之间重新排列，让提交历史看起来更整洁。
- 另外，由于 rebase 会创建新的提交，因此可以避免产生冗余的 merge 提交。
- git merge 的优点是更简单易用，而且显式保留了提交历史。

---
### Q15 — Git — 纯本地操作

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 相比于`git rebase`，`git merge`的一个优势是什么？

**选项:**
1. “git merge”保留了分支的完整历史和时间顺序，而“git rebase”则没有 ✅
2. “git merge”不会在项目历史中添加新的提交，但“git rebase”会
3. “git merge”过程中的数据冲突会自动解决，但“git rebase”不会
4. “git merge”上的提交被缓存，变成比“git rebase”更快的操作

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- git rebase 的优点是可以让开发者在提交之间重新排列，让提交历史看起来更整洁。
- 另外，由于 rebase 会创建新的提交，因此可以避免产生冗余的 merge 提交。
- git merge 的优点是更简单易用，而且显式保留了提交历史。

