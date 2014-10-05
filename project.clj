(defproject
  luminus-fileupload
  "0.1.0-SNAPSHOT"
  :description
  "FIXME: write description"
  :ring
  {:handler luminus-fileupload.handler/app,
   :init luminus-fileupload.handler/init,
   :destroy luminus-fileupload.handler/destroy}
  :plugins
  [[lein-ring "0.8.12"] [lein-environ "0.5.0"] [lein-ancient "0.5.5"]]
  :url
  "http://example.com/FIXME"
  :profiles
  {:uberjar {:aot :all},
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}},
   :dev
   {:dependencies
    [[ring-mock "0.1.5"]
     [ring/ring-devel "1.3.1"]
     [pjstadig/humane-test-output "0.6.0"]],
    :injections
    [(require 'pjstadig.humane-test-output)
     (pjstadig.humane-test-output/activate!)],
    :env {:dev true}}}
  :jvm-opts
  ["-server"]
  :dependencies
  [[log4j
    "1.2.17"
    :exclusions
    [javax.mail/mail
     javax.jms/jms
     com.sun.jdmk/jmxtools
     com.sun.jmx/jmxri]]
   [prone "0.6.0"]
   [noir-exception "0.2.2"]
   [markdown-clj "0.9.54"]
   [com.taoensso/timbre "3.3.1"]
   [com.h2database/h2 "1.4.181"]
   [com.taoensso/tower "3.0.1"]
   [lib-noir "0.9.0"]
   [korma "0.4.0"]
   [selmer "0.7.1"]
   [org.clojure/clojure "1.6.0"]
   [environ "1.0.0"]
   [ring-server "0.3.1"]
   [im.chit/cronj "1.4.2"]]
  :repl-options
  {:init-ns luminus-fileupload.repl}
  :min-lein-version "2.0.0")