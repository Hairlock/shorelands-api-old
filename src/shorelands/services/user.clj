(ns shorelands.services.user
  (:require [clojure.java.io :as io]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [shorelands.db.core :as db]
            [shorelands.middleware :refer [cors-mw]]))


(s/defschema User
  {:id Long
   (s/optional-key :name) String
   (s/optional-key :email) String
   (s/optional-key :password) String})

(defroutes* user-service
  (context* "/api" []

      :tags ["User"]

      (GET* "/users" []
            :return [User]
            :summary "returns the list of users"
            :middlewares [cors-mw]
            (ok (db/get-users)))

      (GET* "/user/:id" []
            :return User
            :path-params [id :- Long]
            :summary "returns the user with a given id"
            (ok (first (db/get-user {:id id}))))

      ;(POST* "/authenticate" []
      ;       :return Boolean
      ;       :body-params [user :- User]
      ;       :summary "authenticates the user using the id and pass."
      ;       (ok (db/authenticate user)))
      ;
      ;(POST* "/user" []
      ;       :return Long
      ;       :body-params [user :- User]
      ;       :summary "creates a new user record."
      ;       (ok (db/create-user-account! user)))

      (DELETE* "/user" []
               :return Long
               :body-params [id :- String]
               :summary "deletes the user record with the given id."
               (ok (db/delete-user! {:id id})))))

