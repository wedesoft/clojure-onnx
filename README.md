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
