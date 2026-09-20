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

**Python 代码：**
```python
import sys

if __name__ == '__main__':
    s = sys.argv[1]
    if not s:
        print(0.00)
    total = 0.00
    count = 0.00
    for ch in s:
        if ch.isdigit():
            total += float(ch)
            count += 1
    avg = total / count
    print("{:.2f}".format(avg))
```

---

### 题目 2：信号传输延迟（单源最短路径）

**题目描述：**
公司有 N 个部门（编号 1 到 N），部门之间通过单向网络连接，每个连接有不同延迟。
从部门 K 发送信号，计算信号到达**所有部门**的最短时间。
如果有部门无法收到信号，返回 -1。

**输入：**
- `K`：源部门编号
- `edges`：二维数组，`edges[i] = (u, v, w)` 表示从 u 到 v 的单向延迟为 w（正整数）
- 命令行传入格式：`;` 分隔各条边，如 `"2 1 1;2 3 1;3 4 1"`

**输出：**
- 到达所有部门的最短时间（即最远距离），不可达返回 -1

**示例：**
```
输入: K = 2, edges = [[2,1,1], [2,3,1], [3,4,1]]
输出: 2
解释: 2→1(1), 2→3(1), 2→3→4(2)，最远为部门4，耗时2
```

**Python 代码：**
```python
import sys
import heapq

def solve(K, edges):
    # 1. 求节点总数 N
    n = K
    for u, v, w in edges:
        n = max(n, u, v)

    # 2. 建图（邻接表）
    graph = [[] for _ in range(n + 1)]
    for u, v, w in edges:
        graph[u].append((v, w))

    # 3. Dijkstra
    INF = float('inf')
    dist = [INF] * (n + 1)
    dist[K] = 0
    pq = [(0, K)]
    while pq:
        d, u = heapq.heappop(pq)
        if d > dist[u]:
            continue
        for v, w in graph[u]:
            new_d = dist[u] + w
            if new_d < dist[v]:
                dist[v] = new_d
                heapq.heappush(pq, (new_d, v))

    # 4. 取最远距离，不可达返回 -1
    max_dist = 0
    for i in range(1, n + 1):
        if dist[i] == INF:
            return -1
        max_dist = max(max_dist, dist[i])
    return max_dist

if __name__ == "__main__":
    K = int(sys.argv[1])
    edges = [
        list(map(int, edge.split()))
        for edge in sys.argv[2].split(";")
    ]
    print(solve(K, edges))
```

---

### 题目 3：最长递增子序列（LIS）

**题目描述：**
给定一个整数数组 nums，找到其中最长严格递增子序列的长度。子序列可以不连续。

**规则：**
- 子序列指删除一些元素而不改变剩余元素顺序
- 严格递增指每个元素都比前一个大
- `1 ≤ nums.length ≤ 2500`
- `-10⁴ ≤ nums[i] ≤ 10⁴`

**输入：** JSON 格式的数组字符串，如 `"[10,9,2,5,3,7,101,18]"`
**输出：** 最长递增子序列的长度

**示例：**
```
输入: "[10,9,2,5,3,7,101,18]"
输出: 4
解释: 最长递增子序列是 [2,3,7,101]，长度为 4
```

**Python 代码：**
```python
import sys
import json

class Solution:
    def lengthOfLIS(self, nums):
        # 贪心 + 二分 O(n log n)
        tails = []  # tails[i] = 长度为 i+1 的递增子序列的最小结尾元素
        for x in nums:
            # 二分查找第一个 >= x 的位置
            left, right = 0, len(tails)
            while left < right:
                mid = left + (right - left) // 2
                if tails[mid] < x:
                    left = mid + 1
                else:
                    right = mid
            if left == len(tails):
                tails.append(x)
            else:
                tails[left] = x
        return len(tails)

if __name__ == '__main__':
    nums = json.loads(sys.argv[1])
    print(Solution().lengthOfLIS(nums), end="")
```

---

### 题目 4：图书及订阅人、订阅信息查询（SQL）

**表结构：**

```sql
-- 书籍表
create table books (
    book_id int primary key,
    title varchar(40),
    author varchar(20),
    publication_year date
);

-- 出版社表
create table publishers (
    publisher_id int primary key,
    name varchar(20),
    address varchar(80)
);

-- 书籍-出版社关联表（book_id 为主键，一本书只对应一个出版社）
create table book_publishers (
    book_id int primary key,
    publisher_id int,
    foreign key (book_id) references books(book_id),
    foreign key (publisher_id) references publishers(publisher_id)
);

-- 借阅记录表
create table borrow_records (
    record_id int primary key,
    book_id int,
    member_id int,
    borrow_date date,
    return_date date
);

-- 会员表
create table members (
    member_id int primary key,
    name varchar(20),
    email varchar(40)
);
```

**查询需求：** 查询每本图书的**名称、作者、出版社**以及**对应的借阅次数**，按照借阅次数**倒序**排序。

**SQL 代码：**
```sql
select
    b.title,
    b.author,
    p.name as publisher_name,
    count(br.record_id) as total_borrows
from books b
left join book_publishers bp on b.book_id = bp.book_id
left join publishers p on bp.publisher_id = p.publisher_id
left join borrow_records br on b.book_id = br.book_id
group by b.book_id, b.title, b.author, p.name
order by borrow_count desc;
```
