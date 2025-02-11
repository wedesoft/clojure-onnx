import numpy as np
import torch
from torch import nn
from torch import onnx
from torch.nn import functional as F
from torch.utils.data import DataLoader, Dataset


class MNISTData(Dataset):
    def __init__(self, images_file_name, labels_file_name):
        # Read images (skip magic, length, height, and width integers)
        self.images = np.fromfile(images_file_name, dtype=np.uint8)[16:].reshape(-1, 28, 28)
        # Read labels (skip magic and length integer)
        self.labels = np.fromfile(labels_file_name, dtype=np.uint8)[8:]

    def __len__(self):
        return len(self.labels)

    def __getitem__(self, idx):
        image = torch.from_numpy(self.images[idx]).to(torch.float) / 255.0
        label = torch.zeros(10)
        label[self.labels[idx]] = 1
        return image, label


class MNISTNet(nn.Module):
    def __init__(self):
        super(MNISTNet, self).__init__()
        self.conv1 = nn.Conv2d(1, 10, kernel_size=5)
        self.conv2 = nn.Conv2d(10, 20, kernel_size=5)
        self.conv2_drop = nn.Dropout2d(p=0.2)
        self.fc1 = nn.Linear(320, 50)
        self.fc2 = nn.Linear(50, 10)

    def forward(self, x):
        x = x.view(-1, 1, 28, 28)
        x = F.relu(F.max_pool2d(self.conv1(x), 2))
        x = F.relu(F.max_pool2d(self.conv2_drop(self.conv2(x)), 2))
        x = x.view(-1, 320)
        x = F.relu(self.fc1(x))
        x = F.dropout(x, p=0.2, training=self.training)
        x = self.fc2(x)
        return F.softmax(x, dim=1)


def main():
    train_data = MNISTData('data/train-images-idx3-ubyte', 'data/train-labels-idx1-ubyte')
    test_data = MNISTData('data/t10k-images-idx3-ubyte', 'data/t10k-labels-idx1-ubyte')

    train_loader = DataLoader(train_data, batch_size=64)
    test_loader = DataLoader(test_data, batch_size=64)

    model = MNISTNet()
    loss = nn.CrossEntropyLoss()
    # Adam optimizer
    optimizer = torch.optim.Adam(model.parameters(), lr=0.001)

    for epoch in range(25):
        for x, y in train_loader:
            pred = model(x)
            l = loss(pred, y)
            optimizer.zero_grad()
            l.backward()
            optimizer.step()

        correct = 0
        total = 0
        for x, y in test_loader:
            pred = model(x).argmax(dim=1)
            correct += (pred == y.argmax(dim=1)).sum().item()
            total += len(y)
        print('Accuracy: {}'.format(correct / total))

    # Save model as ONNX
    torch.onnx.export(model,
                      (torch.randn((1, 1, 28, 28), dtype=torch.float),),
                      'mnist.onnx',
                      input_names=['input'],
                      output_names=['output'])
