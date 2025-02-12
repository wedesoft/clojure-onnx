# Clojure ONNX example using MNIST dataset

Download MNIST dataset
```Shell
make mnist
```

Install Python dependencies
```Shell
poetry install
```

Run model training
```Shell
poetry run train
```

Run inference
```Shell
clj -M:infer
```

![inference GUI screenshot](infer.png)

## External Links

* [The Current State of ML in Clojure](https://codewithkira.com/2024-04-04-state-of-clojure-ml.html)
* [scicloj.ml.tribuo](https://github.com/scicloj/scicloj.ml.tribuo)
* [Tribuo](https://tribuo.org/)
* [Using ONNX models from Clojure](https://scicloj.github.io/clojure-data-tutorials/projects/ml/onnx/onnx.html)
* [MNIST dataset](https://github.com/fgnt/mnist)
* [PyTorch](https://pytorch.org/)
* [ONNX runtime](https://onnxruntime.ai/)
* [Cljfx wrapper for JavaFX](https://github.com/cljfx/cljfx)
* [JavaFX image from a byte array](https://gist.github.com/jamesthompson/3344090)
