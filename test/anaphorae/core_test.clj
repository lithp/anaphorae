(ns anaphorae.core-test
  (:require [clojure.test :refer :all]
            [anaphorae.core :refer :all]))

(deftest partial%ing
  (testing "single argument"
    (let [x2 (partial% * 2)]
      (is (= 4 (x2 2)))))
  (testing "multiple arguments"
    (let [part (partial% str "a")]
      (is (= "abc" (part "b" "c")))))
  (testing "single %"
    (let [part (partial% str % "b" "c")]
      (is (= "abc" (part "a")))))
  (testing "multiple %s"
    (let [part (partial% str % "b" % "d")]
      (is (= "abcd" (part "a" "c")))))
  (testing "%& captures the rest of the args"
    (let [part (partial% str "a" %& "d")]
      (is (= "abcd" (part "b" "c"))))))

(deftest ->>%ing
  (testing "acts like ->> in absence of %"
    (is (= 0 (->>% 10
                   (+ 10)
                   (- 20)))))
  (testing "% overrides last behavior"
    (is (= 0 (->>% +
                   (% 5 5)
                   (- % 5)
                   (- 5 %))))))

(deftest ->%ing
  (testing "acts like -> in absence of %"
    (is (= 2 (->% 12
                  (- 10))))
  (testing "% overrides regular thread order"
    (is (= 0 (->% +
                  (% 5 5)
                  (- % 5)
                  (- 5 %)))))))
