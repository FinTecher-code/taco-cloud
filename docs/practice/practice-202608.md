# 2026年8月 — 编程题练习

---

## 08-19

### 题目 1：字符串中的元音字母反转

**题目描述：**
给定一个字符串 `s`，将字符串中的所有元音字母（`a`、`e`、`i`、`o`、`u`）的位置反转，其他字符保持原位。

**规则：**
- `1 ≤ s.length ≤ 3 * 10^5`
- `s` 只包含可打印的 ASCII 字符
- 只反转元音字母，辅音和其他字符位置不变

**输入：** 一个字符串 `s`
**输出：** 反转元音字母后的字符串

**示例：**
```
输入: "hello"
输出: "holle"
```
解析：元音字母为 e、o，位置互换后得到 holle

```
输入: "leeo"
输出: "loee"
```
解析：元音字母 e、e、o 反转之后得到 o、e、e

---

#### 解题思路

经典 **双指针（Two Pointers）** 问题：
1. 左指针 `left` 从开头向右找元音，右指针 `right` 从结尾向左找元音
2. 两个指针都指向元音时，交换两个字符
3. `left`、`right` 继续移动，直到 `left >= right`

时间复杂度 O(n)，空间复杂度 O(n)（字符串转字符数组）。

---

#### Java 实现

```java
public class Solution {
    public String reverseVowels(String s) {
        char[] arr = s.toCharArray();
        int left = 0, right = arr.length - 1;
        String vowels = "aeiouAEIOU";

        while (left < right) {
            // 左指针向右找元音
            while (left < right && vowels.indexOf(arr[left]) == -1) {
                left++;
            }
            // 右指针向左找元音
            while (left < right && vowels.indexOf(arr[right]) == -1) {
                right--;
            }
            // 交换
            char tmp = arr[left];
            arr[left] = arr[right];
            arr[right] = tmp;
            left++;
            right--;
        }
        return new String(arr);
    }

    // 测试
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.reverseVowels("hello"));  // holle
        System.out.println(sol.reverseVowels("leeo"));   // loee
        System.out.println(sol.reverseVowels("xyz"));    // xyz
    }
}
```

---

#### Python 实现

```python
import sys

def reverse_vowels(s):
    vowels = set("aeiouAEIOU")
    chars = list(s)
    left, right = 0, len(chars) - 1

    while left < right:
        while left < right and chars[left] not in vowels:
            left += 1
        while left < right and chars[right] not in vowels:
            right -= 1

        chars[left], chars[right] = chars[right], chars[left]
        left += 1
        right -= 1

    return "".join(chars)

if __name__ == '__main__':
    print(reverse_vowels(sys.argv[1]))
```

> 在线平台版：输入通过 `sys.argv[1]` 传入，结果用 `print` 输出，兼容 Python 3.7。

---

#### 易错点

| 注意 | 说明 |
|------|------|
| 大小写都要考虑 | 元音包括 `AEIOU`，不能只判小写 |
| 字符串不可变 | Java 的 `String` 不能原地修改，需转 `char[]`；Python 需转 `list` |
| 内层循环要加 `left < right` | 防止指针越界或交叉 |
| 交换后指针要移动 | 否则死循环 |
| 无元音直接返回原串 | 如 `"xyz"` 应返回 `"xyz"` |

---

#### ❌ 错误版本分析

```java
class Solution {
    public String reverseVowels(String s) {
        char[] arr = s.toCharArray();
        int i = 0, j = arr.length - 1;
        while (i < j) {
            if ("aeiou".indexOf(arr[i]) == -1) i++;
            if ("aeiou".indexOf(arr[j]) == -1) j--;
            char tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
            i++;
            j--;
        }
        return new String(arr);
    }
}
```

**错误分析：**

| # | 错误 | 说明 | 修正 |
|:-:|------|------|------|
| 1 | 漏掉大写元音 | 只判 `"aeiou"`，`A/E/I/O/U` 不会被反转 | 补 `"AEIOU"` |
| 2 | 用两个 `if` 而非 `while` | 一次只跳过一个非元音，遇到连续非元音时会在非元音位置直接交换 | 改成 `while` 循环连续跳过 |
| 3 | 交换前没判断是否都指向元音 | 即使指针指向非元音也交换，破坏原字符串 | 先确保 `i`、`j` 都停在元音上再交换 |
| 4 | 缺少 `i < j` 保护 | `i++`/`j--` 可能越界 | 内层循环加 `left < right` 条件 |

**本质问题：** 双指针的核心是"先各自定位到元音，再交换"，而错误版本把定位和交换混在一起，导致非元音也被交换。

---

### 题目 2：交易记录异常分析

**题目描述：**

在某企业实时交易监控系统中，需要分析可能存在的重复交易记录。该系统日志中的交易记录被各种非数字字符分隔，每条交易记录包含交易编号和交易金额。需要开发一个程序来统计具有相同交易金额的记录。

