#!/usr/bin/env python
# encoding: UTF-8

import os
import time
import sys
import socket

def main():
    running = True
    s = socket.socket()         # 创建 socket 对象
    #host = socket.gethostname() # 获取本地主机名
    host = "127.0.0.1"
    port = 12345                # 设置端口
    s.bind((host, port))        # 绑定端口
    print("Server started, listening on port ", port)
    s.listen(5)                 # 等待客户端连接
    while running:
        c, addr = s.accept()     # 建立客户端连接。
        print('连接地址：', addr)
        c.send(b"欢迎光临")
        c.close()                # 关闭连接
    
if __name__ == "__main__":
    main()