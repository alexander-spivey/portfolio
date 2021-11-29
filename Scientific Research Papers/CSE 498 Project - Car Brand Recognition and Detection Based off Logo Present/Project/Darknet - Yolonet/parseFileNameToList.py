#Simple script to receive all file names in one spot (for Yolonet)
import xml.etree.ElementTree as ET
import pickle
import os
from os import listdir, getcwd
from os.path import join
import glob
osDIR = r'G:/Users/Alex/Desktop/Classes/CSE 498 - Computer Vision/Project/Outdated Dataset/Dataset/obj'
from os import walk

filenames = next(walk(osDIR), (None, None, []))[2]
print(filenames)

out_file = open('trainImages' + '.txt', 'w')
out_file2 = open('testImages' + '.txt', 'w')
counter = 0
countertarget = 10
for filename in filenames:
    if filename.endswith(".jpg"):
        if counter == countertarget:
            out_file2.write('/content/darknet/data/obj/' + filename + '\n')
            counter = 0
        else:
            out_file.write('/content/darknet/data/obj/' + filename + '\n')
            counter += 1
        

    
out_file.close()
out_file2.close()