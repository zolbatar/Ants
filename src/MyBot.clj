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

(defn bot-food-gatherer [ant]
  (let [closest-food (measure-distance ant (food))]

    (cond

     ;; In follow wall mode?
;     (contains? follow-mode ant) (follow-continue ant)
     
     ;; Any visible food?
     (not (nil? closest-food)) (let [moves (all-moves ant closest-food)
                                     food-directions (shortest-distances closest-food moves)]
                                 (when (> (count food-directions) 0)
                                   (let [dir (first food-directions)
                                         move-loc (first (first dir))
                                         move-to (second (first dir))]
                                     
                                     ;; Already moved to?
                                     (.println *err* (count @moved-to))
                                     (if (contains? @moved-to move-loc)
                                       (.println *err* (str "Food (Blocked): " dir))
                                       (dosync
                                         (.println *err* (str "Food: " dir))
                                         (move ant move-to)
                                         (alter moved-to conj move-loc))))))
;                                   (random-direction ant)))

     :else
     (dosync
      (alter moved-to conj ant)))))

;     ;; Random move
                                        ;     :else (random-direction ant))))
                                 
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
    (bot-food-gatherer ant)))

(when-not *compile-files*
  (start-game ant-loop))

