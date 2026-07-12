# Java 集合框架基础题

> **题目：** List、Set、Map 哪个继承自 Collection 接口？
> **类型：** 单选题
> **标签：** `Java`
> **分值：** 1 积分

## 选项

| 选项 | 内容 | 结果 |
|:----:|:----|:----:|
| A | List Map | ❌ |
| B | Set Map | ❌ |
| C | **List Set** | ✅ **正确** |
| D | List Map Set | ❌ |

## 解析

`List` 和 `Set` 接口都直接继承自 `Collection` 接口，而 `Map` 是独立的顶层接口，**不继承** `Collection`。

# Java HashMap 基础题

> **题目：** 在 Java HashMap 中，当两个键的哈希值冲突时，如何处理？
> **类型：** 单选题
> **标签：** `Java`
> **分值：** 1 积分

## 选项

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | **使用链表或红黑树存储在同一桶中** | ✅ **正确** |
| B | 重新计算整个哈希表的大小 | ❌ |
| C | 丢弃新插入的键值对 | ❌ |
| D | 抛出并发修改异常 | ❌ |

## 解析

HashMap 采用**拉链法**处理哈希冲突：当多个键的哈希值相同（冲突）时，它们被存储在同一个桶（bucket）中，最初以**链表**形式链接，当链表长度超过阈值（默认 8）时转换为**红黑树**以优化查询性能。

> 你当初选的 B（重新计算哈希表大小）是**扩容（resize/rehash）**操作，在负载因子超标时触发，而不是解决冲突的手段，容易混淆 😄

# Java 泛型基础题

> **题目：** Java 中，创建一个只能存放 String 的泛型 ArrayList 的语句是？
> **类型：** 单选题
> **标签：** `Java`
> **分值：** 1 积分
> **状态：** ✅ 回答正确 +1 积分

## 选项

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | `ArrayList<int> al = new ArrayList<int>()` | ❌ |
| B | **`ArrayList<String> al = new ArrayList<String>()`** | ✅ **正确** |
| C | `ArrayList al = new ArrayList<String>()` | ❌ |
| D | `ArrayList<String> al = new List<String>()` | ❌ |

## 解析

- **A：** 泛型参数不能用基本类型 `int`，需用包装类 `Integer`。
- **B：** ✅ 正确声明方式，指定类型参数为 `String`。
- **C：** 左侧裸类型（raw type）丢失泛型安全检查。
- **D：** `List` 是接口，不能直接 `new` 实例化。

# Java 泛型易错题

> **题目：** 在 Java 程序中，关于泛型的说法，下面选项中**错误的是**？
> **类型：** 单选题
> **标签：** `Java`
> **分值：** 1 积分
> **状态：** ✅ 回答正确 +1 积分

## 选项

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | `List<? extends T>` 可以接受任何继承自 T 类型的 List | ❌ |
| B | 方法可以返回泛型类型 | ❌ |
| C | 不可以把 `List<String>` 传递给一个接受 `List<Object>` 参数的方法 | ❌ |
| D | **数组中可以用泛型** | ✅ **错误（本题答案）** |

## 解析

题目问的是"错误的说法"，所以 D 是正确答案：

- **A：** 正确。`? extends T` 是泛型通配符上界，可以接受 T 及其子类型。
- **B：** 正确。方法可以声明泛型返回值，如 `<T> T get(T t)`。
- **C：** 正确。泛型是不变的（invariant），`List<String>` 不是 `List<Object>` 的子类型，不能传递。
- **D：** ❌ **错误。** Java 不支持泛型数组，如 `new ArrayList<String>[10]` 会编译报错，因为泛型在运行时类型信息会被擦除，而数组需要具体的 reifiable 类型。

# Java JDBC 基础题

> **题目：** 一般哪个方法可以用来执行数据库中增、删、改、查以及创建、删除表的 SQL 语句？
> **类型：** 单选题
> **标签：** `Java`
> **分值：** 1 积分
> **状态：** ✅ 回答正确 +1 积分

## 选项

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | `executeUpdate()` | ❌ |
| B | **`execute()`** | ✅ **正确** |
| C | `executeQuery()` | ❌ |
| D | `executeQueryAndUpdate()` | ❌ |

> **正确答案：选项 2**

## 解析

在 JDBC 的 `Statement` 接口中：

- **`execute()`** ✅ 通用方法，可以执行任意 SQL（SELECT / INSERT / UPDATE / DELETE / CREATE / DROP 等），返回 `boolean` 指示是否有 ResultSet。
- **`executeUpdate()`** 主要用于 INSERT、UPDATE、DELETE 及 DDL，返回受影响行数，无法处理 SELECT。
- **`executeQuery()`** 仅用于 SELECT 查询，返回 `ResultSet`。
- **`executeQueryAndUpdate()`** 不是 JDBC 标准方法。

# Java 线程基础题

> **题目：** 关于 Java 线程的说法中**错误的**一项是？
> **类型：** 单选题
> **标签：** `Java`
> **分值：** 1 积分
> **状态：** ❌ 回答错误

