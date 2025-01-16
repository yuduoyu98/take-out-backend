@echo off
REM 切换到 Redis 安装目录
cd /d D:\Program\Redis-x64-3.2.100

REM 启动 Redis 服务器，并指定配置文件
redis-server.exe redis.windows.conf