(ns shorelands.db.user.transactions
  (:require [buddy.hashers :as hashers]
			[datomic.api :as d]
			[shorelands.db.core :refer :all]
			[shorelands.db.user.queries :as t]))


(defn add-user [user]
  (let [{:keys [name email password]} user
		uid (d/tempid :db.part/user)]
	@(d/transact conn [{:db/id         uid
						:user/name     name
						:user/email    email
						:user/password (hashers/encrypt password)
						:user/status   :user.status/active
						:user/group    (t/get-user-group "Staff")}])))


