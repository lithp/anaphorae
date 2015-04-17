(ns anaphorae.core)

(defn make-args [left right]
  "Left is a list of forms, which might include % and %&.
   Right is a list of forms to be spliced in."
  (loop [accum [], left left, right right]
    (cond (not (seq left))
          (concat accum right)

          (= '%& (first left)) ; splice in the rest of right
          (concat accum right (rest left))

          (= '% (first left)) 
          (recur (conj accum (first right)) (rest left) (rest right))

          :default (recur (conj accum (first left)) (rest left) right))))

(defmacro partial% [f & orig]
  "Like partial except the orig list of args may include % in place of
   an actual argument. When the function partial% returns is invoked the
   additional arguments provided are first used to fill in any %s in orig,
   then the remaining additional args are added to the end of orig, just like
   partial.

   % refers to a single argument
   %& refers to all remaining arguments
   Support for %n might come later."
  `(fn [& args#]
      (apply ~f (make-args '~orig args#))))

(defn validate-thread-expr [expr]
  (when (> (count (filter (partial = '%) expr)) 1)
    (println expr)
    (throw (IllegalArgumentException. "forms provided to thread macros must include at most one %"))))

(defmacro ->>% [& forms]
  "Thread-last macro but forms may override the \"last\" behavior by including
   % to indicate where the previous form should be added.
  "
  (letfn [(thread [inner outer]
            "Replace the % in outer with inner.
             If % does not exist, place inner at the end of outer."
            (let [[left right] (split-with (partial not= '%) outer)]
              (validate-thread-expr outer)
              (concat left [inner] (rest right))))]
    (reduce thread (first forms) (rest forms))))

(defmacro ->% [& forms]
  "Thread-first macro but forms may override the \"first\" behavior by including
   % to indicate where the previous form should be added.
  "
  (letfn [(thread [inner outer]
            (let [[left right] (split-with (partial not= '%) outer)]
              (validate-thread-expr outer)
              (if (seq right) ; outer contained a %
                (concat left [inner] (rest right))
                (concat [(first left) inner] (rest left)))))]
    (reduce thread (first forms) (rest forms))))
