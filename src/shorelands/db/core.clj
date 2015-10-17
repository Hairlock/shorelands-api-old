(ns shorelands.db.core
  (:require
    [cheshire.core :refer [generate-string parse-string]]
    [clojure.java.io :as io]
    [crypto.password.bcrypt :as password]
	[datomic.api :as d]
	[datomic-schema.schema :as s]
    [environ.core :refer [env]]))

(defonce url "datomic:mem://shorelandsdb")


(defn parts []
  [(s/part "shorelands")])

(defn schema []
  [(s/schema user
			 (s/fields
			   [name :string :indexed :unique-value]
			   [email :string :indexed :unique-value]
			   [password :string "Hashed password string"]
			   [status :enum [:pending :active :inactive :cancelled]]
			   [group :ref :many]))
   (s/schema group
			 (s/fields
			   [name :string]
			   [permission :string :many]))])

(defn setup-db [url]
  (d/delete-database url)
  (d/create-database url)
  (d/transact
	(d/connect url)
	(concat
	  (s/generate-parts (parts))
	  (s/generate-schema (schema) {:index-all? true}))))



(defn seed-db []
  (let [gid (d/tempid :db.part/user)
		gid2 (d/tempid :db.part/user)
		conn (d/connect url)]
	(d/transact
	  conn
	  [{:db/id            gid
		:group/name       "Staff"
		:group/permission "Admin"}
	   {:db/id            gid2
		:group/name       "Mod"
		:group/permission "Moderator"}
	   {:db/id         (d/tempid :db.part/user)
		:user/name     "Yannick"
		:user/email    "yannick.sealy08@gmail.com"
		:user/password (password/encrypt "password")
		:user/group    [gid gid2]
		:user/status   :user.status/active}])))

(defn start []
  (setup-db url)
  (seed-db))

(defn stop []
  (d/delete-database url)
  (d/release conn))

(d/create-database url)

(def conn (d/connect url))

;(println "Attributes defined in db:"
;		 (map (comp :db/ident (partial d/entity (d/db (d/connect url))) first)
;			  (d/q '[:find ?e :where [_ :db.install/attribute ?e]] (d/db (d/connect url)))))








