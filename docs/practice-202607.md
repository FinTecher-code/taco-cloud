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


#### Python 版本

```python
def average_of_digits(s: str) -> float:
    """
    计算字符串中数字字符的平均值
    :param s: 输入字符串
    :return: 平均值，保留两位小数
    """
    if not s:
        return 0.0

    total = 0
    count = 0

    for ch in s:
        if ch.isdigit():
            total += int(ch)
            count += 1

    if count == 0:
        return 0.0

    avg = total / count
    return round(avg, 2)


# 测试
if __name__ == "__main__":
    print(average_of_digits("a1b2c3"))   # 2.0
    print(average_of_digits("abc"))      # 0.0
    print(average_of_digits(""))         # 0.0
    print(average_of_digits("999"))      # 9.0
```

**Python 与 Java 的对比差异：**

| 维度 | Java | Python |
|------|------|--------|
| 遍历字符 | `s.charAt(i)` | `for ch in s` 直接遍历 |
| 判数字 | `Character.isDigit(ch)` | `ch.isdigit()` |
| 字符转数字 | `ch - '0'` | `int(ch)` |
| 保留小数 | `String.format("%.2f", avg)` 转回 double | `round(avg, 2)` |
| 判空 | `s == null || s.isEmpty()` | `not s` |
| 函数定义 | `public static double average(...)` | `def average(...) -> float` |

Python 简洁很多，但注意 `round()` 不强制显示两位小数（`2.0` 而不是 `2.00`），严格输出可用 `f"{avg:.2f}"`。


---

## 07-19

### 题目 2：信号传输延迟（单源最短路径）

**题目描述：**
公司有 N 个部门（编号 1 到 N），部门之间通过单向网络连接，每个连接有不同延迟。
从部门 K 发送信号，计算信号到达**所有部门**的最短时间。
如果有部门无法收到信号，返回 -1。

**输入：**
- `K`：源部门编号
- `edges`：二维数组，`edges[i] = (u, v, w)` 表示从 u 到 v 的单向延迟为 w（正整数）

**输出：**
- 到达所有部门的最短时间（即最远距离），不可达返回 -1

**示例：**
```
输入: K = 2, edges = [[2,1,1], [2,3,1], [3,4,1]]
输出: 2
解释: 2→1(1), 2→3(1), 2→3→4(2)，最远为部门4，耗时2
```

---

#### 解题思路

经典 **Dijkstra** 算法（边权为正）：
1. 建邻接表
2. 用优先队列做 BFS，dist[] 记录最短距离
3. 取所有可达部门的最大距离
4. 如果有部门 dist 仍为 INF，返回 -1

---

#### Java 实现

```java
import java.util.*;

public class Solution {
    public int networkDelayTime(int K, int N, int[][] edges) {
        // 1. 建图（邻接表）
        List<int[]>[] graph = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph[u].add(new int[]{v, w});
        }

        // 2. Dijkstra
        int[] dist = new int[N + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[K] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{K, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0], d = cur[1];
            if (d > dist[u]) continue;

            for (int[] next : graph[u]) {
                int v = next[0], w = next[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }

        // 3. 取最远距离
        int maxDist = 0;
        for (int i = 1; i <= N; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                return -1;  // 不可达
            }
            maxDist = Math.max(maxDist, dist[i]);
        }
        return maxDist;
    }

    // 测试
    public static void main(String[] args) {
        Solution sol = new Solution();
        int[][] edges = {{2,1,1}, {2,3,1}, {3,4,1}};
        System.out.println(sol.networkDelayTime(2, 4, edges));  // 2
    }
}
```

---

#### Python 实现

```python
import heapq
from typing import List


def network_delay_time(K: int, N: int, edges: List[List[int]]) -> int:
    # 1. 建图（邻接表）
    graph = [[] for _ in range(N + 1)]
    for u, v, w in edges:
        graph[u].append((v, w))

    # 2. Dijkstra
    INF = float('inf')
    dist = [INF] * (N + 1)
    dist[K] = 0

    pq = [(0, K)]  # (距离, 节点)

    while pq:
        d, u = heapq.heappop(pq)
        if d > dist[u]:
            continue
        for v, w in graph[u]:
            new_d = dist[u] + w
            if new_d < dist[v]:
                dist[v] = new_d
                heapq.heappush(pq, (new_d, v))

    # 3. 取最远距离
    max_dist = 0
    for i in range(1, N + 1):
        if dist[i] == INF:
            return -1
        max_dist = max(max_dist, dist[i])
    return max_dist


# 测试
if __name__ == "__main__":
    edges = [[2, 1, 1], [2, 3, 1], [3, 4, 1]]
    print(network_delay_time(2, 4, edges))  # 2
```

---

#### 易错点

| 注意 | 说明 |
|------|------|
| 部门编号从 1 开始 | 数组长度要 N+1，下标 0 不用 |
| 单向边 | edges[u→v] 只加一条，别加反 |
| 边数为 N*N？ | 其实就是 M 条边，按实际给的数据处理 |
| 不可达返回 -1 | 不是返回 0，也不是抛异常 |
| 最远距离 | 是最短路径中的最大值，不是路径条数 |


---

#### ❌ 错误版本分析

```java
class Solution {
    public static int solve(int K, String[] edges) {
        // 请将您的代码逻辑填写在这里
        int sum = 0;
        int push = []
        for(int i = 0;i<edges.length();i++){
            if (K != edges[i][0][0]){
                return -1
            }else{
                sum += edges[i][0][0]
            }
        }
    }
}
```

**错误分析：**

