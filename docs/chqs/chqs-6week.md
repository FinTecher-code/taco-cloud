# 第六周 - 刷题记录

---

## 2026-08-12

---

### Q1 — Maven 插件描述错误

**来源:** 每日一练 App

**题目:** 下列对 Maven 插件描述错误的是？

**选项:**
1. clean：构建之后清理目标文件，删除目标目录
2. compiler：编译 Java 源文件
3. surefile：不算项目，创建测试报告 ✅
4. antrun：从构建过程的任意一个阶段中运行一个 ant 任务的集合

**我的答案:** 选项3 ✅
**正确答案:** 选项3 ✅

**解析:**
- 题目问"描述**错误**的是"，选项3 有两个明显的错：
  1. 插件名拼错了：正确的插件叫 **surefire**，不是 surefile
  2. surefire 的作用是**运行项目的单元测试**并生成测试报告，而不是"创建测试报告"这么模糊
- 其余三个插件描述都正确：
  - **clean**：`mvn clean` 删除 target 目录，清理上次构建产物 ✅
  - **compiler**：`maven-compiler-plugin` 负责编译 Java 源文件 ✅
  - **antrun**：`maven-antrun-plugin` 在构建生命周期任意阶段执行 Ant 任务 ✅
- 记忆点：跑测试的是 **surefire**（sure = 确保，fire = 开火→跑测试）；clean=清理，compiler=编译，antrun=跑 ant

---

---

### Q2 — Maven Clean 生命周期阶段

**来源:** 每日一练 App

**题目:** Clean 生命周期执行过程不包括？

**选项:**
1. pre-clean：执行一些需要在 clean 之前完成的工作
2. clean：移除所有上一次构建生成的文件
3. post-clean：执行一些需要在 clean 之后立刻完成的工作 ❌
4. after-clean：执行一些 post-clean 之后立刻完成的工作 ✅

**我的答案:** 选项3 ❌
**正确答案:** 选项4 ✅

**解析:**
- Maven 的 Clean 生命周期**只有三个阶段**：`pre-clean` → `clean` → `post-clean`
- 不存在 `after-clean` 这个阶段，选项4 是凭空捏造的
- 三个阶段职责：
  - **pre-clean**：在 clean 之前执行，做清理前的准备工作 ✅
  - **clean**：核心阶段，删除上次构建生成的文件（`mvn clean` 默认执行这个）✅
  - **post-clean**：在 clean 之后立刻执行，做清理后的收尾工作 ✅
- 用户选了 post-clean，误以为它是虚构的——其实 post-clean 是真实存在的阶段，真正的假阶段是 after-clean
- 记忆点：Clean 三兄弟 = pre-clean / clean / post-clean；**没有 after-clean**

---

---

### Q3 — Maven 命令行显示帮助信息

**来源:** 每日一练 App

**题目:** maven 命令行显示帮助信息，可以使用的命令是？

**选项:**
1. `mvn -f`
2. `mvn -h` ✅
3. `mvn -o`
4. `mvn -q`

**我的答案:** 选项2 ✅
**正确答案:** 选项2 ✅

**解析:**
- `mvn -h`（或 `mvn -help`）：显示帮助信息，列出常用命令和选项 ✅
- `mvn -q`：安静模式，只输出 ERROR 级别日志
- `mvn -f`：指定使用某个 POM 文件替换当前 POM（如 `mvn -f /path/pom.xml`）
- `mvn -o`：离线模式（offline），不联网下载依赖
- 记忆点：h=help 帮助，q=quiet 安静，f=file 指定POM，o=offline 离线

---

---

### Q4 — Maven 常见依赖范围

**来源:** 每日一练 App

**题目:** 属于 Maven 常见的依赖范围选项的是？

**选项:**
1. compile、test、provided、runtime ✅
2. compile、provided、runtime
3. test、provided、runtime
4. compile、test、runtime

**我的答案:** 选项1 ✅
**正确答案:** 选项1 ✅

**解析:**
- Maven 常见的依赖范围（scope）有四个：**compile、test、provided、runtime**，全部正确 ✅
- 各 scope 含义：
  - **compile**：默认范围，编译/测试/运行都可用
  - **test**：只在测试编译和执行阶段可用（如 JUnit）
  - **provided**：编译和测试可用，运行时由容器/JDK 提供（如 servlet-api）
  - **runtime**：运行和测试时可用，编译时不可用（如 JDBC 驱动）
- 另有 system、import 等不常用范围，但题目问"常见"的，就是这四个
- 记忆点：compile/test/provided/runtime，四件套全选

---

---

### Q5 — Maven 显示版本信息的命令

**来源:** 每日一练 App

**题目:** 显示版本信息可以使用的命令是？

**选项:**
1. acd（mvn -version / mvn -V / mvn -show-version）
2. abc（mvn -version / mvn -v / mvn -V）❌
3. bcd（mvn -v / mvn -V / mvn -show-version）
4. abcd（全部四个）✅

**我的答案:** 选项2 ❌
**正确答案:** 选项4 ✅

**解析:**
- 四个命令都能显示版本信息，分两类：
  - `mvn -version` / `mvn -v`：**仅显示版本信息**，然后立即停止
  - `mvn -V` / `mvn -show-version`：显示版本信息，但**不停止构建**，接着继续执行后续目标（常用于构建时顺便打出版本）
- 所以 a、b、c、d 四个全都属于"显示版本信息"的命令，全选 abcd ✅
- 用户选了 abc，漏掉了 d（mvn -show-version），以为它不算——其实 `-show-version` 和 `-V` 等价，都是显示版本后继续构建
- 记忆点：version/v = 只显示版本就停；V/show-version = 显示版本继续跑；四个都显示版本

---

---

## 📊 第六周错题汇总

| 日期 | 题数 | 答对 | 答错 |
|:----:|:----:|:----:|:----:|
| 08-12 | 5 | 3 | 2 |
| **合计** | **5** | **3** | **2** |
