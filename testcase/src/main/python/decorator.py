#!/usr/bin/env python
# encoding: GBK

import functools
import logging

# -----------------------------------------------------------------
# ������
# -----------------------------------------------------------------
def use_logging(func):

    def wrapper():
        logging.warn("%s is running" % func.__name__)
        return func()   # �� foo �����������ݽ���ʱ��ִ��func()���൱��ִ��foo()
    return wrapper

   
def foo():
    print('i am foo')

foo = use_logging(foo)  # ��Ϊװ���� use_logging(foo) ���ص�ʱ�������� wrapper����������൱��  foo = wrapper
foo()                   # ִ��foo()���൱��ִ�� wrapper()

# -----------------------------------------------------------------
# ʹ��@����
# -----------------------------------------------------------------
# ʹ��@���ţ�����ʡ��foo = use_logging(foo)�����ĸ�ֵ���
@use_logging
def anotherfoo():
    print("i am anotherfoo")

# ֱ�ӵ��ã�����Ҫanotherfoo = use_logging(anotherfoo)
anotherfoo()

# -----------------------------------------------------------------
# ԭ�������Զ�ʧ
# -----------------------------------------------------------------
# װ����
def logged(func):
    def with_logging(*args, **kwargs):
        print('call %s():' % func.__name__) 
        print(func.__doc__)       
        return func(*args, **kwargs)
    return with_logging

# ����
@logged
def f(x):
   """does some math"""
   return x + x * x
# f����loggedװ�κ󣬱�������with_logging��װ�����������������Ϣ
print(f.__name__)         # ��� 'with_logging'
print(f.__doc__)          # ��� None
print(f(50))

# -----------------------------------------------------------------
# ������װ�κ�����ʹ��functools.wraps����ԭ��������Ϣ
# -----------------------------------------------------------------
# װ����
def log(func):
    @functools.wraps(func)
    def wrapper(*args, **kw):
        print ('call %s():' % func.__name__)
        return func(*args, **kw)
    return wrapper

# ����
@log
def f2(x):
   """does some math in f2"""
   return x + x * x
# f����loggedװ�κ󣬱�������with_logging��װ�����������������Ϣ
print(f2.__name__)         # ��� 'with_logging'
print(f2.__doc__)          # ��� None
print(f2(50))

def main():
    pass
                

if __name__ == "__main__":
    main()
