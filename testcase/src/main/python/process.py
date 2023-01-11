#!/usr/bin/env python
# encoding: UTF-8
"""
Demostrate python process
"""
from multiprocessing import Process, Queue
import os, time, random
import subprocess
from test.test_threading_local import target

def main():
   use_process()
   use_subprocess()
   process_communicate()
    
def use_process():
    print('Parent process %s.' % os.getpid())
    p = Process(target=run_proc, args=('test',))
    print('Child process will start.')
    p.start()
    p.join()
    print('Child process end.')

def run_proc(name):
    print("Run child process %s (%s)" % (name, os.getpid()))

def use_subprocess():
    print('$ nslookup www.python.org')
    r = subprocess.call(['nslookup', 'www.python.org'])
    print('Exit code:', r)

"""
Communication between processes. Produces and comsumer
"""
def process_communicate():
    q = Queue()
    pw = Process(target=produce, args=(q,))
    pr = Process(target=consume, args=(q,))
    pw.start()
    pr.start()
    pw.join()
    pr.terminate()

def produce(q):
    print("Process to write: %s" % os.getpid())
    for value in ["A", "B", "C"]:
        print("produce %s to queue." % value)
        q.put(value)
        time.sleep(random.random())

def consume(q):
    print("Process to read: %s" % os.getpid())
    while True:
        value = q.get(True)
        print("consume %s from queue." % value)

# if run this script
if __name__ == "__main__":
    main()