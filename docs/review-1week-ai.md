# 📖 Java 第一周复习笔记

> 面向对象 · 集合框架 · 泛型 · 注解 · 反射 · IO 流

---

## 📑 目录

- [一、面向对象三大特性](#一面向对象三大特性)
- [二、集合框架](#二集合框架)
- [三、泛型 + 注解 + 反射](#三泛型--注解--反射)
- [四、IO 流](#四io-流)

---

## 一、面向对象三大特性

### 🏠 封装 —— 把数据藏起来

对外暴露方法，对内隐藏细节。

```java
private String name;

public String getName() { return name; }
public void setName(String name) { this.name = name; }
```

**好处：**
- ✅ 保护数据不被随意改（年龄不能是负的）
- ✅ 内部逻辑改了，外部不用改
- ✅ IDEA 按 `Alt+Insert` 一键生成

---

### 🧬 继承 —— is-a 关系

子类 `extends` 父类，获得父类的非 `private` 成员。

| 要点 | 说明 |
|:----|:-----|
| Java 单继承 | 一个子类只能有一个父类 |
| 多个来源 | 要靠接口实现 |
| Liskov 替换 | 子类必须能完全替代父类 |

---

### 🔄 多态 —— 相同方法，不同表现

**三个条件：** 继承 + 方法重写 + 父类引用指向子类对象

```java
Animal a = new Dog();
a.eat();  // 调的是 Dog 重写后的 eat()
```

**实战价值：** 写通用方法，扩展时不用改旧代码。

---

### ⚖️ 接口 vs 抽象类

| | 抽象类 | 接口 |
|:--|:------|:-----|
| 关系 | is-a（Dog is-a Animal） | can-do（Duck can Fly） |
| 本质 | 有共同特征的类 | 能力契约 |
| Spring 应用 | — | 每个 Service 都先写接口再写实现 |

---

## 二、集合框架

> Java 后端每天打交道最多的就是集合，面试必问。

### 📋 List 家族

| 实现 | 底层结构 | 查 | 插删 | 适用场景 |
|:----|:---------|:--|:----|:--------|
| **ArrayList** | `Object[]` 数组 | ✅ O(1) | ❌ O(n) 移动元素 | 读多写少 |
| **LinkedList** | 双向链表 | ❌ O(n) 遍历 | ✅ O(1) 改指针 | 频繁插删，也可当队列/栈 |

> ArrayList 默认容量 10，扩容 1.5 倍。

---

### 🎭 Set 家族

| 实现 | 底层 | 特点 |
|:----|:-----|:-----|
| **HashSet** | HashMap | 不保证顺序，允许 null，判重用 `hashCode()` + `equals()` |
| **TreeSet** | 红黑树 | 自动排序 |

> 自定义对象放 HashSet 必须重写 `hashCode()` 和 `equals()`。

---

### 🔑 重点：HashMap（面试高频）

**JDK 8+ 结构：** 数组 + 链表 + 红黑树

**关键参数：**

| 参数 | 值 |
|:----|:---|
| 初始容量 | 16 |
| 负载因子 | 0.75 |
| 链表 → 红黑树 | 链表长度 ≥ 8 |
| 红黑树 → 链表 | ≤ 6 |
| 扩容倍数 | 2 倍（每次扩容所有元素重新 hash） |

**put 流程：**

```
1. 算 key 的 hash（高 16 位异或低 16 位）
2. (n-1) & hash 定位数组下标
3. 位置空 → 直接放
4. 冲突 → 判断 key 是否相等
   ├─ 相等 → 覆盖
   └─ 不相等 → 挂链表 / 红黑树
5. 超阈值 → 扩容
```

**get 流程：** 类似 put，定位桶后查链表 / 红黑树。

---

### 🎯 选型口诀

| 场景 | 选哪个 |
|:----|:------|
| 有序可重复，频繁查 | `ArrayList` |
| 频繁插删 | `LinkedList` |
| 去重不排序 | `HashSet` |
| key-value 存取 | `HashMap` |
| 线程安全 | `ConcurrentHashMap`（❌ 别用 Hashtable） |

---

## 三、泛型 + 注解 + 反射

### 🔹 泛型 —— 让代码更安全

**核心作用：** 编译时类型检查，把 `ClassCastException` 提前暴露。

| 形式 | 示例 |
|:----|:-----|
| 泛型类 | `class Box<T>` |
| 泛型方法 | `<T> T get(T t)` |
| 泛型接口 | `interface List<T>` |

**通配符（面试重点）：**

| 通配符 | 含义 | 特点 |
|:------|:-----|:-----|
| `?` | 任意类型 | — |
| `? extends T` | T 或 T 的子类 | 适合读，不适合写 |
| `? super T` | T 或 T 的父类 | 适合写，不适合读 |

> **PECS 原则：** Producer Extends, Consumer Super

**SpringBoot 中的应用：** `ResponseResult<T>` 封装返回、`List<User>` 集合操作、MyBatis Mapper 泛型接口。

---

### 🔹 注解 —— 给代码打标签

**本质：** 元数据，不改变代码行为，但框架会读取处理。

**三个元注解：**

| 注解 | 作用 |
|:----|:-----|
| `@Target` | 定义能用在哪（方法/类/字段） |
| `@Retention` | 保留级别（SOURCE/CLASS/**RUNTIME**） |

> Spring 的注解都是 `RUNTIME` 级别。

**Spring 里天天见的注解：**

```java
@SpringBootApplication   // 三个注解组合
@RestController / @Service / @Repository
@Autowired / @Transactional
@RequestMapping / @GetMapping
```

---

### 🔹 反射 —— 运行时看穿一切

**反射三步走：**

```
① 获取 Class 对象 →  Class.forName() / .class / getClass()
② 获取 Method / Field / Constructor
③ 调用 → invoke() / setAccessible()
```

**Spring 框架基于反射干活：**

| 框架特性 | 反射用途 |
|:--------|:---------|
| IoC 容器 | 反射实例化对象 |
| AOP | 反射 + 动态代理做切面 |
| MyBatis | 反射做结果集映射 |

> 反射性能比直接调用慢，带安全检查开销，但框架会缓存优化。

---

## 四、IO 流

### 📂 流 —— 读写数据的桥梁

**四个核心抽象类：**

| 方向 | 字节流 | 字符流 |
|:----|:------|:------|
| 输入 | `InputStream` | `Reader` |
| 输出 | `OutputStream` | `Writer` |

> 字节流万能（图片/视频/PDF 都能读），字符流只处理文本但自动处理编码不乱码。不确定用哪个就拿字节流。

---

### 💡 最常用的读文本写法（try-with-resources）

```java
try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

> try-with-resources 会自动关闭流，不用手动写 `close()`。

---

### ⚡ 缓冲流为什么快？

| 方式 | 读 1MB 文件所需系统调用 |
|:----|:----------------------|
| `FileInputStream` | **1024** 次（每次 1KB） |
| `BufferedInputStream` | **128** 次（每次 8KB 缓冲区） |

> 默认缓冲区 8192 字节（8KB）。

---

### 🆕 NIO 快速上手

NIO 是 IO 的升级版：通道双向、缓冲区核心、支持非阻塞。

日常开发最常用 `java.nio.file.Files` 工具类：

```java
// 读所有行
List<String> lines = Files.readAllLines(Paths.get("data.txt"));

// 写文件
Files.write(Paths.get("out.txt"), "Hello".getBytes());

// 复制文件
Files.copy(Paths.get("a.txt"), Paths.get("b.txt"));
```

---

### ❓ 面试常问

- 字节流和字符流区别？
- 缓冲流作用？
- try-with-resources 原理？
- NIO 和 BIO 区别？
- 文件复制有几种方式？

---

> 📅 复习日期：2026-07-11
