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

## 2026-08-14

---

### Q6 — Maven Site 生命周期阶段

**来源:** 每日一练 App

**题目:** Maven site 周期的主要阶段包括？

**选项:**
1. site 产生项目的站点文档、install 将构件部署到本地仓库
2. install 将构件部署到本地仓库、site-deploy 将项目的站点文档部署到服务器
3. site-deploy 将项目的站点文档部署到服务器、deploy 部署构件到远程仓库 ❌
4. site 产生项目的站点文档、site-deploy 将项目的站点文档部署到服务器 ✅

**我的答案:** 选项3 ❌
**正确答案:** 选项4 ✅

**解析:**
- Maven 的 **Site 生命周期**只有两个阶段：`site` → `site-deploy`
  - **site**：产生项目的站点文档（生成项目报告、API 文档等静态网站）✅
  - **site-deploy**：将生成的站点文档部署到服务器 ✅
- 易混点：`install` 和 `deploy` 属于 **Default（构建）生命周期**，不属于 Site 生命周期
  - **install**：把构件部署到本地仓库（`~/.m2/repository`）
  - **deploy**：把构件部署到远程仓库（如 Nexus/中央仓库）
- 用户选了选项3，把 site-deploy（部署站点文档）和 deploy（部署构件）混在一起——deploy 是 Default 生命周期的阶段，不是 Site 周期的
- 记忆点：Site 两兄弟 = site / site-deploy（都跟"站点文档"相关）；install/deploy 是 Default 周期的构件部署，别混进来

---

---

### Q7 — Maven 打包方式

**来源:** 每日一练 App

**题目:** 关于 Maven 打包方式说法错误的是？

**选项:**
1. war：该资源打成 war 包，默认是 war ✅
2. jar：该资源打成 jar 包，默认是 jar
3. war：该资源打成 war 包
4. pom：该资源是一个父资源（表明使用 maven 分模块管理），打包时只生成一个 pom.xml，不生成 jar 或其他包结构 ❌

**我的答案:** 选项4 ❌
**正确答案:** 选项1 ✅

**解析:**
- 题目问"说法**错误**的是"，答案是选项1：war 打成 war 包没错，但 **Maven 的默认打包方式是 jar**，不是 war
  - 也就是说，如果一个项目没显式声明 `<packaging>`，Maven 默认按 jar 打包
- 其余三个选项都正确：
  - **jar**：打成 jar 包，默认就是 jar ✅
  - **war**：打成 war 包 ✅（只是说能打成 war，没说 war 是默认值）
  - **pom**：父工程/聚合工程，只生成 pom.xml，不产出 jar 等包结构 ✅
- 易混点：`<packaging>` 的默认值是 **jar**，这一点最容易考；war 需要显式写 `<packaging>war</packaging>` 才会打成 war 包
- 记忆点：Maven 默认打包 = jar；war 要手动指定；pom = 父工程只出 pom.xml

---

---

### Q8 — 打包时跳过测试

**来源:** 每日一练 App

**题目:** 如何在打包时跳过测试环境？

**选项:**
1. `mvn clean package`
2. `mvn clean package -Dmaven.test.skip=true` ✅
3. `mvn clean package -Dmaven.test.skip=false` ❌
4. `mvn package`

**我的答案:** 选项3 ❌
**正确答案:** 选项2 ✅

**解析:**
- 跳过测试的关键参数是 `-Dmaven.test.skip=true`，其中 **skip=true 才是跳过**
  - `mvn clean package -Dmaven.test.skip=true`：打包时跳过测试编译和测试执行 ✅
- 选项3 是 `skip=false`，含义是**不跳过测试**，与题目要求正好相反 ❌
- 选项1 `mvn clean package` 和选项4 `mvn package` 都会执行测试，没跳过
- 易混点：还有一个 `-DskipTests` 参数
  - `-Dmaven.test.skip=true`：跳过测试的**编译**和**运行**
  - `-DskipTests`：只跳过测试**运行**，但仍会编译测试代码
- 记忆点：skip 后面跟 `true` 才是跳过；`skipTests` 只跳运行不跳编译

---

---

## 2026-08-23

---

### Q9 — Maven 使用阿里云仓库

**来源:** 每日一练 App

**题目:** Maven 如何使用阿里云（Aliyun）仓库？

