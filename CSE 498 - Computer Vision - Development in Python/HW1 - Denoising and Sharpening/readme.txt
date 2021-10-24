Date: Finished 9/7/2021 6:18pm
Author: Alexander Spivey
Version: 1.0
Purpose: Image Sharpening & Image Denoising

--Writeup--
- To use each script written, 
	please reference each purpose below,
	have all available libraries (conda enviroment.yml),
	and have a proper python interpretter (python 3.9)
- Each file mentioned below have comments pointing out what sections to modify if you want to use a different image or output directory
- Scripts:
	--Image Sharpening--
	task1p1.py
		- This is the script needed to run to enhance data/task1/1.jpg
		- Only works for grayscale images
		- Works by taking orginal grayscale and convert it to grayscale (because original still 3 channels)
		- Applies Gaussian Filter to image
			- One version is not rescaled (operation image)
			- One version is rescaled (output image)
		- (I - G(I)) = D(I) && (I + D(I)) = I' where I is image, G(I) is Gaussian image, D(I) is the difference image
		- Print results
		- If results are acceptable, save the results to output_dir
	task1p2.py
		- This is the script needed to run to enhance data/task1/2.jpg
		- Works for RGB mode images (3 channels)
		- Works by taking orginal and split it into 3 channels (red, green, blue)
		- Applies Gaussian Filter to each channel
		- Merge each channel together (do not rescale)
		- (I - G(I)) = D(I) && (I + D(I)) = I' where I is image, G(I) is Gaussian image, D(I) is the difference image
		- Print results
		- If results are acceptable, save the results to output_dir
		
	--Image Denoising--
	task2p1.py
		- This is the script needed to run to enhance data/task2/1.jpg
		- Only works for grayscale images
		- Works by taking orginal grayscale and convert it to grayscale (because original still 3 channels)
		- Applies Gaussian Filter to image (do rescale)
		- Applies Mean filter to image (do rescale)
		- Applied Median filter to image (do rescale) 
		- Print results
		- If results are acceptable, save the results to output_dir
	task2p2.py - WARNING: THIS SCRIPT WILL TAKE A SIGNIFICANT AMOUNT OF TIME DUE TO MEDIAN FILTER BEING APPLIED 6 TIMES TOTAL
		- This is the script needed to run to enhance data/task2/2.jpg
		- Works for RGB mode images (3 channels)
		- Works by taking orginal and split it into 3 channels (red, green, blue)
		- Applies Gaussian Filter to each channel (do rescale)
		- Applies Mean filter to each channel (do rescale)
		- Applies Median filter (3x3 kernel) to each channel (do rescale)
		- Applies Median filter v2 (5x5 kernel) to each channel (do rescale)
		- Merge each channel together for each filter
		- Print results
		- If results are acceptable, save the results to output_dir

--Questions--
	1. Which method works best for each image and why?
		The method that worked best for denoising, for both detector and salt/pepper noise, consistently was the medium
		filter. This filter works by first sorting everything in order for the ROI and then finds the medium from that. The
		reason why it works so much better is due to it "ignoring" uncommon pixels within the ROI (aka, these unreprsented
		pixels could be noise which would force salt and pepper pixels to be sorted to each end [getting crossed-out]). 
		Another reason, since it isn't finding the mean (which can often decrease/increase 
		brightness of images, due to it averaging everything out), it actually pulls a real used pixel (instead of 
		creating a new "unrealistic" pixel value when it is working on the edges of the image).
		Source (other than proving it with the outputs): https://homepages.inf.ed.ac.uk/rbf/HIPR2/median.htm
		
