(ns demo.form
  (:require [reagent.core :as r]
            [clojure.test.check.generators :as gen]
            [clojure.spec.alpha :as s]
            [clojure.pprint :as pprint]))

(s/def ::first-name (s/and string? #(<= (count %) 24) #(>= (count %) 8)))
(s/def ::last-name (s/and string? #(<= (count %) 24) #(>= (count %) 8)))
(s/def ::phone-number (s/and int? #(<= % 9999999999) #(>= % 100000000)))

(s/def ::person (s/keys :req-un [::first-name ::last-name]
                        :opt-un [::phone-number]))


(def state (r/atom {:first-name nil
                    :last-name nil
                    :phone-number nil}))

(defonce error (r/atom nil))

(defn field [{:keys [label key]}]
  [:div.mb3
   [:div {:class "mb1 f7 black-60 b"} label]
   [:input {:class "pa1"
            :type "text"
            :value (key @state)
            :on-change #(swap! state assoc key (-> % .-target .-value))}]])

(defn validate []
  (s/valid? ::person @state))

;; 00 - Introduce how on-submit works
(defn on-submit []
  (reset! error nil)
  (if (s/valid? ::person @state)
    (prn "Submit to API")
    (reset! error (s/explain-str ::person @state))))

(defn Form []
  [:div.pa3
   [:div.f6.black-40.mv3 "Reagent Form"]
   [field {:label "First Name" :key :first-name}]
   [field {:label "Last Name" :key :last-name}]
   [field {:label "Phone Number" :key :phone-number}]
   (when @error
     [:div {:class "f7 mb3 dark-red"} @error])
   [:button {:class "f6 ba br2 pa1 pointer dim"
             :on-click #(on-submit)}
    "Submit"]])

(comment
  ;; 01 - gen 1 sample
  (gen/sample (s/gen ::person) 1)

  (defn gen-samples [n]
    (let [samples (gen/sample (s/gen ::person) n)]
      (if (= n 1) (first samples) samples)))

  ;; 02 - Function to generate n samples
  (gen-samples 1)

  ;; 03 - View samples in the repl
  (pprint/print-table (gen-samples 8))

  ;; 04 - Check filling form with one value and submit
  (reset! state (first (gen/sample (s/gen ::person) 1)))
  (on-submit)

  ;; 05 - Create table of generative validations
  (let [sample (gen/sample (s/gen ::person) 20)
        with-validation (map #(assoc % :valid (s/valid? ::person %)) sample)]
    (pprint/print-table with-validation))
  )



