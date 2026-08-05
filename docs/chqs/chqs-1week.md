# 📚 Java 第一周刷题记录

> **总分：** 10/13 ✅ | **正确率：** 76.9%

---

## 📋 题目速览

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

---

## ✅ 回答正确的题目

---

### 1. 集合框架 — 继承关系

> **题目：** List、Set、Map 哪个继承自 Collection 接口？

| 选项 | 内容 |
|:----:|:-----|
| **C** ✅ | **List Set** |
| A ❌ | List Map |
| B ❌ | Set Map |
| D ❌ | List Map Set |

**解析：**
`List` 和 `Set` 接口都直接继承自 `Collection` 接口，而 `Map` 是独立的顶层接口，**不继承** `Collection`。

---

### 2. 集合框架 — HashMap 哈希冲突

> **题目：** 在 Java HashMap 中，当两个键的哈希值冲突时，如何处理？

| 选项 | 内容 |
|:----:|:-----|
| **A** ✅ | **使用链表或红黑树存储在同一桶中** |
| B ❌ | 重新计算整个哈希表的大小 |
| C ❌ | 丢弃新插入的键值对 |
| D ❌ | 抛出并发修改异常 |

**解析：**
HashMap 采用**拉链法**处理哈希冲突。当多个键的哈希值相同时，它们存储在同一个桶中，最初以**链表**链接，链表长度超过阈值（默认 8）时转换为**红黑树**。

> 选 B 的同学注意：扩容（resize）是在负载因子超标时触发，不是解决冲突的手段。

---

### 3. 泛型 — ArrayList 声明

> **题目：** Java 中创建一个只能存放 String 的泛型 ArrayList 的语句是？

| 选项 | 内容 |
|:----:|:-----|
| **B** ✅ | **`ArrayList<String> al = new ArrayList<String>()`** |
| A ❌ | `ArrayList<int> al = new ArrayList<int>()` |
| C ❌ | `ArrayList al = new ArrayList<String>()` |
| D ❌ | `ArrayList<String> al = new List<String>()` |

**解析：**
- **B** ✅ 正确声明方式
- **A** ❌ 泛型参数不能用 `int`，需用包装类 `Integer`
- **C** ❌ 左侧裸类型（raw type）丢失类型安全
- **D** ❌ `List` 是接口，不能直接 `new`

---

### 4. 泛型 — 易错题

> **题目：** 关于泛型的说法，下面选项中**错误的是**？

| 选项 | 内容 |
|:----:|:-----|
| **D** ✅ | **数组中可以用泛型 ← 错误说法** |
| A ❌ | `List<? extends T>` 可以接受任何继承自 T 类型的 List |
| B ❌ | 方法可以返回泛型类型 |
| C ❌ | 不可以把 `List<String>` 传给 `List<Object>` 参数 |

**解析：**
- **D** ❌ Java **不支持泛型数组**，如 `new ArrayList<String>[10]` 编译报错
- 泛型在运行时类型信息会被擦除，而数组需要具体的 reifiable 类型

---

### 5. JDBC — execute 方法

> **题目：** 哪个方法可以用来执行增删改查以及 DDL 语句？

| 选项 | 内容 |
|:----:|:-----|
| **B** ✅ | **`execute()`** |
| A ❌ | `executeUpdate()` |
| C ❌ | `executeQuery()` |
| D ❌ | `executeQueryAndUpdate()` |

**解析：**
- **`execute()`** ✅ 通用方法，可执行任意 SQL，返回 boolean
- **`executeUpdate()`** 用于 INSERT/UPDATE/DELETE/DDL，返回受影响行数
- **`executeQuery()`** 仅用于 SELECT
- **`executeQueryAndUpdate()`** ❌ 不是 JDBC 标准方法

---

### 10. 注解 — Annotation 使用

> **题目：** 下列关于 Annotation 的使用正确的是？

```java
// A. 注解可用于类 ✅
@MyAnnotation(value = "Hello")
public class MyClass { }

// B. 注解可用于方法 ✅
public class MyClass {
    @MyAnnotation(value = "Hello")
    public void myMethod() { }
}

// C. 注解可用于字段 ✅
public class MyClass {
    @MyAnnotation(value = "Hello")
    private String myField;
}
```

> **D** ✅ **其他三项均正确**

**解析：** 注解可以应用于类、方法、字段等多个程序元素，A、B、C 都正确。

---

### 11. 并发工具 — CountDownLatch vs CyclicBarrier

> **题目：** 关于 CountDownLatch 和 CyclicBarrier 的说法正确的是？

**解析：**

| 特性 | CountDownLatch | CyclicBarrier |
|:----|:--------------|:--------------|
| 机制 | 等 countDown 到 0，释放后各走各路 | N 个线程互相等待，全部到齐统一走 |
| 重用 | ❌ 一次性 | ✅ 可通过 `reset()` 重置重用 |

---

### 12. 反射机制

> **题目：** 执行下面的反射代码，输出结果是什么？

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

