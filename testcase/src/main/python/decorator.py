#!/usr/bin/env python
# encoding: GBK

import functools
import logging

# -----------------------------------------------------------------
# 简单例子
# -----------------------------------------------------------------
def use_logging(func):

    def wrapper():
        logging.warn("%s is running" % func.__name__)
        return func()   # 把 foo 当做参数传递进来时，执行func()就相当于执行foo()
    return wrapper

   
def foo():
    print('i am foo')

foo = use_logging(foo)  # 因为装饰器 use_logging(foo) 返回的时函数对象 wrapper，这条语句相当于  foo = wrapper
foo()                   # 执行foo()就相当于执行 wrapper()

# -----------------------------------------------------------------
# 使用@符号
# -----------------------------------------------------------------
# 使用@符号，可以省略foo = use_logging(foo)这样的赋值语句
@use_logging
def anotherfoo():
    print("i am anotherfoo")

# 直接调用，不需要anotherfoo = use_logging(anotherfoo)
anotherfoo()

# -----------------------------------------------------------------
# 原函数属性丢失
# -----------------------------------------------------------------
# 装饰器
def logged(func):
    def with_logging(*args, **kwargs):
        print('call %s():' % func.__name__) 
        print(func.__doc__)       
        return func(*args, **kwargs)
    return with_logging

# 函数
@logged
def f(x):
   """does some math"""
   return x + x * x
# f经过logged装饰后，变成里面的with_logging包装函数，故输出如下信息
print(f.__name__)         # 输出 'with_logging'
print(f.__doc__)          # 输出 None
print(f(50))

# -----------------------------------------------------------------
# 完整的装饰函数，使用functools.wraps保留原函数的信息
# -----------------------------------------------------------------
# 装饰器
def log(func):
    @functools.wraps(func)
    def wrapper(*args, **kw):
        print ('call %s():' % func.__name__)
        return func(*args, **kw)
    return wrapper

# 函数
@log
def f2(x):
   """does some math in f2"""
   return x + x * x
# f经过logged装饰后，变成里面的with_logging包装函数，故输出如下信息
print(f2.__name__)         # 输出 'with_logging'
print(f2.__doc__)          # 输出 None
print(f2(50))

def main():
    pass
                

if __name__ == "__main__":
    main()
