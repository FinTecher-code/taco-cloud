# MySQL 慢查询巡检脚本挑战

## 挑战要求

编写一个巡检脚本（Python 或 Shell），实现以下功能：

1. 自动连接到本地 MySQL 实例
2. 读取最近 1 小时的慢查询日志（或使用 performance_schema 查询 Top 10 慢 SQL）
3. 对每条慢 SQL 自动执行 EXPLAIN，收集 type、key、rows、Extra 等关键字段
4. 根据以下规则自动输出优化建议：
   - type=ALL（全表扫描）→ 建议添加索引
   - Extra 含 Using filesort → 建议检查 ORDER BY 字段是否有索引
   - Extra 含 Using temporary → 建议检查 GROUP BY 或 UNION 是否可优化
   - rows > 100000 → 建议拆分查询或添加更精准的条件
5. 将巡检结果输出为 JSON 格式的报告文件（`slow_query_inspection_YYYY-MM-DD.json`）

**注意：** 脚本需要能在本地环境运行，连接配置项需可通过命令行参数传入（如 `--host --port --user --password`）。

---

## 参考答案

- **脚本：** `slow_query_inspector.py`（同目录下）
- **数据源：** `performance_schema.events_statements_history_long`
- **分析逻辑：** 自动 EXPLAIN → 规则匹配 → JSON 报告输出

### 使用方式

```bash
# 默认连接本地 MySQL（root 无密码）
python slow_query_inspector.py

# 指定完整参数
python slow_query_inspector.py \
  --host 127.0.0.1 \
  --port 3306 \
  --user root \
  --password=yourpass \
  --database mydb \
  --top-n 10 \
  --output-dir /path/to/reports
```

### 输出示例

```json
{
  "report_title": "MySQL Slow Query Inspection Report",
  "generated_at": "2026-07-26 18:11:11",
  "summary": {
    "total_queries_analyzed": 2,
    "queries_with_issues": 2,
    "explain_success": 2,
    "explain_failed": 0
  },
  "queries": [
    {
      "query": "SELECT * FROM orders WHERE status = 'pending'",
      "avg_latency_ms": 1258.43,
      "explain_success": true,
      "explain_steps": [
        {
          "step": 0,
          "table": "orders",
          "type": "ALL",
          "key": null,
          "rows": 100,
          "extra": "Using where",
          "suggestions": [
            "全表扫描: 建议为 WHERE 条件字段添加索引 (table=orders, rows=100)"
          ]
        }
      ],
      "has_issues": true
    }
  ]
}
```
