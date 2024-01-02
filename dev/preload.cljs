(ns preload
  (:require
   [app.styles :as styles]
   [cljss.core :as css]
   [uix.dev]))

(uix.dev/init-fast-refresh!)

(defn ^:dev/after-load refresh []
  (css/remove-styles!)
  (styles/inject-init-styles!)
  (uix.dev/refresh!))
