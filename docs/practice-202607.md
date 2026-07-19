# 2026年7月 — 编程题练习

---

## 07-19

### 题目：求字符串中数字字符的平均值

**代码（含错误）：**
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

**错误分析：**

| # | 错误 | 说明 | 正确写法 |
|:-:|------|------|---------|
| 1 | `for(i = 0...)` | 变量 `i` 未声明类型 | `for(int i = 0...)` |
| 2 | `len(s)` | Java 没有 `len()` 函数 | `s.length()` |
| 3 | `s[i]` | 字符串不能用数组下标访问 | `s.charAt(i)` |
| 4 | `if s[i] is char:` | 语法完全不对，Java 没有 `is` 关键字 | `if (Character.isDigit(s.charAt(i)))` |
| 5 | `sum += s[i]` | 直接加字符得到的是 ASCII 码（如 '0'=48），不是数字值 | `sum += s.charAt(i) - '0'` |
| 6 | 缺少分号 | `sum += s[i]` 和 `return` 后面都要加分号 | `sum += digit;` |
| 7 | `return sum/len(s)` | 除以字符串总长度，应该除以**数字的个数** | `return sum / count;` |
| 8 | 未定义数字的个数 | 没有变量记录到底有几个数字字符 | 加一个 `int count = 0;` 来计数 |
| 9 | 空字符串时除零风险 | 字符串无数字时 `count=0`，除零异常 | 加判空返回 0 |

**修正后的正确代码：**
```java
class Solution {
    public static double averageOfDigits(String s) {
        if (s == null || s.isEmpty()) return 0.0;
        
        double sum = 0.0;
        int count = 0;
        
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isDigit(ch)) {
                sum += ch - '0';
                count++;
            }
        }
        
        return count == 0 ? 0.0 : sum / count;
    }
}
```
