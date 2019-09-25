(ns demo.core
  (:require [demo.ttt :as ttt]
            [demo.form :as form]
            [reagent.dom :refer [render]]))

(defn App []
  [:div {:class "sans-serif"}
   ;;[ttt/Lattice]
   [form/Form]
   ])

(defn main! []
  (render [App] (.getElementById js/document "root")))
