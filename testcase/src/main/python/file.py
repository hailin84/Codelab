#!/usr/bin/env python
# encoding: UTF-8

# File Operation

import os
import time
import sys
import socket
from io import StringIO
from io import BytesIO

def main():
    file_op()
    io_op()

# 文件读写默认都为系统编码，可以通过encoding参数进行指定
def file_op():
    try:
        f = open('file.txt', 'r', encoding='gbk')
        print(f.read())
    finally:
        if f:
            f.close()
    
    with open('test.txt', 'w', encoding="UTF-8") as nf:
        nf.write('Hello, world!浣犲ソ锛宲ython') 

def io_op():
    f = StringIO()
    # dir(StringIO)
    f.write("hello");
    f.write(" python")
    print(f.getvalue())
    
    b = BytesIO()
    b.write("中文内容".encode("UTF-8"))
    print(b.getvalue())
        
if __name__ == "__main__":
    main()