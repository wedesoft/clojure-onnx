mnist: data/train-labels-idx1-ubyte data/train-images-idx3-ubyte data/t10k-images-idx3-ubyte data/t10k-labels-idx1-ubyte

data/train-images-idx3-ubyte: data/train-images-idx3-ubyte.gz
	gunzip -k $<

data/train-labels-idx1-ubyte: data/train-labels-idx1-ubyte.gz
	gunzip -k $<

data/t10k-images-idx3-ubyte: data/t10k-images-idx3-ubyte.gz
	gunzip -k $<

data/t10k-labels-idx1-ubyte: data/t10k-labels-idx1-ubyte.gz
	gunzip -k $<

data/train-images-idx3-ubyte.gz:
	wget https://github.com/fgnt/mnist/raw/refs/heads/master/train-images-idx3-ubyte.gz -O $@

data/train-labels-idx1-ubyte.gz:
	wget https://github.com/fgnt/mnist/raw/refs/heads/master/train-labels-idx1-ubyte.gz -O $@

data/t10k-images-idx3-ubyte.gz:
	wget https://github.com/fgnt/mnist/raw/refs/heads/master/t10k-images-idx3-ubyte.gz -O $@

data/t10k-labels-idx1-ubyte.gz:
	wget https://github.com/fgnt/mnist/raw/refs/heads/master/t10k-labels-idx1-ubyte.gz -O $@
