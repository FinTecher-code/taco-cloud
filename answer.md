# 评论功能 — 实操分析与标准答案

## 一、你的代码问题分析

### 1. CommentServiceImpl.java

```java
// ❌ 你写的
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    public List<Comment> pageByArticle(Long articleId) {
        return CommentMapper.selectByArticle(articleId);  // 错误1
    }

    public void deleteById(int deleted) {  // 错误2
        CommentMapper;  // 错误3
    }
}
```

**问题1：** 缺 `@Service` 注解
- Spring 靠注解扫描 Bean，没有 `@Service` 就不会创建这个类的实例
- Controller 里注入 `CommentService` 时会报错：`NoSuchBeanDefinitionException`

**问题2：** `CommentMapper.selectByArticle(articleId)` 写法错误
- `CommentMapper` 是一个接口，不能直接调用它的方法
- 而且 Mapper 里根本没有定义 `selectByArticle` 这个方法
- **正确做法**：用 `lambdaQuery().eq(Comment::getArticleId, articleId).list()` 或者注入 Mapper 实例调用自定义方法

**问题3：** `deleteById(int deleted)` 语义和实现都错了
- 参数名 `deleted` 让人以为传的是删除标记，但实际需要传的是**评论的 ID**
- 方法体没写完整，只有 `CommentMapper;` 一个无意义的表达式
- **正确做法**：方法名 `removeComment(Long id)`，内部调 `removeById(id)`

---

### 2. CommentController.java

```java
// ❌ 你写的（不完整）
@RestController
@RequestMapping("/api/comment")  // 问题1
public class CommentController {
    private final CommentService commentService;
    // 构造器注入 ✅

    @DeleteMapping("/{id}")
    // 方法体缺失  ← 问题2
}
```

**问题1：** 路径 `/api/comment` 是单数
- 项目里其他接口都是复数：`/api/articles`、`/api/categories`、`/api/users`
- 应该统一为 `/api/comments`

**问题2：** 只写了 `@DeleteMapping`，而且方法体是空的
- `@GetMapping`（查评论列表）和 `@PostMapping`（新增评论）完全没写
- `@DeleteMapping` 注解下面没有方法体，编译器会报语法错误

---

### 3. CommentService.java

```java
// ❌ 你写的
void deleteById(int deleted);
```

- 参数类型 `int` 和参数名 `deleted` 都有问题
- 评论 ID 是 `Long` 类型（对应数据库 `BIGINT`），不是 `int`
- 方法名 `deleteById` 虽然能用但不够明确，建议用 `removeComment`

---

### 4. Comment.java

```java
// ❌ 你写的
private Integer deleted;
```

- 缺少 `@TableLogic` 注解
- 不加上这个注解，`removeById()` 执行的是物理删除（`DELETE FROM`）而不是逻辑删除（`UPDATE SET deleted=1`）

---

## 二、标准答案（完整可运行代码）

### 2.1 建表 DDL

先在你的 MySQL 里执行：

```sql
USE blog;
CREATE TABLE IF NOT EXISTS comment (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id  BIGINT       NOT NULL COMMENT '关联的文章ID',
    nickname    VARCHAR(50)  NOT NULL COMMENT '评论者昵称',
    content     TEXT         NOT NULL COMMENT '评论内容',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_article (article_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章评论表';
```

---

### 2.2 Entity — Comment.java

```java
package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;
    private String nickname;
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String articleTitle;
}
```

**关键点：**
- `@TableLogic` 逻辑删除注解不能少
- `articleTitle` 加 `@TableField(exist = false)` 表示这不是数据库字段，用于联表查询时填充

---

### 2.3 DTO — CommentRequest.java

```java
package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "评论内容不能为空")
    private String content;
}
```

**为什么需要 DTO？** 控制前端只能传 `articleId`、`nickname`、`content` 三个字段，`id`、`createdAt`、`deleted` 等字段前端无法传入，保证安全。

---

### 2.4 Mapper — CommentMapper.java

```java
package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("""
            SELECT c.*, a.title AS article_title
            FROM comment c
            LEFT JOIN article a ON c.article_id = a.id
            WHERE c.deleted = 0
            ORDER BY c.created_at DESC
            """)
    IPage<Comment> selectPageWithArticle(Page<?> page);

    @Select("""
            SELECT c.*, a.title AS article_title
            FROM comment c
            LEFT JOIN article a ON c.article_id = a.id
            WHERE c.deleted = 0 AND c.article_id = #{articleId}
            ORDER BY c.created_at DESC
            """)
    IPage<Comment> selectPageByArticle(Page<?> page, @Param("articleId") Long articleId);
}
```

**关键点：**
- 两个自定义方法都用了 `@Select` 注解手写 SQL
- LEFT JOIN 联表查询 `article.title` 赋值给 `articleTitle`
- 返回 `IPage<Comment>` 支持分页

---

### 2.5 Service 接口 — CommentService.java

```java
package com.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.Comment;

public interface CommentService extends IService<Comment> {

    IPage<Comment> page(int pageNum, int pageSize, Long articleId);

    Comment addComment(Long articleId, String nickname, String content);

    void removeComment(Long id);
}
```

