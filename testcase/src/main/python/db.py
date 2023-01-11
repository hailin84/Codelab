#!/usr/bin/env python
# encoding: UTF-8
"""
Python database, use sqlite3
"""

import sqlite3

_default_dabatbase = "test.db"

def main():
    # func_create()
    func_query()
   
def func_create():
    conn = sqlite3.connect('test.db')
    cursor = conn.cursor()
    # cursor.execute('create table user (id varchar(20) primary key, name varchar(20))')
    # cursor.execute('insert into user (id, name) values (\'1\', \'Michael\')')
    cursor.execute('insert into user (id, name) values (\'2\', \'Durant\')')
    print(cursor.rowcount)
    cursor.close()
    conn.commit()
    conn.close()

def create_db(name):
    tmp = name
    if len(name) == 0:
        tmp = _default_dabatbase
    conn = sqlite3.connect(tmp)
    conn.close()

def func_query():
    conn = sqlite3.connect('test.db')
    cursor = conn.cursor()
    
    try:
        cursor.execute('select * from user where id>=?', ('1',))
        conn.commit()
        values = cursor.fetchall()
        print(values)
    except:
        conn.rollback()
    finally:
        cursor.close()
        conn.close()
     
# if run this script
if __name__ == "__main__":
    main()