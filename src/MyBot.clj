(ns MyBot
  (:use ants)
  (:import [java.util Random]))

(def directions [:north :east :west :south])
(def random (Random.))
(def follow-mode (ref (hash-map)))

(defn random-direction [ant]
  (let [directions (filter #(valid-move? ant %) directions)
        directions-count (count directions)]
    (when (> directions-count 0)
      (let [dir-index (. random nextInt directions-count)
            dir (nth directions dir-index)]
        (when dir
          (.println *err* (str "Random: " dir))
          (move ant dir))))))

(defn next-dir [dir]
  (cond
   (= dir :north) :east
   (= dir :east) :south
   (= dir :south) :west
   (= dir :west) :north))

(defn follow-wall [ant dir]
;  (loop
;   (if (unoccupied? (move-ant ant dir
  
;  (.println *err* (str "Follow wall: "))
  false)

(defn follow-continue [ant]
  (.println *err* (str "Continue follow wall: ")))

(defn bot-food-gatherer [ant]
  (let [food-distance (sort-by second (map #(list % (distance ant %)) (food)))
        closest-food (first (first food-distance))]

    (cond
     
     (contains? follow-mode ant) (follow-continue ant)
     
     ;; Any visible food?
     (not (nil? closest-food)) (let [food-directions (direction ant closest-food)
                                     dir (first (filter #(valid-move? ant %) food-directions))]
                                 (if dir
                                   (do
                                     (.println *err* (str "Food: " dir))
                                     (move ant dir))
                                   (when-not (follow-wall ant dir)
                                     (random-direction ant))))
     
     ;; Random move
     :else (random-direction ant))))
      
(defn ant-loop []
  (doseq [ant (my-ants)]
    (bot-food-gatherer ant)))
      
(when-not *compile-files*
  (start-game ant-loop))

