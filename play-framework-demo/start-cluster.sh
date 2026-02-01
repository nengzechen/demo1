#!/bin/bash

# ===================================================
# Akka Cluster 启动脚本
# ===================================================
# 使用方式: ./start-cluster.sh [节点类型]
# 节点类型: seed1, seed2, worker1, worker2, all
# ===================================================

set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# 启动单个节点
start_node() {
    local NODE_TYPE=$1
    local CONFIG_FILE="conf/cluster-${NODE_TYPE}.conf"

    if [ ! -f "$CONFIG_FILE" ]; then
        print_error "配置文件不存在: $CONFIG_FILE"
        exit 1
    fi

    print_info "启动节点: $NODE_TYPE"
    print_info "配置文件: $CONFIG_FILE"

    # 使用sbt启动
    sbt -Dconfig.file="$CONFIG_FILE" run &

    print_success "节点 $NODE_TYPE 启动命令已执行"
}

# 启动所有节点
start_all() {
    print_info "启动完整集群 (2个Seed节点 + 2个Worker节点)"

    print_info "启动 Seed Node 1..."
    start_node "seed1"
    sleep 5

    print_info "启动 Seed Node 2..."
    start_node "seed2"
    sleep 5

    print_info "启动 Worker Node 1..."
    start_node "worker1"
    sleep 5

    print_info "启动 Worker Node 2..."
    start_node "worker2"

    print_success "所有节点启动命令已执行"
    print_info "请等待节点启动并加入集群"
    print_info ""
    print_info "访问地址:"
    print_info "  Seed1:  http://localhost:9001"
    print_info "  Seed2:  http://localhost:9002"
    print_info "  Worker1: http://localhost:9003"
    print_info "  Worker2: http://localhost:9004"
}

# 停止所有Play进程
stop_all() {
    print_info "停止所有Play进程..."

    # 查找并停止所有sbt进程
    pkill -f "sbt.*run" || true

    print_success "所有进程已停止"
}

# 显示帮助信息
show_help() {
    echo "Akka Cluster 启动脚本"
    echo ""
    echo "使用方式:"
    echo "  $0 [命令]"
    echo ""
    echo "命令:"
    echo "  seed1    - 启动Seed节点1 (端口: 9001, Akka: 2551)"
    echo "  seed2    - 启动Seed节点2 (端口: 9002, Akka: 2552)"
    echo "  worker1  - 启动Worker节点1 (端口: 9003, Akka: 2553)"
    echo "  worker2  - 启动Worker节点2 (端口: 9004, Akka: 2554)"
    echo "  all      - 启动所有节点"
    echo "  stop     - 停止所有节点"
    echo "  help     - 显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 seed1        # 启动第一个种子节点"
    echo "  $0 all          # 启动完整集群"
    echo "  $0 stop         # 停止所有节点"
}

# 主逻辑
case "${1:-help}" in
    seed1)
        start_node "seed1"
        ;;
    seed2)
        start_node "seed2"
        ;;
    worker1)
        start_node "worker1"
        ;;
    worker2)
        start_node "worker2"
        ;;
    all)
        start_all
        ;;
    stop)
        stop_all
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        print_error "未知命令: $1"
        echo ""
        show_help
        exit 1
        ;;
esac
