(ns infer.core
    (:require [cljfx.api :as fx])
    (:import [ai.onnxruntime OrtEnvironment OrtSession]
             [javafx.application Platform]))

(def environment (OrtEnvironment/getEnvironment))

(defn load-model
  [filename]
  (-> environment (.createSession filename)))

(def app-state (atom {:image "debian.png"}))

(defn event-handler [& args]
  (swap! app-state update :image (constantly "debian2.png")))

(defn display-image [{:keys [image]}]
  {:fx/type :image-view
   ; :image (javafx.scene.image.Image. "debian.png")
   :fit-width 256
   :fit-height 256
   :image image
   })

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

;(defn -main [& args]
;  (let [model (load-model "mnist.onnx")]
;    ))
