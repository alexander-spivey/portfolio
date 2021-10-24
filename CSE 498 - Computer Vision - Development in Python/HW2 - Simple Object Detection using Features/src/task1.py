# Modified from source by Alexander Spivey
# # Source - Adam Czajka, Jin Huang, September 2019

import cv2
import os
import numpy as np
from skimage import measure
from sys import platform as sys_pf
import warnings

warnings.filterwarnings("ignore")

if sys_pf == 'darwin':
    import matplotlib

    matplotlib.use("TKAgg")
import matplotlib.pyplot as plt

plt.plot()
# print(matplotlib.get_backend())

# Read the image as grayscale
output_dir = r'/home/aspiv/Classes/CSE498/HW2/output/task1'     # Change this to output folder
sample = cv2.imread('/home/aspiv/Classes/CSE498/HW2/data/breakfast1.png', cv2.IMREAD_GRAYSCALE)     # Change this to input image

sample_small = cv2.resize(sample, (640, 480))
cv2.imshow('Grey scale image', sample_small)
# cv2.waitKey(0)
# cv2.destroyAllWindows()


# Binarize the image using Otsu's method
ret1, binary_image = cv2.threshold(sample, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)

sample_small = cv2.resize(binary_image, (640, 480))
cv2.imshow('Image after Otsu''s thresholding', sample_small)
# cv2.waitKey(0)
# cv2.destroyAllWindows()


# *** Here is a good place to apply morphological operations
# definition of a kernel (a.k.a. structuring element):
kernel1 = np.ones((5, 5), np.uint8)
kernel2 = np.ones((10, 10), np.uint8)
kernel3 = np.ones((15, 15), np.uint8)
kernel4 = np.ones((20, 20), np.uint8)

# one iteration of morphological dilation:
sample_step1 = cv2.dilate(binary_image, kernel2, iterations=1)
# one iteration of morphological erosion:
sample_step2 = cv2.erode(sample_step1, kernel4, iterations=1)
# morphological closing:
sample_step3 = cv2.morphologyEx(sample_step2, cv2.MORPH_CLOSE, kernel4)
# morphological opening:
sample_step4 = cv2.morphologyEx(sample_step3, cv2.MORPH_OPEN, kernel2)

# sample_small = cv2.resize(binary_image, (640, 480))
cv2.imshow('Dilate Image after morphological operations', sample_step1)
cv2.imshow('ERODE Image after morphological operations12', sample_step2)
cv2.imshow('CLOSE Image after morphological operations3', sample_step3)
cv2.imshow('OPEN Image after morphological operations4', sample_step4)
cv2.waitKey(0)
cv2.destroyAllWindows()


# Find connected pixels and group them into objects
labels = measure.label(sample_step4, 8)


# Calculate features for each object; since we want to differentiate
# between circular and oval shapes, the major and minor axes may help; we
# will use also the centroid to annotate the final result
features = measure.regionprops(labels)
print("I found %d objects in total." % (len(features)))


# In this task it is enough to calculate the ratio
# between tha major and minor axes
his = []
for i in range(0, len(features)):
    if features[i].minor_axis_length > 0:
        his.append(features[i].major_axis_length / features[i].minor_axis_length)

# Now we can look at the histogram to select a global threshold
plt.hist(his)
plt.xlabel("Ratio")
plt.ylabel("Count")
os.chdir(output_dir)
plt.savefig('t1_histogram.png')
plt.show()


# *** Select a proper threshold
fThr = 1.6


# It's time to classify, count and display the objects
squares = 0
cashews = 0

fig, ax = plt.subplots()
ax.imshow(sample, cmap=plt.cm.gray)


# skipping properties[0] due to it being the entire image as a feature
for i in range(1, len(his)):
    if his[i] <= fThr:
        squares = squares + 1
        y, x = features[i].centroid
        ax.plot(x, y, '.g', markersize=10)
    else:
        cashews = cashews + 1
        y, x = features[i].centroid
        ax.plot(x, y, '.b', markersize=10)
plt.savefig('t1_count_result.png')
plt.show()


# That's all! Let's display the result:
print("I found %d squares, and %d cashew nuts." % (squares, cashews))


# --Saving Results
print("before")
print(os.listdir(output_dir))
cv2.imwrite('t1_binary_image.png', binary_image)
cv2.imwrite('t1_morph_result.png', sample_step4)
print("after")
print(os.listdir(output_dir))