**选项:**
1. settings.xml 配 `<mirrors><mirror>`（带 `mirrorOf>central`），pom.xml 配 `<repositories><repository>`（带 releases/snapshots 开关）✅
2. settings.xml 配 `<repositories><repository>`（带 `mirrorOf>central`），pom.xml 配 `<repositories><repository>`（带 releases/snapshots 开关）
3. settings.xml 配 `<repositories><repository>`（带 `mirrorOf>central`），pom.xml 配 `<mirrors><mirror>`（带 releases/snapshots 开关）
4. 其他选项均正确 ❌

**我的答案:** 选项4 ❌
**正确答案:** 选项1 ✅

**解析:**

阿里云仓库配置的核心是**两个文件、两种标签，位置不能搞混**：

- **settings.xml（全局，Maven 根目录 conf/ 下）**：配**镜像**用 `<mirrors><mirror>`，通过 `mirrorOf` 指定镜像哪个仓库（`central` = 只镜像中央仓库，`*` = 镜像所有仓库）
- **pom.xml（项目级）**：配**仓库**用 `<repositories><repository>`，可加 `<releases>/<snapshots>` 开关控制是否拉取正式版/快照版

逐项分析：
- **选项1**：settings.xml 用 mirrors ✅ + pom.xml 用 repositories ✅ → **两处都正确**
- **选项2**：settings.xml 错用 `<repositories>`（镜像应该用 `<mirrors>`，且 repository 里没有 `mirrorOf` 这个子元素）❌；pom.xml 部分正确 → 整体错
- **选项3**：settings.xml 错用 `<repositories>` ❌，pom.xml 又错用 `<mirrors>`（pom.xml 里没有 mirrors 配置，releases/snapshots 也不是 mirror 的子元素）❌ → 全错
- **选项4**：选这个的坑在于——题目问的是哪种配置**正确**，但选项2、3都有硬伤，所以"其他选项均正确"不成立 ❌

**记忆点:** settings.xml = `<mirrors>` 镜像（mirrorOf 指定范围）；pom.xml = `<repositories>` 仓库（releases/snapshots 开关）；**mirror 进 settings，repository 进 pom，两者别串**

---

---

### Q10 — Maven 文件激活 Profile

**来源:** 每日一练 App

**题目:** 需要通过文件的存在或者缺失激活配置文件，如何设置 pom？

**选项:**
1. `<profile><id>test</id><activation><file><missing>...</missing></file></activation></profile>` ✅
2. `<profile><groupId>test</groupId><activation><file><missing>...</missing></file></activation></profile>`
3. `<profile><groupId>test</groupId><activations><file><missing>...</missing></file></activations></profile>`
4. `<profile><groupId>test</groupId><activations><activation><file><missing>...</missing></file></activation></activations></profile>`

**我的答案:** 选项4 ❌
**正确答案:** 选项1 ✅

**解析:**

Maven 通过文件激活 profile 的标准结构：

```xml
<profile>
  <id>test</id>
  <activation>
    <file>
      <missing>target/generated-sources/.../com/companyname/group</missing>
    </file>
  </activation>
</profile>
```

关键点：
- **标识符是 `<id>`**：profile 通过 `id` 唯一标识，`<groupId>` 是项目的坐标元素，profile 里**没有** groupId ❌
- **激活标签是 `<activation>`（单数）**：`<activations>` 复数不存在，Maven 不认识 ❌
- `<file>` 下有 `<exists>`（文件存在时激活）和 `<missing>`（文件缺失时激活）两种触发条件，本题用的是 `<missing>`
- 本题的激活条件：当 `target/generated-sources/axistools/wsdl2java/com/companyname/group` 这个文件/目录**不存在**时，激活 `test` profile

逐项分析：
- **选项1**：id + activation(单数) + file/missing → 全部正确 ✅
- **选项2**：错用 `<groupId>`（应为 `<id>`）❌
- **选项3**：`<groupId>` ❌ + `<activations>` 复数 ❌ → 两处错
- **选项4**：`<groupId>` ❌ + `<activations>` 复数外壳 ❌（里面虽是 activation，但外层不合法）→ 错

**记忆点:** profile 用 `<id>` 命名、`<activation>` 单数激活；文件触发看 `<file><exists>/<missing>`；出现 groupId 或 activations 就直接排除

---

---

## 📊 第六周错题汇总

| 日期 | 题数 | 答对 | 答错 |
|:----:|:----:|:----:|:----:|
| 08-12 | 5 | 3 | 2 |
| 08-14 | 3 | 0 | 3 |
| 08-23 | 2 | 0 | 2 |
| **合计** | **10** | **3** | **7** |
