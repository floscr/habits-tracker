(ns app.main.state.events-test
  (:require
   [app.main.state.events :as sut]
   [cljs.test :refer [deftest is testing]]
   [tick.core :as t]))

(deftest events-getters-test
  (testing "Display all events"
    (let [[day-1 day-2 :as days] [(t/new-date 2023 12 1)
                                  (t/new-date 2023 12 2)]
          state {:events {:workout {:events #{day-1}}
                          :meat {:events #{day-1 day-2}}}}]
      (is (= (sut/days-with-events state) (set days))))))

(deftest events-setters-toggle
  (testing "Setters: Toggle date"
    (let [day-1 (t/new-date 2023 12 1)
          state {:events {:workout {:events #{day-1}}}}]
      (sut/toggle state [:workout day-1]))))

(deftest events-setters-save
  (testing "Setters: Save to non existing collection"
    (let [day (t/new-date 2023 12 1)]
      (is (= (sut/save {} [:workout (t/new-date 2023 12 1)])
             {:events {:workout {:events #{day}}}}))))

  (testing "Setters: Save to non existing collection"
    (let [day-1 (t/new-date 2023 12 1)
          day-2 (t/new-date 2023 12 2)
          state {:events {:workout {:events #{day-1}}}}]
      (is (= (-> (sut/save state [:workout day-1])
                 (sut/save [:workout day-2]))
             {:events {:workout {:events #{day-1 day-2}}}})))))
