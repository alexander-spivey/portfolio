Date: 11/29/2021
Author: Alexander Spivey and Rishika G.
Version 3.0
Purpose: Car Logo Detection and Classification

Folder: Scripts - Inside this folder contains all the primary scripts used for the current project purpose.
	1. Analysis of Directories - HOG + SGD or CNN w. Sliding Window: 
	- Imports and Method Definitions:
		- This is the initial start of our project, used to create the classification algorthim used.
		- Method 1. parseXMLtoDF_Region(osDIR): This method was used to parse all XML of a certain directory and load the images associated. However,
		this method should be only used to parse the full car images to get logo bndbox truth. This method uses glob to find all files associated
		with a certain format and xml element tree to parse the xmls. After one xml has been parsed, it appends itself to the final DataFrame.
		The bndbox of logos are scaled with respected to the images scaling (always right region)
		- Method 2. parseXMLtoDF_Logo(osDIR): This method was used to parse all XML of a certain directory, as long as the images only contained the 
		logos. As earlier, it returns a DataFrame. All logos are loaded in as grayscale.
		- Method 3. displayBNDBOXinDF(df): It takes an input of a DataFrame and then outputs as many 5 image per layers as possible.
		- Method 4. testImage(path): It takes an image path and makes a prediction using the SVM model provided further down. It outputs the prediction 
		as numerical and converted to label using label encoder.
		- Method 5. testImageCNN(path): Same method as above but uses the CNN model provided further down.
		- Method 6. testRegionImage(df): Pick random image within the DataFrame and loads it out. Use '0' to close.
	- Import of All Logo Classes:
		- To load all logo images, we use parseXMLtoDF_Logo(). The background class was added, but not used in RCNN, but was used for Sliding window
		- To save time, it is possible to export the dataframes as CSV and read them back in with ease.
		- To see count of dataframes use .info()
	- Reading Logos From Car Logo Regions:
		- To load all logo images, we use parseXMLtoDF_Region(). The end result should grab all the logo regions from the car dataset, which in turn
		gives us more images to train on.
	- Combining Logos:
		- To save space, I withdrew the 3 primary features from all the datasets loaded in: filename, bndbox_img, objectType aka logo label
		- After which, they were all combined into combinedDF and then the resulting objectType column was extracted and used for the labelEncoder
		- The combinedDF bndbox_img was then extracted to create our train_images
	- Extracting HOG Features:
		- To extract HOG features, import hog and do it on the images. All training images have to be same size. In the end, the train_images list
		was looped through and used to extract hog features and the resulting hog image. The extracted were appended to hog_images and hog_features
		respectfully.
	- HOG:
		- The resuling Hog features were used as X, and labels as Y, with a .2 validation test size. 
		- The model was trained on both SGDclassifier and LinearSVM. Choose, which model to use.
		- Predictions were made and received a general 0.98.
		- A line has been made available to save the model, change with respect to purpose
	- HOG Testing below:
		- This section demonstrates how to test the image on one from the dataframe or a new one entirely
	- HOG Images + CNN:
		- The resulting Hog Images were used as X, and labels as Y. 
		- Using tensorflow, a CNN model was created for the one-dimensional training images.
		- NOTICE: When trained on the normal images of logo vs the hog images of logo, the hog variation received an 98% percision and normal received
		83% when given same amount of data. 
		- A line has been made avaialble to save the model, change with respect to purpose. CNN for model 4 is CNNHOGwRegion3. CNN for model 5, all
		classes, is saved as ALLcnnHOGwRegion
	- Sliding Window Implementation and Testing - FAILURE:
		- As the section title explains, this was one of the failed attempts to get the ROI. 
		- Method 1. sliding_window(image, stepSize, windowSize): The method takes an image and the windowsize, aka training logo image size, and 
		calculates the region that is currently on. This loops all the way down for x and y, in turn, going over every single region of the image.
		- image_pyramid(image, scale, minSize): This method takes an image and scales it, returning each image level that is smaller and smaller. 
		This used in combination with the sliding window will allows us to search for the logo, no matter the area or size.
	- CNN IMPLEMENTATION and SVM IMPLEMENTATION:
		- The giant loops contain all infromation needed to explain process, but the loop first grabs the image*scale we are on and then runs the
		sliding window over it. If it hits a high enough confidence, it will add that to the detections list along with the x, y, w, h, and pred.
		(The x, y, w, h is in respect to the original image dimensions so the detections would appear in the correct location)
		- The image is downscaled and then looped through once again.
		- Once the loop is done, non max suppression is done on the detections, based on the probability score. 
		- The resulting boxes are then displayed on the original image.
		- To see prediction probability score, run the last line. 
	Implementation based on: https://github.com/jianlong-yuan/HOG-SVM-python/blob/master/object-detector/test-classifier.py
	
	2. Analysis of Directories - RCNN on All 7 Classes:
	Imports and Method Definitions:
		- Refer to script 1's method definitions
	Import all Classes (7):
		- Using parseXMLtoDF_Region(path) we save each car type into its own DataFrame
		- Combine all DataFrames into combinedRegionDF
	Testing Selective Search on Image:
		- Method 1. checkAllRegions(df): It allows the user to load every image iteratively and see where the bndbox has been labeled as (to make
		sure scaling was accurate)
		- cv2 is loaded and used to create our selective search
		- The generated image is the result of selective search on the test image. The code is easily followable.
		- Method 2. IOU(box1, box2): Calculates the boxes overlap/union precentage. box1 and box2 are in format [xmin, ymin, xmax, ymax]
		- Train_images and bndbox is extracted from the combinedDFRegion. 
	Grabbing Our Data from the Training Images:
		- Once the training images have been defined, selective search is conducted on the image and for every result, the computer will
		try to append 5 cases where the IOU between the result's box and the true bndbox is above 0.75 and 5 below 0.3. However, it is typical for 
		the image to append one 'true' region (where IOU is > 0.75) or not even at all (only 234/355 true regions were found for the training case
		with all 7 classes). 
		- The resulting X_images and X_labels will be a collection of images of logo and not, with respect to X_labels label and index.
		- X_images and X_labels were converted into numpy arrays, respectfully called X_new and y_new
	VGG16 Feature Extraction:
		- VGG16 was downloaded and loaded with the weights of imagenet.
		- The last 2 layers of VGG16 was removed and replaced with a softmax, 2 output dense layer (since we want only 2 results: yes logo, or no)
		- Using Adam as the optimizer, with the main metric being accuracy, the model is done and summary printed out.
	Data Splitting:
		- Class 1. MyLabelBinarizer(): Convert label to [0, 1] format and back.
		- The data is split on X_new and the converted y_new, Y, with 0.3 as testing.
		- Using ImageDataGenerator and flow, load the data into the model. The batch size max varies on computer capabilities. We were limited to 10
		- Modify ModelCheckpoint first argument to modify what to save the weights as. 
		- Early stop is set when val_loss doesn't modify enough
		- Modify model_final.fit_generator to choose proper steps_per_epochs (how many batches of training) and validation_step (how many batches of
		testing)
		- Modify the save statment of the model to proper save location. Model for all 7 classes called modelALL
	Importing Trained Model:
		- Since our prediction models for logo were created in script 1 and saved, we can reload them.
		- Load cnn model into cnnHOGwRegion
		- Load the associated DataFrame the CNN was trained on to create the same label encoder used in dataset, so predictions can be converted.
		- Once label encoder has been set up, hog is imported and tested on one of the car's true bounding boxes for the logo. Make sure the pred-
		iction is accurate.
	Testing:
		- Similar to the grabbing the data for the training, we loop through each image and then using selective search, we get results that we feed
		into our model and then based on whether or not it passes the threshold (set at 0.7, but change if needed). If it does, it is appended to
		a list of all ROIS along with their confidence. The highest confidence is then chosen after all results have been explored and then the
		feed into the CNN model. The prediction is then outputed along with the ROI's [xmin, ymin, xmax, ymax, (width, height).
		- For testing purposes, please make sure that every output, such as true name, image file, logo, is available and has been modified with
		respect to purpose.
	Mad Mans Idea:
		- The idea below was based ont the premise that there are never enough actual logo images in X_new. Typically, only 70-85% of training 
		images provided did it even extract a positive logo region. Instead, we combined our logo dataset into X_new so that there would be a 3:4 
		ratio on logos to not logos for X_new. This was how model4 was created.
	Implementation based on: https://github.com/1297rohit/RCNN/blob/master/RCNN.ipynb
	
	3. Analysis of Directories - RCNN model 4 and 5 with cnnHOGwRegion3.ipynb
	Model Loading:
		- Please load in the model that you want to test. The following is an explanation of every model present:
			- Availble in (Logo Annotation Dataset\Test\model)
			1. ALLcnnHOGwRegion
				- CNN trained on all hog images of logos available for all 7 classes
			2. cnnHOG
				- CNN trained on all hog images of from only the logo dataset 5 classes [Benz, Chevrolet, BMW, Lambo, Toyota] 
			3. cnnHOGwRegion2
				- CNN trained on all hog images of logos for 5 classes [Benz, Chevrolet, BMW, Lambo, Toyota,]
				- Car logos included
			4. cnnHOGwRegion3
				- CNN trained on all hog images of logos for 4 classes [Benz, Chev, Toyota, Volkswagen]	
				- Car logos included
			5. cnnNorm
				- CNN trained on normal images of logo from the logo dataset
			6. model
				- Built on VGG16, for [mercedes, lambo, bmw, toyota, chevrolet]
			7. model2moreImages
				- Built on Resnet, for [mercedes, lambo, bmw, toyota, chevrolet]
			8. model3moreImages
				- Built on VGG16, 1:50 Ratio for True to False Logo Images (don't ever use), to be used with cnnHOGwRegion2
			9. model4moreImages
				- Built on VGG16, 1:5 Ratio for True to False Logo Images, to be used with cnnHOGwRegion3 
			10. model5moreImages
				- Built on VGG16, 3:4 Ratio for True to False Logo Images using Mad Man's method in script 2, to be used with cnnHOGwRegion3
			11. modelALL
				- Built on VGG16, 1:5 Ratio for True to False Logo Images, to be used with ALLcnnHOGwRegion
			12. sgdCwRegion.sav
				- SGDclassifier, used for Sliding Window
	Testing Follows Script 2 implementation. Modify with respect to purpose.
	Implementation based on: https://github.com/1297rohit/RCNN/blob/master/RCNN.ipynb

	4. Faster RCNN Attempt - FAILURE
	- Do not use script.
	- Due to all efforts being unfruitful with faster RCNN, and not to mention, a bit of an overkill for the project, we moved backwards to RCNN. 
	This notebook has been left uncommented due to it's lack of use, but has been provided to show how RCNN was implemented by first 
	understanding faster-rcnn, through hands on experience.
	- Implementation based on: https://github.com/rbgirshick/py-faster-rcnn
		

Folder: Darkent - Yolonet
	- This was implemented before RCNN and didn't work due to lack of data and tried to predict car brand off model
	- Within this folder contain a multitude of small scripts that are easily modifable and understandable that can remove duplicate images, parse
	all file names within a directory to a list, view and convert images to jpg, and basic analysis on the no longer used dataset of car models.
	- All other odd files within the folder are used to give weight, training images, training lables, testing images, testing lables, along with 
	a custom configuration for Yolonet. 
	- Modify all code with respect to purpose and data available.
	Implementation based on: https://medium.com/@thomas.lever.business/training-yolov3-convolutional-neural-networks-using-darknet-2f429858583c

Folder: Dataset
	- Contains a collection of all logo only images used along with their respective DataFrame csv files
	- Benz and Bentley were used in Sliding Window, but are not used in RCNN
	- Every other class was used for RCNN model and CNN model at least once
	- Please extract all files from the zip folder and move them back directly under the Dataset folder. 

Folder: Logo Annotation Dataset
	- Contains a collection of different brand cars along with XML files containing each files respectful bounding boxes and labels.
	- The odd text files contains outputs of predictions for each model along with their respective experiment number
	- Please extract all files from the zip folder and move them back directly under the Logo Annotation Dataset folder. 
	- Due to space limitations, please download the dataset from: https://drive.google.com/drive/folders/1tJNlhN2hSqWABPrldnuJ3Nqhms4u8Iep?usp=sharing

Folder: Logo Annotation Dataset\TEST
	- Contains models and model weights, along with a singular quick access test image.
	- Due to the gigabytes of space it takes to save the models, all models have been moved to GDrive for download:
		- https://drive.google.com/drive/folders/1tJNlhN2hSqWABPrldnuJ3Nqhms4u8Iep?usp=sharing

Item: tf.yml
	- Anaconda enviroment of all dependencies. Make sure that all dependencies used are predownloaded beforehand. 


**WARNING: CREATION OF TRAINING DATA TYPICALLY TAKES LESS THAN ONE HOUR. TESTING OF IMAGE THROUGH RCNN AND CNN TAKES APPROXIMATELY 3-4 MINUTES PER IMAGE**		