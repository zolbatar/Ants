(ns MyBot
  (:use ants))
;  (:import [java.util Random]))

(def directions [:north :east :west :south])
;(def random (Random.))

(defn simple-bot [ant]
  (let [dir (first (filter #(valid-move? ant %) directions))]
    (when dir
      (move ant dir))))

(defn bot-food-gatherer [ant]
  (let [df (sort-by second (map #(list % (distance ant %)) (food)))]
    (doseq [x df]
      (.println *err* x))))

;  (doseq [f (food)]
;    (.println *err* f)

    ;; How close are we to food?
;    (when (< 5 (distance ant f))
 ;     (doseq [dir (direction ant f)]
 ;       (when (valid-move? ant dir)
  ;        (move ant dir))))))

(defn ant-loop []
  (doseq [ant (my-ants)]
    (.println *err* ant)
    (bot-food-gatherer ant)))
      
(when-not *compile-files*
  (start-game ant-loop))

