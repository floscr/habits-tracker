#!/usr/bin/env bb
(ns deploy
  (:require
   [babashka.fs :as fs]
   [babashka.process :as bp]
   [clojure.edn :as edn]))

(defn find-parent-with
  "Traverse updward from a `path` until `pred` function returns true."
  [pred path]
  (cond
    (pred path) path
    (fs/parent path) (find-parent-with pred (fs/parent path))
    :else nil))

(defn find-git-root
  "Find git root directory for `path`.
  Returns nil when not found.
  Ignores worktree roots."
  [path]
  (find-parent-with #(fs/directory? (fs/path % ".git")) path))

(def src (fs/path (find-git-root *file*) "public"))

(def dest (-> (slurp "env.edn")
              (edn/read-string)
              :deploy-path))

(let [src (fs/path src)
      dst (fs/path dest)]
  (bp/shell ["chmod -R u=rwX,go=rX" src])
  (println
   (:out (bp/shell ["rsync" "-ravE" (str src "/") (str dst)] {:out :string}))))
