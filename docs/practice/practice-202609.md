# 2026年9月 — 编程题练习

---

## 09-16

### 题目 1：字符串重排（按频率降序 + ASCII 升序）

**题目描述：**
给定一个字符串，请将字符串里的字符按照出现的频率从高到低重新排列。如果两个字符出现的频率相同，则按照它们的 ASCII 码值升序排列。

**规则：**
- 统计字符串中每个字符的出现次数，将字符按频率降序排列
- 若频率相同，则按字符的 ASCII 码值升序排列
- 根据排好序的字符列表，依次将字符重复拼接其频率次数，组成最终结果字符串
- 命令行传入的参数为一个纯字符串（如 `tree`，包含字母、数字及常见符号，保证不包含空格）
- 最终输出也必须是一个纯字符串，不包含空格

**输入：** 一个纯字符串 `s`
**输出：** 按规则重排后的字符串

**示例：**
```
输入: "tree"
输出: "eert"
```
解析：统计 t:1, r:1, e:2 → 排序 e 频率最高排第一；t 和 r 频率相同为 1，按 ASCII 码升序 r(114) < t(116)，r 排在 t 前 → 顺序 e, r, t → 按频率拼接 e 两次 + r 一次 + t 一次 → eert

```
输入: "cccaaa"
输出: "aaaccc"
```
解析：统计 c:3, a:3 → 频率相同，按 ASCII 码升序 a(97) < c(99)，a 在前 → aaaccc

---

#### 解题思路

**统计 + 排序 + 拼接** 三步：
1. **统计频率**：ASCII 可打印字符范围固定，用长度为 256 的数组 `freq` 计数（比 HashMap 更简洁高效）
2. **排序**：收集出现过的字符，按「频率降序，频率相同按 ASCII 升序」排序（Comparator：先比频率 `freq[b] - freq[a]`，相同再比字符 `a - b`）
3. **拼接**：遍历排序后的字符列表，每个字符重复其频率次数拼到 StringBuilder

时间复杂度 O(n + k log k)（n 为字符串长度，k 为不同字符数，k ≤ 256 可视为常数，近似 O(n)），空间复杂度 O(k)。

---

#### Java 实现

```java
import java.util.ArrayList;
import java.util.List;

public class Solution {
    public String frequencySort(String s) {
        // 1. 统计每个字符出现次数（可打印 ASCII 字符 0-255）
        int[] freq = new int[256];
        for (char c : s.toCharArray()) {
            freq[c]++;
        }

        // 2. 收集出现过的字符，按（频率降序，ASCII 升序）排序
        List<Character> chars = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            if (freq[i] > 0) {
                chars.add((char) i);
            }
        }
        chars.sort((a, b) -> {
            if (freq[a] != freq[b]) {
                return freq[b] - freq[a]; // 频率降序
            }
            return a - b;                  // 频率相同，ASCII 升序
        });

        // 3. 按频率拼接
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            for (int i = 0; i < freq[c]; i++) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // 测试
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.frequencySort("tree"));    // eert
        System.out.println(sol.frequencySort("cccaaa"));  // aaaccc
        System.out.println(sol.frequencySort("Aabb"));    // bbAa（b 频率 2 > A/a 频率 1，ASCII 升序 A(65) < a(97)）
    }
}
```

---

#### Python 实现

```python
from collections import Counter

def frequencySort(s: str) -> str:
    # 1. 统计频率
    freq = Counter(s)
    # 2. 按（频率降序，ASCII 升序）排序
    chars = sorted(freq.keys(), key=lambda c: (-freq[c], ord(c)))
    # 3. 拼接
    return ''.join(c * freq[c] for c in chars)

# 测试
print(frequencySort("tree"))    # eert
print(frequencySort("cccaaa"))  # aaaccc
```

---

## 09-17

### 题目 2：寻找全排列的下一个数（Next Permutation）

**题目描述：**
给定一个由数字和大小写字母组成的字符串 `s`，你可以将其视为一个由字符组成的序列。请你找到比 `s` 的字典序大的、由相同字符重新排列而成的最小序列。如果不存在这样的序列（即 `s` 已经是所有排列中字典序最大的），则返回英文减号 `-`。

