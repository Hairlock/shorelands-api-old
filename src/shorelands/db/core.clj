(ns shorelands.db.core
  (:require
    [cheshire.core :refer [generate-string parse-string]]
    [clojure.java.jdbc :as jdbc]
    [clojure.java.io :as io]
    [conman.core :as conman]
    [crypto.password.bcrypt :as password]
    [environ.core :refer [env]])
  (:import org.postgresql.util.PGobject
           org.postgresql.jdbc4.Jdbc4Array
           clojure.lang.IPersistentMap
           clojure.lang.IPersistentVector
           [java.sql
            BatchUpdateException
            Date
            Timestamp
            PreparedStatement]))

(defonce ^:dynamic *conn* (atom nil))

(conman/bind-connection *conn* "sql/build/bundle.sql")

(defn concat-sql []
  (let [directory (io/file "resources/sql/src")
        files     (file-seq directory)
        directoryPath (.getAbsolutePath (io/file (.getParent (first files))))
        outputFile (str directoryPath "//build//bundle.sql")
        reduce-fn  (fn [concatenated file]
                     (str concatenated "\r\n" (slurp (.getAbsolutePath file))))
        merged-sql (reduce reduce-fn "" (filter #(.isFile %) files))]
    (spit outputFile merged-sql)))


(def pool-spec
  {:adapter    :postgresql
   :init-size  1
   :min-idle   1
   :max-idle   4
   :max-active 32})

(defn connect! []
  (conman/connect!
    *conn*
   (assoc
     pool-spec
     :jdbc-url (env :database-url))))

(defn disconnect! []
  (conman/disconnect! *conn*))

(defn to-date [sql-date]
  (-> sql-date (.getTime) (java.util.Date.)))

(extend-protocol jdbc/IResultSetReadColumn
  Date
  (result-set-read-column [v _ _] (to-date v))

  Timestamp
  (result-set-read-column [v _ _] (to-date v))

  Jdbc4Array
  (result-set-read-column [v _ _] (vec (.getArray v)))

  PGobject
  (result-set-read-column [pgobj _metadata _index]
    (let [type  (.getType pgobj)
          value (.getValue pgobj)]
      (case type
        "json" (parse-string value true)
        "jsonb" (parse-string value true)
        "citext" (str value)
        value))))

(extend-type java.util.Date
  jdbc/ISQLParameter
  (set-parameter [v ^PreparedStatement stmt idx]
    (.setTimestamp stmt idx (Timestamp. (.getTime v)))))

(defn to-pg-json [value]
  (doto (PGobject.)
    (.setType "jsonb")
    (.setValue (generate-string value))))

(extend-protocol jdbc/ISQLValue
  IPersistentMap
  (sql-value [value] (to-pg-json value))
  IPersistentVector
  (sql-value [value] (to-pg-json value)))

(defn create-user-account! [user]
  (create-user! (update user :pass password/encrypt)))

(defn authenticate [user]
  (boolean
    (when-let [db-user (-> user (get-user) first)]
      (password/check (:pass user) (:pass db-user)))))

