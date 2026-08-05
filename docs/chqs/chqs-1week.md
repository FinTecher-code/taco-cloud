# 第一周 - 刷题记录

---

## 2026-07-11

---

### Q1 — 集合框架 — 继承关系

**来源:** 每日一练 App

**题目:** List、Set、Map 哪个继承自 Collection 接口？

**选项:**
1. List Set ✅
2. List Map
3. Set Map
4. List Map Set

**我的答案:** List Set ✅
**正确答案:** List Set

**解析:**
- `List` 和 `Set` 接口都直接继承自 `Collection` 接口，而 `Map` 是独立的顶层接口，**不继承** `Collection`

---

### Q2 — 集合框架 — HashMap 哈希冲突

**来源:** 每日一练 App

**题目:** 在 Java HashMap 中，当两个键的哈希值冲突时，如何处理？

**选项:**
1. 使用链表或红黑树存储在同一桶中 ✅
2. 重新计算整个哈希表的大小
3. 丢弃新插入的键值对
4. 抛出并发修改异常

**我的答案:** 使用链表或红黑树存储在同一桶中 ✅
**正确答案:** 使用链表或红黑树存储在同一桶中

**解析:**
- HashMap 采用**拉链法**处理哈希冲突。当多个键的哈希值相同时，它们存储在同一个桶中，最初以**链表**链接，链表长度超过阈值（默认 8）时转换为**红黑树**
- 选 B 的同学注意：扩容（resize）是在负载因子超标时触发，不是解决冲突的手段

---

### Q3 — 泛型 — ArrayList 声明

**来源:** 每日一练 App

**题目:** Java 中创建一个只能存放 String 的泛型 ArrayList 的语句是？

**选项:**
1. `ArrayList<String> al = new ArrayList<String>()` ✅
2. `ArrayList<int> al = new ArrayList<int>()`
3. `ArrayList al = new ArrayList<String>()`
4. `ArrayList<String> al = new List<String>()`

**我的答案:** `ArrayList<String> al = new ArrayList<String>()` ✅
**正确答案:** `ArrayList<String> al = new ArrayList<String>()`

**解析:**
- 正确声明方式，泛型参数为 String
- 泛型参数不能用 `int`，需用包装类 `Integer`
- `ArrayList al = new ArrayList<String>()` 左侧为裸类型（raw type），丢失类型安全
- `List` 是接口，不能直接 `new`

---

### Q4 — 泛型 — 易错题

**来源:** 每日一练 App

**题目:** 关于泛型的说法，下面选项中**错误的是**？

**选项:**
1. 数组中可以用泛型 ✅
2. `List<? extends T>` 可以接受任何继承自 T 类型的 List
3. 方法可以返回泛型类型
4. 不可以把 `List<String>` 传给 `List<Object>` 参数

**我的答案:** 数组中可以用泛型 ✅
**正确答案:** 数组中可以用泛型

**解析:**
- Java **不支持泛型数组**，如 `new ArrayList<String>[10]` 编译报错
- 泛型在运行时类型信息会被擦除，而数组需要具体的 reifiable 类型

---

### Q5 — JDBC — execute 方法

**来源:** 每日一练 App

**题目:** 哪个方法可以用来执行增删改查以及 DDL 语句？

**选项:**
1. `execute()` ✅
2. `executeUpdate()`
3. `executeQuery()`
4. `executeQueryAndUpdate()`

**我的答案:** `execute()` ✅
**正确答案:** `execute()`

**解析:**
- `execute()` 通用方法，可执行任意 SQL，返回 boolean
- `executeUpdate()` 用于 INSERT/UPDATE/DELETE/DDL，返回受影响行数
- `executeQuery()` 仅用于 SELECT
- `executeQueryAndUpdate()` 不是 JDBC 标准方法

---

### Q6 — 线程 — 线程与程序

**来源:** 每日一练 App

**题目:** 关于 Java 线程的说法中**错误的**一项是？

**选项:**
1. 线程就是程序 ✅
2. 线程是一个程序的单个执行流 ❌
3. 多线程是指一个程序的多个执行流
4. 多线程用于实现并发

**我的答案:** 线程是一个程序的单个执行流 ❌
**正确答案:** 线程就是程序

