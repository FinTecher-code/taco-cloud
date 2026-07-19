# 2026年7月 — 编程题练习

---

## 07-19

### 题目 1：计算字符串中数字字符的平均值

**题目描述：**
给定一个字符串，计算其中数字字符（0-9）的平均值。

**规则：**
- 字符串长度不超过 1000
- 只考虑数字字符
- 如果没有数字字符返回 0
- 平均值保留 2 位小数

**输入：** 一个字符串
**输出：** 数字字符的平均值（保留两位小数）

**示例：**
```
输入: "a1b2c3"
输出: 2.00
```
解析：数字字符为 1, 2, 3，平均值 (1+2+3)/3 = 2.00

---

#### 错误版本分析

```java
class Solution {
    public static double averageOfDigits(String s) {
        double sum = 0.0;
        // 代码逻辑写这
        for(i = 0; i<len(s);i++){
            if s[i] is char:
                sum += s[i]
        }
        return sum/len(s)
    }
}
```

**共 9 个错误：**

| # | 错误 | 说明 | 修正 |
|:-:|------|------|------|
| 1 | `for(i = 0...)` | 变量 `i` 未声明类型 | `for(int i = 0...)` |
| 2 | `len(s)` | Java 中没有 `len()` 函数 | `s.length()` |
| 3 | `s[i]` | String 不能用数组下标访问 | `s.charAt(i)` |
| 4 | `if s[i] is char:` | Java 没有 `is` 关键字，语法错误 | `if (Character.isDigit(...))` |
| 5 | `sum += s[i]` | 加的是 ASCII 码（'0'=48），不是数字值 | `sum += (s.charAt(i) - '0')` |
| 6 | 缺少分号 | 赋值和 return 语句后需加分号 | 补 `;` |
| 7 | `return sum/len(s)` | 分母是字符串长度，应为数字个数 | 用 `count` 变量记录数字个数 |
| 8 | 未计数字个数 | 没统计有多少个数字字符 | 定义 `int count = 0;` 并递增 |
| 9 | 空/无数字时除零 | `count = 0` 时除零异常 | 先判 `count == 0` 返回 0 |

---

#### 正确实现

```java
public class Solution {
    /**
     * 计算字符串中数字字符的平均值
     * @param s 输入字符串
     * @return 平均值，保留两位小数
     */
    public static double averageOfDigits(String s) {
        if (s == null || s.isEmpty()) {
            return 0.0;
        }

        int sum = 0;
        int count = 0;

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') {  // 或用 Character.isDigit(ch)
                sum += ch - '0';           // 字符转数字
                count++;
            }
        }

        if (count == 0) {
            return 0.0;
        }

        // 保留两位小数
        double avg = (double) sum / count;
        return Double.parseDouble(String.format("%.2f", avg));
    }

    // 测试
    public static void main(String[] args) {
        System.out.println(averageOfDigits("a1b2c3"));  // 2.00
        System.out.println(averageOfDigits("abc"));     // 0.00
        System.out.println(averageOfDigits(""));        // 0.00
        System.out.println(averageOfDigits("999"));     // 9.00
    }
}
```
