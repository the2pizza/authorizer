(ns authorizer.integration-test
  (:require [clojure.test :refer :all]
            [authorizer.account :as account]
            [authorizer.state :refer :all]))

(def record-seq-ok [{:account {:available-limit 1000 :active-card true}}
                 {:transaction {:merchant "KFC" :amount 20, :time "2019-02-13T12:02:07.000Z"}}
                 {:transaction {:merchant "LocalMarket" :amount 25, :time "2019-02-13T15:00:07.000Z"}}
                 {:transaction {:merchant "Starbucks" :amount 80, :time "2019-02-13T17:02:07.000Z"}}
                 {:transaction {:merchant "Coop" :amount 25, :time "2019-02-13T20:02:07.000Z"}}])

(deftest state-machine-is-ok?
  (testing "Test Machine run is not Ok"
    (prn "=== Test Run Ok ===")
    (let [state (machine->run  record-seq-ok)]
      (is (= (:available-limit state)
             850)))))

(def record-already-init-ok [{:account {:available-limit 100 :active-card true}}
                             {:account {:available-limit 1000 :active-card true}}])

(deftest state-machine-already-init-is-ok?
  (testing "Test Machine Already Init is not Ok"
    (prn "=== Test: Already Initialized [Ok] ===")
    (let [state (machine->run  record-already-init-ok)]
      (is (= (:available-limit state)
             100)))))

(def record-already-not-init [{:transaction {:merchant "KFC" :amount 20, :time "2019-02-13T12:02:07.000Z"}}
                             {:account {:available-limit 1000 :active-card true}}])

(deftest state-machine-not-init-is-ok?
  (testing "Test Machine Already Not Initialized is not Ok"
    (prn "=== Test: Not Initialized [Ok] ===")
    (let [state (machine->run  record-already-not-init)]
      (is (= (:available-limit state)
             1000)))))

(def record-already-card-not-active [{:account {:available-limit 1000 :active-card false}}
                              {:transaction {:merchant "KFC" :amount 20, :time "2019-02-13T12:02:07.000Z"}}])

(deftest state-machine-card-is-not-active-ok?
  (testing "Test Card is not Acrtive"
    (prn "=== Test: Card is not Acrtive [Ok] ===")
    (let [state (machine->run  record-already-card-not-active)]
      (is (= (:available-limit state)
             1000)))))

(def record-insufficient-limit [{:account {:available-limit 100 :active-card true}}
                                        {:transaction {:merchant "KFC" :amount 200, :time "2019-02-13T12:02:07.000Z"}}])

(deftest state-machine-insufficient-limit-is-ok?
  (testing "Test Insufficient Limit is not Ok"
    (prn "=== Test: Insufficient Limit [Ok] ===")
    (let [state (machine->run  record-insufficient-limit)]
      (is (= (:available-limit state)
             100)))))

(def record-already-doubled-transaction [{:account {:available-limit 100 :active-card true}}
                                        {:transaction {:merchant "KFC" :amount 20, :time "2019-02-13T12:03:07.000Z"}}
                                        {:transaction {:merchant "KFC" :amount 20, :time "2019-02-13T12:04:07.000Z"}}])

(deftest state-machine-doubled-transaction-is-ok?
  (testing "Test Doubled Transaction is not Ok"
    (prn "=== Test: Doubled Transaction [Ok] ===")
    (let [state (machine->run  record-already-doubled-transaction)]
      (is (= (:available-limit state)
             80)))))

(def record-high-frequency-small-interval [{:account {:available-limit 100 :active-card true}}
                                           {:transaction {:merchant "KFC" :amount 10, :time "2019-02-13T12:03:30.000Z"}}
                                           {:transaction {:merchant "KFC" :amount 25, :time "2019-02-13T12:04:00.000Z"}}
                                           {:transaction {:merchant "KFC" :amount 30, :time "2019-02-13T12:04:30.000Z"}}
                                           {:transaction {:merchant "Starbucks" :amount 30, :time "2019-02-13T12:04:45.000Z"}}])

(deftest state-machine-high-frequency-small-interval-is-ok?
  (testing "Test High Frequence Small Interval is not Ok"
    (prn "=== Test: High Frequence Small Interval [Ok] ===")
    (let [state (machine->run  record-high-frequency-small-interval)]
      (is (= (:available-limit state)
             35)))))
