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

**Python 代码：**
```python
import sys

if __name__ == '__main__':
    s = sys.argv[1]
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
    print("".join(chars))
```

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

**Python 代码：**
```python
import sys
import re

def analyze_transactions(word):
    groups = {}
    # 编号、金额都必须紧贴 m
    for id_str, amount in re.findall(r'(\d+)m(\d+)', word):
        ids = groups.setdefault(amount, set())
        ids.add(int(id_str))  # int 去除前导零
    return max(len(v) for v in groups.values()) if groups else 0

if __name__ == '__main__':
    print(analyze_transactions(sys.argv[1]), end='')
```

---

## 08-22

### 题目 3：通配符模式匹配

**题目描述：**

实现一个支持 `?` 和 `*` 的通配符匹配函数 `is_match(s, p)`，其中：
- `?` 匹配任意单个字符
- `*` 匹配任意字符序列（包括空序列）
- 匹配应覆盖整个输入字符串
- 不使用内置正则表达式库

**输入：**
- 输入字符串 `s` 长度 0~20，模式 `p` 长度 0~30
- 命令行传入格式：`s;p`，分号分隔，如 `"adceb;*a*b"`

**输出：**
- 返回 `true` / `false` 表示是否匹配（小写输出，无换行）

**示例：**

```
输入: "adceb;*a*b"
输出: true
```
解析：第一个 `*` 匹配空序列，`a` 匹配 `a`，第二个 `*` 匹配 `"dceb"`

**测试用例：**

| # | 输入 | 输出 |
|---|------|------|
| 1 | `adceb;*a*b` | `true` |
| 2 | `acdcb;a*c?b` | `false` |

**Python 代码：**
```python
import sys

def is_match(s: str, p: str) -> bool:
    # 编码区
    m, n = len(s), len(p)
    dp = [[False] * (n + 1) for _ in range(m + 1)]
    dp[0][0] = True
    # 初始化：模式串开头的 * 可以匹配空串
    for j in range(1, n + 1):
        if p[j - 1] == '*':
            dp[0][j] = True
        else:
            break
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if p[j - 1] == '*':
                dp[i][j] = dp[i - 1][j] or dp[i][j - 1]
            elif p[j - 1] == '?' or p[j - 1] == s[i - 1]:
                dp[i][j] = dp[i - 1][j - 1]
    return dp[m][n]

if __name__ == '__main__':
    s, p = sys.argv[1].split(';')
    print(is_match(s, p), end='')
```

---

### 题目 4：删除重复分数记录（SQL）

**题目描述：**

`tb_user_score` 是一个学生成绩记录表：

| 列名 | 数据类型 | 说明 |
|------|----------|------|
| id | INT | 自增 ID |
| uid | INT | 用户 ID |
| score | DATETIME | 分数 |
| created_at | timestamp | 创建时间 |

老师需要删除重复的分数，重复的分数只保留 id 最小的那条记录。请帮老师找出需要删除的记录 id，结果按 id 升序排列。

**测试用例：**

测试用例 1：分数 40、20 重复，id 3<5、4<6，所以应删除的 id 为 5 和 6。

表数据（7 条记录）：

| id | uid | score | created_at |
|----|-----|-------|------------|
| 1 | 1 | 30 | 2022-10-10 00:00:00 |
| 2 | 1 | 35 | 2022-10-10 00:00:00 |
| 3 | 1 | 40 | 2022-10-10 00:00:00 |
| 4 | 1 | 20 | 2022-10-10 00:00:00 |
| 5 | 2 | 40 | 2022-10-10 00:00:00 |
| 6 | 2 | 20 | 2022-10-10 00:00:00 |
| 7 | 2 | 39 | 2022-10-10 00:00:00 |

输出：`5`、`6`

**SQL 代码：**
```sql
select distinct t1.id
from tb_user_score t1
join tb_user_score t2
  on t1.score = t2.score
 and t2.id < t1.id
order by t1.id;
```
