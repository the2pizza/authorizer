(ns authorizer.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [authorizer.state :as state]))

(defn read-lines [in]
  (let [lines (line-seq (java.io.BufferedReader. in))]
    (map #(json/read-str % :key-fn keyword) lines)))

(defn -main []
  (let [lines (read-lines *in*)]
    (state/machine->run lines)))
