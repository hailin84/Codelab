#!/usr/bin/env python
# encoding: UTF-8
"""
Python GUI
"""

from tkinter import *
import tkinter.messagebox as messagebox

def main():
    app = Application()
    # set title:
    app.master.title('Hello World')
    # message loop:
    app.mainloop()
   
class Application(Frame):
    def __init__(self, master=None):
        Frame.__init__(self, master)
        self.pack()
        self.createWidgets()

    def createWidgets(self):
        self.nameInput = Entry(self)
        self.nameInput.pack()
        self.alertButton = Button(self, text='Hello', command=self.hello)
        self.alertButton.pack()

    def hello(self):
        name = self.nameInput.get() or 'world'
        messagebox.showinfo('Message', 'Hello, %s' % name)
        
# if run this script
if __name__ == "__main__":
    main()