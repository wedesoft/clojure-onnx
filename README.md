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

* [JavaFX image from a byte array](https://gist.github.com/jamesthompson/3344090)