**方法命名规范：**
- `page` — 分页查询（参数里加 `articleId` 支持按文章过滤）
- `addComment` — 新增评论（用 add 不用 save，避免和 IService 的 save 混淆）
- `removeComment` — 删除评论（用 remove 不用 delete，避免混淆）

---

### 2.6 Service 实现 — CommentServiceImpl.java

```java
package com.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service  // ← 别忘了！
public class CommentServiceImpl
        extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public IPage<Comment> page(int pageNum, int pageSize, Long articleId) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        if (articleId != null) {
            return commentMapper.selectPageByArticle(page, articleId);
        }
        return commentMapper.selectPageWithArticle(page);
    }

    @Override
    @Transactional
    public Comment addComment(Long articleId, String nickname, String content) {
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setNickname(nickname);
        comment.setContent(content);
        save(comment);  // 继承自 ServiceImpl
        return comment;
    }

    @Override
    @Transactional
    public void removeComment(Long id) {
        removeById(id);  // 继承自 ServiceImpl，逻辑删除
    }
}
```

**关键点：**
- `@Service` 必加
- 自定义 SQL 查询 → 注入 `commentMapper` 调自定义方法
- 简单 CRUD → 直接调继承来的 `save()` / `removeById()`
- 写操作加 `@Transactional` 保证事务

---

### 2.7 Controller — CommentController.java

```java
package com.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.dto.ApiResult;
import com.blog.dto.CommentRequest;
import com.blog.entity.Comment;
import com.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")  // 复数
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // GET /api/comments?page=1&size=10&articleId=1
    @GetMapping
    public ApiResult<IPage<Comment>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long articleId) {
        return ApiResult.success(commentService.page(page, size, articleId));
    }

    // POST /api/comments  body: {articleId, nickname, content}
    @PostMapping
    public ApiResult<Comment> add(@Valid @RequestBody CommentRequest request) {
        Comment comment = commentService.addComment(
                request.getArticleId(),
                request.getNickname(),
                request.getContent()
        );
        return ApiResult.success(comment);
    }

    // DELETE /api/comments/{id}
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        commentService.removeComment(id);
        return ApiResult.success();
    }
}
```

**关键点：**
- `@RequestMapping("/api/comments")` 用复数
- 三个方法对应三个接口：查 / 增 / 删
- `@Valid` 触发 DTO 校验
- 所有返回都包在 `ApiResult.success()` 里，统一格式

---

## 三、测试命令

先建表，然后启动项目，执行以下测试：

```bash
# 1. 新增评论
curl -X POST http://localhost:8080/api/comments \
  -H "Content-Type: application/json" \
  -d '{"articleId":1,"nickname":"张三","content":"写得好！"}'

# 2. 查全部评论
curl http://localhost:8080/api/comments

# 3. 按文章查评论
curl "http://localhost:8080/api/comments?articleId=1"

# 4. 删除评论
curl -X DELETE http://localhost:8080/api/comments/1
```

---

## 四、你的代码 vs 标准答案对照表

| 对比项 | 你的写法 | 标准写法 |
|---|---|---|
| `@Service` 注解 | 缺失 | 必须加 |
| 调用 Mapper 方法 | `CommentMapper.selectByArticle()` ❌ | `commentMapper.selectPageByArticle()` ✅ 或 `lambdaQuery().eq(...)` ✅ |
| 删除方法参数 | `int deleted` ❌ | `Long id` ✅ |
| 删除方法体 | `CommentMapper;` ❌ | `removeById(id)` ✅ |
| Controller 路径 | `/api/comment` ❌ 单数 | `/api/comments` ✅ 复数 |
| Controller 接口 | 只有半个 DELETE ❌ | GET + POST + DELETE 完整三个 ✅ |
| `@TableLogic` | 缺失 ❌ | 必须加 ✅ |
| 联表查文章标题 | 没有 ❌ | 加了 `articleTitle` 字段 ✅ |
| DTO | 没有 ❌ | 新增 `CommentRequest.java` ✅ |

---

## 五、常见错误速查

| 错误现象 | 原因 | 解决 |
|---|---|---|
| `NoSuchBeanDefinitionException` | 忘了加 `@Service` / `@Mapper` | 检查注解 |
| `commentMapper.selectByArticle` 报错 | Mapper 里没定义这个方法，或者调用的方式不对 | 在 Mapper 接口里用 `@Select` 定义，或者用 `lambdaQuery()` |
| `required a bean of type 'CommentMapper'` | ServiceImpl 需要注入 Mapper，但没写构造器 | 添加构造器注入 |
| 删除后数据还在但查不到了 | ✅ 这是逻辑删除的正常表现（`deleted=1`） | 想物理删除的话调 `baseMapper.deleteById()` |
| 日期字段为 null | 忘了 `@TableField(fill = FieldFill.INSERT)` | 加上自动填充注解，并确认 `MyMetaObjectHandler` 配置正确 |
