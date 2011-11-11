(ns nodes
  (:use ants))

(defn add-node [node dir distance]
  (swap! node assoc dir distance))

(defn scan-node-dir [loc dir]
  (loop [distance 0 pos loc]
    (let [new-pos (valid-move? pos dir)]
      (if new-pos
        (recur (inc distance) new-pos)
        distance))))

(defn scan-node [node loc]
  (add-node node :north (scan-node-dir loc :north))
  (add-node node :east (scan-node-dir loc :east))
  (add-node node :south (scan-node-dir loc :south))
  (add-node node :west (scan-node-dir loc :west)))

(defn map-out-map []
  (let [node (atom {})] 
    (doseq [x (hills)]
      (scan-node node x))
    (.println *err* @node)))
    


