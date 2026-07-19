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
