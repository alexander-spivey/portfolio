Date: Finished 9/24/2021 10:57am
Author: Alexander Spivey
Version: 1.0
Purpose: Deep Learning for Computer Vision

--Writeup--
- To use each script written, 
	please reference each purpose below,
	have all available libraries (conda enviroment.yml),
	and have a proper python interpretter (python 3.9)
- Each file mentioned below have comments pointing out what sections to modify if you want to use a different image or 
output directory
- Scripts:
	task1-3.py
		- Task 1:
            - For task 1, I modified the code to capture the frame when space was pressed, and then save the image 
            to the output directory.
            - To include the scoring for each class, I modified the code to have probability = True, and then I 
            added so the class name and probabilities were in the output file.
            - It is suggested that you modfiy the save filename to include the CNN layer and kernel
            - It is suggested that you save each image into the general data directory and then create separate folders
            afterwards
            - One observation was that the proability percentages don't properly match up with the class predicted
                - i.e, t1_fc1_linear_watch showed that the max percentage was index 3, which would be dollar_bill
                - This is a result of the fact that the "probability estimates may be inconsistent with the scores"
                    - "Platt's method is also known to have theoritical issues"
            - The base detection (which can be found in the ..._image of each directory) of the system was 'watch'
        - Task 2:
            - For task 2, we had to try the effects of using a different CNN layer and kernel.
                - fc1_linear
                    - the constant/base/null detection was 'watch'
                    - this was the base configuration and was capable of detecting all 3 classes correctly
                    - the phone screen had to be on to be detected properly 
                    - detected the dollar_bill without having to be extremely close to camera
                - fc1_poly
                    - this configuration consistently guessed watch regardless of the item present
                - fc1_rbf
                    - this configuration consistently guessed watch regardless of the item present
                - fc2_linear
                    - the constant/base/null detection was 'dollar_bill'
                    - the first case where we can confirm that is properly detected the watch, due to others having
                    'watch' as the null prediction
                    - feels by far the smoothest model
                    - was able to detect watch from entirely differnet angles (side vs head-on)
                    - the phone screen did not need to be on to be detected
                - fc2_poly
                    - the constant/base/null detection was 'watch'
                    - both the dollar_bill and cellphone had to be extremely close to be detected
                    - the phone screen had to be on to be detected properly
                - fc2_rbf
                    - the constant/base/null detection was 'dollar_bill'
                    - was able to identify all 3 classes correctly
                    - the phone screen had to be on to be detected properly 
            - We used the same classes as in task 1, and the same output directory
            - It is suggested that you modfiy the save filename to include the CNN layer and kernel
            - It is suggested that you save each image into the general data directory and then create separate folders
            afterwards
            - Question: "Which configuration is the best? How would you explain why this selected configuration works best?"
                - The best configuration based of the smoothness, overall detection, and differnt angle/illuminosity
                detection capabilities would be fc2_linear. A close second up would be rbf
                - fc1 vs fc2 [3]
                    - to understand the difference, i implemented test.py
                    - fc1 is the first activision layer
                    - fc2 is the second activision layer
                    - fc1 goes down to 102764544 
                    - fc2 takes it down to 16781312
                - linear vs rbf [4]
                    - linear svm is parametric
                    - rbf kernel complexity depends on the size of the training set 
                    - rbf is more expensive to train and have to keep rbf in memory, which grows in expensivness
                    due to the inifinite higher dimineional space
                        - which can "becomes linearly separable"
                    - linear kernel is a special version of the rbf kernel
                    - when number of features is large, may make the data more linear [5]
                - Overall due to the fact that fc2 was the second activision layer, we were able to get better results,
                explaining why poly and rbf was unusable during fc1, but capable during fc2. And linear was the better
                kernel due to the data being linearly separable and rbf being more expensive overall. 
        - Task 3:
            - My previous configuration was capable of scoring and prediciting each object correctly. However, due to 
            the images I used being on my phone, I had to make sure the camera would be detecting the image on the phone
            and not the phone. The only way to do so, was to place the camera close enough to the screen where it would 
            then focus and then accurately predict on the image on the screen.
            - I wish further analysis could be done in comparing the scores we received in the 3 class prediction
            vs this 10 class prediction. However, due to the issues with Platt's method, which was mentioned in Task 1,
            the scores that were present are not a good reflection as oftentimes the prediction class score are less
            than its companions. For example, t3_fc2_linear_watch.jpg gave an 0.7 score of being a buddha, but the actual
            predicted class of watch was only given a 0.005%.
            - However, take this testing with a grain of salt. Most of these images, required a couple shots and diffe-
            rent distances before the prediction was accurate. This was mainly due to the classes being tested were of
            objects that I didn't own. For the original buddha photo (t3_fc2_linear_budhha), I used a image online and 
            it quickly classified it correctly. However, when using my own buddha head, it was extremely difficult as
            it continued to predict, brain (due to the hair), butterfly (????), and chandlier (????? Extra what ????).
            This problem with finding the right angle can be seen in t3_fc2_linear_budhhav3. It correctly classified
            the real head in t3_fc2_linear_budhhav2, but was in such a pecuilar angle and it jumped between predictions
            rapidly. 
        - Task 4: (**PLEASE LOOK AT task4.py for progression AND LOOK AT task4GPU.py FOR ACTUAL FINAL RUN**)
            - After spending a couple of hours reading through the references for Task 4, I felt prepared enough to
            start the assignment. The first thing we had to do was transform the image. The transform pipeline first
            converts the image toTensor() so it can be used in torch. Then we normalized all images.
            - After modifying the train_data and val_data to be transformed and downloaded, we had to define our tr-
            ain_model() function. Within it, we needed to create 4 lists/histories: loss, val_loss, correct, and val-
            correct. Each list has an associate value that will be appended to the list at the each pass so the graph
            may be anaylzed.
            - After everything was properly implemented, I tried testing first on 10 epochs. This result was less
            than 45%. Due to this, I increased the number of epochs to 25 and had it raised to around 56%... Comparing
            my code with the tutorials online, I can't increase the validation by increase the number of epochs. the
            only other option would be to modify the optimizer instead. 
            - Due to the assignment stating that we only needed to create a single model, I decided I want to stick
            with LeNet and instead change the optimizer to Adam instead. Doing so, I was able to achieve 55% in 10
            epochs, instead of 25. 
                - However, there is a few issues with this output. This model overfits, and works better for 
                training than validation...
            - After running the model, I realized there is another parameter we can modify. Changing the transform
                - First run, I changed optimizer back to SGD and added 'transforms.Resize((32,32)' to the pipeline.
                This however, resulted with similar results to that of not resized.
                - 2nd run, I found the CIFar10 dataset on Kaggle and was able to stumble across sources [6]. Within
                it, the user created a secondary transform function specific to training. The code was copied directly
                into the script for testing purposes only. I do not take any credit for that work... This ended up Doing
                nothing except increasing the runtime by almost double. I didn't save the output, due to similarity
                being exactly same. 
            - So after messing with different parameters, why don't we work instead with a new LeNet? So taking A
            look at the LeNet provided by professor, it only goes up to 16 kernels, doesn't use dropout. So, I decided
            to increase the number of convulation layers to 3, a total of 64 kernels, and decreasing linear layers to 2,
            since I will be adding a dropout layer inbetween linear layers. 
                - As I am writing this sentence, it is clear to see that changing out LeNet with more kernels increases
                score. The score from this model ended up reaching 58% for validation, and 60% for testing
            - To test if using the GPU would increase scoring, I wrote task4GPU using LeNet2 (created by me)
                - It ended up doing the entire process 2x times faster, but no visable increase in scoring.
            - The last option we have to increase the score would be to use LeNet2 with a different optimizer: Adam
                - This ended up being the best combination!!! This is due to Adam using SGD extenstions to calculate
                the learning rates adaptively for the parameters [7].
                - However, this optimizer does significantly cost more time so it is better to run the task4GPU.py 
                instead
                - One error that was present was trying to plot the items while they were within the GPU. By moving 
                items back into cpu, we can actually plot the graphs and save them
            - THE BEST MODEL AND OPTIMIZER COMBINATION WAS: LeNet2 (made by Alex) and ADAM
                - Training Accuracy: 91.7161
                - Validation Accuracy: 84.6962
                - GPU_accuracy_50_Adam_LeNet2.png
                - GPU_loss_50_Adam_LeNet2.png
                - GPU_lenet_results_50_Adam_LeNet2.png
                - task4GPU.py

