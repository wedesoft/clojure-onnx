(ns infer.core
    (:require [clojure.java.io :as io]
              [cljfx.api :as fx])
    (:import [java.io ByteArrayOutputStream ByteArrayInputStream]
             [javafx.application Platform]
             [ai.onnxruntime OrtEnvironment OrtSession]))

(def environment (OrtEnvironment/getEnvironment))

(def mnist (-> environment (.createSession "mnist.onnx")))

(defn read-digit [n]
  (with-open [in (io/input-stream "data/t10k-images-idx3-ubyte")
              out (ByteArrayOutputStream.)]
    (.skip in (+ 16 (* n 784)))
    (.write out (.readNBytes in (* 28 28)))
    (.toByteArray out)))

(defn digit->image [data]
  (let [image  (java.awt.image.BufferedImage. 28 28 java.awt.image.BufferedImage/TYPE_BYTE_GRAY)
        raster (.getRaster image)
        out    (ByteArrayOutputStream.)
        ]
    (.setDataElements raster 0 0 28 28 data)
    (javax.imageio.ImageIO/write image "png" out)
    (.flush out)
    (javafx.scene.image.Image. (ByteArrayInputStream. (.toByteArray out)))))

; https://gist.github.com/jamesthompson/3344090

(def app-state (atom {:image "debian.png"}))

(defn event-handler [& args]
  (swap! app-state update :image (constantly (digit->image (read-digit 0)))))

(defn display-image [{:keys [image]}]
  {:fx/type :image-view
   :fit-width 256
   :fit-height 256
   :image image})

(defn next-button [_]
  {:fx/type :button
   :text "Next"
   :on-action event-handler})

(defn root [{:keys [image]}]
  {:fx/type :stage
   :showing true
   :title "MNIST"
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :children [{:fx/type display-image :image image} {:fx/type next-button}]}}})

(def renderer
  (fx/create-renderer
   :middleware (fx/wrap-map-desc assoc :fx/type root)))

(defn -main [& args]
  (Platform/setImplicitExit true)
  (fx/mount-renderer app-state renderer))
