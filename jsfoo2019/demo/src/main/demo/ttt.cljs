(ns demo.ttt
  (:require [reagent.core :as r]))

(defn els-equal-and-not-nil [els]
  (and (apply = els)
       (not (some nil? els))))

(defonce base-lattice
  [[nil nil nil]
   [nil nil nil]
   [nil nil nil]])

;; 02 - Make this reactive
(def lattice (atom base-lattice))
(def turn (atom :x))
;; (def lattice (r/atom base-lattice))
;; (def turn (r/atom :x))


(defn reset-state! []
  (reset! lattice base-lattice)
  (reset! turn :x))

;; 03 - Send code/data/control to the runtime
(reset-state!)

(defn can-move? [x y]
  ;; Make sure that coord is not marked already

  ;; 04 - Update can move to check pos (x, y) is nil
  ;; (nil? (get-in @lattice [x y]))
  true
  )

(defn move [x y mark]
  (when (can-move? x y)
    (swap! lattice update-in [x y] (fn [_] mark))
    (reset! turn (if (= @turn :o) :x :o))))

(defn all-marks-in-row-equal? [matrix]
  (map els-equal-and-not-nil matrix))

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

(defn row-winner [matrix]
  (cond
    (els-equal-and-not-nil (nth matrix 0)) (get-in matrix [0 0])
    (els-equal-and-not-nil (nth matrix 1)) (get-in matrix [1 0])
    (els-equal-and-not-nil (nth matrix 2)) (get-in matrix [2 0])))

(defn winner []
  (let [win-stat (somebody-won?)
        winner? (some true? (vals win-stat))]
    (when winner?
      (cond
        (or (:bottom-to-top-diag? win-stat)
            (:top-to-bottom-diag? win-stat)) (get-in @lattice [1 1])
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
                          :onClick #(move idx idy @turn)}
                    (or col "-")
                    [:div {:class "f7 mt2"} (str "(" idx "," idy ")")]])
                 row)])
             @lattice)])
   [:button {:class "db center mt4 pointer dim pa2 br2 ba b--black-20"
             :onClick #(reset-state!)} "Reset"]])

(comment
  ;; Rich Comment
  ;; 00 Send/ Recieve data to/from runtime
  ;; 01 - Read Lattice from atom after making changes to the UI
  @lattice

  ;; 05 - Just evaluate and check that row matching works
  ;; first row
  (do
    (reset-state!)
    (move 0 0 :o)
    (move 0 1 :o)
    (move 0 2 :o))

  ;; 06 - Evaluate and check that column work
  ;; first col
  (do
    (reset-state!)
    (move 0 0 :o)
    (move 1 0 :o)
    (move 2 0 :o))

  ;; 07 - Evaluate and check that diag works (talk about ability to use functions to debug)
  ;; (Close the ui now and let people visualise what's happening)
  ;; top-to-bottom diag
  @lattice
  (reset-state!)
  (let [coords [[0 0] [1 1] [2 2]]]
    (map (fn [[x y]]
           (move x y :x)) coords))

  (winner)

  (bottom-to-top-diagonal-equal? @lattice)
  (top-to-bottom-diagonal-equal? @lattice)
  (playable?)

  (move 0 0 :x)
  (can-move? 0 0)
  )







