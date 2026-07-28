# 第四周 - 刷题记录

---

## 2026-07-27

---

### Q1 — MySQL root 超级用户

**来源:** 每日一练 App

**题目:** MySQL 中预设拥有最高权限的超级用户是？

**选项:**
1. Administrator
2. DBA
3. admin
4. root ✅

**我的答案:** root ✅
**正确答案:** root

**解析:**
- root 是 MySQL 内置的最高权限超级用户，拥有所有权限
- Administrator 是 Windows 系统管理员，非 MySQL 概念
- DBA 是数据库管理员职位，不是用户
- admin 是常见用户名，但不是 MySQL 预设超级用户

---

### Q2 — MySQL BETWEEN 区间查询

**来源:** 每日一练 App

**题目:** 测试整数列 i 是否至少为 1 且不超过 10 的正确表达式是？

**选项:**
1. `i BETWEEN 1 AND 10` ✅
2. `i IN INTERVAL(1, 10)`
3. `i IN INTERVAL(0, 11)`
4. `i BETWEEN 0 AND 11`

**我的答案:** `i BETWEEN 1 AND 10` ✅
**正确答案:** `i BETWEEN 1 AND 10`

**解析:**
- `BETWEEN 1 AND 10` 等价于 `i >= 1 AND i <= 10`，包含边界值
- `IN INTERVAL` 不是 MySQL 合法语法
- `BETWEEN 0 AND 11` 范围过大，不符合要求

---

### Q3 — MySQL 水平分区模式

**来源:** 每日一练 App

**题目:** 水平分区模式中，大多数用到的模式为？

**选项:**
1. Range 分区 ✅
2. Hash 分区
3. Key 分区
4. List 分区

**我的答案:** Range 分区 ✅
**正确答案:** Range 分区

**解析:**
- Range 分区是最常用的水平分区模式，允许 DBA 将数据按范围划分
- 例如按年份分区：80年代、90年代、2000年代等
- 其他分区方式各有适用场景，但 Range 是使用最广泛的

---

## 2026-07-28

---

### Q4 — 给已有表添加主键约束

**来源:** 每日一练 App

**题目:** `ALTER TABLE tbl_name ADD PRIMARY KEY (column_list)` 的功能是？

**选项:**
1. 创建一个主键，不能为空不能重复
2. 创建一个主键，能为空不能重复
3. 创建一个主键，不能为空能重复
4. 创建一个索引主键，不能为空不可重复 ✅

**我的答案:** 选项4 ✅
**正确答案:** 选项4

**解析:**
- `ALTER TABLE ... ADD PRIMARY KEY` 用于给已存在的表添加主键约束
- 主键特性：**非空（NOT NULL）+ 唯一（UNIQUE）**
- MySQL 会自动为主键创建一个**索引主键**（clustered index）
- 选项4 准确描述了主键的全部特性：索引主键、不能为空、不可重复


### Q5 — MyISAM 全文索引查询优化

**来源:** 每日一练 App

**题目:** MyISAM 表 user（字段 id, name, email），name 和 email 均已建全文索引，最有效查询关键词的写法是？

**选项:**
1. `select id,name from user where name like '%关键词%' or email like '%关键词%';`
2. `select id,name from user where match(name,email) against('关键词' in Boolean mode);` ✅
3. `select id,name from user where match(name) against('关键词' in Boolean mode) or match(email) against('关键词' in Boolean mode);`
4. `select id,name from user where match(name) against('关键词' in Boolean mode) union select id,name from user where match(email) against('关键词' in Boolean mode);`

**我的答案:** 选项2 ✅
**正确答案:** 选项2

**解析:**
- `LIKE '%关键词%'` 无法走索引，大表性能差
- 选项2 使用**联合全文索引** `MATCH(name, email)`，一次检索两列，效率最高
- 选项3、4 虽然也用了全文索引，但需要拆成两次检索
- MyISAM 支持全文索引，`IN BOOLEAN MODE` 支持布尔运算符

---

## 📊 第四周错题汇总

| 日期 | 题数 | 答对 | 答错 |
|:----:|:----:|:----:|:----:|
| 07-27 | 3 | 3 | 0 |
| 07-28 | 2 | 2 | 0 |
| **合计** | **5** | **5** | **0** |
