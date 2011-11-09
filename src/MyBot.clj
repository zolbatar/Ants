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
  (let [food-distance (sort-by second (map #(list % (distance ant %)) (food)))
        closest-food (first (first food-distance))]

    ;; Any visible food?
    (when-not (nil? closest-food)
      (let [directions (direction ant closest-food)
            dir (first (filter #(valid-move? ant %) directions))]
        (when dir
          (.println *err* (str ant " " dir " " closest-food))
          (move ant dir))))))

(defn ant-loop []
  (doseq [ant (my-ants)]
    (bot-food-gatherer ant)))
      
(when-not *compile-files*
  (start-game ant-loop))

