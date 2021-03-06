(ns demo.form
  (:require [reagent.core :as r]
            [clojure.test.check.generators :as gen]
            [clojure.spec.alpha :as s]
            [clojure.pprint :as pprint]))

(defn count-between? [lower upper s]
  (and (<= (count s) upper)
       (>= (count s) lower)))

(defn formatted-name? [s]
  (re-matches #"[A-Z][a-z]*" s))

(s/def ::first-name (s/and string? #(count-between? 4 24 %)))
(s/def ::last-name (s/and string? #(count-between? 4 24 %)))
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
            :on-change #(if (= key :phone-number)
                          (swap! state assoc key (-> % .-target .-value js/parseInt))
                          (swap! state assoc key (-> % .-target .-value)))}]])

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

  ;; 01 - Validate manually
  (s/valid? ::person {:first-name "Shivek" :last-name "Khurana"
                      :phone-number 9999999997})

  ;; 02 - gen 1 sample
  (gen/generate (s/gen ::person))

  ;; 03 - Function to generate n samples
  (gen/sample (s/gen ::person) 4)

  ;; 04 - View samples in the repl
  (pprint/print-table (gen/sample (s/gen ::person) 8))

  ;; 05 - Check filling form with one value and submit
  (reset! state (gen/generate (s/gen ::person)))
  (on-submit)

  ;; 06 - Create table of generative validations
  (let [sample (gen/sample (s/gen ::person) 20)
        with-validation (map #(assoc % :valid (s/valid? ::person %)) sample)]
    (pprint/print-table with-validation))
  )



