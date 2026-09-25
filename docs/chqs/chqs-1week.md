# 第一周 - 刷题记录

---

## 2026-07-11

---

### Q1 — 集合框架 — 继承关系

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** List、Set、Map哪个继承自Collection接口

**选项:**
1. List Map
2. Set Map
3. List Set ✅
4. List Map Set

**我的答案:** 选项3 ✅

**正确答案:** 选项3

**解析:**
- Map接口继承了java.lang.Object类,但没有实现任何接口.

---
### Q2 — 集合框架 — HashMap 哈希冲突

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 在Java HashMap中，当两个键的哈希值冲突时，如何处理？

**选项:**
1. 使用链表或红黑树存储在同一桶中 ✅
2. 重新计算整个哈希表的大小
3. 丢弃新插入的键值对
4. 抛出并发修改异常

**我的答案:** 选项1 ✅

**正确答案:** 选项1

**解析:**
- 正确答案是：使用链表或红黑树存储在同一桶中。
- HashMap使用数组和链表（或红黑树）解决冲突，冲突元素存储在同一个桶中。其他选项均为错误。分别为：resize操作只在容量不足时触发，非冲突解决方式。不符合HashMap行为，HashMap不会丢弃元素或抛出异常。

---
### Q3 — 泛型 — ArrayList 声明

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Java 中，创建一个只能存放String的泛型ArrayList的语句是？

**选项:**
1. `ArrayList<int> al=new ArrayList<int>()`
2. `ArrayList<String> al=new ArrayList<String>()` ✅
3. `ArrayList al=new ArrayList<String>()`
4. `ArrayList<String> al =new List<String>()   `

**我的答案:** 选项2 ✅

**正确答案:** 选项2

**解析:**
- 通过指定`ArrayList<String>`表明这个`ArrayList`实例只能存放`String`类型的元素。`ArrayList`类在使用时可以在尖括号`<>`中指定具体的类型参数。

---
### Q4 — 泛型 — 易错题

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 在Java程序中，关于泛型的说法，下面选项中**错误**的是？

**选项:**
1. `List<? extends T>`为可以接受任何继承自T类型的List
2. 方法可以返回泛型类型
3. 不可以把`List<String>`传递给一个接受`List<Object>`参数的方法
4. 数组中可以用泛型 ✅

**我的答案:** 选项4 ✅

**正确答案:** 选项4

**解析:**
- Java的泛型是通过类型擦除来实现的，这意味着泛型信息在编译时会被擦除，并在运行时用原生类型（如Object）替换。由于数组在Java中是协变的（即`String[]`是`Object[]`的子类型），如果允许泛型数组，那么运行时类型信息可能会与编译时类型信息不一致，从而导致类型安全问题。因此，Java不允许创建泛型数组，例如`T[] array = new T[10];`是不合法的。

---
### Q5 — JDBC — execute 方法

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 一般哪个方法可以用来执行数据库中增、删、改、查以及创建、删除表的SQL语句？

**选项:**
1. executeUpdate()
2. execute() ✅
3. executeQuery()
4. executeQueryAndUpdate()

**我的答案:** 选项2 ✅

**正确答案:** 选项2

**解析:**
- Statement接口的executeQuery()能够执SQL 查询语句。executeUpdate()方法还能执行update/insert/delete等语句。Statement接口没有定义executeQueryAndUpdate()方法。

---
### Q6 — 线程 — 线程与程序

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下列关于 Java 线程的说法中，错误的一项是？

**选项:**
1. 线程就是程序 ✅
2. 线程是一个程序的单个执行流
3. 多线程是指一个程序的多个执行流
4. 多线程用于实现并发

**我的答案:** 选项2 ❌

**正确答案:** 选项1

**解析:**
- 线程是一个程序的单个执行流，而不是程序本身。
- 而多线程作为实现并发的一个重要手段，是一个程序的多个执行流。

---
### Q7 — 集合框架 — HashMap 特性

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下列关于 Java 中 HashMap 集合说法正确的是？

**选项:**
1. 底层是数组结构
2. 底层是链表结构
3. 可以存储null值和null键 ✅
4. 不可以存储null值和null键

