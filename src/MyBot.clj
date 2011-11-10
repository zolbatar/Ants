(ns MyBot
  (:use ants helpers)
  (:import [java.util Random]))

;(def follow-mode (ref (hash-map)))
(def moved-to (ref ()))

;(defn follow-wall [ant dir]
;  (loop
;   (if (unoccupied? (move-ant ant dir
  
;  (.println *err* (str "Follow wall: "))
;  false)

;(defn follow-continue [ant]
;  (.println *err* (str "Continue follow wall: ")))

(defn bot-search [ant closest type]
  (let [moves (all-moves ant closest)
        directions (shortest-distances closest moves)]
    (when (> (count directions) 0)
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
            ant))))))
 
(defn bot-food-gatherer [ant]
;  (.println *err* @moved-to)
  (let [closest-food (measure-distance ant (food))
        closest-enemy (measure-distance ant (enemy-ants))]

    (cond

     ;; In follow wall mode?
;     (contains? follow-mode ant) (follow-continue ant)
     
     ;; Any visible food?
     (not (nil? closest-food)) (bot-search ant closest-food "Food")

     ;; Any visible enemy?
     (not (nil? closest-enemy)) (bot-search ant closest-enemy "Enemy")

     :else
     ant)))
                                 
(defn ant-loop []
  ; Clear current positions
  (dosync
   (ref-set moved-to ()))

  ; Save current ant positions
;  (dosync
;   (doseq [ant (my-ants)]
;     (alter moved-to conj ant)))
  
  ; Run each ant
  (doseq [ant (my-ants)]
    (dosync
     (alter moved-to conj (bot-food-gatherer ant)))))

(when-not *compile-files*
  (start-game ant-loop))

