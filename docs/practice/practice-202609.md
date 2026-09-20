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

**Python 代码：**
```python
import sys
from collections import Counter

# ===== 请在下方填空实现函数 =====
def frequencySort(s):
    freq = Counter(s)
    chars = sorted(freq.keys(), key=lambda c: (-freq[c], ord(c)))
    return ''.join(c * freq[c] for c in chars)
# ==============================

# 以下为固定的已知代码，仅获取参数并调用业务函数
if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python frequency_sort.py <STRING>")
        sys.exit(1)
    print(frequencySort(sys.argv[1]))
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

**Python 代码：**
```python
import sys

# ===== 请在下方填空实现函数 =====
def nextPermutation(s):
    arr = list(s)
    n = len(arr)
    # 1. 从右往左找第一个 arr[i] < arr[i+1]
    i = n - 2
    while i >= 0 and arr[i] >= arr[i + 1]:
        i -= 1
    if i < 0:
        return "-"
    # 2. 从右往左找第一个大于 arr[i] 的字符并交换
    j = n - 1
    while arr[j] <= arr[i]:
        j -= 1
    arr[i], arr[j] = arr[j], arr[i]
    # 3. 反转尾部（降序→升序，保证后缀最小）
    arr[i + 1:] = reversed(arr[i + 1:])
    return ''.join(arr)
# ==============================

# 以下为固定的已知代码，请勿修改
if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python next_perm.py <S>")
        sys.exit(1)
    print(nextPermutation(sys.argv[1]))
```

---

<!-- 后续题目贴这里，格式同上 -->