**解析:**
- 线程 ≠ 程序。程序是静态的代码集合，线程是程序中的一条执行路径
- 本题要找错误说法，即"线程就是程序"这一项（正确答案）

---

### Q7 — 集合框架 — HashMap 特性

**来源:** 每日一练 App

**题目:** 下列关于 Java 中 HashMap 集合说法正确的是？

**选项:**
1. 可以存储 null 值和 null 键 ✅
2. 底层是数组结构
3. 底层是链表结构
4. 不可以存储 null 值和 null 键 ❌

**我的答案:** 不可以存储 null 值和 null 键 ❌
**正确答案:** 可以存储 null 值和 null 键

**解析:**
- HashMap **允许一个 null 键和多个 null 值**
- `Hashtable` 才不允许 null，不要混淆

---

### Q8 — 线程 — 定义线程的方法

**来源:** 每日一练 App

**题目:** 下列哪个 Thread 类的方法定义了线程？

**选项:**
1. `run()` ✅
2. `init()` ❌
3. `application()`
4. `main()`

**我的答案:** `init()` ❌
**正确答案:** `run()`

**解析:**
- `run()` — 线程核心方法，`start()` 会调用 `run()` 来执行线程任务
- `init()` — Thread 内部初始化方法，不是定义线程任务的

---

### Q9 — 综合概念 — 找错

**来源:** 每日一练 App

**题目:** 下列关于 Java 中的相关概念说法错误的是？

**选项:**
1. 正则中 `.` 表示字符出现多次 ✅
2. `BufferedReader` 可以调用 `readLine()` 方法 ❌
3. `ByteArrayOutputStream` 相当于内存流
4. `DataOutputStream` 可以二进制写入 double

**我的答案:** `BufferedReader` 可以调用 `readLine()` 方法 ❌
**正确答案:** 正则中 `.` 表示字符出现多次

**解析:**
- 正则 `.` 匹配**任意单个字符**（除换行），不是"多次"
- 表示多次的是 `+`（一次或多次）或 `*`（零次或多次）

---

### Q10 — 注解 — Annotation 使用

**来源:** 每日一练 App

**题目:** 下列关于 Annotation 的使用正确的是？

**选项:**
1. 注解可用于类：`@MyAnnotation(value = "Hello")` 标注在类上
2. 注解可用于方法：`@MyAnnotation(value = "Hello")` 标注在方法上
3. 注解可用于字段：`@MyAnnotation(value = "Hello")` 标注在字段上
4. 其他三项均正确 ✅

**我的答案:** 其他三项均正确 ✅
**正确答案:** 其他三项均正确

**解析:**
- 注解可以应用于类、方法、字段等多个程序元素，A、B、C 都正确
- 示例代码：
  - 类注解：`@MyAnnotation(value = "Hello")` → `public class MyClass { }`
  - 方法注解：`@MyAnnotation(value = "Hello")` → `public void myMethod() { }`
  - 字段注解：`@MyAnnotation(value = "Hello")` → `private String myField;`

---

### Q11 — 并发工具 — CountDownLatch vs CyclicBarrier

**来源:** 每日一练 App

**题目:** 关于 CountDownLatch 和 CyclicBarrier 的说法正确的是？

**选项:**
1. CountDownLatch：等 countDown 到 0，释放后各走各路；不可重用
2. CyclicBarrier：N 个线程互相等待，全部到齐统一走；可通过 `reset()` 重置重用

**我的答案:** 本题原文件未提供明确用户选项，无法确定
**正确答案:** （见下方解析对比表）

**解析:**
- CountDownLatch — 等 countDown 到 0，释放后各走各路；一次性，不可重用
- CyclicBarrier — N 个线程互相等待，全部到齐统一走；可通过 `reset()` 重置重用

| 特性 | CountDownLatch | CyclicBarrier |
|:----|:--------------|:--------------|
| 机制 | 等 countDown 到 0，释放后各走各路 | N 个线程互相等待，全部到齐统一走 |
| 重用 | 一次性 | 可通过 `reset()` 重置重用 |

---

### Q12 — 反射机制

**来源:** 每日一练 App

**题目:** 执行下面的反射代码，输出结果是什么？