---Relevant Links---
https://homepages.inf.ed.ac.uk/rbf/HIPR2/noise.htm
https://homepages.inf.ed.ac.uk/rbf/HIPR2/median.htm
https://homepages.inf.ed.ac.uk/rbf/HIPR2/mean.htm
https://numpy.org/doc/stable/reference/generated/numpy.median.html
https://thispointer.com/sorting-2d-numpy-array-by-column-or-row-in-python/
https://docs.opencv.org/3.4/d2/de8/group__core__array.html
https://www.researchgate.net/figure/Discrete-approximation-of-the-Gaussian-kernels-3x3-5x5-7x7_fig2_325768087
https://stackoverflow.com/questions/27035672/cv-extract-differences-between-two-images
https://www.pyimagesearch.com/2021/01/19/opencv-bitwise-and-or-xor-and-not/
https://dbader.org/blog/python-multiline-comment
https://stackoverflow.com/questions/37203970/opencv-grayscale-mode-vs-gray-color-conversion
https://stackoverflow.com/questions/21596281/how-does-one-convert-a-grayscale-image-to-rgb-in-opencv-python
https://stackoverflow.com/questions/59669385/how-does-cv2-merger-g-b-works
https://stackoverflow.com/questions/26681756/how-to-convert-a-python-numpy-array-to-an-rgb-image-with-opencv-2-4
https://stackoverflow.com/questions/19181485/splitting-image-using-opencv-in-python
https://stackoverflow.com/questions/7733364/python-pil-create-indexed-color-image-with-transparent-background
https://pillow.readthedocs.io/en/stable/handbook/concepts.html#concept-modes
https://pillow.readthedocs.io/en/stable/reference/Image.html
https://www.kite.com/python/docs/PIL.Image.fromarray
https://stackoverflow.com/questions/28226179/valueerror-unrecognized-mode
https://stackoverflow.com/questions/2659312/how-do-i-convert-a-numpy-array-to-and-display-an-image
https://stackoverflow.com/questions/54011487/typeerror-unsupported-operand-types-for-image-and-int
https://stackoverflow.com/questions/44090179/numpy-ndarray-object-has-no-attribute-mode
https://www.stechies.com/typeerror-only-integer-scalar-arrays-converted-scalar-index/
https://stackoverflow.com/questions/50997928/typeerror-only-integer-scalar-arrays-can-be-converted-to-a-scalar-index-with-1d
https://www.geeksforgeeks.org/convert-a-numpy-array-to-an-image/
https://www.geeksforgeeks.org/python-pil-image-merge-method/
https://www.tutorialspoint.com/python_pillow/Python_pillow_merging_images.htm
https://www.geeksforgeeks.org/python-pil-image-split-method/
https://www.tutorialspoint.com/python/string_join.htm
https://stackoverflow.com/questions/17667780/use-python-imaging-library-to-isolate-a-single-channel/31743259
https://docs.opencv.org/4.5.0/d4/d86/group__imgproc__filter.html#gaabe8c836e97159a9193fb0b11ac52cf1
https://docs.opencv.org/4.5.0/d4/d13/tutorial_py_filtering.html
https://techtutorialsx.com/2018/06/02/python-opencv-converting-an-image-to-gray-scale/
https://www.geeksforgeeks.org/apply-a-gauss-filter-to-an-image-with-python/
https://www.geeksforgeeks.org/gaussian-filter-generation-c/
https://opencv-python-tutroals.readthedocs.io/en/latest/py_tutorials/py_gui/py_image_display/py_image_display.html
https://www.pyimagesearch.com/2016/07/25/convolutions-with-opencv-and-python/
https://anaconda.org/
https://www.oreilly.com/library/view/programming-computer-vision/9781449341916/ch01.html
https://www.geeksforgeeks.org/python-opencv-cv2-imwrite-method/
https://practice.geeksforgeeks.org/courses/competitive-programming-live/?utm_source=geeksforgeeks&utm_medium=banner&utm_campaign=GFG_Home_Rightbar_CP
https://stackoverflow.com/questions/31586385/assertion-failure-size-width0-size-height0-in-function-imshow
https://www.geeksforgeeks.org/reading-images-in-python/
https://medium.com/@pranav.keyboard/installing-opencv-for-python-on-windows-using-anaconda-or-winpython-f24dd5c895eb
https://docs.conda.io/projects/conda/en/4.6.0/_downloads/52a95608c49671267e40c689e0bc00ca/conda-cheatsheet.pdf
https://docs.python.org/3/tutorial/interpreter.html
https://www.jetbrains.com/help/pycharm/configuring-python-interpreter.html#add-existing-interpreter
https://anaconda.org/conda-forge/python
https://anaconda.org/search?q=python
https://github.com/conda/conda/issues/9367#issuecomment-558863143
https://anaconda.org/conda-forge/opencv
https://stackoverflow.com/questions/63734508/stuck-at-solving-environment-on-anaconda
https://coursesite.lehigh.edu/pluginfile.php/5317759/mod_assign/introattachment/0/HW1.zip?forcedownload=1
