#!/bin/bash

# PostgreSQL连接脚本
# 使用方法: ./connect-db.sh

echo "🔗 连接到PostgreSQL数据库..."
echo "数据库: v2ex_clone"
echo "用户: omgzui"
echo "端口: 5432"
echo ""

# 设置PATH
export PATH="/opt/homebrew/opt/postgresql@15/bin:$PATH"

# 检查PostgreSQL是否运行
if ! pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
    echo "❌ PostgreSQL服务未运行，正在启动..."
    brew services start postgresql@15
    sleep 2
fi

# 连接到数据库
echo "✅ 连接到数据库..."
psql -d v2ex_clone
