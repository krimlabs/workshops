(ns demo.core
  (:require [demo.ttt :as ttt]
            [reagent.dom :refer [render]]))

(defn App []
  [:div {:class "sans-serif"}
   [ttt/Lattice]])

(defn main! []
  (render [App] (.getElementById js/document "root")))
