(ns shorelands.services.user.functions
		(:require [shorelands.db.core :as db]
				  [buddy.hashers :as hashers]
				  [ring.util.http-response :as respond]))

(defn create-new-user [user]
  (let [{:keys [username email password]} user
		hashed-password (hashers/encrypt password)
		new-user		(db/create-user-account! user)
		permission		(db/update-permission-for-user<!)]))