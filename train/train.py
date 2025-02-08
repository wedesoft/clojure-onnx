import numpy as np
import torch
from torch import nn
from torch.nn import functional as F


def main():
    train_images = np.fromfile('train-images-idx3-ubyte', dtype=np.uint8)
    print(train_images)
