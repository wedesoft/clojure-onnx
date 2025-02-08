mnist: train-labels-idx1-ubyte train-images-idx3-ubyte t10k-images-idx3-ubyte t10k-labels-idx1-ubyte

train-images-idx3-ubyte: train-images-idx3-ubyte.gz
	gunzip -k $<

train-labels-idx1-ubyte: train-labels-idx1-ubyte.gz
	gunzip -k $<

t10k-images-idx3-ubyte: t10k-images-idx3-ubyte.gz
	gunzip -k $<

t10k-labels-idx1-ubyte: t10k-labels-idx1-ubyte.gz
	gunzip -k $<

train-images-idx3-ubyte.gz:
	wget https://github.com/fgnt/mnist/raw/refs/heads/master/train-images-idx3-ubyte.gz

train-labels-idx1-ubyte.gz:
	wget https://github.com/fgnt/mnist/raw/refs/heads/master/train-labels-idx1-ubyte.gz

t10k-images-idx3-ubyte.gz:
	wget https://github.com/fgnt/mnist/raw/refs/heads/master/t10k-images-idx3-ubyte.gz

t10k-labels-idx1-ubyte.gz:
	wget https://github.com/fgnt/mnist/raw/refs/heads/master/t10k-labels-idx1-ubyte.gz