**我的答案:** 选项3 ✅

**正确答案:** 选项3

**解析:**
- HashMap 是基于哈希表的 Map 接口的非同步实现。
- 此实现提供所有可选的映射操作，并允许使用 null 值和 null 键。
- 哈希表可以说就是数组链表，底层还是数组但是这个数组每一项就是一个链表。

---
### Q8 — 线程 — 定义线程的方法

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** Java中线程是由线程类的____方法定义的。

**选项:**
1. init()
2. run() ✅
3. application()
4. main()

**我的答案:** 选项1 ❌

**正确答案:** 选项2

**解析:**
- Java中，线程是由线程类的run()方法定义的，该方法包含了线程要执行的具体逻辑。

---
### Q9 — 综合概念 — 找错

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下列关于 Java 中的相关概念说法错误的是？

**选项:**
1. BufferedReader可以调用readLine()方法
2. ByteArrayOutputStream可以相当于内存流
3. DataOutputStream可以以二进制的方式写入double
4. 正则表达式中“.”表示字符出现多次 ✅

**我的答案:** 选项1 ❌

**正确答案:** 选项4

**解析:**
- "."代表一个字符的通配符，能和回车符之外的任何字符相匹配。

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

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 在Java并发编程中，CountDownLatch 和 CyclicBarrier 都可以用来协调多个线程的执行，下面对它们的用途描述最准确的是？

**选项:**
1. CountDownLatch 用于等待所有线程完成，而 CyclicBarrier 用于等待某个条件达到
2. CountDownLatch 用于等待某个条件达到，而 CyclicBarrier 用于等待所有线程完成
3. CountDownLatch 可以重复使用，而 CyclicBarrier 只能使用一次
4. CountDownLatch 允许线程在达到某个点后继续执行不同任务，而 CyclicBarrier 则让一组线程在某个点相互等待 ✅

**我的答案:** 选项1 ❌

**正确答案:** 选项4

**解析:**
- CountDownLatch 是一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程等待。CyclicBarrier 允许一组线程互相等待，直到所有线程都到达一个共同屏障点（common barrier point）。与 CountDownLatch 不同的是，CyclicBarrier 在屏障被打破后，还可以重新使用。但关键在于，CountDownLatch 主要用于等待所有线程完成某个任务，而 CyclicBarrier 则用于让一组线程在某个点相互等待，然后一起继续执行。

---
### Q12 — 反射机制

**来源:** 每日一练 App

**题目:** 执行下面的反射代码，输出结果是什么？

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

```java
public class Test {
    public static void main(String[] args) throws Exception {
        Class<?> cls = Class.forName("Person");
        Constructor<?> constructor = cls.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        Object obj = constructor.newInstance("Alice", 25);
        Field field = cls.getDeclaredField("age");
        field.setInt(obj, -1);
        Method method = cls.getDeclaredMethod("hello");
        method.invoke(obj);
    }
}
```

**选项:**
1. `age` 是私有字段导致编译错误
2. `hello()` 是私有方法导致编译错误
3. 设置负值导致运行时异常
4. 未设置 Field/Method 可访问导致运行时异常

**我的答案:** 选项1 ❌
**正确答案:** 选项4 ✅

**解析:**
- 反射代码编译期不检查私有访问，1、2 的"编译错误"都是干扰项
- 关键陷阱：`constructor.setAccessible(true)` **只对构造器生效**，不会传给后续的 Field 和 Method
- `field.setInt(obj, -1)` 时 field 没调 `setAccessible(true)`，从外部类反射访问 Person 私有字段 age → 运行时抛 `IllegalAccessException`，输出前就中断
- 即使 field 能过，`method.invoke(obj)` 调私有方法 hello() 也会因同样原因抛异常（method 也没 setAccessible）

---

### Q13 — Spring 事务传播特性

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 下面有关SPRING的事务传播特性，说法错误的是？

