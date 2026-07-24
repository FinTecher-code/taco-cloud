# MyBatis-Plus 固定公式速查

## 增删改查四件套模板

每新增一张数据库表，按以下 **4 个文件** 创建，只需改三个名字：

### 1️⃣ Entity — 跟数据库表一一对应

```java
package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("你的表名")
public class 你的Entity {

    @TableId(type = IdType.AUTO)
    private Long id;

    // ... 其他字段（一个字段 = 表的一列）...

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
```

### 2️⃣ Mapper — 不用写 SQL

```java
package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.你的Entity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface 你的Mapper extends BaseMapper<你的Entity> {
    // 空着即可，BaseMapper 已提供 insert / deleteById / updateById / selectById / selectList / selectPage ...
}
```

### 3️⃣ Service 接口

```java
package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.你的Entity;

public interface 你的Service extends IService<你的Entity> {
    // 这里声明自定义的业务方法（非 CRUD）
}
```

### 4️⃣ Service 实现

```java
package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.你的Entity;
import com.blog.mapper.你的Mapper;
import com.blog.service.你的Service;
import org.springframework.stereotype.Service;

@Service
public class 你的ServiceImpl
        extends ServiceImpl<你的Mapper, 你的Entity>
        implements 你的Service {
    // 这里实现自定义的业务方法
}
```

### 5️⃣ Controller — 接收 HTTP 请求（可选）

```java
package com.blog.controller;

import com.blog.dto.ApiResult;
import com.blog.entity.你的Entity;
import com.blog.service.你的Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/你的路径")
public class 你的Controller {

    private final 你的Service 你的Service;

    public 你的Controller(你的Service 你的Service) {
        this.你的Service = 你的Service;
    }

    @GetMapping
    public ApiResult<List<你的Entity>> list() {
        return ApiResult.success(你的Service.list());
    }

    @PostMapping
    public ApiResult<你的Entity> add(@RequestBody 你的Entity entity) {
        你的Service.save(entity);
        return ApiResult.success(entity);
    }

    @PutMapping("/{id}")
    public ApiResult<你的Entity> update(@PathVariable Long id, @RequestBody 你的Entity entity) {
        entity.setId(id);
        你的Service.updateById(entity);
        return ApiResult.success(entity);
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        你的Service.removeById(id);
        return ApiResult.success();
    }
}
```

---

## 命名规律

| 层 | 命名公式 | 示例（user 表） |
|---|---|---|
| Entity | `表名单词` | `User.java` |
| Mapper | `Entity名 + Mapper` | `UserMapper.java` |
| Service 接口 | `Entity名 + Service` | `UserService.java` |
| Service 实现 | `Entity名 + ServiceImpl` | `UserServiceImpl.java` |
| Controller | `Entity名 + Controller` | `UserController.java` |

---

## 泛型对照速查

```java
ServiceImpl<  Mapper类  ,  Entity类  >
             ↑            ↑
           操作哪张表    操作什么数据类型
```

**示例：**

| 表 | Mapper 泛型 | ServiceImpl 泛型 |
|---|---|---|
| `user` | `BaseMapper<User>` | `ServiceImpl<UserMapper, User>` |
| `article` | `BaseMapper<Article>` | `ServiceImpl<ArticleMapper, Article>` |
| `category` | `BaseMapper<Category>` | `ServiceImpl<CategoryMapper, Category>` |
| `remote_data_record` | `BaseMapper<RemoteDataRecord>` | `ServiceImpl<RemoteDataMapper, RemoteDataRecord>` |

**规律：** 三个类共用同一个 Entity 名字：
- `UserMapper` ← 操作 User → `BaseMapper<User>`
- `UserService` ← 操作 User → `IService<User>`
- `UserServiceImpl` ← 操作 User → `ServiceImpl<UserMapper, User>`

---

## 常用 CRUD 方法（BaseMapper/IService 已内置）

### 查询

```java
// 查全部
List<User> list = userService.list();

// 按 ID 查
User user = userService.getById(1L);

// 条件查（Lambda 写法，编译期安全）
User user = userService.lambdaQuery()
        .eq(User::getUsername, "admin")
        .one();                    // 返回一条

List<User> list = userService.lambdaQuery()
        .eq(User::getStatus, 1)
        .orderByDesc(User::getCreatedAt)
        .list();                   // 返回列表

// 分页查
IPage<User> page = userService.page(
        new Page<>(1, 10),         // 第1页，每页10条
        new LambdaQueryWrapper<User>()
                .eq(User::getStatus, 1)
);
```

### 新增

```java
User user = new User();
user.setUsername("newUser");
user.setPassword("123456");
userService.save(user);   // INSERT，user.getId() 自动有值
```

### 更新

```java
User user = userService.getById(1L);
user.setNickname("新昵称");
userService.updateById(user);

// 条件更新
userService.lambdaUpdate()
        .eq(User::getStatus, 0)
        .set(User::getStatus, 1)
        .update();
```

### 删除

```java
// 逻辑删除（deleted = 1）
userService.removeById(1L);

// 物理删除
userService.getBaseMapper().deleteById(1L);  // 跳过逻辑删除
```

---

## DTO 校验注解速查

```java
@NotBlank                     // 字符串不能为 null 也不能是空串
@NotEmpty                     // 集合/数组不能为空
@NotNull                      // 任何类型不能为 null
@Size(min = 3, max = 50)      // 字符串长度范围
@Min(1) @Max(100)             // 数值范围
@Pattern(regexp = "^\\d+$")   // 正则校验
@Email                        // 邮箱格式
```

---

## 后端 → 前端数据流

```
浏览器请求
    │
    ▼
Controller (@RequestBody 接收 JSON → 自动转成 DTO/Entity)
    │
    ▼
Service (业务逻辑：查 → 改 → 存)
    │
    ▼
Mapper (MyBatis-Plus 自动生成 SQL)
    │
    ▼
MySQL
    │
    ▼ (返回一路反向回去)
    │
Controller (把结果包进 ApiResult，Spring 自动转 JSON)
    │
    ▼
浏览器拿到 JSON，渲染页面
```
