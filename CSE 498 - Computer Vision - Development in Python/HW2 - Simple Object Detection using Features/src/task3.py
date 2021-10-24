# Modified from source by Alexander Spivey
# # Source - Adam Czajka, Jin Huang, September 2019

import cv2
import numpy as np
from skimage import measure
from sys import platform as sys_pf
import os
import warnings

warnings.filterwarnings("ignore")

if sys_pf == 'darwin':
    import matplotlib

    matplotlib.use("TKAgg")
import matplotlib.pyplot as plt

plt.plot()

# Read the image
output_dir = r'/home/aspiv/Classes/CSE498/HW2/output/task3'   # Change this to output folder
sample = cv2.imread('/home/aspiv/Classes/CSE498/HW2/data/pills.png')     # Change this to input image

sample_small = cv2.resize(sample, (640, 480))
cv2.imshow('Original image', sample_small)


# Convert the original image to HSV
# and take H channel for further calculations
sample_hsv = cv2.cvtColor(sample, cv2.COLOR_BGR2HSV)
sample_h = sample_hsv[:, :, 0]


# Show the H channel of the image
sample_small = cv2.resize(sample_h, (640, 480))
cv2.imshow('H channel of the image', sample_small)


# Convert the original image to grayscale
sample_grey = cv2.cvtColor(sample, cv2.COLOR_BGR2GRAY)

# Show the grey scale image
sample_small = cv2.resize(sample_grey, (640, 480))
cv2.imshow('Grey scale image', sample_small)


# Binarize the image using Otsu's method
ret1, binary_image = cv2.threshold(sample_grey, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)


# Filling the holes
im_floodfill = binary_image.copy()
h, w = binary_image.shape[: 2]
mask = np.zeros((h + 2, w + 2), np.uint8)
cv2.floodFill(im_floodfill, mask, (0, 0), 255)
im_floodfill_inv = cv2.bitwise_not(im_floodfill)
binary_image = binary_image | im_floodfill_inv

sample_small = cv2.resize(binary_image, (640, 480))
cv2.imshow('Image after Otsu''s thresholding', sample_small)


# *** It's a good place to apply morphological opening, closing and erosion
# definition of a kernel (a.k.a. structuring element):
kernel1 = np.ones((5, 5), np.uint8)
kernel2 = np.ones((10, 10), np.uint8)
kernel3 = np.ones((15, 15), np.uint8)
kernel4 = np.ones((20, 20), np.uint8)

sample_step1 = cv2.erode(binary_image, kernel2, iterations=1)
sample_step2 = cv2.morphologyEx(sample_step1, cv2.MORPH_CLOSE, kernel1)
sample_step3 = cv2.morphologyEx(sample_step2, cv2.MORPH_OPEN, kernel1)

cv2.imshow('Image after morphological transformation1', sample_step1)
cv2.imshow('Image after morphological transformation2', sample_step2)
cv2.imshow('Image after morphological transformation3', sample_step3)

cv2.waitKey(0)
cv2.destroyAllWindows()


# Find connected pixels and compose them into objects
labels = measure.label(sample_step3, 8)


# Calculate features for each object;
# For task3, since we want to differentiate`
# between circular and oval shapes, the major and minor axes may help; we
# will use also the centroid to annotate the final result
properties = measure.regionprops(labels, intensity_image=sample_h)


# *** Calculate features for each object:
features = np.zeros((len(properties), 2))
for i in range(0, len(properties)):
    features[i, 0] = properties[i].major_axis_length
    features[i, 1] = properties[i].mean_intensity


# Show our objects in the feature space
plt.plot(features[:, 0], features[:, 1], 'ro')
plt.xlabel('Feature 1: Major Axis Length')
plt.xlim(0, 60)
plt.ylabel('Feature 2: Mean_Intensity')
plt.ylim(0, 60)
os.chdir(output_dir)
plt.savefig('t3_point_plot.png')
plt.show()


# *** Choose the thresholds for your features
thrF1 = 27
thrF2 = 16


# *** It's time to classify, count and display the objects
long_pill = 0
pill_circles = 0

fig, ax = plt.subplots()
ax.imshow(cv2.cvtColor(sample, cv2.COLOR_BGR2RGB))

# skipping properties[0] due to it being the entire image as a feature
for i in range(1, len(properties)):
    if features[i, 0] > thrF1 and features[i, 1] > thrF2:
        long_pill = long_pill + 1
        ax.plot(np.round(properties[i].centroid[1]), np.round(properties[i].centroid[0]), '.g', markersize=15)

    if features[i, 0] < thrF1 and features[i, 1] < thrF2:
        pill_circles = pill_circles + 1
        ax.plot(np.round(properties[i].centroid[1]), np.round(properties[i].centroid[0]), '.r', markersize=15)
plt.savefig('t3_count_result.png')
plt.show()


# That's all! Let's display the result:
print("I found %d long_pill and %d circle pills." % (long_pill, pill_circles))


# --Saving Results
print("before")
print(os.listdir(output_dir))
cv2.imwrite('t3_binary_image.png', binary_image)
cv2.imwrite('t3_sample_h.png', sample_h)
cv2.imwrite('t3_morph_result.png', sample_step3)
print("after")
print(os.listdir(output_dir))
