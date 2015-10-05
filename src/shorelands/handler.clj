(ns shorelands.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes]]
            [shorelands.layout :refer [error-page]]
            [shorelands.routes.home :refer [home-routes]]
            [shorelands.services.user :refer [user-service]]
            [shorelands.middleware :as middleware]
            [shorelands.db.core :as db]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  (timbre/merge-config!
    {:level     (if (env :dev) :trace :info)
     :appenders {:rotor (rotor/rotor-appender
                          {:path "shorelands.log"
                           :max-size (* 512 1024)
                           :backlog 10})}})

  (if (env :dev) (parser/cache-off!))
  (db/connect!)
  (timbre/info (str
                 "\n-=[shorelands started successfully"
                 (when (env :dev) " using the development profile")
                 "]=-")))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "shorelands is shutting down...")
  (db/disconnect!)
  (timbre/info "shutdown complete!"))

(def app-routes
  (routes
    (var user-service)
    (wrap-routes #'home-routes middleware/wrap-csrf)
    (route/resources "/")
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(def app (middleware/wrap-base #'app-routes))