## 选项

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | **线程就是程序** | ✅ **错误（本题答案）** |
| B | 线程是一个程序的单个执行流 | ❌ 你选的 |
| C | 多线程是指一个程序的多个执行流 | ❌ |
| D | 多线程用于实现并发 | ❌ |

> **正确答案：选项 1**

## 解析

题目问的是"错误的说法"：

- **A：** ❌ **错误。** 线程 ≠ 程序。程序是静态的代码集合，线程是程序中的一条执行路径。一个程序可以包含多个线程。
- **B：** 正确。线程是一个程序中的单个执行流（单线程）。
- **C：** 正确。多线程就是一个程序同时运行多个执行流。
- **D：** 正确。多线程的典型用途就是实现并发执行。

# Java HashMap 基础题

> **题目：** 下列关于 Java 中 HashMap 集合说法正确的是？
> **类型：** 单选题
> **标签：** `Java`
> **分值：** 1 积分
> **状态：** ❌ 回答错误

## 选项

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | 底层是数组结构 | ❌ |
| B | 底层是链表结构 | ❌ |
| C | **可以存储 null 值和 null 键** | ✅ **正确** |
| D | 不可以存储 null 值和 null 键 | ❌ 你选的 |

> **正确答案：选项 3**

## 解析

- **A：** ❌ 不完整。HashMap 底层是 **数组 + 链表/红黑树**，不只是数组。
- **B：** ❌ 不完整。同 A。
- **C：** ✅ **正确。** HashMap **允许一个 null 键和多个 null 值**。`Hashtable` 才不允许 null。
- **D：** ❌ 混淆项。这是 `Hashtable` 的特性，不是 HashMap 的。

# Java 线程基础题

> **题目：** 下列哪个 Thread 类的方法定义了线程？
> **类型：** 单选题
> **标签：** `Java`
> **分值：** 1 积分
> **状态：** ❌ 回答错误

## 选项

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | `init()` | ❌ 你选的 |
| B | **`run()`** | ✅ **正确** |
| C | `application()` | ❌ |
| D | `main()` | ❌ |

> **正确答案：选项 2**

## 解析

- **`run()`** ✅ 线程的核心方法。线程启动后，`start()` 会调用 `run()` 中的代码来定义线程要执行的任务。
- **`init()`** ❌ Thread 内部确有 `init()` 方法（初始化线程），但不是用来"定义线程任务"的。
- **`application()` / `main()`** ❌ 均非 Thread 类的方法。

# Java 综合概念题

> **题目：** 下列关于 Java 中的相关概念说法错误的是？
> **类型：** 单选题
> **标签：** `Java`
> **分值：** 1 积分
> **状态：** ❌ 回答错误

## 选项

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | `BufferedReader` 可以调用 `readLine()` 方法 | ❌ 你选的 |
| B | `ByteArrayOutputStream` 可以相当于内存流 | ❌ |
| C | `DataOutputStream` 可以以二进制的方式写入 double | ❌ |
| D | **正则表达式中 `.` 表示字符出现多次** | ✅ **错误（本题答案）** |

> **正确答案：选项 4**

## 解析

- **A：** ✅ 正确。`BufferedReader` 确实有 `readLine()` 方法，用于按行读取文本。
- **B：** ✅ 正确。`ByteArrayOutputStream` 是一个内存中的输出流，数据写入字节数组。
- **C：** ✅ 正确。`DataOutputStream` 支持 `writeDouble()` 以二进制格式写入。
- **D：** ❌ **错误。** 正则中 `.` 匹配**任意单个字符**（除换行外），不是"出现多次"。表示多次的是 `+`（一次或多次）或 `*`（零次或多次）。

# Java 注解基础题

> **题目：** 下列关于 Annotation 的使用正确的是？
> **类型：** 单选题
> **标签：** `Java`
> **分值：** 1 积分
> **状态：** ✅ 回答正确 +1 积分

## 选项

**A. 注解可以用于类**

```java
@MyAnnotation(value = "Hello")
public class MyClass {
    // ...
}
```
**B. 注解可以用于方法**
```java
public class MyClass {
    @MyAnnotation(value = "Hello")
    public void myMethod() {
        // ...
    }
}
```
**C. 注解可以用于字段**
```java
public class MyClass {
    @MyAnnotation(value = "Hello")
    private String myField;
}
```
>**D. ✅ 其他三项均正确 ← 正确**

## 解析

注解（Annotation）可以应用于类、方法、字段等多个程序元素。选项 A、B、C 分别演示了这三个目标的使用方式，均正确，因此 D "其他三项均正确" 为正确答案。

# Java 并发工具类对比题

> **题目：** 下列关于 CountDownLatch 和 CyclicBarrier 的说法正确的是？

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | CountDownLatch 等待所有线程完成，CyclicBarrier 等待条件触发 | ❌ |
| B | CountDownLatch 可以重复使用，CyclicBarrier 只能使用一次 | ❌ |
| C | CountDownLatch 是线程互相等待，CyclicBarrier 是等待计数归零 | ❌ |
| D | **CountDownLatch 让线程到达同步点后执行不同任务，CyclicBarrier 让一组线程互相等待到达屏障** | ✅ **正确** |

