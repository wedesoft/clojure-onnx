(ns infer.core
    (:require [cljfx.api :as fx])
    (:import [ai.onnxruntime OrtEnvironment OrtSession]
             [javafx.application Platform]))

(def environment (OrtEnvironment/getEnvironment))

(defn load-model
  [filename]
  (-> environment (.createSession filename)))

(defn display-image [image]
  {:fx/type :image-view
   :image (javafx.scene.image.Image. "debian.png")})

(defmulti event-handler :event/type)

(defmethod event-handler :default [event]
  (prn event))

; key press event handler
(defmethod event-handler :key-pressed [event]
  (prn "key press" event))

(defn root [& args]
  {:fx/type :stage
   :showing true
   :title "MNIST"
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :padding 25
                  :spacing 40
                  :children [{:fx/type display-image}]}}})

(def renderer
  (fx/create-renderer))

(defn -main [& args]
  (Platform/setImplicitExit true)
  (renderer {:fx/type root}))

;(defn -main [& args]
;  (let [model (load-model "mnist.onnx")]
;    ))
