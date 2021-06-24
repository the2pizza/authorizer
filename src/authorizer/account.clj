(ns authorizer.account
  (:require [clojure.data.json :as json]
            [authorizer.conditions :as conditions]
            [authorizer.time :as time]))

(defrecord Account [available-limit active-card violations history])

(defrecord Record [merchant time amount])

(defn init [{:keys [available-limit active-card]}]
  (->Account available-limit active-card [] '()))

(defn spend [account amount]
    (assoc account :available-limit (- (:available-limit account) amount)))

(defn record->history [acc {:keys [merchant time amount] :as record}]
  (cond
    (nil? record) acc
    :else (->Account (:available-limit acc)
                     (:active-card acc)
                     (:violations acc)
                     (conj (:history acc) (->Record merchant
                                                    (time/parse-date-time time)
                                                    amount)))))


(defn prettify [{:keys [active-card available-limit violations]}]
  (cond (and (nil? active-card)
             (nil? available-limit)) {:account {} :violations violations}

        :else {:account {:active-card active-card :available-limit available-limit}
               :violations violations}))

(defn pprint [account]
    (println (json/write-str (prettify account)))
    nil)
