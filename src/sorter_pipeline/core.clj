(ns sorter-pipeline.core
  (:require
    [sorter-pipeline.pipeline :as pipeline]
    [sorter-pipeline.ui-selection :as ui-selection]
    [org.httpkit.server :as http-kit]
    [lambdacd.runners :as runners]
    [lambdacd.core :as lambdacd]
    [clojure.tools.logging :as log]
    [lambdacd-git.core :as lambdacd-git]
    [ring.middleware.basic-authentication :refer [wrap-basic-authentication]])

  (:import
   (java.nio.file.attribute FileAttribute)
   (java.nio.file Files LinkOption))
  (:gen-class))


(defn- create-temp-dir []
  (str (Files/createTempDirectory "lambdacd" (into-array FileAttribute []))))

(defn authenticated? [name pass]
  (and (= pass "ToHiprAW3vAaVW") (= name "Operate6993")))

(defn -main [& args]
  (let [;; the home dir is where LambdaCD saves all data.
        ;; point this to a particular directory to keep builds around after restarting
        home-dir (create-temp-dir)
        config {:home-dir home-dir
                :name     "sorter pipeline"
                :git {:ssh {:use-agent true
                            :known-hosts-files ["/root/.ssh/known_hosts"]
 		            :strict-host-key-checking "no"
                            }}}
        ;; initialize and wire everything together
        pipeline (lambdacd/assemble-pipeline pipeline/pipeline-def config)
        ;; create a Ring handler for the UI
        app      (ui-selection/ui-routes pipeline)]
    (log/info "LambdaCD Home Directory is " home-dir)
    ;; this starts the pipeline and runs one build after the other.
    ;; there are other runners and you can define your own as well.
    (runners/start-one-run-after-another pipeline)
    ;; start the webserver to serve the UI
    (http-kit/run-server (wrap-basic-authentication app authenticated?) {:port 8081})))
