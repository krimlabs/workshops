(ns dealer-api.drugs
  (:require [dealer-api.sql.drugs :as sql]
            [dealer-api.config :refer [db]]
            [io.pedestal.http :as http]))

(defn all-drugs [_]
  (http/json-response (sql/drugs db)))

(defn drug-by-id [request])

(defn create-drug [request]
  (let [created-drug (sql/new-drug db {:name :availability :price})]
    (http/json-response created-drug)))

(defn update-drug-by-id [request])

(defn delete-drug [request])

