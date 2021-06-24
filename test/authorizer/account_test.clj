(ns authorizer.account-test
  (:require [clojure.test :refer :all]

            [authorizer.account :refer :all])
  (:import (authorizer.account Account Record)
           (clojure.lang PersistentList)
           (java.time LocalDateTime)))

(deftest is-init-ok?
  (testing "Init Account is not Ok."
    (let [acc (init {:available-limit 1000
                     :active-card true})]
      (is (instance? Account acc))
      (is (:active-card acc))
      (is (= (:available-limit acc) 1000)))))

(deftest is-spend-ok?
  (testing "Spend Account is not Ok."
    (let [acc (init {:available-limit 1000
                     :active-card true})]
      (is (instance? Account (spend acc 100)))
      (is (:available-limit (spend acc 100)) 900))))

(deftest is-history-record-add-ok?
  (testing "History Record Add is not Ok"
    (let [acc (init {:available-limit 1000
                     :active-card true})
          record {:merchant "KFC" :time "2019-02-13T12:02:00.000Z" :amount 200}
          acc-with-history (record->history acc record)
          history-record (first (:history acc-with-history))]

      (is (instance? Account acc-with-history))
      (is (instance? PersistentList (:history acc-with-history)))
      (is (instance? Record history-record))
      (is (instance? LocalDateTime (:time history-record)))
      (is (= (:merchant record) (:merchant history-record)))
      (is (= (:amount record) (:amount history-record))))))


