# Java 接口与抽象类

## 1. 接口 vs 抽象类的区别

### 语法层面

| | 接口 `interface` | 抽象类 `abstract class` |
|---|---|---|
| 构造器 | ❌ 没有 | ✅ 有 |
| 成员变量 | 只能 `public static final` 常量 | 可以有普通变量、各种修饰符 |
| 方法实现 | Java 8+ 可有 `default` 方法 | 可以有普通方法 |
| 继承 | 一个类可以 `implements` 多个接口 | 一个类只能 `extends` 一个抽象类 |
| 访问修饰符 | 方法默认 `public` | 可以是 `protected`、`public` 等 |

### 设计意图层面

- **抽象类** = **"是什么"**（is-a），是对一组有共同特征的事物的抽象，提取**共性代码**复用
  - 例如：`Animal` 是抽象类，猫和狗都是动物，共享"吃东西"的逻辑
- **接口** = **"能做什么"**（can-do），是定义一种**能力/契约**，不关心谁来实现
  - 例如：`Flyable` 是接口，鸟能飞，飞机也能飞，它们没有共同祖先，但都具备"飞"的能力

---

## 2. "面向接口编程"是什么意思

**核心思想**：代码中依赖接口，而不是具体实现类。这样换实现时，调用方代码不用改。

### ❌ 面向实现编程

写死了具体类，换不了：

```java
class RestaurantImpl implements Restaurant {
    public void takeOrder(OrderImpl o) {   // 依赖了具体类 OrderImpl
        System.out.println("订单金额：" + o.getTotalPrice());
    }
}
```

### ✅ 面向接口编程

依赖抽象，任何 `Order` 都能传进来：

```java
class RestaurantImpl implements Restaurant {
    public void takeOrder(Order o) {       // 依赖接口 Order
        System.out.println("订单金额：" + o.getTotalPrice());
    }
}
```

以后新增一个 `VIPDiscountOrder implements Order`（打折订单），`RestaurantImpl` 的代码**一行不用改**就能处理它。

---

## 3. 多态示例：一个方法处理多种对象，调用各自行为

```java
// 定义通知能力
interface Notifiable {
    void notify();
}

// 不同对象各自实现通知行为
class SMSNotification implements Notifiable {
    @Override
    public void notify() {
        System.out.println("发送短信通知");
    }
}

class EmailNotification implements Notifiable {
    @Override
    public void notify() {
        System.out.println("发送邮件通知");
    }
}

class WechatNotification implements Notifiable {
    @Override
    public void notify() {
        System.out.println("发送微信通知");
    }
}

// 一个方法，处理所有类型
public class NotificationService {
    public static void sendAll(List<Notifiable> channels) {
        for (Notifiable n : channels) {
            n.notify();  // 不关心具体是谁，只调用 notify()
        }
    }

    public static void main(String[] args) {
        List<Notifiable> channels = List.of(
            new SMSNotification(),
            new EmailNotification(),
            new WechatNotification()
        );
        sendAll(channels);
        // 输出：
        // 发送短信通知
        // 发送电子邮件通知
        // 发送微信通知
    }
}
```

**关键点**：`sendAll()` 方法完全不知道传进来的是什么具体类，它只认 `Notifiable` 接口。每种对象自己决定 `notify()` 怎么做——这就是**多态**，也是面向接口编程的核心价值。