## 解析
- **CountDownLatch：** 等 N 个操作 countDown 到 0，释放后各线程继续**各走各路**，一次性不可重用。
- **CyclicBarrier：** N 个线程互相等待，全部到齐后统一继续，可通过 `reset()` 重置重用。

# Java 反射机制题

> **题目：** 假设有以下 Java 类：

```java
public class Person {
    public String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private void hello() {
        System.out.println("Hello, " + this.name + "!");
    }
}
```

以下为测试代码：

```java
public class Test {
    public static void main(String[] args) {
        Class<?> cls = Class.forName("Person");
        Constructor<?> constructor = cls.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        Object obj = constructor.newInstance("Alice",25);
        Field field = cls.getDeclaredField("age");
        field.setInt(obj, -1);
        Method method = cls.getDeclaredMethod("hello");
        method.invoke(obj);
    }
}
```
> 执行上述代码后，输出结果是什么？

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | `age` 是私有字段，导致编译错误 | ✅ **正确** |
| B | `hello()` 是私有方法，导致编译错误 | ❌ |
| C | 试图设置负属性值，导致运行时异常 | ❌ |
| D | 未设置 Field 或 Method 可访问，导致运行时异常 | ❌ |

> **正确答案：选项 1**

## 解析

- **A：** ✅ **正确。** `age` 字段声明为 `private`，测试代码中 `field.setInt(obj, -1)` 之前**没有调用 `field.setAccessible(true)`**，运行时抛出 `IllegalAccessException`。
  > 注意：`constructor.setAccessible(true)` 只对构造器生效，不会自动传播到 Field 和 Method。

- **B：** ❌ 代码中确实也没给 `method` 调用 `setAccessible(true)`，但程序在第 14 行设置 `age` 字段时已经抛异常了，执行不到 `hello()`。

- **C：** ❌ 设置负值本身是合法的 int 值，不会抛异常。

- **D：** ❌ 正确描述了问题原因，但选项 A 更准确——具体是 `age` 字段的访问权限问题导致的异常。





# Spring 事务传播特性题

> **题目：** 下面有关 SPRING 的事务传播特性，说法错误的是？

| 选项 | 内容 | 结果 |
|:----:|:-----|:----:|
| A | `PROPAGATION_SUPPORTS`：支持当前事务，如果当前没有事务，就以非事务方式执行 | ✅ 正确 |
| B | **`PROPAGATION_REQUIRED`：支持当前事务，如果当前没有事务，就抛出异常** | ❌ **错误（本题答案）** |
| C | `PROPAGATION_REQUIRES_NEW`：新建事务，如果当前存在事务，把当前事务挂起 | ✅ 正确 |
| D | `PROPAGATION_NESTED`：支持当前事务，新增 Savepoint 点，与当前事务同步提交或回滚 | ✅ 正确 |

> **正确答案：选项 2**

## 解析

- **SUPPORTS：** ✅ 有事务则加入，无事务则以非事务方式运行。
- **REQUIRED：** ❌ 描述错误。正确行为是**支持当前事务，没有事务则新建一个事务**，而不是抛出异常。
- **REQUIRES_NEW：** ✅ 总是新建事务，已有事务则挂起。
- **NESTED：** ✅ 基于 Savepoint 机制，在当前事务中嵌套子事务，与主事务同步提交/回滚。

# 并发编程题

> **题目：** 两个等价线程 T1、T2 并发执行 `foo()`，`a` 初值为 0。
> 假设 `printf`、`++`、`--` 是原子操作，但 `if` 条件判断与后续 `++/--` 之间不是原子操作块。
> 考虑所有可能的交错执行顺序，找出**不可能**出现的输出。

```c
int a = 0;

void foo() {
    if (a <= 0) a++;
    else         a--;
    printf("%d", a);
}

| 选项 | 输出 | 结果 |
|:----:|:----:|:----:|
| A | 10 | ✅ 可能 |
| B | 12 | ✅ 可能 |
| C | **01** | ❌ **不可能（本题答案）** |
| D | 22 | ✅ 可能 |

> **正确答案：选项 3**

## 解析

T1、T2 各执行 `foo()` 一次，`a` 初值为 0。列举所有交错执行：

- **全增（都读 a=0）：** `a` 从 0 → 1 → 2，输出 **22**（先后打印均为 2）或 **12**（中间有打印）。
- **一增一减：** `a` 从 0 → 1 → 0，输出 **10** 或 **00**。
- **01 不可能：** 若要前一位打印 0、后一位打印 1，意味着前一位打印时 `a=0`、后一位打印时 `a=1`。但 `a` 只能在后面被**递减**（从 1 到 0）或**递增**（从 0 到 1 或更高），不存在先 0 后 1，因为打印 0 意味着 a 已被减回了 0，后续再无递增机会。