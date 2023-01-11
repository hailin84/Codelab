#!/usr/bin/env python
# encoding: UTF-8

"""序列化"""

import pickle
import json

def main():
    func_serial()

# 文件读写默认都为系统编码，可以通过encoding参数进行指定
def func_serial():
    d = dict(name='Bob', age=20, score=88)
    
    bytes = pickle.dumps(d)
    print(bytes)
    print(json.dumps(d))
    print(dir(pickle))
    
        
if __name__ == "__main__":
    main()