**解析:**
- 示例代码：
  ```java
  public class Person {
      public String name;
      private int age;
      public Person(String name, int age) { ... }
      private void hello() { ... }
  }
  ```
  ```java
  Class<?> cls = Class.forName("Person");
  Constructor<?> constructor = cls.getDeclaredConstructor(String.class, int.class);
  constructor.setAccessible(true);
  Object obj = constructor.newInstance("Alice", 25);

  Field field = cls.getDeclaredField("age");
  field.setInt(obj, -1);                      // ← 这里抛异常

  Method method = cls.getDeclaredMethod("hello");
  method.invoke(obj);
  ```

**选项:**
1. `age` 是私有字段导致编译错误 ✅
2. `hello()` 是私有方法导致编译错误
3. 设置负值导致运行时异常
4. 未设置 Field/Method 可访问导致运行时异常

**我的答案:** `age` 是私有字段导致编译错误 ✅
**正确答案:** `age` 是私有字段导致编译错误

**解析:**
- 关键陷阱：`constructor.setAccessible(true)` **只对构造器生效**，不会自动传给 Field 和 Method。`field` 没有单独调 `setAccessible(true)`，运行时抛 `IllegalAccessException`

---

### Q13 — Spring 事务传播特性

**来源:** 每日一练 App

**题目:** 下面有关 SPRING 的事务传播特性，说法错误的是？

**选项:**
1. PROPAGATION_REQUIRED：没事务就抛异常 ✅
2. PROPAGATION_SUPPORTS：没事务就以非事务方式执行
3. PROPAGATION_REQUIRES_NEW：新建事务，挂起当前事务
4. PROPAGATION_NESTED：支持当前事务，新增 Savepoint

**我的答案:** PROPAGATION_REQUIRED：没事务就抛异常 ✅
**正确答案:** PROPAGATION_REQUIRED：没事务就抛异常

**解析:**
- SUPPORTS — 有则加入，无则以非事务运行
- REQUIRED — **有则加入，无则新建**，而不是抛异常
- REQUIRES_NEW — 总是新建，已有事务则挂起
- NESTED — 基于 Savepoint 在当前事务中嵌套子事务

---

### Q14 — 并发编程 — 交错执行

**来源:** 每日一练 App

**题目:** 两个线程 T1、T2 并发执行 `foo()`，`a` 初值为 0。找出**不可能**出现的输出。

**解析:**
- 示例代码：
  ```c
  int a = 0;
  void foo() {
      if (a <= 0) a++;
      else        a--;
      printf("%d", a);
  }
  ```
- 假设 `printf`、`++`、`--` 是原子操作，但 `if` 与 `++/--` 之间**不是**原子操作块

**选项:**
1. 输出 10
2. 输出 12
3. 输出 01 ✅（不可能出现的输出）
4. 输出 22

**我的答案:** 输出 01 ✅
**正确答案:** 输出 01

**解析:**
- 全增（都读 a=0）：`a: 0→1→2`，输出 **22** 或 **12**
- 一增一减：`a: 0→1→0`，输出 **10** 或 **00**
- **01 不可能：** 增必须在减之前发生，所以 1 的 printf 一定先于 0 入列，输出为 "10" 而非 "01"

---

## 📊 统计数据

> **总分：** 10/13 ✅ | **正确率：** 76.9%

| # | 题目 | 知识点 | 状态 |
|:-:|:----|:------|:----:|
| 1 | List/Set/Map 与 Collection 关系 | 集合框架 | ✅ |
| 2 | HashMap 哈希冲突处理 | 集合框架 | ✅ |
| 3 | 泛型 ArrayList 声明 | 泛型 | ✅ |
| 4 | 泛型说法找错 | 泛型 | ✅ |
| 5 | JDBC execute 方法 | JDBC | ✅ |
| 6 | 线程与程序的关系 | 线程 | ❌ |
| 7 | HashMap 特性 | 集合框架 | ❌ |
| 8 | Thread 定义线程的方法 | 线程 | ❌ |
| 9 | Java 概念综合找错 | 综合 | ❌ |
| 10 | Annotation 使用 | 注解 | ✅ |
| 11 | CountDownLatch vs CyclicBarrier | 并发工具 | ✅ |
| 12 | 反射机制 | 反射 | ✅ |
| 13 | Spring 事务传播特性 | Spring | ✅ |
| 14 | 并发交错执行 | 并发编程 | ✅ |
