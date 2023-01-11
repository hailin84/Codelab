#!/usr/bin/env python
# encoding: UTF-8
"""
Python数据类型示例
"""
import os
import time
import sys
import socket

def main():
    func_number()
    func_str()
    func_call()

def func_number():
    a = 100
    b = 99999 # python3已经没有long类型数字
    c = 66.55
    d = 1.23 + 4.56j
    e = 0xff00 # 16进制
    print(a, b, c, d, e)
    # print(cmp(a, b))
    print(hex(a))
    print(power(5))
    print(power(5, 4))
    
def func_str():
    a = "ABC"
    b = b"ABC"
    print(a.encode("ASCII"))
    print("中华人民共和国".encode("UTF-8"))

def func_call():
    a = set(["abc", "def", "efg"])
    b = {'Michael': 95, 'Bob': 75, 'Tracy': 85}

    person('Bob', 35, city='Beijing')
    person('Adam', 45, gender='M', job='Engineer')

    print(calc(1, 3, 4, 5))

def power(x, n=2):
    s = 1
    while n > 0:
        n = n - 1
        s = s * x
    return s

# 可变参数，参数自动组装成元组tuple
def calc(*numbers):
    sum = 0
    for n in numbers:
        sum = sum + n * n
    return sum

# 关键字参数，参数自动组装成dict
def person(name, age, **kw):
    print('name:', name, 'age:', age, 'other:', kw)

if __name__ == "__main__":
    main()