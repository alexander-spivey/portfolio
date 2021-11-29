## Simple script to view images, convert to jpg
# Import all computer vision modules
import cv2
import numpy as np
import os
import matplotlib.pyplot as plt
from os import walk

# Import an image from a specified directory and display it on the screen
def load_images_from_folder(folder):
    images = []
    for filename in os.listdir(folder):
        img = cv2.imread(os.path.join(folder,filename))
        if img is not None:
            images.append((img, filename))
    return images

def convert_to_jpg(path):
    for root, dirs, files in walk(path):
        for file in files:
            if file.endswith(".png"):
                img = cv2.imread(os.path.join(root,file))
                if img is not None:
                    cv2.imwrite(os.path.join(root,file[:-4]+".jpg"), img)
                    os.remove(os.path.join(root,file))

path = r"G:/Users/Alex/Desktop/Classes/CSE 498 - Computer Vision/Project/Outdated Dataset/Dataset/Hyundai-1"
#convert_to_jpg(path)
imgs = load_images_from_folder(path)
# counter = 301
# for root, dirs, files in walk(path):
#     for file in files:
#         img = cv2.imread(os.path.join(root,file))
#         if img is not None:
#             cv2.imwrite(os.path.join(root,str(counter)+".jpg"), img)
#             os.remove(os.path.join(root,file))
#             counter += 1


for img in imgs:
    cv2.imshow('img',img[0])
    print(img[1])
    print(img[0].shape)
    cv2.waitKey(0)
    cv2.destroyAllWindows()