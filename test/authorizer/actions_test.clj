(ns authorizer.actions-test
  (:require [clojure.test :refer :all]
            [authorizer.account :as account]
            [authorizer.actions :refer :all])
  (:import (authorizer.account Account)))

(def test-account-record {:account {:available-limit 1000 :active-card true}})
(def acc (init-account test-account-record) )

(deftest is-init-account-record-ok?
  (testing "Account Init action is not Ok."
    (is (instance? Account acc))
    (is (true? (:active-card acc)))
    (is (= (:available-limit acc) 1000))))

(deftest is-violation->account-ok?
  (testing "violation->account action is not ok"
    (is (= (:violations (violation->account acc "test-violation"))
           ["test-violation"]))))





