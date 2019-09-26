(ns demo.orchestra
  (:require [orchestra.core :refer-macros [defn-spec]]
            [orchestra-cljs.spec.test :as st]))

; The return spec comes after the fn name.
(defn-spec my-inc integer?
  [a integer?] ; Each argument is followed by its spec.
  (+ a 1))

; Doc strings work as expected.
(defn-spec my-add integer?
  "Returns the sum of `a` and `b`."
  [a integer? b integer?]
  (+ a b))

;; 00 - Need to turn on instrumentation
(st/instrument)

(comment
  ;; 01 - works fine with ints
  (my-inc 1)

  ;; 02 - fails if not int
  (my-inc :a)

  )

