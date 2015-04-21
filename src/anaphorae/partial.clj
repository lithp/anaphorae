(ns anaphorae.partial
  (:refer-clojure :exclude [partial]))

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

          :default
          (recur (conj accum (first left)) (rest left) right))))

(defmacro partial [f & orig]
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
