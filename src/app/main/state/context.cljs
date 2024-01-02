(ns app.main.state.context
  (:require
   [app.main.repl :as repl]
   [app.main.state.config :as config]
   [app.main.state.events :as events]
   [clojure.data :refer [diff]]
   [clojure.edn :as edn]
   [uix.core :as uix :refer [$ defui]]
   [uix.dom])
  (:refer-clojure :exclude [use]))

;; Initial State ---------------------------------------------------------------

(defn initial []
  {:config config/initial
   :events events/initial})

;; Middleware ------------------------------------------------------------------

(defn apply-middleware!
  "Apply (side-effectful) middleware to the state in `middleware`
  A middleware fn would look like this:
  (middleware `prev-state` `next-state`)
  When a middleware fn returns `nil` (just a side-effect) we keep the accumulated state."
  [prev-state next-state middlewares]
  (reduce
   (fn [state-acc {:keys [update]}]
     (or (update prev-state state-acc)
         state-acc))
   next-state middlewares))

(defn get-middleware [middleware]
  (when-let [get-fn (->> middleware
                         (filter :get)
                         (first)
                         :get)]
    (get-fn)))

;; Local storage persist

(def local-storage-key "habits-tracker")

(defn update-local-storage! [_prev-state state]
  (js/localStorage.setItem local-storage-key (str state)))

(defn parse-persistence-layer [string]
  (-> (edn/read-string {:readers {'time/date events/parse-date}} string)))

(defn get-local-storage! []
  (try
    (->> (js/localStorage.getItem local-storage-key)
         (parse-persistence-layer))
    (catch js/Error e
      (js/console.error "Could not read state from local-storage" (js/localStorage.getItem local-storage-key) e))))

(defn remove-local-storage! []
  (js/localStorage.removeItem local-storage-key))

(def local-storage-middleware
 {:get get-local-storage!
  :update update-local-storage!})

;; State logging

(defn log-state [prev-state state]
  (when (not= prev-state state)
    (js/console.log
     "State update\n"
     "prev:" prev-state "\n"
     "next:" state "\n"
     "diff" (let [[prev-diff next-diff _] (diff prev-state state)]
              (if (and (not prev-diff) (not next-diff))
                ":same-state"
                [prev-diff next-diff])))))

(def log-state-middleware
  {:update log-state})

;; Reducer ---------------------------------------------------------------------

(def context (uix/create-context (initial)))

(def context-provider (.-Provider context))

(defn new-game [_ _]
  (initial))

(def local-storage {:get get-local-storage!
                    :set update-local-storage!})

(def no-persist {:get (constantly nil)
                 :set identity})

(def reducer-map
  {:event/toggle events/toggle})

(defn reducer [state [action payload] & {:keys [middleware]}]
  (if-let [f (get reducer-map action)]
    (let [updated-state (f state payload)]
      (apply-middleware! state updated-state middleware))
    (do
      (js/console.error (str "No action found " action) payload)
      state)))

(defui provider [{:keys [children middleware]
                  :or {middleware []}}]
  (let [[state dispatch] (uix/use-reducer #(reducer %1 %2 {:middleware middleware})
                                          (-> (or (get-middleware middleware)
                                                  (initial))))]
    (repl/update-state-atom! state dispatch)
    ($ context-provider {:value {:state state
                                 :dispatch dispatch}}
       children)))

(defn use []
  (if-let [ctx (uix/use-context context)]
    ctx
    (throw (js/Error. "useMyContext must be used within a MyProvider"))))
