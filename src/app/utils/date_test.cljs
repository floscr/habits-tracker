(ns app.utils.date-test
  (:require
   [cljs.test :refer [deftest is testing]]
   [app.utils.date :as sut]
   [tick.core :as t]))

(deftest shift-days
  (testing "Shift to monday of week."
    (is (= (sut/monday-of-week (t/new-date 2023 12 1)) #time/date "2023-11-27")))
  (testing "Shift to sunday of week."
    (is (= (sut/sunday-of-week (t/new-date 2023 12 1)) #time/date "2023-12-03"))))
