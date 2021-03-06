(ns sorter-pipeline.pipeline
  (:use [lambdacd.steps.control-flow]
        [sorter-pipeline.steps])
  (:require
        [lambdacd.steps.manualtrigger :as manualtrigger]))

(def pipeline-def
  `(
    (either
     manualtrigger/wait-for-manual-trigger
     wait-for-repo)
    (with-workspace
      clone
      build-frontend
      check-js-assets
      lein-build
      deploy
      kill-old-proc
      run-new-proc)))

