import tkinter as tk
from tkinter import *
from tkinter import font as tkfont
import random

#Methods
def hide_n_display():
    hideText()
    displayText()

def displayText():
    workoutNumber = random.randrange(0,len(workouts))
    #print(workoutNumber)
    label1 = tk.Label(root, text= workouts[workoutNumber], fg='black', font=('arial', 16, 'bold'))
    canvas1.create_window(150, 150, window=label1)

def hideText():
    label1 = tk.Label(root, text= eraser, fg='black', font=('arial', 16, 'bold'))
    canvas1.create_window(150, 150, window=label1)

def modifyList():
    userInput = ""
    
#Variables
eraser = "                                                              "
root = tk.Tk()
WIDTH = 300
HEIGHT = 300
canvas1 = tk.Canvas(root, width = WIDTH, height = HEIGHT)
canvas1.pack()
root.title("Workout Dice - by Alexander Spivey")

#Read into file
'''
f = open("workoutDice\workouts.txt")
workouts = f.readlines()
workouts = [x.strip() for x in workouts]
f.close()
'''
workouts = ["Push-Ups - 25", "Sit-Ups - 20","Toe-Touches - 15","V-Ups - 15","Plank - 30 secs","Jumping Jacks - 20","Squats - 15","Crunches - 30","Wall Sit - 30 secs","Burpess - 10","Russian Twist - 15","Lunges - 10","Mock Jump Rope - 30 secs"]

#Actual Program
clickButt = tk.Button(text='Click Me!!!',command = hide_n_display, bg='red',fg='white')
modifyWorkButt = tk.Button( text='Modify Workouts [to come]',command= modifyList, bg='black',fg='white')
canvas1.create_window(150, 250, window=clickButt)
canvas1.create_window(90, 30, window=modifyWorkButt)

#End File Shit
root.resizable(width=False, height=False)
root.maxsize(300,300)
root.minsize(300,300)
root.mainloop()