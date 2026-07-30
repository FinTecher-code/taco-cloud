#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
MySQL Slow Query Inspector
- Connects to local MySQL instance via command-line args
- Reads Top 10 slow queries from performance_schema
- Runs EXPLAIN on each SELECT query
- Collects type, key, rows, Extra fields
- Outputs optimization suggestions based on rules
- Generates JSON report: slow_query_inspection_YYYY-MM-DD.json
"""

import argparse
import json
import re
import sys
import os
from datetime import datetime
from decimal import Decimal

try:
    import pymysql
    from pymysql.cursors import DictCursor
except ImportError:
    print("ERROR: pymysql is required. Install with: pip install pymysql")
    sys.exit(1)


# ─── JSON Custom Encoder ──────────────────────────────────────────────────────

class CustomEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Decimal):
            return float(obj)
        if isinstance(obj, datetime):
            return obj.strftime('%Y-%m-%d %H:%M:%S')
        if isinstance(obj, bytes):
            return obj.decode('utf-8', errors='replace')
        return super().default(obj)


# ─── System Query Blocklist ──────────────────────────────────────────────────

SYSTEM_DBS = {'mysql', 'performance_schema', 'information_schema', 'sys'}

SYSTEM_QUERY_PATTERNS = [
    r'^\s*SHOW\s+',
    r'^\s*DESC\b',
    r'^\s*EXPLAIN\b',
    r'^\s*SET\s+',
    r'^\s*USE\s+',
    r'^\s*COMMIT\b',
    r'^\s*ROLLBACK\b',
    r'^\s*CREATE\s+(DATABASE|SCHEMA|TABLE|INDEX|VIEW|PROCEDURE|FUNCTION)',
    r'^\s*ALTER\s+',
    r'^\s*DROP\s+',
    r'^\s*TRUNCATE\s+',
    r'^\s*INSERT\s+INTO\s+',
    r'^\s*UPDATE\s+',
    r'^\s*DELETE\s+FROM\s+',
    r'^\s*GRANT\s+',
    r'^\s*REVOKE\s+',
    r'^\s*FLUSH\s+',
    r'^\s*CALL\s+',
    r'^\s*SELECT\s+DATABASE\b',
    r'^\s*SELECT\s+VERSION\b',
    r'^\s*SELECT\s+COUNT\b',
    r'^\s*SELECT\s+@@',
    r'performance_schema\s*\.',
    r'information_schema\s*\.',
    r'mysql\.',
    r'sys\.',
]

def is_user_select_query(sql):
    """Check if a SQL is a user SELECT query (not system/admin query)."""
    if not sql:
        return False
    sql_upper = sql.strip().upper()
    if not sql_upper.startswith('SELECT'):
        return False
    for pattern in SYSTEM_QUERY_PATTERNS:
        if re.search(pattern, sql, re.IGNORECASE):
            return False
    return True


# ─── Argument Parsing ─────────────────────────────────────────────────────────

def parse_args():
    parser = argparse.ArgumentParser(
        description="MySQL Slow Query Inspector"
    )
    parser.add_argument("--host", default="127.0.0.1")
    parser.add_argument("--port", type=int, default=3306)
    parser.add_argument("--user", default="root")
    parser.add_argument("--password", default="")
    parser.add_argument("--database", default="")
    parser.add_argument("--top-n", type=int, default=10)
    parser.add_argument("--output-dir", default=".")
    parser.add_argument("--source", choices=["pfs", "slowlog", "auto"], default="auto")
    parser.add_argument("--slow-log-path", default=None)
    return parser.parse_args()


def log(msg, level="INFO"):
    print(f"[{datetime.now().strftime('%H:%M:%S')}] [{level}] {msg}")


# ─── Get Slow Queries ────────────────────────────────────────────────────────

def get_slow_queries(conn, top_n, hours=1):
    """Get slow SELECT queries from events_statements_history_long."""
    cur = conn.cursor(DictCursor)
    results = []

    # Use events_statements_history_long for actual SQL text
    sql = """
        SELECT
            e.SQL_TEXT,
            e.DIGEST_TEXT,
            ROUND(e.TIMER_WAIT / 1000000, 2) AS latency_ms,
            e.ROWS_EXAMINED,
            e.ROWS_SENT,
            e.MESSAGE_TEXT,
            e.THREAD_ID,
            e.EVENT_NAME,
            e.CURRENT_SCHEMA AS query_db
        FROM performance_schema.events_statements_history_long e
        WHERE e.SQL_TEXT IS NOT NULL
          AND e.TIMER_WAIT > 0
          AND e.NESTING_EVENT_ID IS NULL
        ORDER BY e.TIMER_WAIT DESC
        LIMIT 50
    """
    try:
        cur.execute(sql)
        rows = cur.fetchall()
    except Exception as e:
        log(f"PFS query failed: {e}", "WARN")
        rows = []

    # Also try events_statements_current
    if not rows:
        try:
            cur.execute("""
                SELECT
                    e.SQL_TEXT,
                    e.DIGEST_TEXT,
                    ROUND(e.TIMER_WAIT / 1000000, 2) AS latency_ms,
                    e.ROWS_EXAMINED,
                    e.ROWS_SENT,
                    e.MESSAGE_TEXT,
                    e.THREAD_ID,
                    e.EVENT_NAME,
                    e.CURRENT_SCHEMA AS query_db
                FROM performance_schema.events_statements_current e
                WHERE e.SQL_TEXT IS NOT NULL
                  AND e.TIMER_WAIT > 0
                ORDER BY e.TIMER_WAIT DESC
                LIMIT 50
            """)
            rows = cur.fetchall()
        except Exception as e2:
            log(f"PFS current query also failed: {e2}", "WARN")

    # Deduplicate by SQL_TEXT
    seen = set()
    for r in rows:
        qt = r.get('SQL_TEXT', '')
        if not qt:
            continue
        key = qt[:300]
        if key in seen:
            continue
        seen.add(key)

        if not is_user_select_query(qt):
            continue

        results.append({
            'query': qt,
            'exec_count': 1,
            'avg_latency_ms': r.get('latency_ms', 0),
            'total_latency_ms': r.get('latency_ms', 0),
            'max_latency_ms': r.get('latency_ms', 0),
            'rows_examined_avg': r.get('ROWS_EXAMINED', 0),
            'rows_sent_avg': r.get('ROWS_SENT', 0),
            'query_db': r.get('query_db', ''),
        })

    return results[:top_n]


# ─── EXPLAIN & Analysis ──────────────────────────────────────────────────────

def run_explain(cur, sql, db_name=None):
    """Run EXPLAIN on a SQL statement."""
    sql = sql.strip().rstrip(';')
    orig_db = None

    try:
        # Switch to the query's database if available
        if db_name:
            cur.execute("SELECT DATABASE() AS db")
            orig_db = cur.fetchone()['db']
            if orig_db != db_name:
                cur.execute(f"USE `{db_name}`")

        explain_sql = f"EXPLAIN FORMAT=TRADITIONAL {sql}"
        cur.execute(explain_sql)
        rows = cur.fetchall()
        return rows
    except Exception as e:
        log(f"EXPLAIN failed: {e}", "WARN")
        return None
    finally:
        if orig_db and db_name and orig_db != db_name:
            try:
                cur.execute(f"USE `{orig_db}`")
            except Exception:
                pass


def analyze_explain(explain_rows):
    """Analyze EXPLAIN output and generate optimization suggestions."""
    suggestions = []
    if not explain_rows:
        return suggestions

    for step, row in enumerate(explain_rows):
        row_suggestions = []
        table = row.get('table', 'UNKNOWN')
        stype = row.get('type', '') or ''
        extra = row.get('Extra', '') or ''
        key = row.get('key', None)
        rows_count = int(row.get('rows', 0) or 0)

        # Rule 1: type=ALL => full table scan
        if stype.upper() == 'ALL':
            row_suggestions.append(
                "全表扫描: 建议为 WHERE 条件字段添加索引"
                f" (table={table}, rows={rows_count})"
            )

        # Rule 2: Using filesort
        if 'Using filesort' in extra:
            row_suggestions.append(
                "文件排序: 检查 ORDER BY 字段是否已建索引"
                f" (table={table})"
            )

        # Rule 3: Using temporary
        if 'Using temporary' in extra:
            row_suggestions.append(
                "临时表: 检查 GROUP BY 或 UNION 是否可优化"
                f" (table={table})"
            )

        # Rule 4: rows > 100000
        if rows_count > 100000:
            row_suggestions.append(
                "扫描行数过多: 建议拆分查询或添加更精准的过滤条件"
                f" (table={table}, rows={rows_count})"
            )

        if not row_suggestions:
            if key:
                row_suggestions.append(
                    f"索引使用良好 (type={stype}, key={key}, rows={rows_count})"
                )
            else:
                row_suggestions.append(
                    f"无显著问题 (type={stype}, rows={rows_count})"
                )

        suggestions.append({
            "step": step,
            "table": table,
            "type": stype,
            "key": key,
            "rows": rows_count,
            "extra": extra,
            "suggestions": row_suggestions
        })

    return suggestions


# ─── Report Generation ────────────────────────────────────────────────────────

def generate_report(analyzed_queries, args):
    report = {
        "report_title": "MySQL Slow Query Inspection Report",
        "generated_at": datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
        "mysql_host": f"{args.host}:{args.port}",
        "config": {
            "top_n": args.top_n,
            "query_source": args.source,
        },
        "summary": {
            "total_queries_analyzed": len(analyzed_queries),
            "queries_with_issues": 0,
            "explain_success": 0,
            "explain_failed": 0,
        },
        "queries": []
    }

    for item in analyzed_queries:
        has_issues = item.get("has_issues", False)
        explain_ok = item.get("explain_success", False)

        entry = {
            "query": item.get("query", ""),
            "query_db": item.get("query_db", ""),
            "exec_count": item.get("exec_count", "N/A"),
            "avg_latency_ms": item.get("avg_latency_ms", "N/A"),
            "max_latency_ms": item.get("max_latency_ms", "N/A"),
            "total_latency_ms": item.get("total_latency_ms", "N/A"),
            "rows_examined_avg": item.get("rows_examined_avg", "N/A"),
            "rows_sent_avg": item.get("rows_sent_avg", "N/A"),
            "explain_success": explain_ok,
            "explain_steps": item.get("explain", []),
            "suggestions": item.get("suggestions", []),
            "has_issues": has_issues,
        }
        report["queries"].append(entry)

        if has_issues:
            report["summary"]["queries_with_issues"] += 1
        if explain_ok:
            report["summary"]["explain_success"] += 1
        else:
            report["summary"]["explain_failed"] += 1

    return report


def write_report(report, output_dir):
    today = datetime.now().strftime('%Y-%m-%d')
    filename = f"slow_query_inspection_{today}.json"
    filepath = os.path.join(output_dir, filename)

    with open(filepath, 'w', encoding='utf-8') as f:
        json.dump(report, f, ensure_ascii=False, indent=2, cls=CustomEncoder)

    print(f"\n{'='*60}")
    print(f"[OK] Report: {filepath}")
    print(f"{'='*60}")
    print(f"  Total queries:    {report['summary']['total_queries_analyzed']}")
    print(f"  Has issues:       {report['summary']['queries_with_issues']}")
    print(f"  EXPLAIN OK:       {report['summary']['explain_success']}")
    print(f"  EXPLAIN failed:   {report['summary']['explain_failed']}")
    print(f"{'='*60}")
    return filepath


# ─── Main ─────────────────────────────────────────────────────────────────────

def main():
    args = parse_args()

    # Connect
    try:
        conn = pymysql.connect(
            host=args.host,
            port=args.port,
            user=args.user,
            password=args.password,
        )
        log(f"Connected to MySQL {args.host}:{args.port}")
    except Exception as e:
        log(f"Connection failed: {e}", "ERROR")
        sys.exit(1)

    # Get user SELECT queries
    slow_queries = get_slow_queries(conn, args.top_n)

    if not slow_queries:
        log("No user SELECT queries found.", "WARN")
        log("Tip: Enable events_statements_history_long consumer and run some queries first.")
        report = generate_report([], args)
        write_report(report, args.output_dir)
        conn.close()
        return

    log(f"Found {len(slow_queries)} SELECT queries to analyze")

    # Analyze each
    cur = conn.cursor(DictCursor)
    analyzed = []

    for i, sq in enumerate(slow_queries):
        qtext = sq.get('query', '')
        db_name = sq.get('query_db', '')
        display = qtext[:80].replace('\n', ' ').strip()
        if len(qtext) > 80:
            display += '...'

        log(f"[{i+1}/{len(slow_queries)}] EXPLAIN: {display}")

        explain_result = run_explain(cur, qtext, db_name)

        if explain_result:
            sq['explain'] = analyze_explain(explain_result)
            sq['explain_success'] = True
            sq['has_issues'] = any(
                s for step in sq['explain']
                for s in step.get('suggestions', [])
                if not s.startswith('无显著问题') and not s.startswith('索引使用良好')
            )
        else:
            sq['explain'] = []
            sq['explain_success'] = False
            sq['has_issues'] = False
            sq['suggestions'] = ['EXPLAIN skipped / not a SELECT']

        analyzed.append(sq)

    # Report
    report = generate_report(analyzed, args)
    report_path = write_report(report, args.output_dir)

    # Console summary
    print(f"\n[SUMMARY] 分析结果:")
    for q in report['queries']:
        short = q['query'][:70].replace('\n', ' ').strip()
        tag = '[ISSUE]' if q['has_issues'] else '[OK]'
        etag = '[E]' if q['explain_success'] else '[-]'
        print(f"  {tag} {etag} [{q.get('avg_latency_ms','?')}ms] {short}")
        for step in q.get('explain_steps', []):
            for s in step.get('suggestions', []):
                if not s.startswith('无显著问题') and not s.startswith('索引使用良好'):
                    print(f"    -> {s}")

    conn.close()


if __name__ == "__main__":
    main()
