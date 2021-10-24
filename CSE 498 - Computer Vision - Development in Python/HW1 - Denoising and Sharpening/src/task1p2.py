import cv2
import os
import numpy as np
import argparse
from PIL import Image
from pylab import *
from skimage.exposure import rescale_intensity


def convolve(image, kernel, numDivide):  # Code provided by PyImageSearch
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
            k = (roi * kernel).sum() / numDivide  # do matrix multiplication -> this is center value
            # some kernels don't require division, but gaussian filter does (1/16 * matrix table)
            output[y - pad, x - pad] = k  # reason for subtract is output size not same as img, due to padding
    # make sure that our values are within range
    # output = rescale_intensity(output, in_range=(0, 255))
    # output = (output * 255).astype("uint8")
    # return the output image
    return output


# --Setup (REPLACE THE FOLLOWING DIRECTORIES WITH WHAT YOU WANT TO WORK WITH)
directory = r'/home/aspiv/Classes/CSE498/HW1/data/task1'
img_path1 = r'/home/aspiv/Classes/CSE498/HW1/data/task1/1.jpg'
img_path2 = r'/home/aspiv/Classes/CSE498/HW1/data/task1/2.jpg'
output_dir = r'/home/aspiv/Classes/CSE498/HW1/output/task1'

# --Read Image and Split Channels
# img0 = Image.open(img_path2)
img = cv2.imread(img_path2)
red, green, blue = cv2.split(img)

# --Convert to Array (needed when was working with PIL)
# im = array(img)
# red = array(red)
# green = array(green)
# blue = array(blue)

# --Kernels
sharpen = np.array((
    [0, -1, 0],
    [-1, 5, -1],
    [0, -1, 0]), dtype="int")
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
"""
gaussian = np.array(( #for some reason, trying to do the division within the matrix, ends up increasing
	[1/16, 2/16, 1/16], #brightness and saturation (harder to see)
	[2/16, 4/16, 2/16],
	[1/16, 2/16, 1/16]), dtype="int")
"""

# --Computer Generated Gaussian (for comparison)
cGaussianIMG = cv2.GaussianBlur(img, (5, 5), 0)

# --Testing (pre-rescale problem solved)
# rblur = cv2.GaussianBlur(red, (3, 3), 0) #seeing the gaussian filter on each channel
# gblur = cv2.GaussianBlur(green, (3, 3), 0)
# bblur = cv2.GaussianBlur(blue, (3, 3), 0)
# blur = Image.merge('RGB', (rblur, gblur, rblur))

# --Create Gaussian Image:
# print(im)
# print(im.shape, im.dtype)
redG = convolve(red, gaussian, 100)
greenG = convolve(green, gaussian, 100)
blueG = convolve(blue, gaussian, 100)
# redG = Image.fromarray(convolve(red, gaussian), 'R')
# greenG = Image.fromarray(convolve(green, gaussian), 'G')
# blueG = Image.fromarray(convolve(blue, gaussian), 'B')
gaussianIMG = cv2.merge((redG, greenG, blueG))

# --(I - G(I)) = D(I) && (I + D(I)) = I'
diff = img - gaussianIMG
# diff = rescale_intensity(diff, in_range=(0, 255))
# diff = (diff * 255).astype("uint8")
result = img + diff
result = rescale_intensity(result, in_range=(0, 255))
result = (result * 255).astype("uint8")

# --Printing Results
cv2.imshow('Original', img)

cv2.imshow('Gaussian Image', gaussianIMG)
cv2.imshow('Computer Generated Gaussian', cGaussianIMG)

cdiff = img - cGaussianIMG
cv2.imshow('Residual Image', cdiff)
cv2.imshow('Computer Generated Residual Image', diff)

cresult = img + cdiff
cv2.imshow('Computer Result', cresult)
cv2.imshow('Result: Normal Operations', result)

# diff2 = cv2.bitwise_xor(img, gaussianIMG)
# result2 = cv2.bitwise_or(img, diff2)
# cv2.imshow('Result2: Bitwise Operations', result2)
# if(img.all() == result4.all()): print(True)

""" #This attempt here was trying to fix the weird pixel overlay on edges
#Completey unnecessary since the issues that develop was rescaling intensity to early
# --Try applying difference individually
red1, green1, blue1 = cv2.split(img)
#gr1 = red1 - redG
gr1 = red1 - redG

gg1 = green1 - greenG
gb1 = blue1 - blueG
dr1 = red1 + gr1
dg1 = green1 + gg1
db1 = blue1 + gb1

otherresult = cv2.merge((dr1, dg1, db1))
cv2.imshow('Individual Operations before Merge', otherresult)
cv2.imshow('red1', red1)
cv2.imshow('gr1', gr1)
cv2.imshow('dr1', dr1)

gr1 = red1 - cv2.cvtColor(gaussianIMG, cv2.COLOR_BGR2GRAY)
gg1 = green1 - cv2.cvtColor(gaussianIMG, cv2.COLOR_BGR2GRAY)
gb1 = blue1 - cv2.cvtColor(gaussianIMG, cv2.COLOR_BGR2GRAY)
otherresult = cv2.merge((dr1, dg1, db1))
cv2.imshow('2Individual Operations before Merge', otherresult)
cv2.imshow('red2', red1)
cv2.imshow('gr2', gr1)
cv2.imshow('dr2', dr1)
"""

# --Saving Results
filename = 'enhanced2.jpg'
os.chdir(output_dir)
print("before")
print(os.listdir(output_dir))
cv2.imwrite(filename, result)
print("after")
print(os.listdir(output_dir))

# --Close Window
cv2.waitKey(0)
cv2.destroyAllWindows()
