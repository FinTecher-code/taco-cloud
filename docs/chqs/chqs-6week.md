# 第六周 - 刷题记录

---

## 2026-08-12

---

### Q1 — Maven 插件描述错误

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下列对Maven插件描述错误的是？

**选项:**
1. clean：构建之后清理目标文件，删除目标目录
2. compiler：编译Java源文件
3. surefile：不算项目，创建测试报告 ✅
4. antrun：从构建过程的任意一个阶段中运行一个ant任务的集合

**我的答案:** 选项3 ✅

**正确答案:** 选项3

**解析:**
- 正确答案是：surefile：不算项目，创建测试报告。
- 1. surefile：运行 JUnit 单元测试。创建测试报告。

---
### Q2 — Maven Clean 生命周期阶段

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Clean生命周期执行过程不包括？

**选项:**
1. pre-clean：执行一些需要在clean之前完成的工作
2. clean：移除所有上一次构建生成的文件
3. post-clean：执行一些需要在clean之后立刻完成的工作
4. after-clean：执行一些post-clean之后立刻完成的工作 ✅

**我的答案:** 选项3 ❌

**正确答案:** 选项4

**解析:**
- 正确答案是：after-clean：执行一些post-clean之后立刻完成的工作。没有该过程

---
### Q3 — Maven 命令行显示帮助信息

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** maven命令行显示帮助信息，可以使用的命令是？

**选项:**
1. `mvn -f`
2. `mvn -h` ✅
3. `mvn -o`
4. `mvn -q`

**我的答案:** 选项2 ✅

**正确答案:** 选项2

**解析:**
- - mvn -h 显示帮助信息，同mvn -help<br>
- - mvn -q 安静模式,只输出ERROR<br>
- - mvn -f 使用指定的POM文件替换当前POM文件<br>
- - mvn -o 运行offline模式,不联网进行依赖更新<br>

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

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 显示版本信息可以使用的命令是

a、`mvn -version`

b、`mvn -v`

c、`mvn -V`

d、`mvn -show-version`

**选项:**
1. acd
2. abc
3. bcd
4. abcd ✅

**我的答案:** 选项2 ❌

**正确答案:** 选项4

**解析:**
- mvn -version/-v 仅显示版本信息<br>
- mvn -show-version/-V 显示版本，不会跳过build过程

---
### Q6 — Maven Site 生命周期阶段

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Mavensite 周期的主要阶段包括？

**选项:**
1. site 产生项目的站点文档、install 将构件部署到本地仓库
2. install 将构件部署到本地仓库、site-deploy 将项目的站点文档部署到服务器
3. site-deploy 将项目的站点文档部署到服务器、deploy 部署构件到远程仓库
4. site 产生项目的站点文档、site-deploy 将项目的站点文档部署到服务器 ✅

**我的答案:** 选项3 ❌

**正确答案:** 选项4

**解析:**
- Mavensite周期主要包含两个阶段：site产生项目的站点文档、site-deploy将项目的站点文档部署到服务器

---
### Q7 — Maven 打包方式

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 关于maven打包方式说法错误的是？

**选项:**
1. war：该资源打成war包，默认是war ✅
2. jar：该资源打成jar包，默认是jar
3. war：该资源打成war包
4. pom：该资源是一个父资源（表明使用maven分模块管理），打包时只生成一个pom.xml不生成jar或其他包结构

**我的答案:** 选项4 ❌

**正确答案:** 选项1

**解析:**
- war：该资源打成war包，默认是jar

---
### Q8 — 打包时跳过测试

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 如何在打包时跳过测试环境？

**选项:**
1. mvn clean package
2. mvn clean package -Dmaven.test.skip=true ✅
3. mvn clean package -Dmaven.test.skip=false
4. mvn package

**我的答案:** 选项1 ❌

**正确答案:** 选项2

**解析:**
- 正确答案是：mvn clean package -Dmaven.test.skip=true

---
### Q9 — Maven 使用阿里云仓库

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Maven 如何使用阿里云（Aliyun）仓库？

**选项:**
1. 修改 maven 根目录下的 conf 文件夹中的 settings.xml 文件：
    ```
    <mirrors>
        <mirror>
          <id>alimaven</id>
          <name>aliyun maven</name>
          <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
          <mirrorOf>central</mirrorOf>        
        </mirror>
    </mirrors>
    ```
    pom.xml文件里添加
    ```
    <repositories>  
            <repository>  
                <id>alimaven</id>  
                <name>aliyun maven</name>  
                <url>http://maven.aliyun.com/nexus/content/groups/public/</url>  
                <releases>  
                    <enabled>true</enabled>  
                </releases>  
                <snapshots>  
                    <enabled>false</enabled>  
                </snapshots>  
            </repository>  
    </repositories>
    ``` ✅