**字典序比较规则：**
按字符 ASCII 码数值比较：`'0' < '9' < 'A' < 'Z' < 'a' < 'z'`。从左到右比较，第一个不相等字符的 ASCII 值大的序列字典序更大。

**要求：**
- 必须使用原字符串的所有字符，不能多也不能少，只是重新打乱顺序
- 找到的序列必须严格大于原字符串的字典序，且是所有合法排列中字典序最小的那个
- 如果原字符串已经是降序排列（字典序最大），返回 `-`
- 字符串长度 1 ~ 1000

**示例：**
```
输入: 12345  → 输出: 12354（大于12345的最小排列）
输入: 54321  → 输出: -（完全降序，已是最大排列）
输入: aa     → 输出: -（两字符相同，无法严格更大）
```

---

#### 解题思路（Next Permutation 经典三步）

1. **从右往左找第一个升序对** `s[i] < s[i+1]` 的位置 `i`（即从右数第一个"不降"的拐点）
   - 若找不到（整个字符串从左到右降序），说明已是最大排列，返回 `-`
2. **从右往左找第一个大于 `s[i]` 的字符** `s[j]`，交换 `s[i]` 与 `s[j]`
   - 交换后，`i` 右边的部分仍是降序（字典序最大段）
3. **反转 `i+1` 到末尾**，把降序段变成升序，得到最小后缀 → 拼接即答案

**为什么正确：**
- 找拐点：字典序下一个排列必须"从右数第一个能变大的位置"变
- 交换最小的更大字符：保证增量最小
- 反转尾部：保证尾部是升序（最小），整体才是"最小的更大排列"

时间复杂度 O(n)，空间复杂度 O(n)（char 数组）。字符串长度 ≤ 1000，完全没问题。

---

#### Java 实现

```java
public class Solution {
    public String nextPermutation(String s) {
        char[] arr = s.toCharArray();
        int n = arr.length;

        // 1. 从右往左找第一个 arr[i] < arr[i+1] 的位置
        int i = n - 2;
        while (i >= 0 && arr[i] >= arr[i + 1]) {
            i--;
        }

        // 2. 整个序列降序（或长度1），已是最大排列
        if (i < 0) {
            return "-";
        }

        // 3. 从右往左找第一个大于 arr[i] 的字符
        int j = n - 1;
        while (arr[j] <= arr[i]) {
            j--;
        }

        // 4. 交换
        char tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;

        // 5. 反转 i+1 到末尾（降序→升序，保证后缀最小）
        reverse(arr, i + 1, n - 1);

        return new String(arr);
    }

    private void reverse(char[] arr, int l, int r) {
        while (l < r) {
            char tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
            l++;
            r--;
        }
    }

    // 测试
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.nextPermutation("12345"));  // 12354
        System.out.println(sol.nextPermutation("54321"));  // -
        System.out.println(sol.nextPermutation("aa"));     // -
        System.out.println(sol.nextPermutation("abc"));    // acb
        System.out.println(sol.nextPermutation("aab"));    // aba
        System.out.println(sol.nextPermutation("132"));    // 213
        System.out.println(sol.nextPermutation("21543"));  // 23145
    }
}
```

---

#### Python 实现

```python
def nextPermutation(s: str) -> str:
    arr = list(s)
    n = len(arr)

    # 1. 从右往左找第一个 arr[i] < arr[i+1]
    i = n - 2
    while i >= 0 and arr[i] >= arr[i + 1]:
        i -= 1

    # 2. 已是最降序（最大排列）
    if i < 0:
        return "-"

    # 3. 从右往左找第一个大于 arr[i] 的字符
    j = n - 1
    while arr[j] <= arr[i]:
        j -= 1

    # 4. 交换
    arr[i], arr[j] = arr[j], arr[i]

    # 5. 反转尾部（降序→升序）
    arr[i+1:] = reversed(arr[i+1:])

    return ''.join(arr)

# 测试
print(nextPermutation("12345"))  # 12354
print(nextPermutation("54321"))  # -
print(nextPermutation("aa"))     # -
print(nextPermutation("abc"))    # acb
print(nextPermutation("aab"))    # aba
print(nextPermutation("132"))    # 213
print(nextPermutation("21543"))  # 23145
```

---

<!-- 后续题目贴这里，格式同上 -->