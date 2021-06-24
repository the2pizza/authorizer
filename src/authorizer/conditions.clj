(ns authorizer.conditions
  (:require [authorizer.time :as time]))

(def not-nil? (complement nil?))

(defn type? [type coll]
  (not-nil? (type coll)))

(defn account-record? [record]
  (type? :account record))

(defn transaction-record? [record]
  (type? :transaction record))

(defn account->not-initialized? [account]
  (nil? account))

(def account->initialized?
  (complement account->not-initialized?))

(defn transaction->not-available? [account]
  (or (nil? account)
      (false? (:active-card account))))

(def transaction->available?
  (complement transaction->not-available?))

(defn transaction->insufficient-limit? [account record]
  (<= (:available-limit account) (:amount (:transaction record))))

(defn transaction->high-frequency-small-interval? [account record interval frequency]
  (>= (count (filter #(time/record-in-interval? (:time %) (:time (:transaction record)) interval)
                     (:history account))) frequency))

(defn transaction->not-doubled? [account record interval]
  (empty? (filter #(and (= (:amount (:transaction record)) (:amount %))
                (= (:merchant (:transaction record)) (:merchant %)))
                  (filter #(time/record-in-interval? (:time %)
                                                     (:time (:transaction record))
                                                     interval) (:history account)))))

(def transaction->doubled? (complement transaction->not-doubled?))
