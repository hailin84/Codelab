#!/usr/bin/env python
# encoding: UTF-8
"""
Demostrate python syntax usage
Python3 tutorial: https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000
"""
import os
import time
import sys

def main():
    basic()
    func_str()
    func_list()
    func_tuple()
    useclass()
    
def basic():
    print("hello python")
    for one in (sys.path, sys.platform, sys.version, sys.api_version):
        print(one)
    print(123)
    print("%s your number is %d, salary %f" % ("Kobe", 2034, 5000.45))
    
    # use input instead of raw_input in python3
    # name = input("Enter your name: ")
    # print(name)
    sys.stdout.write("Hello, Python\n")
    
def func_str():
    print("=" * 20)
    str1 = "Pythonisgreat"
    print(str1[2:])
    print(str1[2:5])
    str2 = """Multi line str, also as comment
    Haha.
    """
    print(str2)

def func_list():
    lst1 = [1, 2, 3, 4, "abc"]
    lst1.append(5000.45)
    lst2 = range(10)
    print(lst1)

def func_tuple():
    # tuple元组不能被修改
    tuple1 = (1, 2, 3, 4)
    tuple2 = ()
    tuple3 = (5,) # must add ,
    

class House(object):
    """
    A simple class
    """
    version = "1.0"
    author = "myumen"
    
    # 相当于构造函数后的初始化操作
    def __init__(self, location, roomNum):
        self.location = location
        self.roomNum = roomNum
    
    def show(self):
        print(self.roomNum, self.location)
        print(self.__class__.__name__)

def useclass():
    myhouse = House("深圳市龙华区", 3)
    myhouse.show()
    print(id(myhouse))
    print(type(myhouse))

# if run this script
if __name__ == "__main__":
    main()