import cv2
import os
import numpy as np
import argparse
from PIL import Image
from pylab import *
from skimage.exposure import rescale_intensity


def convolve(image, kernel, divideNum): # Code provided by PyImageSearch
    # gets the spatial dimensions of both inputs
    (iH, iW) = image.shape[:2]
    (kH, kW) = kernel.shape[:2]

    pad = (kW - 1) // 2  # for example, 3x3 kernal = 3-1 / 2 = 1 <- how much we need to pad by

    # pad the image by replicating the border
    image = cv2.copyMakeBorder(image, pad, pad, pad, pad, cv2.BORDER_REPLICATE)
    output = np.zeros((iH, iW), dtype="float32")

    # loop over the image, while sliding the kernel we are using (let-right, top-bottom)
    for y in np.arange(pad, iH + pad):
        # skips the length of pad, so it may start on actual start pixel
        for x in np.arange(pad, iW + pad):
            # extracts region of interest (roi)
            roi = image[y - pad:y + pad + 1, x - pad: x + pad + 1]  # grabs guys around the target y and x
            k = (roi * kernel).sum() / divideNum  # do matrix multiplication -> this is center value
            # some kernels don't require division, but gaussian2 does (1/16 * matrix table)
            # gausian3 does (1/273 * matrix)
            # gausian does (1/100 * matrix)
            output[y - pad, x - pad] = k  # reason for subtract is output size not same as img, due to padding
    output = rescale_intensity(output, in_range=(0, 255))
    output = (output * 255).astype("uint8")
    return output


def median(image, kernelSize):  # Structure by PyImageSearch
    (iH, iW) = image.shape[:2]  # gets the spatial dimensions of image
    pad = (kernelSize - 1) // 2  # for example, 3x3 kernal = 3-1 / 2 = 1 <- how much we need to pad by

    # pad the image by replicating the border
    image = cv2.copyMakeBorder(image, pad, pad, pad, pad, cv2.BORDER_REPLICATE)
    output = np.zeros((iH, iW), dtype="float32")  # create an empty structure to modify

    for y in np.arange(pad, iH + pad):
        # skips the length of pad, so it may start on actual start pixel
        for x in np.arange(pad, iW + pad):
            # extracts region of interest (roi)
            roi = image[y - pad:y + pad + 1, x - pad: x + pad + 1]  # grabs guys around the target y and x
            temp = []  # create a temp list
            for row in roi:  # add each item from ROI into list (2d to 1d conversion)
                for item in row:
                    temp.append(item)
            temp = np.sort(temp)  # sort the array
            # print("TEMP")
            # print(temp)
            k = np.median(temp)  # find the median
            # print(k)
            output[y - pad, x - pad] = k  # reason for subtract is output size not same as img, due to padding
    output = rescale_intensity(output, in_range=(0, 255))
    output = (output * 255).astype("uint8")
    return output


# --Testing with Array
# image = np.arange(0, 100).reshape(10, 10)
# print(image)
# foo = median(image, 3)
# print(foo)

# --Setup (REPLACE THE FOLLOWING DIRECTORIES WITH WHAT YOU WANT TO WORK WITH)
directory = r'/home/aspiv/Classes/CSE498/HW1/data/task2'
img_path1 = r'/home/aspiv/Classes/CSE498/HW1/data/task2/sp_image1.jpg'
img_path2 = r'/home/aspiv/Classes/CSE498/HW1/data/task2/sp_image2.jpg'
img_pathGB1 = r'/home/aspiv/Classes/CSE498/HW1/data/task2/gb_image1.jpg'
img_pathGB2 = r'/home/aspiv/Classes/CSE498/HW1/data/task2/gb_image2.jpg'
output_dir = r'/home/aspiv/Classes/CSE498/HW1/output/task2'
# --Read Image and Split Channels
img = cv2.imread(img_pathGB2)
red, green, blue = cv2.split(img)

# --Kernels
mean = np.array((
    [1, 1, 1],
    [1, 1, 1],
    [1, 1, 1]), dtype="int")
gaussian2 = np.array((  # /16
    [1, 2, 1],
    [2, 4, 2],
    [1, 2, 1]), dtype="int")
gaussian3 = np.array((  # /273
    [1, 4, 7, 4, 1],
    [4, 16, 26, 16, 4],
    [7, 26, 41, 26, 7],
    [4, 16, 26, 16, 4],
    [1, 4, 7, 4, 1]), dtype="int")
gaussian = np.array((  # /100
    [1, 2, 4, 2, 1],
    [2, 4, 8, 4, 2],
    [4, 8, 16, 8, 4],
    [2, 4, 8, 4, 2],
    [1, 2, 4, 2, 1]), dtype="int")

# --Testing
# ---Gaussian
redG = convolve(red, gaussian, 100)
greenG = convolve(green, gaussian, 100)
blueG = convolve(blue, gaussian, 100)
gaussianIMG = cv2.merge((redG, greenG, blueG))

# ---Mean
redMean = convolve(red, mean, 9)
greenMean = convolve(green, mean, 9)
blueMean = convolve(blue, mean, 9)
meanIMG = cv2.merge((redMean, greenMean, blueMean))

# ---Median 3x3
redMed = median(red, 3)
greenMed = median(green, 3)
blueMed = median(blue, 3)
medianIMG = cv2.merge((redMed, greenMed, blueMed))
# ---Median 5x5
redMed2 = median(red, 5)
greenMed2 = median(green, 5)
blueMed2 = median(blue, 5)
medianIMG2 = cv2.merge((redMed2, greenMed2, blueMed2))

# --Printing Results
cv2.imshow('Original', img)
cv2.imshow('Gaussian Image', gaussianIMG)
cv2.imshow('Mean Image', meanIMG)
cv2.imshow('Median Image 3x3', medianIMG)
cv2.imshow('Mean Image 5x5', medianIMG2)

# --Saving Results
gaussFilename = 'gaussianGB2.jpg'  # 'gaussian2.jpg'
meanFilename = 'meanGB2.jpg'  # 'mean2.jpg'
medianFilename = 'medianGB2_size3kernel.jpg'  # 'median2_size3kernel.jpg'
medianFilename2 = 'medianGB2_size5kernel.jpg'  # 'median2_size5kernel.jpg'
os.chdir(output_dir)
print("before")
print(os.listdir(output_dir))
cv2.imwrite(gaussFilename, gaussianIMG)
cv2.imwrite(medianFilename, medianIMG)
cv2.imwrite(medianFilename2, medianIMG2)
cv2.imwrite(meanFilename,  meanIMG)
print("after")
print(os.listdir(output_dir))

# --Close Window
cv2.waitKey(0)
cv2.destroyAllWindows()
