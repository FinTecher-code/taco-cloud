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
1.
```xml
<profile>
 <id>test</id>
 <activation>
 <file>
 <missing>target/generated-sources/axistools/wsdl2java/
 com/companyname/group</missing>
 </file>
 </activation>
</profile>
```
✅
2.
```xml
<profile>
 <groupId>test</groupId>
 <activation>
 <file>
 <missing>target/generated-sources/axistools/wsdl2java/
 com/companyname/group</missing>
 </file>
 </activation>
</profile>
```
3.
```xml
<profile>
 <groupId>test</groupId>
 <activations>
 <file>
 <missing>target/generated-sources/axistools/wsdl2java/
 com/companyname/group</missing>
 </file>
 </activations>
</profile>
```
4.
```xml
<profile>
 <groupId>test</groupId>
 <activations>
 <activation>
 <missing>target/generated-sources/axistools/wsdl2java/
 com/companyname/group</missing>
 </activation>
 </activations>
</profile>
```

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

## 2026-08-24

---

### Q11 — 常见代码安全漏洞类型

**来源:** 每日一练 App

**题目:** 常见的代码安全漏洞包括哪些类型？

**选项:**
1. SQL注入、跨站脚本攻击、文件包含等 ✅
2. 编码不规范、命名不规范、注释不足等
3. 数组越界、内存泄漏、栈溢出等
4. 逻辑错误、运行时异常、断言错误等

**我的答案:** 选项1 ✅
**正确答案:** 选项1 ✅

**解析:**
- 选项1 列的是典型的**代码安全漏洞**：SQL 注入、跨站脚本攻击（XSS）、文件包含都属于常见 Web 安全漏洞（OWASP Top 10 范畴）✅
- 选项2 是**代码规范/可读性**问题（编码、命名、注释），影响维护性但不构成安全漏洞 ❌
- 选项3 是**内存/运行时错误**（数组越界、内存泄漏、栈溢出），属于程序健壮性问题 ❌
- 选项4 是**逻辑与异常**问题（逻辑错误、运行时异常、断言错误），属于程序正确性问题 ❌
- 记忆点：安全漏洞 = 能被外部攻击利用的（注入 / XSS / 文件包含 / CSRF / 越权等）；规范、内存、逻辑类问题都不算安全漏洞

---

### Q12 — 越权漏洞的分类

**来源:** 每日一练 App

**题目:** 越权漏洞通常分为几种类型？

**选项:**
1. 一种
2. 两种 ✅
3. 三种
4. 四种

**我的答案:** 选项2 ✅
**正确答案:** 选项2 ✅

**解析:**
- 越权漏洞通常分为 **两种**：**水平越权** 和 **垂直越权** ✅
  - **水平越权（同级越权）**：同一权限级别的用户之间越权，如普通用户 A 通过改 URL 里的 ID 访问普通用户 B 的数据（典型如 IDOR）
  - **垂直越权（跨级越权）**：低权限用户越权访问高权限功能，如普通用户直接调用管理员接口
- 其他选项：一种（漏了分类）、三种/四种（多出来的是干扰项，常见分类就是两种）❌
- 记忆点：越权两兄弟 = 水平（同级横向）/ 垂直（低→高纵向）；看到“访问他人数据”想水平，看到“访问管理员功能”想垂直

---

### Q13 — 防止路径遍历的方法

**来源:** 每日一练 App

**题目:** 以下那个方法可防止路径遍历？

**选项:**
1. 在服务端根据文件白名单决定是否响应下载请求 ✅
2. 在前端进行文件白名单校验
3. 禁止上传文件
4. 禁止下载文件 ❌

**我的答案:** 选项4 ❌
**正确答案:** 选项1 ✅

**解析:**
- 选项1 正确：**服务端文件白名单** —— 只有预定的、安全的文件才允许被下载，攻击者用 `../` 之类的路径遍历手法也无法访问白名单外的任意文件 ✅
- 选项2 错误：**前端校验不可信**，攻击者可以直接构造请求绕过前端，白名单必须放在服务端才有效 ❌
- 选项3 错误：禁止上传文件解决的是“上传恶意文件”问题，路径遍历是**读取/下载**任意文件，两者不相关 ❌
- 选项4 错误：禁止下载属于“一刀切”拒绝服务，牺牲正常功能，不是针对性防御；正确做法是**服务端白名单 + 路径规范化校验**（先 resolve 再验证）❌
- 记忆点：安全校验一律**服务端**为准（前端可绕过）；路径遍历 → 服务端白名单/规范化路径；上传≠下载，别混淆攻击面

---

### Q14 — 最容易引起代码执行漏洞的方式

**来源:** 每日一练 App

**题目:** 下列哪种代码执行方式最容易引起代码执行漏洞？

**选项:**
1. 使用 eval() 函数执行字符串中的 PHP 代码 ✅
2. 直接在代码中写入系统命令进行执行
3. 使用 system() 函数执行系统命令
4. 使用 include() 函数引入其他 PHP 文件

**我的答案:** 选项1 ✅
**正确答案:** 选项1 ✅

**解析:**
- 选项1 正确：`eval()` 会把**字符串当作 PHP 代码执行**，字符串一旦被用户输入污染（如拼接用户参数），恶意代码就直接被执行 —— 这是最容易失控的代码执行点 ✅
- 选项2：系统命令**硬编码在代码里**，内容是开发者写死的，用户无法控制，风险有限 ❌
- 选项3：`system()` 确实危险，但它需要配合命令注入（用户输入拼进命令）才构成漏洞，比 eval 多一道前置条件 ❌
- 选项4：`include()` 危险点是**文件包含**漏洞（配合可控路径可转为 RCE），但它本身是“引入文件”，不是直接执行字符串代码 ❌
- 记忆点：`eval()` = “字符串即代码”，输入一旦可控就是直接 RCE；system/include 等需要组合条件才构成漏洞，危险等级低于 eval

---

### Q15 — 不属于不安全编码导致的安全漏洞

**来源:** 每日一练 App

**题目:** 下面不属于不安全编码导致的安全漏洞是？

**选项:**
1. SQL注入
2. 中间件版本漏洞 ✅
3. 路径遍历
4. 跨站脚本攻击

**我的答案:** 选项2 ✅
**正确答案:** 选项2 ✅

**解析:**
- 中间件版本漏洞是**中间件自身的安全缺陷**（如 Tomcat/nginx 某版本存在 CVE），不是开发者编码不当造成的 ✅
- SQL注入、路径遍历、XSS 都属于**不安全编码**导致的漏洞：
  - **SQL注入**：SQL 语句拼接用户输入，未参数化 ❌
  - **路径遍历**：文件路径拼接用户输入，未做白名单/规范化 ❌
  - **XSS**：输出未转义，用户输入直接进页面 ❌
- 记忆点：编码类漏洞 = 注入 / 遍历 / XSS / CSRF 等“代码写出来的问题”；版本漏洞 = 组件/中间件**自身**的 CVE，靠升级补丁解决，不靠改代码

---

## 📊 第六周错题汇总

| 日期 | 题数 | 答对 | 答错 |
|:----:|:----:|:----:|:----:|
| 08-12 | 5 | 3 | 2 |
| 08-14 | 3 | 0 | 3 |
| 08-23 | 2 | 0 | 2 |
| 08-24 | 5 | 4 | 1 |
| **合计** | **15** | **7** | **8** |
