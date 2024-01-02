(ns app.main.core
  (:require
   [app.main.components.calendar :refer [calendar]]
   [app.main.state.context :as state.context]
   [app.styles]
   [app.utils.env :as env]
   [uix.core :as uix :refer [$ defui]]
   [uix.dom]))

;; Middleware ------------------------------------------------------------------

(def middleware
  (concat
   [state.context/local-storage-middleware]
   (when env/debug?
     [state.context/log-state-middleware])))

;; Hooks -----------------------------------------------------------------------

(defn reset-viewport-meta []
  (let [viewportmeta (js/document.querySelector "meta[name=\"viewport\"")]
    (when viewportmeta
     (.setAttribute viewportmeta "content" "width=device-width, minimum-scale=1.0, maximum-scale=1.0, initial-scale=1.0"
      (.setAttribute viewportmeta "content" "width=device-width, minimum-scale=1.0, initial-scale=1.0")))))

(defn use-reset-ios-zoom []
  (uix/use-effect
   (fn []
     (when (re-find #"(iPhone|iPad)" (.-userAgent js/navigator))
       (reset-viewport-meta)))
   []))

(defn use-dark-mode []
  (let [theme (-> (state.context/use)
                  (get-in [:config :theme] :light))]
    (uix/use-effect
     #(js/document.documentElement.setAttribute "data-theme" (str theme))
     [theme])))

;; Main ------------------------------------------------------------------------

(defui main []
  (use-reset-ios-zoom)
  ($ state.context/provider {:middleware middleware}
     (use-dark-mode)
     ($ calendar)))
