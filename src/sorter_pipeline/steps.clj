(ns sorter-pipeline.steps
  (:require [lambdacd.steps.shell :as shell]
            [lambdacd-git.core :as lambdacd-git]))

(def repo-uri "git@github.com:tommy-mor/dtorter.git")
(def repo-branch "main")

(defn wait-for-repo [args ctx]
  (lambdacd-git/wait-for-git ctx repo-uri :ref (str "refs/heads/" repo-branch)))

(defn clone [args ctx]
  (let [revision (:revision args)
        cwd (:cwd args)
        ref (or revision repo-branch)]
    (lambdacd-git/clone ctx repo-uri ref cwd)))

(defn print-step [args ctx]
  {:status :success
   :details {:label "epic"
             :raw (str (prn-str args)
                       "--"
                       (prn-str ctx))}})

(defn lein-test [{cwd :cwd} ctx]
  (shell/bash ctx cwd "lein test"))

(defn lein-build [{cwd :cwd} ctx]
  (shell/bash ctx cwd "lein uberjar"))

(defn deploy [{cwd :cwd} ctx]
  (shell/bash ctx (str cwd "/target") "cp *-standalone.jar /root/sorter.jar && java -jar /root/sorter.jar &"))





(defn some-step-that-does-nothing [args ctx]
  {:status :success})

(defn some-step-that-echos-foo [args ctx]
  (shell/bash ctx "/" "echo foo"))

(defn some-step-that-echos-bar [args ctx]
  (shell/bash ctx "/" "echo bar"))

(defn some-failing-step [args ctx]
  (shell/bash ctx "/" "echo \"i am going to fail now...\"" "exit 1"))
