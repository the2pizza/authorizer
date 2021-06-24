(ns authorizer.time
  (:require [java-time :as jt]))

(defn parse-date-time [t]
  (let [format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"]
    (jt/local-date-time format t)))

(defn record-in-interval? [history-record-time record-time interval]
  (let [record-datetime (parse-date-time record-time)
        ago (jt/minus record-datetime (jt/minutes interval))]
    (and (or (jt/after? history-record-time ago )
             (= history-record-time ago))
         (or (jt/before? history-record-time record-datetime)
             (= history-record-time record-datetime)))))
