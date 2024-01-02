(ns app.core
  (:require
   [app.main.core :as game]
   [tick.locale-en-us]
   [app.styles]
   [uix.core :as uix :refer [$ defui]]
   [uix.dom]))

(defui app []
  ($ game/main))

(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (uix.dom/render-root ($ app) root))

(defn ^:export init []
  (render))