2. 修改 maven 根目录下的 conf 文件夹中的 settings.xml 文件：
    ```
    <repositories>
        <repository>
          <id>alimaven</id>
          <name>aliyun maven</name>
          <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
          <mirrorOf>central</mirrorOf>        
        </repository>
    </repositories>
    ```
    pom.xml文件里添加
    ```
    <repositories>  
            <repository>  
                <id>alimaven</id>  
                <name>aliyun maven</name>  
                <url>http://maven.aliyun.com/nexus/content/groups/public/</url>  
                <releases>  
                    <enabled>true</enabled>  
                </releases>  
                <snapshots>  
                    <enabled>false</enabled>  
                </snapshots>  
            </repository>  
    </repositories>
    ```
3. 修改 maven 根目录下的 conf 文件夹中的 settings.xml 文件：
    ```
    <repositories>
        <repository>
          <id>alimaven</id>
          <name>aliyun maven</name>
          <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
          <mirrorOf>central</mirrorOf>        
        </repository>
    </repositories>
    ```
    pom.xml文件里添加
    ```
    <mirrors>  
            <mirror>  
                <id>alimaven</id>  
                <name>aliyun maven</name>  
                <url>http://maven.aliyun.com/nexus/content/groups/public/</url>  
                <releases>  
                    <enabled>true</enabled>  
                </releases>  
                <snapshots>  
                    <enabled>false</enabled>  
                </snapshots>  
            </mirror>  
    </mirrors>
    ```
4. 其他选项均正确

**我的答案:** 选项4 ❌

**正确答案:** 选项1

**解析:**
- 正确答案是：
- 修改 maven 根目录下的 conf 文件夹中的 settings.xml 文件：
- ```
- <mirrors>
- <mirror>
- <id>alimaven</id>
- <name>aliyun maven</name>
- <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
- <mirrorOf>central</mirrorOf>
- </mirror>
- </mirrors>
- ```
- pom.xml文件里添加
- ```
- <repositories>
- <repository>
- <id>alimaven</id>
- <name>aliyun maven</name>
- <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
- <releases>
- <enabled>true</enabled>
- </releases>
- <snapshots>
- <enabled>false</enabled>
- </snapshots>
- </repository>
- </repositories>
- ```

---
### Q10 — Maven 文件激活 Profile

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 在某社交平台的用户查询功能中，允许根据可选的关键词 `searchKey` 和可选的用户状态 `status` 来筛选用户。如果不传入 `searchKey`，则不做用户名搜索；如果不传入 `status`，则不做状态过滤。为实现部分匹配（模糊搜索），自定义了一个 `<bind>` 变量，将用户输入的关键词拼装成带有通配符的模式字符串。以下是 `UserMapper.xml` 中的核心查询方法，其中 `/* 代码缺失 */` 处需要填写动态 SQL 代码，正确使用 `<bind>` 并在必要时进行条件拼接，请从下列选项中选出正确的填充代码：

```xml
<mapper namespace="com.example.mapper.UserMapper">

    <select id="searchUsers" parameterType="java.util.Map" resultType="com.example.model.User">
        SELECT 
            id,
            username,
            status,
            created_at
        FROM t_user
        <where>
            /* 代码缺失 */
        </where>
    </select>

</mapper>
```

**选项:**
1. ```xml
    <bind name="pattern" value="'%' + searchKey + '%'" />
    <if test="searchKey != null and searchKey != ''">
        AND username LIKE #{pattern}
    </if>
    <if test="status != null and status != ''">
        AND status = #{status}
    </if>
    ``` ✅
2. ```xml
    <bind name="pattern" value="searchKey" />
    <if test="pattern != ''">
        OR username LIKE #{searchKey}
    </if>
    <if test="status != ''">
        AND status = #{status}
    </if>
    ```
3. ```xml
    <bind name="searchKey" value="'%' + searchKey + '%'" />
    <if test="searchKey == null">
        AND username LIKE #{searchKey}
    </if>
    <if test="status != null">
        OR status = #{status}
    </if>
    ```
