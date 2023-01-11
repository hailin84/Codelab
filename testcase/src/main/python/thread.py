#!/usr/bin/env python
# encoding: UTF-8
"""
Demostrate python multi threads
"""
import threading
import time

def main():
   thread_run()
   
"loop for thread"
def loop():
    print('thread %s is running...' % threading.current_thread().name)
    n = 0
    while n < 5:
        n = n + 1
        print('thread %s >>> %s' % (threading.current_thread().name, n))
        time.sleep(1)
    print('thread %s ended.' % threading.current_thread().name)

def thread_run():
    print('thread %s is running...' % threading.current_thread().name)
    t = threading.Thread(target=loop, name='LoopThread')
    t.start()
    t.join()
    print('thread %s ended.' % threading.current_thread().name)
    
# if run this script
if __name__ == "__main__":
    main()