(ns app.utils.date
  (:require
   [tick.core :as t]))

(def days-of-week
  [t/MONDAY
   t/TUESDAY
   t/WEDNESDAY
   t/THURSDAY
   t/FRIDAY
   t/SATURDAY
   t/SUNDAY])

(defn shift-until
  "Recursively shift `date` by a `temporal-unit` using `shift-fn` until `pred` is met.

  Example:
  Shift date to the last monday
  (shift-until (t/new-date 2023 12 1) :days #(= (t/day-of-week %) t/MONDAY) t/<<)
  ;; => #time/date 2023-11-27"
  ([date temporal-unit pred shift-fn]
   (shift-until date temporal-unit pred shift-fn 0))
  ([date temporal-unit pred shift-fn i]
   (let [next-date (shift-fn date (t/new-period 1 temporal-unit))]
     (cond
       (= i 1000) (js/console.error "Shift-until iteration limit reached")
       (pred next-date) next-date
       :else (recur next-date temporal-unit pred shift-fn (inc i))))))

(defn day-of-week-eq [date dow]
  (= (t/day-of-week date) dow))

(def monday? #(day-of-week-eq % t/MONDAY))
(def tuesday? #(day-of-week-eq % t/TUESDAY))
(def wednesday? #(day-of-week-eq % t/WEDNESDAY))
(def thursday? #(day-of-week-eq % t/THURSDAY))
(def friday? #(day-of-week-eq % t/FRIDAY))
(def saturday? #(day-of-week-eq % t/SATURDAY))
(def sunday? #(day-of-week-eq % t/SUNDAY))

(defn monday-of-week [date]
  (shift-until date :days monday? t/<<))

(defn sunday-of-week [date]
  (shift-until date :days sunday? t/>>))
