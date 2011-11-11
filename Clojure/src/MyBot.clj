(ns MyBot
  (:use ants nodes))

(def is-first? (atom true))

(defn ant-loop []
  (when @is-first?
    (map-out-map)
    (reset! is-first? false)))

(start-game ant-loop)
