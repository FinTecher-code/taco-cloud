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
def reverse_vowels(s: str) -> str:
    arr = list(s)
    left, right = 0, len(arr) - 1
    vowels = set("aeiouAEIOU")

    while left < right:
        while left < right and arr[left] not in vowels:
            left += 1
        while left < right and arr[right] not in vowels:
            right -= 1
        arr[left], arr[right] = arr[right], arr[left]
        left += 1
        right -= 1

    return "".join(arr)


# 测试
if __name__ == "__main__":
    print(reverse_vowels("hello"))  # holle
    print(reverse_vowels("leeo"))   # loee
    print(reverse_vowels("xyz"))    # xyz
```

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
