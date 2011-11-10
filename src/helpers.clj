(ns helpers
  (:use ants)
  (:import [java.util Random]))

(def directions [:north :east :west :south])
(def random (Random.))

(defn rotate-clockwise [dir]
  (cond
   (= dir :north) :east
   (= dir :east) :south
   (= dir :south) :west
   (= dir :west) :north))

(defn rotate-anticlockwise [dir]
  (cond
   (= dir :north) :west
   (= dir :east) :north
   (= dir :south) :east
   (= dir :west) :south))

(defn all-moves [loc loc2]
  (map #(list (valid-move-food? loc %) %) (filter #(valid-move-food? loc %) (direction loc loc2))))

(defn shortest-distances [loc directions]
  (sort-by second (map #(list % (distance (first %) loc)) directions)))

(defn score-point-to-point [loc1 loc2]
;  (.println *err* (str "Find: " loc1 loc2))
  (loop [score 0 loc loc1]
;    (.println *err* (str "Position: " loc loc2))
    (cond
     (= loc loc2)
       score

     (> score 20)
       score

     :else
     (do
       (let [moves (all-moves loc loc2)
             distances (shortest-distances loc2 moves)]
         (if (= (count distances) 0)
           (+ score 20)
           (do
           (recur (inc score) (first (first (first distances)))))))))))
          
(defn measure-distance [ant type]
  (let [distances (sort-by second (map #(list % (score-point-to-point ant %)) type))
        closest (first (first distances))]    
    (if (nil? closest)
      nil
      (do
        (if (< (second closest) 15)
          closest
          nil)))))


