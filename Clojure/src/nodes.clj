(ns nodes
  (:use ants))

(def nodes (atom (list {})))

(defn add-node [loc dir distance]
  (swap! nodes conj {:loc loc :dir dir :distance distance}))

(defn does-node-exist? [loc dir]
  (= (count (filter #(and (= (:loc %) loc) (= (:dir %) dir)) @nodes)) 0))

(defn scan-node-dir [loc dir]
  (loop [distance 0 pos loc]
    (let [new-pos (valid-move? pos dir)]
      (if new-pos
        (recur (inc distance) new-pos)
        (do
          (add-node loc dir distance)
          pos)))))

(defn scan-node [depth loc dir]
  (.println *err* (str depth " " loc " " dir))
  (when (does-node-exist? loc dir)
    (let [next-loc (scan-node-dir loc dir)]
      (when-not (or (= depth 0) (= next-loc loc))
        (scan-node (inc depth) next-loc :north)
        (scan-node (inc depth) next-loc :east)
        (scan-node (inc depth) next-loc :south)
        (scan-node (inc depth) next-loc :west)))))

(defn map-out-map []
  (doseq [x (hills)]
    (scan-node 0 x :north)
    (scan-node 0 x :east)
    (scan-node 0 x :south)
    (scan-node 0 x :west))
  (.println *err* @nodes))
    


