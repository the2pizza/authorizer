(ns authorizer.state
  (:require [authorizer.conditions :as conditions]
            [authorizer.actions :as actions]))

(def INTERVAL 2)
(def FREQUENCY 3)

(defn state [account record]
  (cond
    (and (conditions/account-record? record)
         (conditions/account->not-initialized? account))
    (actions/init-account record)

    (and (conditions/account->initialized? account)
         (conditions/account-record? record))
    (actions/violation account "account-already-initialized")

    (and (conditions/transaction-record? record)
         (conditions/account->not-initialized? account))
    (actions/set-account-not-initialized)

    (and (conditions/transaction-record? record)
         (conditions/transaction->not-available? account))
    (actions/transaction-not-available account)

    (and (conditions/transaction-record? record)
         (conditions/transaction->available? account)
         (conditions/transaction->insufficient-limit? account record))
    (actions/violation account "insufficient-limit")

    (and (conditions/transaction-record? record)
         (conditions/transaction->available? account)
         (conditions/transaction->doubled? account record INTERVAL))
    (actions/violation account "doubled-transaction")

    (and (conditions/transaction-record? record)
         (conditions/transaction->available? account)
         (conditions/transaction->high-frequency-small-interval? account record INTERVAL FREQUENCY))
    (actions/violation account "high-frequency-small-interval" )

    (and (conditions/transaction-record? record)
         (conditions/transaction->available? account))
    (actions/make-transaction account record)))

(defn machine->run [records]
  (reduce state nil records))