**选项:**
1. PROPAGATION_SUPPORTS：支持当前事务，如果当前没有事务，就以非事务方式执行
2. PROPAGATION_REQUIRED：支持当前事务，如果当前没有事务，就抛出异常 ✅
3. PROPAGATION_REQUIRES_NEW：新建事务，如果当前存在事务，把当前事务挂起
4. PROPAGATION_NESTED：支持当前事务，新增Savepoint点，与当前事务同步提交或回滚

**我的答案:** 选项2 ✅

**正确答案:** 选项2

**解析:**
- **Spring事务传播特性分析**
- - **选项：PROPAGATION_SUPPORTS**
- - 这种事务传播特性是支持当前事务。如果当前存在事务，就加入到这个事务中一起执行；如果当前没有事务，就以非事务方式执行。例如，在一个服务层方法调用另一个服务层方法时，如果外层方法已经开启了事务，内层方法设置为PROPAGATION_SUPPORTS就会加入外层事务；如果外层没有事务，内层就以非事务方式运行，这个选项描述正确。
- - **选项：PROPAGATION_REQUIRED**
- - PROPAGATION_REQUIRED的特性是如果当前没有事务，就新建一个事务；如果当前已经存在事务，就加入到当前事务中。而不是如选项中描述的如果当前没有事务就抛出异常，所以这个选项描述错误。
- - **选项：PROPAGATION_REQUIRES_NEW**
- - 此特性是新建一个事务。如果当前存在事务，会把当前事务挂起，然后新事务执行完毕后再恢复之前挂起的事务。例如，在一个复杂的业务场景中，可能有一些操作需要独立于主事务进行提交或回滚，就可以使用PROPAGATION_REQUIRES_NEW，这个选项描述正确。
- - **选项：PROPAGATION_NESTED**
- - PROPAGATION_NESTED是支持当前事务，并且会在当前事务中新增Savepoint（保存点）。嵌套事务可以独立于主事务进行部分回滚，最后和当前事务同步提交或回滚，这个选项描述正确。

---
### Q14 — 并发编程 — 交错执行

**来源:** 新版题库 Excel（2026-09-25 同步）

**题目:** 两个等价的线程（T1 和 T2）并发执行下列程序。`a` 是一个全局变量，初始值为 `0`。假设 `System.out.print`、`++`、`--` 操作本身都是原子性的，但 `if` 条件判断与后续的 `++` 或 `--` 操作之间不是一个原子操作块。请考虑所有可能的交错执行顺序，则下列哪一项输出是不可能出现的？
```java
static int a = 0; // 全局变量
public void foo() {
  if (a <= 0) {
     a++;
  } else {
     a--;
  }
  System.out.print(a);
}
```

**选项:**
1. 1 0
2. 1 2
3. 0 1 ✅
4. 2 2

**我的答案:** 选项3 ✅

**正确答案:** 选项3

**解析:**
- - 要打印 `0`，某个线程必须执行 **`a--`**，这要求它在条件判断时读到 `a > 0`。
- - 初始 `a = 0`，所以必须先有另一个线程执行 **`a++`** 使 `a` 变成 `1`。
- - 一旦那个线程 `++` 之后，`a` 最多只会被另一个线程 `--` 回 `0`，不会再被 `++`（因为每个线程只执行一次更新）。
- - 因此，如果在某一时刻打印了 `0`，之后不可能再出现 `1`；`0` 之后只可能是 `0`，或 `0` 在 `1` 之后出现（即 `1 0`）。

---
### Q15 — Arrays.sort 数组排序

**来源:** 每日一练 App

**题目:** 以下代码执行后，数组 x 中的元素值依次是？

```java
int[] x = {12, 35, 8, 7, 2};
Arrays.sort(x);
```

**正确答案:** `2, 7, 8, 12, 35`（升序）

**解析:**
- `Arrays.sort(int[])` 对基本类型数组做**升序**排序
- 注意：基本类型版本没有 Comparator 重载，想降序要用 `Integer[]` + `Comparator.reverseOrder()`

---

## 📊 第一周错题汇总

| 日期 | 题数 | 答对 | 答错 |
|:----:|:----:|:----:|:----:|
| 07-11 | 14 | 10 | 4 |
| **合计** | **14** | **10** | **4** |
