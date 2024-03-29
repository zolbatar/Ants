(ns MyBot
  (:use ants nodes helpers))

(def ants (atom {}))
(def state-new 0)
(def state-idle 10)
(def state-moving 20)
(def state-seeking 30)

(defn new-ant [ant]
  (.println *err* (str "New ant at " ant))
  (swap! ants assoc ant { :state state-new }))

(defn my-valid-move? [loc dir]
  (let [move (valid-move-food? loc dir)]
    (if (nil? move)
      nil
      (if (contains? @ants move)
        nil
        move))))

;(defn min [n1 n2]
 ; (if (> n1 n2)
 ;   n2
 ;   n1))

(defn ant-move [ant] 
  (.println *err* (str "Started moving ant at " ant))
  (let [best-direction (map-directions ant)
        direction (first best-direction)
        distance (second best-direction)
        new-position (my-valid-move? ant direction)]
    (if (nil? new-position)
      (swap! ants assoc new-position { :state state-idle })
      (do
        (move ant (first best-direction))
        (swap! ants assoc new-position { :state state-moving :direction direction :distance (min 5 distance) })
        (swap! ants dissoc ant)))))

(defn ant-continue-move [ant ant-state]
  (.println *err* (str "Moving ant at " ant))
  (let [direction (:direction ant-state)
        distance (:distance ant-state)
        distance-remaining (dec distance)
        new-position (my-valid-move? ant direction)]
    (if (nil? new-position)
      (swap! ants assoc new-position { :state state-idle })
      (do
        (move ant direction)
        (swap! ants dissoc ant)
        (if (<= distance-remaining 0)
          (swap! ants assoc new-position { :state state-idle })
          (swap! ants assoc new-position { :state state-moving :direction direction :distance distance-remaining }))))))

(defn ant-move-to [ant loc]
  (.println *err* (str "Seeking ant at " ant " to " loc))
  (let [dir (filter #(my-valid-move? ant %) (direction ant loc))]
    (if (= (count dir) 0)
      (do
        (swap! ants dissoc ant)
        (ant-move ant))
      (do
        (let [move-to (my-valid-move? ant (first dir))]
          (swap! ants dissoc ant)
          (move ant (first dir))
          (swap! ants assoc move-to { :state state-seeking :loc loc }))))))

(defn ant-loop []
  (doseq [ant (my-ants)]

    ;; New ant?
    (if (not (contains? @ants ant))
      (new-ant ant))

    ;; Get ant and do based on state
    (let [ant-state (get @ants ant)]
      (cond

       ; Idle?
       (or (= (:state ant-state) state-new) (= (:state ant-state) state-idle))
       (let [closest-food (measure-distance ant (food))]
         (if (nil? closest-food)
           (ant-move ant)
           (ant-move-to ant closest-food)))
       
       ; Moving?
       (= (:state ant-state) state-moving) (ant-continue-move ant ant-state)
       
       ; Seeking?
       (= (:state ant-state) state-seeking) (ant-move-to ant ( :loc ant-state ))))))

(start-game ant-loop)