| 选项 | 内容 |
|:----:|:-----|
| **A** ✅ | **`age` 是私有字段导致编译错误** |
| B ❌ | `hello()` 是私有方法导致编译错误 |
| C ❌ | 设置负值导致运行时异常 |
| D ❌ | 未设置 Field/Method 可访问导致运行时异常 |

> **关键陷阱：** `constructor.setAccessible(true)` **只对构造器生效**，不会自动传给 Field 和 Method。`field` 没有单独调 `setAccessible(true)`，运行时抛 `IllegalAccessException`。

---

### 13. Spring 事务传播特性

> **题目：** 下面有关 SPRING 的事务传播特性，说法错误的是？

| 选项 | 内容 |
|:----:|:-----|
| **B** ✅ | **PROPAGATION_REQUIRED：没事务就抛异常 ← 错误说法** |
| A ❌ | PROPAGATION_SUPPORTS：没事务就以非事务方式执行 |
| C ❌ | PROPAGATION_REQUIRES_NEW：新建事务，挂起当前事务 |
| D ❌ | PROPAGATION_NESTED：支持当前事务，新增 Savepoint |

**解析：**
- **SUPPORTS** — 有则加入，无则以非事务运行
- **REQUIRED** — **有则加入，无则新建**，而不是抛异常
- **REQUIRES_NEW** — 总是新建，已有事务则挂起
- **NESTED** — 基于 Savepoint 在当前事务中嵌套子事务

---

### 14. 并发编程 — 交错执行

> **题目：** 两个线程 T1、T2 并发执行 `foo()`，`a` 初值为 0。找出**不可能**出现的输出。

```c
int a = 0;
void foo() {
    if (a <= 0) a++;
    else        a--;
    printf("%d", a);
}
```

> 假设 `printf`、`++`、`--` 是原子操作，但 `if` 与 `++/--` 之间**不是**原子操作块。

| 选项 | 输出 | 结果 |
|:----:|:----:|:----:|
| A | 10 | ✅ 可能 |
| B | 12 | ✅ 可能 |
| C | **01** | ❌ **不可能** |
| D | 22 | ✅ 可能 |

**解析：**
- **全增（都读 a=0）：** `a: 0→1→2`，输出 **22** 或 **12**
- **一增一减：** `a: 0→1→0`，输出 **10** 或 **00**
- **01 不可能：** 增必须在减之前发生，所以 1 的 printf 一定先于 0 入列，输出为 "10" 而非 "01"

---

## ❌ 回答错误的题目

---

### 6. 线程 — 线程与程序

> **题目：** 关于 Java 线程的说法中**错误的**一项是？

| 选项 | 内容 |
|:----:|:-----|
| **A** ✅ | **线程就是程序 ← 错误说法（正确答案）** |
| B ❌ 你选的 | 线程是一个程序的单个执行流 |
| C ❌ | 多线程是指一个程序的多个执行流 |
| D ❌ | 多线程用于实现并发 |

**解析：**
- **线程 ≠ 程序。** 程序是静态的代码集合，线程是程序中的一条执行路径

---

### 7. 集合框架 — HashMap 特性

> **题目：** 下列关于 Java 中 HashMap 集合说法正确的是？

| 选项 | 内容 |
|:----:|:-----|
| **C** ✅ | **可以存储 null 值和 null 键（正确答案）** |
| A ❌ | 底层是数组结构 |
| B ❌ | 底层是链表结构 |
| D ❌ 你选的 | 不可以存储 null 值和 null 键 |

**解析：**
- HashMap **允许一个 null 键和多个 null 值**
- `Hashtable` 才不允许 null，不要混淆

---

### 8. 线程 — 定义线程的方法

> **题目：** 下列哪个 Thread 类的方法定义了线程？

| 选项 | 内容 |
|:----:|:-----|
| **B** ✅ | **`run()`（正确答案）** |
| A ❌ 你选的 | `init()` |
| C ❌ | `application()` |
| D ❌ | `main()` |

**解析：**
- **`run()`** — 线程核心方法，`start()` 会调用 `run()` 来执行线程任务
- **`init()`** — Thread 内部初始化方法，不是定义线程任务的

---

### 9. 综合概念 — 找错

> **题目：** 下列关于 Java 中的相关概念说法错误的是？

| 选项 | 内容 |
|:----:|:-----|
| **D** ✅ | **正则中 `.` 表示字符出现多次 ← 错误说法（正确答案）** |
| A ❌ 你选的 | `BufferedReader` 可以调用 `readLine()` 方法 |
| B ❌ | `ByteArrayOutputStream` 相当于内存流 |
| C ❌ | `DataOutputStream` 可以二进制写入 double |

**解析：**
- **D** ❌ 正则 `.` 匹配**任意单个字符**（除换行），不是"多次"
- 表示多次的是 `+`（一次或多次）或 `*`（零次或多次）

---

> 📅 刷题日期：2026-07-11
