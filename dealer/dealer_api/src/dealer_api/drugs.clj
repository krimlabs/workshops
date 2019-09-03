(ns dealer-api.drugs
  (:require [dealer-api.sql.drugs :as sql]
            [dealer-api.config :refer [db]]
            [io.pedestal.http :as http]))

(defn all-drugs [_]
  (http/json-response (sql/drugs db)))

(defn drug-by-id [request])

(defn create-drug [request])

(defn update-drug-by-id [request])

(defn delete-drug [request])

