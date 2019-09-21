(ns demo.ttt
  (:require [reagent.core :as r]))

(defonce base-lattice
  [[nil nil nil]
   [nil nil nil]
   [nil nil nil]])

(def lattice (r/atom base-lattice))
(def turn (r/atom :x))

(defn reset-state! []
  (reset! lattice base-lattice)
  (reset! turn :x))


;; 00 - no checks
;; (defn move [x y mark]
;;   (swap! lattice update-in [x y] (fn [_] mark)))


;; 01 - check that data is not overwritten
(defn can-move? [x y]
  ;; Make sure that coord is not marked already
  (-> @lattice (nth x) (nth y) nil?))

(defn move [x y mark]
  (when (can-move? x y)
    (swap! lattice update-in [x y] (fn [_] mark))))

(defn all-marks-in-row-equal? [matrix]
  (map #(and (apply = %) (some keyword? %)) matrix))

(defn transpose [m]
  (apply mapv vector m))

(defn top-to-bottom-diagonal-equal? [matrix]
  (let [diag (map-indexed (fn [idx row] (nth row idx)) matrix)]
    (and (some keyword? diag)
         (apply = diag))))

(defn bottom-to-top-diagonal-equal? [matrix]
  (let [diag (map-indexed (fn [idx row] (nth row (- 2 idx))) matrix)]
    (and (some keyword? diag)
         (apply = diag))))

(defn somebody-won?
  "Check the lattice to see if someone won"
  []
  {:row? (some true? (all-marks-in-row-equal? @lattice))
   :col? (some true? (-> @lattice transpose all-marks-in-row-equal?))
   :top-to-bottom-diag? (top-to-bottom-diagonal-equal? @lattice)
   :bottom-to-top-diag? (bottom-to-top-diagonal-equal? @lattice)})

(defn get-at [matrix x y]
        (-> matrix
            (nth x)
            (nth y)))

(defn els-equal-and-not-nil [els]
  (and (apply = els)
       (not (some nil? els))))

(defn row-winner [matrix]
  (cond
    (els-equal-and-not-nil (nth matrix 0)) (get-at matrix 0 0)
    (els-equal-and-not-nil (nth matrix 1)) (get-at matrix 1 0)
    (els-equal-and-not-nil (nth matrix 2)) (get-at matrix 2 0)))

(defn winner []
  (let [win-stat (somebody-won?)
        winner? (some true? (vals win-stat))]
    (when winner?
      (cond
        (or (:bottom-to-top-diag? win-stat)
            (:top-to-bottom-diag? win-stat)) (get-at @lattice 1 1)
        (:row? win-stat) (row-winner @lattice)
        (:col? win-stat) (row-winner (transpose @lattice))))))



(defn playable?
  "Check if more moves can be made"
  []
  (boolean (some nil? (flatten @lattice))))

(defn Lattice []
  [:div {:class "pa3 tc"}
   [:div {:class "f6 black-40 b tc ttu"} "Reagent - Tic Tac Toe"]
   (cond
     (winner) [:div {:class "green f2 mt3"} (str (winner) " won")]
     (not (playable?)) [:div {:class "yellow f2 mt3"} "Draw"]
     :else [:div {:class "w-90 w-20-l w-30-m center"}
            [:div {:class "f3 b tc mv3"} (str @turn "'s turn")]
            (map-indexed
             (fn [idx row]
               ^{:key (str "row-"idx)}
               [:div {:class "w-100 tc flex justify-between"}
                (map-indexed
                 (fn [idy col]
                   ^{:key (str "col-"idy)}
                   [:div {:class (str "pointer pa2 br2 dim ma1 w-100 "
                                      (condp = col
                                  nil"bg-washed-blue"
                                  :x "bg-green"
                                  :o "bg-yellow"))
                          :onClick #(when (can-move? idx idy)
                                      (move idx idy @turn)
                                      (reset! turn (if (= @turn :o) :x :o)))}
                    (or col "-")])
                 row)])
             @lattice)])
   [:button {:class "db center mt4"
             :onClick #(reset-state!)} "Reset"]])

(comment
  (map #(and (apply = %) (some keyword? %)) @lattice)
  @lattice
  (transpose @lattice)
  (somebody-won?)
  (move 1 1 @turn)
  (do
    (reset-state!)
    (move 0 0 :o)
    (move 1 0 :o)
    (move 2 0 :o))

  (winner)
  (bottom-to-top-diagonal-equal? @lattice)
  (playable?))
