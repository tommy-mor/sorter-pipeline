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
      some-step-that-echos-foo)))
