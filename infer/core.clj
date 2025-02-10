(ns infer.core
    (:require [clojure.java.io :as io]
              [cljfx.api :as fx])
    (:import [java.io ByteArrayOutputStream]
             [javafx.application Platform]
             [ai.onnxruntime OrtEnvironment OrtSession]))

(def environment (OrtEnvironment/getEnvironment))

(def mnist (-> environment (.createSession "mnist.onnx")))

(def test-data
  (with-open [in (io/input-stream "data/t10k-images-idx3-ubyte")
              out (ByteArrayOutputStream.)]
    (.skip in 16)
    (io/copy in out)
    (.toByteArray out)))

(def app-state (atom {:image "debian.png"}))

(defn event-handler [& args]
  (swap! app-state update :image (constantly "debian2.png")))

(defn display-image [{:keys [image]}]
  {:fx/type :image-view
   ; :image (javafx.scene.image.Image. "debian.png")
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