4. ```xml
    <if test="searchKey != null">
        username = CONCAT('%', #{searchKey}, '%')
    </if>
    <if test="status != ''">
        status = #{status}
    </if>
    <bind name="extra" value="'unused'" />
    ```

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- ```xml
- <bind name="pattern" value="'%' + searchKey + '%'" />
- <if test="searchKey != null and searchKey != ''">
- AND username LIKE #{pattern}
- </if>
- <if test="status != null and status != ''">
- AND status = #{status}
- </if>
- ```
- 解析：正确。
- 首先使用 `<bind>` 将入参 `searchKey` 转换为“%...%”模式字符串，并在 `<if>` 标签内检查 `searchKey` 或 `status` 是否非空，从而决定是否拼接相应的过滤条件。满足模糊搜索和可选状态查询的需求。
- ```xml
- <bind name="pattern" value="searchKey" />
- <if test="pattern != ''">
- OR username LIKE #{searchKey}
- </if>
- <if test="status != ''">
- AND status = #{status}
- </if>
- ```
- 解析：错误。
- `<bind>` 中只将 `searchKey` 直接赋值给 `pattern`，并未加上通配符，无法实现模糊匹配；同时在拼接用户名条件时使用了 `OR`，会在空条件场景下产生逻辑混乱，与需求不符。
- ```xml
- <bind name="searchKey" value="'%' + searchKey + '%'" />
- <if test="searchKey == null">
- AND username LIKE #{searchKey}
- </if>
- <if test="status != null">
- OR status = #{status}
- </if>
- ```
- 解析：错误。
- `<bind>` 的 `name` 与入参重名，且条件判断反向：当 `searchKey == null` 才进行模糊搜索，逻辑不合理；对 `status` 也使用了 `OR`，导致在有关键词搜索时与状态条件产生无效或错误的查询结果。
- ```xml
- <if test="searchKey != null">
- username = CONCAT('%', #{searchKey}, '%')
- </if>
- <if test="status != ''">
- status = #{status}
- </if>
- <bind name="extra" value="'unused'" />
- ```
- 解析：错误。
- 第一条 `<if>` 逻辑直接将 `username` 赋值为 `CONCAT('%', #{searchKey}, '%')`，这并不是条件语句，而是试图更新字段值；缺少 `AND` 关键字无法形成正确的 `WHERE` 过滤。同时 `<bind>` 定义的变量也没有被实际使用，不符合题干要求的模糊查询方案。

---
### Q11 — 常见代码安全漏洞类型

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 常见的代码安全漏洞包括哪些类型？

**选项:**
1. SQL注入、跨站脚本攻击、文件包含等 ✅
2. 编码不规范、命名不规范、注释不足等
3. 数组越界、内存泄漏、栈溢出等
4. 逻辑错误、运行时异常、断言错误等

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- 常见的代码安全漏洞包括SQL注入、跨站脚本攻击、文件包含等等。

---
### Q12 — 越权漏洞的分类

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 越权漏洞通常分为几种类型？

**选项:**
1. 一种
2. 两种 ✅
3. 三种
4. 四种

**我的答案:** 选项2 ✅

**正确答案:** 选项2

**解析:**
- 越权漏洞通常分为两种类型：垂直越权和水平越权。垂直越权是指攻击者通过访问或者修改受限资源或者执行受限操作来获取比其权限更高的访问权限。而水平越权则是指攻击者通过访问或者修改其他用户的资源或者执行其他用户的操作来获取比其权限更高的访问权限。

---
### Q13 — 防止路径遍历的方法

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 以下那个方法可防止路径遍历？

**选项:**
1. 在服务端根据文件白名单决定是否响应下载请求 ✅
2. 在前端进行文件白名单校验
3. 禁止上传文件
4. 禁止下载文件

**我的答案:** 选项4 ❌

**正确答案:** 选项1

**解析:**
- 选项「在服务端根据文件白名单决定是否响应下载请求」是有效的，因为在服务端实施文件白名单可以确保只有预定的、安全的文件可以被下载。这可以防止攻击者通过路径遍历攻击来访问或下载服务器上的任意文件。
- 其他选项：
- - 选项：在前端进行文件白名单校验 - 前端的校验可以被绕过，因为攻击者可以直接与服务器通信，忽略前端的安全措施。
- - 选项：禁止上传文件 - 这不会防止路径遍历，因为路径遍历攻击通常涉及访问已经存在于服务器上的文件，而不是上传新文件。
- - 选项：禁止下载文件 - 这是一种极端的做法，它可能会破坏应用程序的功能，而且不是必要的。通过适当的文件访问控制，可以允许安全的文件下载，同时防止路径遍历攻击。

---
### Q14 — 最容易引起代码执行漏洞的方式

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下列哪种代码执行方式最容易引起代码执行漏洞？

**选项:**
1. 使用`eval()`函数执行字符串中的 PHP 代码 ✅
2. 直接在代码中写入系统命令进行执行
3. 使用`system()`函数执行系统命令
4. 使用`include()`函数引入其他 PHP 文件

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- 使用`eval()`函数执行字符串中的 PHP 代码时，如果字符串中包含恶意代码，恶意代码就会被执行，从而引发代码执行漏洞。

---
### Q15 — 不属于不安全编码导致的安全漏洞

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下面不属于不安全编码导致的安全漏洞是？

**选项:**
1. SQL注入
2. 中间件版本漏洞 ✅
3. 路径遍历
4. 跨站脚本攻击

**我的答案:** 选项2 ✅

**正确答案:** 选项2

**解析:**
- 中间件版本漏洞通常是由于中间件自身存在安全缺陷，而不是由于编码不当造成的。不安全编码通常会导致如SQL注入、路径遍历和跨站脚本攻击等安全漏洞。因此，中间件版本漏洞不属于不安全编码导致的安全漏洞。

