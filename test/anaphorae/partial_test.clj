(ns anaphorae.partial-test
  (:refer-clojure :exclude [partial])
  (:require [clojure.test :refer :all]
            [anaphorae.partial :refer :all]))

(deftest test-partial
  (testing "single argument"
    (let [x2 (partial * 2)]
      (is (= 4 (x2 2)))))
  (testing "multiple arguments"
    (let [part (partial str "a")]
      (is (= "abc" (part "b" "c")))))
  (testing "single %"
    (let [part (partial str % "b" "c")]
      (is (= "abc" (part "a")))))
  (testing "multiple %s"
    (let [part (partial str % "b" % "d")]
      (is (= "abcd" (part "a" "c")))))
  (testing "%& captures the rest of the args"
    (let [part (partial str "a" %& "d")]
      (is (= "abcd" (part "b" "c"))))))

