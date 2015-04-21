(ns anaphorae.thread
  (:refer-clojure :exclude [-> ->>]))

(defn validate-thread-expr [expr]
  (when (> (count (filter (partial = '%) expr)) 1)
    (throw (IllegalArgumentException. "forms provided to thread macros must include at most one %"))))

(defmacro ->> [& forms]
  "Thread-last macro but forms may override the \"last\" behavior by including
   % to indicate where the previous form should be added."
  (letfn [(thread [inner outer]
            (let [[left right] (split-with (partial not= '%) outer)]
              (validate-thread-expr outer)
              (concat left [inner] (rest right))))]
    (reduce thread (first forms) (rest forms))))

(defmacro -> [& forms]
  "Thread-first macro but forms may override the \"first\" behavior by including
   % to indicate where the previous form should be added."
  (letfn [(thread [inner outer]
            (let [[left right] (split-with (partial not= '%) outer)]
              (validate-thread-expr outer)
              (if (seq right) ; outer contained a %
                (concat left [inner] (rest right))
                (concat [(first left) inner] (rest left)))))]
    (reduce thread (first forms) (rest forms))))
