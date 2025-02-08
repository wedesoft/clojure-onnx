import numpy as np
import torch
from torch import nn
from torch.nn import functional as F


def main():
    # Read labels (skip magic and length integer)
    train_labels = np.fromfile('train-labels-idx1-ubyte', dtype=np.uint8)[8:]
    print(train_labels.shape)
    # Read images (skip magic, length, height, and width integers)
    train_images = np.fromfile('train-images-idx3-ubyte', dtype=np.uint8)[16:].reshape(-1, 28, 28)
    print(train_images.shape)
