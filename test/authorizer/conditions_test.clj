(ns authorizer.conditions-test
  (:require [clojure.test :refer :all]
            [authorizer.account :as account]
            [authorizer.conditions :refer :all]))

(def test-transaction-record {:transaction {:merchant "KFC" :amount 20, :time "2019-02-13T12:02:07.000Z"}})
(def test-transaction-record2 {:transaction {:merchant "KFC" :amount 25, :time "2019-02-13T12:03:07.000Z"}})
(def test-transaction-record3 {:transaction {:merchant "KFC" :amount 25, :time "2019-02-13T12:03:08.000Z"}})
(def test-transaction-record4 {:transaction {:merchant "Macdonalds" :amount 25, :time "2019-02-13T12:03:10.000Z"}})
(def test-transaction-big-amount-record {:transaction {:merchant "KFC" :amount 1001, :time "2019-02-13T12:02:07.000Z"}})

(def test-account-record {:account {:available-limit 1000 :active-card true}})
(def test-account-record-inactive-card {:account {:available-limit 1000 :active-card false}})

(def test-account (account/init (:account test-account-record)))
(def test-account-inactive-card (account/init
                                  (:account test-account-record-inactive-card)))

(def test-account-with-history (account/record->history
                                 test-account
                                 (:transaction test-transaction-record)))
(def test-account-with-history2 (account/record->history
                                  test-account-with-history
                                  (:transaction test-transaction-record2)))


(deftest is-account-record-ok?
  (testing "Account Record is not Ok."
    (is (true? (account-record? test-account-record)))))

(deftest is-transaction-record-ok?
  (testing "Transaction Record is not Ok."
    (is (true? (transaction-record? test-transaction-record)))))

(deftest is-account-not-initialized?
  (testing "Account not-initialized is not Ok."
    (is (false? (account->not-initialized? test-account)))))

(deftest is-account-initialized?
  (testing "Account initialized is not Ok."
    (is (true? (account->initialized? test-account)))))

(deftest is-transaction-available-ok?
  (testing "Account transaction available is not Ok."
    (is (true? (transaction->available? test-account)))))

(deftest is-transaction-available-wrong?
  (testing "Account transaction available but shouldn't."
    (is (false? (transaction->available? test-account-inactive-card)))))

(deftest is-transaction-not-available-ok?
  (testing "Account transaction not available is not Ok."
    (is (true? (transaction->not-available? test-account-inactive-card)))))

(deftest is-transaction-not-available-wrong?
  (testing "Account transaction not available but should be."
    (is (false? (transaction->not-available? test-account)))))

(deftest is-insufficient-limit-ok?
  (testing "Account transaction insufficient limit is not Ok."
    (is (true? (transaction->insufficient-limit? test-account test-transaction-big-amount-record)))))

(deftest is-insufficient-limit-wrong?
  (testing "Account transaction insufficient limit is not Ok."
    (is (false? (transaction->insufficient-limit? test-account test-transaction-record)))))

(deftest is-insufficient-limit-wrong?
  (testing "Account transaction insufficient limit is not Ok."
    (is (false? (transaction->insufficient-limit? test-account test-transaction-record)))))

(deftest is-high-frequency-small-interval-ok?
  (testing "Account transaction insufficient limit is not Ok."
    (is (true? (transaction->high-frequency-small-interval?
                 test-account-with-history2
                 test-transaction-record3 2 2)))))

(deftest is-high-frequency-small-interval-wrong?
  (testing "Account transaction insufficient limit is not Ok."
    (is (false? (transaction->high-frequency-small-interval?
                  test-account-with-history2
                  test-transaction-record3 1 2)))))

(deftest is-doubled-ok?
  (testing "Account transaction insufficient limit is not Ok."
    (is (true? (transaction->doubled?
                  test-account-with-history2
                  test-transaction-record3 2)))))

(deftest is-not-doubled-ok?
  (testing "Account transaction insufficient limit is not Ok."
    (is (false? (transaction->doubled?
                 test-account-with-history2
                 test-transaction-record4 2)))))




