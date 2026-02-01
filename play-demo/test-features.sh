#!/bin/bash

echo "=========================================="
echo "Play Framework 功能测试"
echo "=========================================="
echo ""

echo "1. 测试健康检查..."
curl -s http://localhost:9000/health | jq .
echo ""

echo "2. 测试性能过滤器（查看响应头）..."
curl -i -s http://localhost:9000/api/tasks/types | grep -E "X-Response-Time|X-Server-Name"
echo ""

echo "3. 测试Actor系统..."
curl -s http://localhost:9000/api/actor/info | jq .
echo ""

echo "4. 测试任务优先级API..."
curl -s http://localhost:9000/api/tasks/priorities | jq .
echo ""

echo "5. 测试限流拦截器（连续请求15次，超过10次应该被限流）..."
for i in {1..15}; do
  echo -n "请求 $i: "
  response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:9000/api/tasks/types)
  if [ "$response" = "200" ]; then
    echo "✓ 成功"
  elif [ "$response" = "429" ]; then
    echo "✗ 被限流 (429 Too Many Requests)"
  else
    echo "? 状态码: $response"
  fi
  sleep 0.1
done
echo ""

echo "6. 测试认证拦截器（需要Bearer Token的端点）..."
echo "   a) 无认证访问（应该失败）:"
curl -s http://localhost:9000/api/actor/task -X POST \
  -H "Content-Type: application/json" \
  -d '{"taskName": "Test"}' | jq . || echo "需要认证"

echo ""
echo "   b) 带认证访问（应该成功）:"
curl -s http://localhost:9000/api/actor/task -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test-token" \
  -d '{"taskName": "Test"}' | jq .

echo ""
echo "=========================================="
echo "测试完成！"
echo "=========================================="

echo ""
echo "所有功能："
echo "  ✓ MySQL数据库集成"
echo "  ✓ Ebean ORM"
echo "  ✓ 过滤器(Filter) - LoggingFilter, PerformanceFilter, RequestValidationFilter"
echo "  ✓ 拦截器(Interceptor) - @Authenticated, @Logged, @RateLimited"
echo "  ✓ Actor系统"
echo "  ✓ WebSocket支持"
echo "  ✓ .gitignore配置"
