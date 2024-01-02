(ns app.main.components.calendar
  (:require
   [app.main.state.context :as state.context]
   [app.main.state.events :as events]
   [app.utils.date :as utils.date]
   [cljss.core :refer-macros [defstyles]]
   [tick.core :as t]
   [uix.core :as uix :refer [$ defui]]))

(defstyles wrapper-css []
  {:padding "30px"})

(defstyles streak-css []
  {:text-align "center"})

(defstyles grid-css []
 {:max-width "400px"
  :text-align "center"
  :padding 0
  :flex-grow 1
  :display "grid"
  :grid-template-columns "repeat(7, 1fr)"
  :grid-gap "16px"
  :list-style-type "none"})

(defstyles bullet-css [active? today? pad-day?]
  {:aspect-ratio "1"
   :border-radius "100%"
   :opacity (if pad-day? 0.3 1)
   :background-color (cond
                       active? "var(--theme-bullet-active)"
                       pad-day? "var(--theme-bullet-padded)"
                       :else "var(--theme-bullet-inactive)")
   :box-shadow (when today? "inset 0 0 0 3px var(--theme-bg), 0 0 0 2px var(--theme-bullet-inactive)")})

(defn days-month [date]
  (let [first-of-month (t/first-day-of-month date)
        last-of-month (t/last-day-of-month date)]
    {:left-pad (t/range (utils.date/monday-of-week first-of-month) first-of-month)
     :days (t/range first-of-month last-of-month)
     :right-pad (t/range (utils.date/sunday-of-week last-of-month) last-of-month)}))

(defui bullet [{:keys [date days-with-events
                       today pad-day?
                       on-click]
                :or {days-with-events #{}}}]
  (let [key (t/format "YYYY-MM-dd" date)
        today? (= today date)]
    ($ :li {:data-date key
            :class (bullet-css (days-with-events date) today? (some? pad-day?))
            :on-click #(on-click date)})))

(defui grid-days [{:keys [days] :as props}]
  (for [date days]
    ($ bullet (assoc props
                     :key (t/format "YYYY-MM-dd" date)
                     :date date))))

(defn days-streak [date events]
  (when (events date)
    (let [from (utils.date/shift-until date :days #(nil? (events %)) t/<<)
          days (t/range from date)]
      (count days))))

(defui calendar [{:keys []}]
  (let [{:keys [state dispatch] :as _ctx} (state.context/use)
        days-with-events (events/days-with-events state)
        today (t/date (t/now))
        current-streak (days-streak today days-with-events)
        {:keys [days left-pad right-pad]} (days-month today)]
    ($ :div {:class (wrapper-css)}
       ($ :div
          {:class (streak-css)}
          "Current streak: "
          ($ :b (or current-streak 0) " days"))
       ($ :ol {:class (grid-css)}
          (for [d utils.date/days-of-week]
            ($ :li
               {:key d}
               (take 3 (str d))))
          ($ grid-days {:days left-pad
                        :pad-day? true})
          ($ grid-days {:today today
                        :days days
                        :days-with-events days-with-events
                        :on-click #(dispatch [:event/toggle [:drinks %]])})
          ($ grid-days {:days right-pad
                        :pad-day? true})))))
