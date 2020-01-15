(ns dealer-api.core
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :refer [body-params]]
            [io.pedestal.http.route :as route]
            [dealer-api.drugs]))

(defn respond-hello [request]
  {:status 200
   :body "Hello World"})


(def routes
  #{["/hello" :get `respond-hello]
    ["/drugs" :get dealer-api.drugs/all-drugs :route-name :get-drugs]
    ["/drugs" :post [(body-params) dealer-api.drugs/create-drug] :route-name :post-drugs]
    })

(def service-map
  {::http/routes routes
   ::http/type :jetty
   ::http/port 8890})

(defn start-server []
  (http/start (http/create-server
               (assoc service-map
                      ::http/join? false))))
