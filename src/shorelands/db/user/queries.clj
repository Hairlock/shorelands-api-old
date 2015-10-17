(ns shorelands.db.user.queries
  (:require [datomic.api :as d :refer [pull]]
			[shorelands.db.core :refer [conn]]
			[clojure.set :refer [union]]
			[crypto.password.bcrypt :as password]))


(def db (d/db conn))

(defn get-user-group [name]
  (->> (d/q '[:find ?e
			  :in $ ?name
			  :where [?e :group/name ?name]]
		  	db
			name)
	   ffirst))

(defn get-group-permission
  [{gid :db/id}]
  (let [permission  (d/q '[:find ?perm
						   :in $ ?eid
						   :where [?eid :group/permission ?perm]]
						 db gid)]
	(first permission)))


(defn format-users
  [{username :user/name email :user/email groups :user/group}]
  {:username username
   :email    email
   :groups   (flatten (map get-group-permission groups))})

(defn get-users []
  (d/q '[:find [(pull ?uid [*]) ...]
			   :where [?uid :user/name _]]
			 db))

;(def users (get-users))

;(map format-users users)