#!/usr/bin/env python
# encoding: UTF-8
"""
Excel数据处理工具
"""
import os
import time
import sys
import socket

def main():
    # 从code_price.txt文件中读取数据
    code_price = loadCodePrice()
    # print(code_price)

    # 文件路径修改成实际情况
    f = open("D:\\Git\\testcase\\src\\main\\python\\target.txt")
    for line in f:
        parts = line.split()
        num = int(parts[1])
        totalprice = num * float(code_price[parts[0]])
        print(line, code_price[parts[0]], str(totalprice))
    


def loadCodePrice():
    # 文件名修改成实际的样式
    f = open("D:\\Git\\testcase\\src\\main\\python\\code_price.txt")
    code_price = {}
    for line in f:
        # print(line)
        parts = line.split()
        # print(parts[2])
        code_price[parts[0]] = parts[2]
    return code_price


# if run this script
if __name__ == "__main__":
    main()