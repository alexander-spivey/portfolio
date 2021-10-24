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
input_dir = r'/home/aspiv/Classes/CSE498/HW1/data/task1'
img_path1 = r'/home/aspiv/Classes/CSE498/HW1/data/task1/1.jpg'
img_path2 = r'/home/aspiv/Classes/CSE498/HW1/data/task1/2.jpg'
output_dir = r'/home/aspiv/Classes/CSE498/HW1/output/task1'

# --Read Image and Convert
# img = Image.open(img_path).convert('L')
img0 = cv2.imread(img_path1)
img = cv2.cvtColor(img0, cv2.COLOR_BGR2GRAY)
# im = array(img)

# --Kernels
sharpen = np.array((
    [0, -1, 0],
    [-1, 5, -1],
    [0, -1, 0]), dtype="int")
gaussian = np.array((  # sum/16 aka divideNum = 16
    [1, 2, 1],
    [2, 4, 2],
    [1, 2, 1]), dtype="int")

# --Computer Generated Gaussian (for comparison)
blur = cv2.GaussianBlur(img, (3, 3), 0)
# print(im)
# print(im.shape, im.dtype)

# --Create Gaussian Image:
gaussianIMG = convolve(img, gaussian, 16)  # NOT RESCALED (not for display, but for operations)
gaussianIMGResult = rescale_intensity(gaussianIMG, in_range=(0, 255))
gaussianIMGResult = (gaussianIMGResult * 255).astype("uint8")

# --(I - G(I)) = D(I) && (I + D(I)) = I'
diff = img - gaussianIMG
result = img + diff  # not rescaled still (will look wack!)
result = rescale_intensity(result, in_range=(0, 255))  # rescale back to 0, 255
result = (result * 255).astype("uint8")

# --Printing Results
cv2.imshow('pre-original', img0)
cv2.imshow("original (grayscale)", img)

cv2.imshow('gaussian', gaussianIMGResult)
cv2.imshow('compGaussian', blur)

compDiff = img - blur
cv2.imshow('compDiff', compDiff)
cv2.imshow('difference', diff)

compResult = img + compDiff
cv2.imshow('compResult', compResult)
cv2.imshow('result', result)

# --Saving Results
filename = 'enhanced1.jpg'
os.chdir(output_dir)
print("before")
print(os.listdir(output_dir))
cv2.imwrite(filename, result)
print("after")
print(os.listdir(output_dir))

# --Close Window
cv2.waitKey(0)
cv2.destroyAllWindows()