| # | 错误 | 说明 | 修正 |
|:-:|------|------|------|
| 1 | `int push = []` | 语法错误，`int` 不能赋数组 | 类型应为 `int[]`，或用 `new int[]{}` |
| 2 | `edges.length()` | 数组是 `.length` 不是方法 | `edges.length` |
| 3 | `edges[i][0][0]` | `edges[i]` 是 String 不是二维数组 | 需先 split 解析：`edges[i].split(",")` |
| 4 | 直接返回 -1 | 如果 K 不是边起点就返回 -1，**完全错误** | 信号可以多跳传输，不是只有直连 |
| 5 | `sum += ...` | 把边的起点编号相加，毫无意义 | 应该记录到各节点的最短距离 |
| 6 | 缺少分号 | `return -1` 和 `sum += ...` 后缺 `;` | 补分号 |
| 7 | 没有返回值兜底 | if-else 外没有 return，编译报错 | 补 return 语句 |
| 8 | **核心逻辑缺失** | 没有建图、没有 Dijkstra/BFS，根本不算最短路径 | 整段重写，见上方正确实现 |

**本质问题：** 直接把 `edges[i][0]` 当二维数组下标用，但 `edges` 是 `String[]`，每个元素是像 `"2,1,1"` 的字符串，必须先用 `split(",")` 解析成三个整数，再建图跑 Dijkstra。


---

### 题目 3：最长递增子序列（LIS）

**题目描述：**
给定一个整数数组 nums，找到其中最长严格递增子序列的长度。子序列可以不连续。

**规则：**
- 子序列指删除一些元素而不改变剩余元素顺序
- 严格递增指每个元素都比前一个大
- `1 ≤ nums.length ≤ 2500`
- `-10⁴ ≤ nums[i] ≤ 10⁴`

**输入：** 字符串形式的数组，如 `"[10,9,2,5,3,7,101,18]"`
**输出：** 最长递增子序列的长度

**示例：**
```
输入: "[10,9,2,5,3,7,101,18]"
输出: 4
解释: 最长递增子序列是 [2,3,7,101]，长度为 4
```

---

#### 解题思路

**方法一：动态规划 O(n²)**

`dp[i]` 表示以 `nums[i]` 结尾的最长递增子序列长度。

```
dp[i] = max(dp[j] + 1)  其中 j < i 且 nums[j] < nums[i]
```

最终结果取 `max(dp)`

**方法二：贪心 + 二分查找 O(n log n)**

维护数组 `tails`，`tails[i]` 表示长度为 i+1 的递增子序列的最小结尾元素。
遍历 nums，对每个数二分查找其在 tails 中的位置，替换或追加。

---

#### Java 实现

```java
import java.util.*;

public class Solution {
    // 方法一：DP O(n²)
    public static int lengthOfLIS_DP(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int maxLen = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        return maxLen;
    }

    // 方法二：贪心 + 二分 O(n log n) — 最优
    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] tails = new int[nums.length];
        int size = 0;

        for (int x : nums) {
            int left = 0, right = size;
            // 二分查找第一个 >= x 的位置
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < x) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            tails[left] = x;
            if (left == size) size++;
        }
        return size;
    }

    // 解析输入： "[10,9,2,5,3,7,101,18]" → int[]
    public static int[] parseArray(String s) {
        s = s.trim();
        if (s.startsWith("[") && s.endsWith("]")) {
            s = s.substring(1, s.length() - 1);
        }
        if (s.isEmpty()) return new int[0];
        String[] parts = s.split(",");
        int[] res = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            res[i] = Integer.parseInt(parts[i].trim());
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = parseArray("[10,9,2,5,3,7,101,18]");
        System.out.println(lengthOfLIS(nums));     // 4
        System.out.println(lengthOfLIS_DP(nums));  // 4
    }
}
```

---

#### Python 实现

```python
from typing import List
import bisect


# 方法一：DP O(n²)
def length_of_lis_dp(nums: List[int]) -> int:
    if not nums:
        return 0
    dp = [1] * len(nums)
    for i in range(1, len(nums)):
        for j in range(i):
            if nums[j] < nums[i]:
                dp[i] = max(dp[i], dp[j] + 1)
    return max(dp)


# 方法二：贪心 + 二分 O(n log n) — 最优
def length_of_lis(nums: List[int]) -> int:
    tails = []  # tails[i] = 长度为 i+1 的递增子序列的最小结尾元素
    for x in nums:
        pos = bisect.bisect_left(tails, x)  # 第一个 >= x 的位置
        if pos == len(tails):
            tails.append(x)
        else:
            tails[pos] = x
    return len(tails)


# 解析输入
def parse_array(s: str) -> List[int]:
    s = s.strip()
    if s.startswith("[") and s.endswith("]"):
        s = s[1:-1]
    if not s:
        return []
    return [int(x.strip()) for x in s.split(",")]


# 测试
if __name__ == "__main__":
    nums = parse_array("[10,9,2,5,3,7,101,18]")
    print(length_of_lis(nums))      # 4
    print(length_of_lis_dp(nums))   # 4
```

---

#### 两种方法对比

| 方法 | 时间复杂度 | 空间复杂度 | 适用场景 |
|------|:---------:|:---------:|---------|
| DP | O(n²) | O(n) | 容易理解，n ≤ 1000 可接受 |
| 贪心 + 二分 | O(n log n) | O(n) | **最优解**，n ≤ 2500 刚好够用 |

#### 易错点

- 严格递增是 `nums[j] < nums[i]`，不是 `<=`
- 子序列可以不连续，但顺序不能变
- 输入是字符串格式，要先解析成 int 数组
- 贪心+二分法中 `bisect_left` / 二分查找找的是**第一个 >= x** 的位置
