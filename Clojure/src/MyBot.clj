(ns MyBot
  (:use ants nodes))

(def ants (atom {}))
(def state-new 0)
(def state-idle 10)
(def state-moving 20)

(defn new-ant [ant]
  (.println *err* (str "New ant at " ant))
  (swap! ants assoc ant { :state state-new }))

(defn ant-move [ant ant-state] 
  (.println *err* (str "Started moving ant at " ant))
  (let [best-direction (map-directions ant)
        direction (first best-direction)
        distance (second best-direction)
        new-position (valid-move? ant direction)]
    (when-not (nil? new-position)
      (move ant (first best-direction))
      (swap! ants assoc new-position { :state state-moving :direction direction :distance (/ distance 2) })
      (swap! ants dissoc ant))))

(defn ant-continue-move [ant ant-state]
  (.println *err* (str "Moving ant at " ant))
  (let [direction (:direction ant-state)
        distance (:distance ant-state)
        distance-remaining (dec distance)
        new-position (valid-move? ant direction)]
    (when-not (nil? new-position)
      (move ant direction)
      (swap! ants dissoc ant))
      (if (= distance-remaining 0)
        (swap! ants assoc new-position { :state state-idle })
        (swap! ants assoc new-position { :state state-moving :direction direction :distance distance-remaining }))))
  
(defn ant-loop []
  (doseq [ant (my-ants)]

    (.println *err* @ants)
    
    ;; New ant?
    (if (not (contains? @ants ant))
      (new-ant ant))

    ;; Get ant and do based on state
    (let [ant-state (get @ants ant)]
      (cond
       (or (= (:state ant-state) state-new) (= (:state ant-state) state-idle)) (ant-move ant ant-state)
       (= (:state ant-state) state-moving) (ant-continue-move ant ant-state)))))

(start-game ant-loop)
