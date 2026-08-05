# 第五周 - 刷题记录

---

## 2026-08-05

---

### Q1 — OpenSSH 客户端和服务器的全局配置文件

**来源:** 每日一练 App

**题目:** OpenSSH 客户端和服务器的两个全局配置文件是什么？

**选项:**
1. `/etc/ssh/ssh_config` 和 `/etc/ssh/sshd_config` ✅
2. `~/.sshconfig` 和 `/etc/ssh_config`
3. `~/.ssh/config` 和 `/etc/ssh.config` ❌
4. `~/.ssh` 和 `/users/ssh/ssh_config`

**我的答案:** 选项3 ❌
**正确答案:** 选项1 ✅

**解析:**
- OpenSSH 分客户端（ssh）和服务端（sshd 守护进程），全局配置文件都在 `/etc/ssh/` 目录下
- 客户端全局配置 → `/etc/ssh/ssh_config`；服务端全局配置 → `/etc/ssh/sshd_config`（多一个 d = daemon）
- `~/.ssh/config` 是**用户级**配置文件，只对当前用户生效，不算全局配置
- `/etc/ssh.config` 路径不存在，全局配置统一在 `/etc/ssh/` 下
- 口诀：客户端 `ssh_config`，服务端 `sshd_config`；全局放 `/etc/ssh/`，个人放 `~/.ssh/`

---

### Q2 — dig 命令指定 DNS 服务器

**来源:** 每日一练 App

**题目:** 下列哪个命令将使用 example1.com 上的 ns1 DNS 服务器，返回 example2.com 域上的域名系统（DNS）数据？

**选项:**
1. `dig @ns1.example1.com example2.com` ✅
2. `dig =ns1.example1.com example2.com`
3. `dig ns1.example2.com`
4. `dig ns1.example1.com example2.com`

**我的答案:** 选项1 ✅
**正确答案:** 选项1 ✅

**解析:**
- `dig` 是 Linux/Unix 查询 DNS 记录的工具
- 指定 DNS 服务器的语法固定：`dig @<DNS服务器> <域名>`，`@` 后跟要使用的 DNS 服务器
- 选项2 用 `=` 语法错误；选项3 把服务器和域名混在一起；选项4 缺少 `@`，dig 会用系统默认 DNS（`/etc/resolv.conf`）
- 记忆点：`@` = 指定 DNS 服务器，`dig @服务器 域名`

---

### Q3 — shebang 行的作用

**来源:** 每日一练 App

**题目:** Linux 脚本中 shebang 行的目的是什么（例如：`#!/bin/bash`）？

**选项:**
1. 识别想要使用的 shell 解释器 ✅
2. 识别脚本的最后一行
3. 启动 shell 解释器
4. 识别脚本的第一行

**我的答案:** 选项1 ✅
**正确答案:** 选项1 ✅

**解析:**
- shebang 行（`#!/bin/bash`）位于脚本**第一行**，作用是告诉系统用哪个解释器执行该脚本
- 选项4 是陷阱：shebang 确实写在第一行，但目的是识别解释器，不是“识别第一行”
- 选项3 不准确：是指定解释器，不是启动解释器
- 记忆点：`#!` = 用后面这个程序来跑我；`#!/bin/bash` = 用 bash 执行本脚本

---

### Q4 — killall 按名称停止进程

**来源:** 每日一练 App

**题目:** 下列哪个 Linux 命令，会根据名称停止正在运行的进程？

**选项:**
1. purge
2. killall ✅
3. exit
4. rest

**我的答案:** 选项2 ✅
**正确答案:** 选项2 ✅

**解析:**
- `killall` 按进程**名称**向所有匹配的进程发送信号（默认 SIGTERM），从而停止进程
- 区分：`kill` 按 PID（进程号）杀，`killall` 按名称杀；`pkill` 也能按名称匹配
- 选项1 `purge` 是清理缓存用；选项3 `exit` 是退出 shell；选项4 `rest` 不是标准命令
- 记忆点：kill 按编号，killall 按名字

---

### Q5 — /etc/resolv.conf 的用途

**来源:** 每日一练 App

**题目:** Linux 中，/etc/resolv.conf 的用途是？

**选项:**
1. 邮件服务的设置文件
2. DHCP 的设置文件
3. DNS 解析的设置文件 ✅
4. 网络路由的设置文件 ❌

**我的答案:** 选项4 ❌
**正确答案:** 选项3 ✅

**解析:**
- `/etc/resolv.conf` 是 DNS 解析配置文件，主要包含 `nameserver` 行，告诉系统域名解析时向哪些 DNS 服务器发送请求（如 `nameserver 8.8.8.8`）
- 路由配置不在这个文件：在 `/etc/sysconfig/network-scripts/`（ifcfg-*）、`/etc/network/interfaces`，或用 `ip route` 命令
- 邮件服务配置：`/etc/postfix/main.cf`、`/etc/aliases`；DHCP 配置：`/etc/dhcp/dhclient.conf`
- 记忆点：`resolv` = resolve（解析）→ DNS；路由记 `route`，两者别混

---