给定一个包含数字和其他字符的字符串，表示原始的交易日志。每个交易记录由交易编号和金额组成，两者之间用字母 `m` 分隔。需要提取出所有的交易记录，统计交易金额相同的记录数量。交易编号在比较时会去除前导零，例如 `"001"` 和 `"1"` 视为相同的交易编号。

**输入：**
- 输入为一个字符串，包含数字、小写字母
- 字符串长度范围：`1 ≤ length ≤ 10^5`
- 字符串中至少包含一个有效的交易记录
- 每个有效交易记录格式为 `编号m金额`，如 `123m456` 表示编号 123、金额 456 的交易
- 金额范围：`1 ≤ 金额 ≤ 10^9`

**输出：**

返回一个整数，表示金额相同的记录中，**不重复交易编号的最大数量**（编号相同的记录不重复计数）。

**示例：**

```
输入: "tx123m456atm34m456p8m789q34m456"
输出: 2
```
解析：提取出的记录为
- 编号 123，金额 456
- 编号 34，金额 456
- 编号 8，金额 789
- 编号 34，金额 456

金额 456 对应的不重复编号有 2 个（123 和 34），金额 789 对应的不重复编号有 1 个（8），因此返回较大值 2。

**测试用例：**

| # | 输入 | 输出 |
|---|------|------|
| 1 | `payment001m100payment1m100payment01m200` | 1 |
| 2 | `trade99me888order88m888note77m888` | 2 |
| 3 | `tx123m456tx124m789tx125m456tx126am456tx127am456` | 2 |

---

#### 解题思路

1. **提取记录**：用正则 `(\d+)m(\d+)` 提取所有 `编号m金额` 三元组。**关键点：编号和金额都必须紧贴字母 `m`**——中间不能夹其他字母，否则不构成有效记录（例如 `126am456` 中 `126` 与 `m` 之间隔了 `a`，不匹配）。
2. **去除前导零**：编号转成整数（`int()` / `Integer.parseInt()` 自动去掉前导零），这样 `001` 和 `1` 归为同一编号。
3. **按金额分组去重编号**：以金额为 key，用 `Set` 存该金额下所有不重复编号。
4. **取最大值**：统计每个金额对应的去重编号个数，返回其中最大者。

时间复杂度 O(n)，空间复杂度 O(n)。

---

#### Java 实现

```java
import java.util.*;
import java.util.regex.*;

public class Solution {
    public int maxSameAmount(String s) {
        Map<String, Set<Integer>> groups = new HashMap<>();
        Pattern p = Pattern.compile("(\\d+)m(\\d+)");
        Matcher m = p.matcher(s);

        while (m.find()) {
            String amount = m.group(2);            // 金额
            int id = Integer.parseInt(m.group(1)); // 编号，去前导零
            groups.computeIfAbsent(amount, k -> new HashSet<>()).add(id);
        }

        int max = 0;
        for (Set<Integer> ids : groups.values()) {
            max = Math.max(max, ids.size());
        }
        return max;
    }

    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.maxSameAmount("tx123m456atm34m456p8m789q34m456")); // 2
        System.out.println(sol.maxSameAmount("payment001m100payment1m100payment01m200")); // 1
        System.out.println(sol.maxSameAmount("trade99me888order88m888note77m888"));   // 2
    }
}
```

---

#### Python 实现

```python
import sys
import re
from collections import defaultdict

def max_same_amount(s):
    groups = defaultdict(set)
    # 编号、金额都必须紧贴 m
    for id_str, amount in re.findall(r'(\d+)m(\d+)', s):
        groups[amount].add(int(id_str))  # int 去除前导零
    return max(len(v) for v in groups.values()) if groups else 0

if __name__ == '__main__':
    print(max_same_amount(sys.argv[1]))
```

> 在线平台版：输入通过 `sys.argv[1]` 传入，结果用 `print` 输出，兼容 Python 3.7。

---

#### 易错点

| 注意 | 说明 |
|------|------|
| 正则不能加 `[a-z]*` | 编号和金额必须**紧贴 `m`**。若写成 `(\d+)[a-z]*m[a-z]*(\d+)` 会把 `99me888` 误判为记录（金额应为 888），导致结果偏大 |
| 编号要去前导零 | 用 `int()` / `Integer.parseInt()`，否则 `001` 和 `1` 被当成不同编号，结果偏大 |
| 用 `Set` 去重编号 | 同一金额下的重复编号（如 34 出现两次）只算一次 |
| 金额之间的字母会断开记录 | `126am456` 中 `126` 与 `m` 被 `a` 隔开，不是有效记录，不应计入 |
| 边界：至少一条记录 | 题意保证至少一条有效记录，但代码仍对 `groups` 为空做了保护 |
