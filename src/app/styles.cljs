(ns app.styles
  (:require
   [cljss.core :refer-macros [inject-global] :as css]))

(defn inject-init-styles! []
  (inject-global
   {":root"
    {"--bp-medium" "460px"
     "--theme-bg" "#FFF"
     "--theme-bullet-inactive" "rgb(77 97 125 / 34%)"
     "--theme-bullet-active" "rgb(52 176 138)"
     "--theme-bullet-padded" "black"
     "--theme-text" "#374151"}
    "html[data-theme=':dark']"
    {"--theme-bg" "#1f2937"
     "--theme-bullet-inactive" "rgb(77 97 125 / 34%)"
     "--theme-bullet-active" "rgb(52 176 138)"
     "--theme-bullet-padded" "black"
     "--theme-text" "rgb(102 126 153)"}
    :html
    {:box-sizing "border-box"}
    "html *" {:box-sizing "inherit"
              :user-select "none"
              "-webkit-user-select" "none"}
    "#root" {:width "100%"
             :height "100%"
             :display "flex"
             :justify-content "center"
             :align-items "start"}
    :body {:display "flex"
           :background-color "var(--theme-bg)"
           :justify-content "center"
           :margin 0
           :width "100vw"
           :height "100vh"
           :overflow "hidden"
           :color "var(--theme-text)"
           :font-family "-apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, Helvetica, Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\""
           :letter-spacing 0
           :line-height 1.4
           :text-rendering "optimizeLegibility"
           "-webkit-font-smoothing" "antialiased"
           "-moz-osx-font-smoothing" "grayscale"
           "-moz-font-feature-settings" "\"liga\" on"
           :touch-action "none"}
    "p" {:margin 0}
    "::-webkit-scrollbar" {:width "20px"}
    "::-webkit-scrollbar-track" {:border-radius "100px"
                                 :background-color "rgba(0,0,0,0.3)"}
    "::-webkit-scrollbar-thumb" {:border-radius "100px"
                                 :border "5px solid transparent"
                                 :background-clip "content-box"
                                 :background-color "var(--color-background)"}
    "button" {:all "unset"
              :outline "revert"
              :cursor "pointer"}}))

(inject-init-styles!)
