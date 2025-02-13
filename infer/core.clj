(ns infer.core
    (:require [clojure.java.io :as io]
              [cljfx.api :as fx])
    (:import [java.io ByteArrayOutputStream ByteArrayInputStream]
             [java.nio FloatBuffer]
             [javafx.application Platform]
             [ai.onnxruntime OrtEnvironment OrtSession OrtSession$SessionOptions OnnxTensor]))

(def device-id 0)
(def options (OrtSession$SessionOptions.))
(.addCUDA options device-id)
(def environment (OrtEnvironment/getEnvironment))

(def mnist (-> environment (.createSession "mnist.onnx" options)))

(defn read-digit [n]
  "Read a 28*28 gray-scale byte block from the MNIST dataset."
  (with-open [in (io/input-stream "data/t10k-images-idx3-ubyte")]
    (.skip in (+ 16 (* n 28 28)))
    (.readNBytes in (* 28 28))))

(defn byte->ubyte [b]
  "Convert byte to unsigned byte"
  (if (>= b 0) b (+ b 256)))

(defn feature-scaling [digit]
  "Scale features to [0, 1] range"
  (float-array (map #(/ (byte->ubyte %) 255.0) digit)))

(defn argmax [arr]
  "Return the index of the maximum value in the array"
  (first
    (reduce (fn [[result maximum] [index value]] (if (> value maximum) [index value] [result maximum]))
            [0 (first arr)]
            (map vector (range) arr))))

(defn inference [digit]
  "Run inference on a digit image"
  (let [scaled        (feature-scaling digit)
        input-buffer  (FloatBuffer/wrap scaled)
        inputs        {"input" (OnnxTensor/createTensor environment input-buffer (long-array [1 1 28 28]))}
        outputs       (.run mnist inputs)
        output-tensor (.get (.get outputs "output"))
        output-buffer (.getFloatBuffer output-tensor)
        result        (float-array 10)]
    (.get output-buffer result)
    (argmax result)))

(defn digit->image [data]
  "Convert a 28*28 byte array to JavaFX image"
  (let [image  (java.awt.image.BufferedImage. 28 28 java.awt.image.BufferedImage/TYPE_BYTE_GRAY)
        raster (.getRaster image)
        out    (ByteArrayOutputStream.)]
    (.setDataElements raster 0 0 28 28 data)
    (javax.imageio.ImageIO/write image "png" out)
    (.flush out)
    (javafx.scene.image.Image. (ByteArrayInputStream. (.toByteArray out)))))

(def app-state (atom {:index (rand-int 10000)}))

(defn event-handler [& args]
  "Update application state with random index"
  (swap! app-state update :index (fn [_] (rand-int 10000))))

(defn display-image [{:keys [image]}]
  "Image display for cljfx GUI"
  {:fx/type :image-view
   :fit-width 256
   :fit-height 256
   :image image})

(defn next-button [_]
  "Next button for cljfx GUI"
  {:fx/type :button
   :text "Next"
   :on-action event-handler})

(defn root [{:keys [index]}]
  "Main window for cljfx GUI"
  (let [digit  (read-digit index)
        result (inference digit)]
    {:fx/type :stage
     :showing true
     :title "MNIST"
     :scene {:fx/type :scene
             :root {:fx/type :v-box
                    :padding 3
                    :spacing 5
                    :children [{:fx/type display-image :image (digit->image digit)}
                               {:fx/type :h-box
                                :padding 3
                                :spacing 5
                                :children [{:fx/type next-button}
                                           {:fx/type :label :text (str "result = " result)}]}]}}}))

(def renderer
  "Renderer for cljfx GUI"
  (fx/create-renderer
   :middleware (fx/wrap-map-desc assoc :fx/type root)))

(defn -main [& args]
  (Platform/setImplicitExit true)
  (fx/mount-renderer app-state renderer))
