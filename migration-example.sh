#!/bin/bash

# 数据迁移示例脚本
# 使用方法: ./migration-example.sh

BASE_URL="http://localhost:9000/base"

echo "========================================="
echo "   品牌数据库合并迁移系统 - 示例脚本"
echo "========================================="
echo ""

# 检查服务是否启动
echo "1. 检查服务状态..."
response=$(curl -s -o /dev/null -w "%{http_code}" ${BASE_URL}/migration/mappings/account)
if [ "$response" != "200" ]; then
    echo "❌ 服务未启动或无法访问！"
    echo "请先启动服务: ./mvnw spring-boot:run"
    exit 1
fi
echo "✅ 服务运行正常"
echo ""

# 步骤1: 初始化Account映射
echo "2. 初始化Account映射..."
echo "示例：品牌A的accountId 1,2,3 映射到品牌B的 101,102,103"
curl -X POST ${BASE_URL}/migration/init-account-mapping \
  -H "Content-Type: application/json" \
  -d '{
    "accountMapping": {
      "1": 101,
      "2": 102,
      "3": 103
    }
  }' | jq '.'
echo ""
read -p "按回车继续下一步..."

# 步骤2: 迁移User表
echo ""
echo "3. 迁移User表..."
curl -X POST ${BASE_URL}/migration/user \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 1000}' | jq '.'
echo ""
read -p "按回车继续下一步..."

# 步骤3: 迁移UserToAgent表
echo ""
echo "4. 迁移UserToAgent表..."
curl -X POST ${BASE_URL}/migration/usertoagent \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 1000}' | jq '.'
echo ""
read -p "按回车继续下一步..."

# 步骤4: 迁移RideCard表
echo ""
echo "5. 迁移RideCard表..."
curl -X POST ${BASE_URL}/migration/ridecard \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 500}' | jq '.'
echo ""
read -p "按回车继续下一步..."

# 步骤5: 迁移RideCardUser表
echo ""
echo "6. 迁移RideCardUser表..."
curl -X POST ${BASE_URL}/migration/ridecarduser \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 1000}' | jq '.'
echo ""
read -p "按回车继续下一步..."

# 步骤6: 迁移GiftCard表
echo ""
echo "7. 迁移GiftCard表..."
curl -X POST ${BASE_URL}/migration/giftcard \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 500}' | jq '.'
echo ""
read -p "按回车继续下一步..."

# 步骤7: 迁移GiftCardUser表
echo ""
echo "8. 迁移GiftCardUser表..."
curl -X POST ${BASE_URL}/migration/giftcarduser \
  -H "Content-Type: application/json" \
  -d '{"batchSize": 1000}' | jq '.'
echo ""
read -p "按回车继续下一步..."

# 步骤8: 获取用户手机号映射进行校验
echo ""
echo "9. 获取用户手机号映射（用于数据校验）..."
curl -X GET ${BASE_URL}/migration/validate-user-phone | jq '.'
echo ""

echo ""
echo "========================================="
echo "   迁移完成！"
echo "========================================="
echo ""
echo "下一步操作："
echo "1. 对比源数据库和目标数据库的数据量"
echo "2. 使用userPhoneMapping验证数据正确性"
echo "3. 抽查部分数据进行人工验证"
echo ""
