(ns dealer-api.sql.drugs
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "dealer_api/sql/drugs.sql")
