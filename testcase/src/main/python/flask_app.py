#!/usr/bin/env python
# encoding: UTF-8
"""
Python web app using flask framework.

install flask first: pip install flask
"""

from flask import Flask
from flask import request

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def home():
    return '<h1>Home</h1>'

@app.route('/signin', methods=['GET'])
def signin_form():
    return '''<form action="/signin" method="post">
              <p>Username: <input name="username"></p>
              <p>Password: <input name="password" type="password"></p>
              <p><button type="submit">Sign In</button></p>
              </form>'''

@app.route('/signin', methods=['POST'])
def signin():
    # get request parameters
    if request.form['username'] == 'admin' and request.form['password'] == 'admin123':
        return '<h3>Hello, admin!</h3>'
    return '<h3>Bad username or password.</h3>'

if __name__ == '__main__':
    app.run()
