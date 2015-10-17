(ns shorelands.db.utils
  (:import datomic.Util java.util.Random)
  (:require
    [clojure.java.io :as io]
    [datomic.api :as d]))

(def resource io/resource)


(defn read-all
  "Read all forms in f"
  [f]
  (Util/readAll (io/reader f)))


(defn transact-all
  "Load and run all transactions from f"
  [conn f]
  (loop [n 0
         [tx & more] (read-all f)]
    (if tx
      (recur (+ n (count (:tx-data @(d/transact conn tx))))
             more)
      {:datoms n})))


