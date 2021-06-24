(ns authorizer.actions
  (:require [authorizer.account :as account]))

(defn violation->account [account violation]
  (assoc account :violations (conj (:violations account) violation)))

(defn init-account [record]
  (let [acc (account/init (:account record))]
    (account/pprint acc)
    acc))

(defn transaction-not-available [account]
  (account/pprint account)
  account)

(defn set-account-not-initialized []
  (account/pprint (violation->account (account/init {:init false}) "account-not-initialized"))
  nil)

(defn violation [account violation]
  (account/pprint (violation->account account violation))
  account)

(defn make-transaction [account record]
  (let [acc (account/spend (account/record->history account (:transaction record))
                           (:amount (:transaction record)))]
      (account/pprint  acc)
      acc))
