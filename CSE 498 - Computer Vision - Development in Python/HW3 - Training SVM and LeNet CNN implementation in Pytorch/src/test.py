#SCRIPT FROM https://medium.com/@jeff.lee.1990710/image-similarity-using-vgg16-transfer-learning-and-cosine-similarity-98571d8055e3
#USED TO PRINT BASEMODEL SUMMARY
#DIFFER VS FC1 and FC2


import pandas as pd
import numpy as np
import keras
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import h5py
import cv2
from keras.layers import Flatten, Dense, Input,concatenate
from keras.layers import Conv2D, MaxPooling2D
from keras.layers import Activation, Dropout
from keras.models import Model
from keras.models import Sequential
import tensorflow as tf

vgg16 = keras.applications.VGG16(weights='imagenet', include_top=True, pooling='max', input_shape=(224, 224, 3))

basemodel = Model(inputs=vgg16.input, outputs=vgg16.get_layer('fc2').output)

print(basemodel.summary())