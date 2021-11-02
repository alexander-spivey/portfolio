from torch._C import device
from torch.optim import optimizer
import torchvision, torch
from torchvision import datasets, transforms
from torch.utils.data import DataLoader
import torch.nn.functional as F
import numpy as np
import matplotlib.pyplot as plt

# This is a convenient data reader.
categories = ['airplane', 'automobile', 'bird', 'cat', 'deer',
              'dog', 'frog', 'horse', 'ship', 'truck']

transform = torchvision.transforms.Compose([transforms.Resize((32,32)), transforms.ToTensor(), transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))])

#### ---- You can change the two lines below to get your own data loaded
train_data = datasets.CIFAR10('./data', train = True, download = True, transform = transform)
validation_data = datasets.CIFAR10('./data', train = False, download = True, transform = transform)

#### ---- Create your dataloaders here. Read about pytorch DataLoader class.
batch_size = 128

# It additionally has utilities for threaded and multi-parallel data loading.
trainLoader = DataLoader(train_data, batch_size = batch_size,
                         shuffle = True, num_workers = 0)
valLoader = DataLoader(validation_data, batch_size = batch_size,
                       shuffle = False, num_workers = 0)

# Defining the model.
from tqdm import tqdm as tqdm
import torch.nn as nn
import torch.optim as optim

learningRate = 1e-2  # Single learning rate for this lab.

class LeNet(nn.Module):
    def __init__(self):
        super().__init__()

        # Convolutional layers.
        self.conv1 = nn.Conv2d(3, 16, 3, stride = 1, padding = 1) # change kernel size from 5 to 1 and added padding, increased kernels to 16
        self.conv2 = nn.Conv2d(16, 32, 3, stride = 1, padding = 1) # doubling the number of filters from 8 to 16
        self.conv3 = nn.Conv2d(32, 64, 3, stride = 1, padding = 1) # doubling the number of filters from 16 to 32

        # Linear layers.
        self.fc1 = nn.Linear(64*4*4, 500)
        self.dropout1 = nn.Dropout(p = 0.5)
        self.fc2 = nn.Linear(500, 10)

    def forward(self, x):
        # Conv1 + ReLU + MaxPooling.
        out = F.relu(self.conv1(x))
        out = F.max_pool2d(out, 2, 2)

        # Conv2 + ReLU + MaPooling.
        out = F.relu(self.conv2(out))
        out = F.max_pool2d(out, 2, 2)

        # Conv3 + ReLU + MaPooling.
        out = F.relu(self.conv3(out))
        out = F.max_pool2d(out, 2, 2)

        # This flattens the output of the previous layer into a vector.
        out = out.view(out.size(0), -1)
        # Linear layer + ReLU.
        out = F.relu(self.fc1(out))
        # Applying dropout to prevent overfitting
        out = self.dropout1(out)
        # Another linear layer + ReLU.
        out = self.fc2(out)

        return out


device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
# Definition of our network.
classifier = LeNet().to(device)

#Definition of our loss.
criterion = nn.CrossEntropyLoss()

# Definition of optimization strategy.
optimizer = optim.SGD(classifier.parameters(), lr = learningRate)

# Write a method called train_model that takes in the classifier, the criterion, the optimizer, trainLoader, valLoader, and number of epochs.
# This method should train the model for the specified number of epochs.
def train_model(classifier, criterion, optimizer, trainLoader, valLoader, epochs):
    loss_list = []
    val_loss_list = []
    correct_list = []
    val_correct_list = []
    for epoch in range(epochs):
        running_loss = 0.0
        val_running_loss = 0.0
        running_correct = 0.0
        val_running_correct = 0.0
        for i, data in enumerate(tqdm(trainLoader)):
            inputs, labels = data   # Get the inputs and labels from the data loader
            inputs = inputs.to(device) # input to device as our model is running in mentioned device.
            labels = labels.to(device)
            optimizer.zero_grad()   # Zero the parameter gradients
            
            # Forward pass
            outputs = classifier(inputs)
            loss = criterion(outputs, labels) # Compute the loss
            
            # Backward pass
            optimizer.zero_grad()
            loss.backward()
            optimizer.step() # Parameter update

            # Calculate the running loss and correct
            running_loss += loss.item()
            _, predicted = torch.max(outputs.data, 1)
            running_correct += torch.sum(predicted == labels.data)
        else:
            with torch.no_grad():
                for j, data in enumerate(valLoader):
                    inputs, labels = data
                    inputs = inputs.to(device) # input to device as our model is running in mentioned device.
                    labels = labels.to(device)
                    # Forward pass
                    outputs = classifier(inputs)
                    # Calculate loss
                    loss = criterion(outputs, labels)
                    val_running_loss += loss.item()
                    # Compute the running correct
                    _, predicted = torch.max(outputs.data, 1)
                    val_running_correct += torch.sum(predicted == labels.data)
            epoch_loss = running_loss/len(trainLoader)
            epoch_acc = running_correct.float()/ len(trainLoader)
            loss_list.append(epoch_loss)
            correct_list.append(epoch_acc.cpu()) # convert to cpu to avoid error
            
            val_epoch_loss = val_running_loss/len(valLoader)
            val_epoch_acc = val_running_correct.float()/ len(valLoader)
            val_loss_list.append(val_epoch_loss)
            val_correct_list.append(val_epoch_acc.cpu())    # convert to cpu to avoid error
            print('Epoch {}/{}'.format(epoch + 1, epochs))
            print('Training Loss: {:.4f}'.format(epoch_loss))
            print('Training Accuracy: {:.4f}'.format(epoch_acc))
            print('Validation Loss: {:.4f}'.format(val_epoch_loss))
            print('Validation Accuracy: {:.4f}'.format(val_epoch_acc))
    return loss_list, correct_list, val_loss_list, val_correct_list

# Call  your training function and
loss_list, correct_list, val_loss_list, val_correct_list = train_model(classifier, criterion, optimizer,
            trainLoader, valLoader, epochs = 50)
plt.style.use('ggplot')
plt.plot(loss_list, label = 'Training Loss')
plt.plot(val_loss_list, label = 'Validation Loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.title('Training and Validation Loss')
plt.legend()
plt.savefig('data/GPU_loss_50_Adam_LeNet2.png') # MODIFY OUTPUT NAME
plt.show()


plt.style.use('ggplot')
plt.plot(correct_list, label = 'Training Accuracy')
plt.plot(val_correct_list, label = 'Validation Accuracy')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.title('Training and Validation Accuracy')
plt.legend()
plt.savefig('data/GPU_accuracy_50_Adam_LeNet2.png') # MODIFY OUTPUT NAME
plt.show()

# https://www.javatpoint.com/pytorch-testing-of-lenet-model-for-cifar-10-dataset
# All code below is for testing the model and was retrieved from the above link.
def im_convert(tensor):  
    image = tensor.cpu().clone().detach().numpy() # This process will happen in normal cpu.
    image = image.transpose(1, 2, 0)
    image = image * np.array((0.5, 0.5, 0.5)) + np.array((0.5, 0.5, 0.5))
    image = image.clip(0, 1)
    return image

dataiter=iter(trainLoader)  
images,labels=dataiter.next()  
fig=plt.figure(figsize=(25,4))  
for idx in np.arange(20):  
    ax=fig.add_subplot(2,10,idx+1)  
    plt.imshow(im_convert(images[idx]))  
    ax.set_title(categories[labels[idx].item()]) 
    plt.savefig('data/GPU_lenet_results_50_Adam_LeNet2.png') # MODIFY OUTPUT NAME