(ns anaphorae.thread-test
  (:refer-clojure :exclude [-> ->>])
  (:require [clojure.test :refer :all]
            [anaphorae.thread :refer :all]))

(deftest test->>
  (testing "acts like ->> in absence of %"
    (is (= 1 (->> 10
                   (+ 9)
                   (- 20)))))
  (testing "% overrides last behavior"
    (is (= 1 (->> +
                   (% 5 5)
                   (- % 4)
                   (- 7 %))))))

(deftest test->%
  (testing "acts like -> in absence of %"
    (is (= 2 (-> 12
                  (- 10))))
  (testing "% overrides regular thread order"
    (is (= 1 (-> +
                  (% 5 5)
                  (- % 4)
                  (- 7 %)))))))
