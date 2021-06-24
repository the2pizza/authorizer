(ns authorizer.time-test
  (:require [clojure.test :refer :all]
            [authorizer.account :as account]
            [authorizer.time :refer :all])
  (:import (java.time LocalDateTime)))


(def test-transaction-record {:transaction {:merchant "KFC" :amount 20, :time "2019-02-13T12:02:07.000Z"}})
(def test-account-record {:account {:available-limit 1000 :active-card true}})

(def test-account (account/init (:account test-account-record)))
(def test-account-with-history (account/record->history
                                 test-account
                                 (:transaction test-transaction-record)))


(deftest parse-time-is-ok?
  (testing "Time Record is not Ok."
    (let [t (parse-date-time  "2019-02-13T12:03:07.000Z")]
      (is (instance? LocalDateTime t)))))

(deftest record-in-interval-is-ok?
  (testing "Time Record in interval is not Ok."
    (is (true? (record-in-interval? (:time (first (:history test-account-with-history))) "2019-02-13T12:03:07.000Z" 1)))))

(deftest record-not-in-interval-is-ok?
  (testing "Time Record not in interval is not Ok."
    (is (false? (record-in-interval? (:time (first (:history test-account-with-history))) "2019-02-13T12:03:08.000Z" 1)))))