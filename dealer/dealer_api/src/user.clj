(ns user
  (:require [dealer-api.core :refer [start-server]]
            [io.pedestal.http :as http]
            [clojure.tools.namespace.repl :refer [refresh]]))

;; For interactive development
(defonce server (atom nil))

(defn go []
  (reset! server (start-server))
  (prn "Server started on localhost:8890")
  (prn "Enter (reset) to reload.")
  :started)

(defn halt []
  (when @server
    (http/stop @server)))

(defn reset []
  (halt)
  (refresh :after 'user/go))

