(ns app.main.state.events
  (:require
   [clojure.set :as set]
   [tick.locale-en-us]
   [tick.core :as t]))

;; Initial ---------------------------------------------------------------------

(def initial {:workout {:events #{}}})

;; Utils -----------------------------------------------------------------------

(def date-formatter (t/formatter "yyyy-MM-dd"))

(def parse-date #(t/parse-date % date-formatter))

;; Getters ---------------------------------------------------------------------

(defn days-with-events
  "Get set of all days with events of any type."
  [{:keys [events] :as _state}]
  (transduce (comp (map val)
                   (map :events))
             set/union #{}
             events))

;; Setters ---------------------------------------------------------------------

(defn save
  "Save `event` to coll in `:events` state under `key` with a `day`
  Day should be in day format."
  [state [key day]]
  (update-in state [:events key :events] (fnil conj #{}) day))

(defn toggle
  "Toggle and save `event` to coll in `:events` state under `key` with a `day`
  Day should be in day format."
  [state [key day :as payload]]
  (if (get-in state [:events key :events day])
    (update-in state [:events key :events] (fnil disj #{}) day)
    (save state payload)))
