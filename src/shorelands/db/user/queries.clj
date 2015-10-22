(ns shorelands.db.user.queries
  (:require [datomic.api :as d :refer [pull]]
			[shorelands.db.core :refer [uri]]
			[clojure.set :refer [union]]
			[crypto.password.bcrypt :as password]))


(def db (d/db (d/connect uri)))

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
  [{id :db/id username :user/name email :user/email groups :user/group}]
  {:id id
   :username username
   :email    email
   ;:groups   (flatten (map get-group-permission groups))
   })

(defn get-users []
  (let [users (d/q '[:find [(pull ?uid [*]) ...]
					 :where [?uid :user/name _]]
				   db)]
	(map format-users users)))

(get-users)
;(def users (get-users))


;(def groups (:groups (first (map format-users users))))

;(some #{"Admin"} groups)