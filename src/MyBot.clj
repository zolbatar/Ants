(ns MyBot
  (:use ants helpers))

(def moved-to (ref ()))

(defn random-direction [ant]
  (.println *err* "Random...")
  (let [directions (filter #(valid-move? ant %) directions)
        directions-count (count directions)]
    (when (> directions-count 0)
      (let [dir-index (. random nextInt directions-count)
            dir (nth directions dir-index)
            move-loc (valid-move? ant dir)]
        (when dir
          (if (not (contains? @moved-to move-loc))
            (do
              (.println *err* (str "Random: " dir))
              (move ant dir))
            (do
              (.println *err* (str "Random (Blocked): " dir))
              move-loc)))))))
    
(defn bot-search [ant closest type]
  (.println *err* (str "Search: " type))
  (let [moves (all-moves ant closest)
        directions (shortest-distances closest moves)]
    (if (> (count directions) 0)
      (let [dir (first directions)
            move-loc (first (first dir))
            move-to (second (first dir))]
        
        ;; Already moved to?
        (if (not (contains? @moved-to move-loc)) 
          (do
            (.println *err* (str type ": " dir))
            (move ant move-to)
            move-to)
          (do
            (.println *err* (str type " (Blocked): " dir))
            nil)))
      nil)))
 
(defn bot-food-gatherer [ant]
  (let [closest-food (measure-distance ant (food))
        closest-enemy (measure-distance ant (enemy-ants))
        done (atom 0)]

    ;; Any visible food?
    (when-not (nil? closest-food)
      (let [food-return (bot-search ant closest-food "Food")]
        (when-not (nil? food-return)
          (reset! done 1)
          food-return)))

    ;; Any visible enemy?
    (when-not (= done 0)
      (when-not (nil? closest-enemy)
        (let [enemy-return (bot-search ant closest-enemy "Enemy")]
          (when-not (nil? enemy-return)
            (reset! done 1)
            enemy-return))))
                
    ;; Less than 5 spaces from our hill, move randomly
    (when-not (= done 0)
        (if (< (distance ant (first (hills))) 5.0)
          (do
            (.println *err* "Run away from hill")
            (random-direction ant))
          ant))))
                                 
(defn ant-loop []
  ; Clear current positions
  (dosync
   (ref-set moved-to ()))

  ; Save current ant positions
  (dosync
   (doseq [ant (my-ants)]
     (alter moved-to conj ant)))
  
  ; Run each ant
  (doseq [ant (my-ants)]
    (dosync
     (alter moved-to conj (bot-food-gatherer ant)))))

(when-not *compile-files*
  (start-game ant-loop))

