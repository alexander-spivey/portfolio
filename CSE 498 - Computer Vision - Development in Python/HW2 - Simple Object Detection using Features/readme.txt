Date: Finished 9/24/2021 10:57am
Author: Alexander Spivey
Version: 1.0
Purpose: Object Detection and Feature Thresholding

--Writeup--
- To use each script written, 
	please reference each purpose below,
	have all available libraries (conda enviroment.yml),
	and have a proper python interpretter (python 3.9)
- Each file mentioned below have comments pointing out what sections to modify if you want to use a different image or output directory
- Scripts:
	task1.py
		- This script was made to introduce object detection through the features attributes
		- The main features being compared here is the features major divided by minor axis lengths
		- By breaking it down like this and plotting out each feature, we can see that the histrogram becomes divided
		- This division here seperates the cashew from the squares
		- The threshold for this image was 1.6, modify this value to be the histrogram divider
		- The morphological operators used here was close, open, erode, and dilate. 
			1. The first move was dilate, since I wanted to remove all the holes inside each square
			2. The erode was then used to decrease the sizes of each object and disconnecting their spaces from one another
			3. Morph close and open was used to dilate and erode, followed by erode and dilate
			4. The final image shows each object fully filled and seperated from each object
			
	task2.py
		- This script was made to introduce object detection through multiple feature attributes
		- The main features being compared here is the perimeter and hue of each object
		- This is done by converting the image into HSV space and saving the H layer to a variable: sample_h
		- Sample_h is used as the intensity image, aka, the image that will be analyzed
			- This is done by checking each attributes bbox against the intensity_image
			- It then find the mean intensity of the bbox on sample_h and saves that to the attributes feature
		- By breaking it down like this and plotting out each feature, we can see that the histrogram becomes divided
			- The first division is on the x-axis, perimeter
				- This divides the squares from the circles 
			- The second division is on the y-axis, mean_intensity
				- This divides the blue circles (greater than) from the red circles (less than)
		- The threshold for this image was 250 for perimeter and 40 for intensity, modify values to be the histrogram divider
		- The morphological operators used here was close, open, and erode
			1. Before the morphological operator was used, we found the inverse of the binary image and summed both up
			2. This resulted in an binary image with every hole filled in.
			3. Due to this, we don't need dilate to dilate the holes and can skip directly to eroding
			4. Then the image is closed and open again (the same cleaning effect as earlier) to do a bit of touch up
			5. One last erode is applied to decrease the size of each object one last time
		
	task3.py
		- This script was made by combining the two scripts from above
		- Since the object being compared here give almost similar perimeters, the following features were used
			- axis length division
			- hue
		- This is done by converting the image into HSV space and saving the H layer to a variable: sample_h
		- Sample_h is used as the intensity image, aka, the image that will be analyzed
			- This is done by checking each attributes bbox against the intensity_image
			- It then find the mean intensity of the bbox on sample_h and saves that to the attributes feature
		- By breaking it down like this and plotting out each feature, we can see that the histrogram becomes divided
			- The first division is on the x-axis, axis division
				- This divides the long from the circles 
			- The second division is on the y-axis, mean_intensity
				- Technically not needed, as the top does create a true divider, but used to reinforce
		- The threshold for this image was 27 for axis divided and 16 for intensity, modify values to be the histrogram divider
		- Due to the similarity between the images from task 2 and 3, the same morphological operators were used