References
    Task 1-3:
        [1]. https://scikit-learn.org/stable/modules/generated/sklearn.svm.SVC.html
        [2]. https://data-flair.training/blogs/svm-kernel-functions/
        [3]. https://medium.com/@jeff.lee.1990710/image-similarity-using-vgg16-transfer-learning-and-cosine-similarity-98571d8055e3
        [4]. https://www.kdnuggets.com/2016/06/select-support-vector-machine-kernels.html
        [5]. https://stats.stackexchange.com/questions/18030/how-to-select-kernel-for-svm
    Task 4:
        [1]. https://pytorch.org/tutorials/beginner/blitz/cifar10_tutorial.html
        [2]. https://developer.habana.ai/tutorials/pytorch/training-a-classifier/
        [3]. http://www.cs.toronto.edu/~kriz/cifar.html
        [4]. https://www.javatpoint.com/pytorch-testing-of-lenet-model-for-cifar-10-dataset
        [5]. https://medium.com/@sergioalves94/deep-learning-in-pytorch-with-cifar-10-dataset-858b504a6b54
        [6]. https://www.kaggle.com/vikasbhadoria/cifar10-high-accuracy-model-build-on-pytorch
        [7]. https://medium.com/syncedreview/iclr-2019-fast-as-adam-good-as-sgd-new-optimizer-has-both-78e37e8f9a34