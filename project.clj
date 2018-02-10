(defproject app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :jvm-opts ["--add-modules" "java.xml.bind"]
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.omcljs/om "1.0.0-alpha34"]
                 [figwheel-sidecar "0.5.0-SNAPSHOT" :scope "test"]]
  :aliases {"start" ["run" "-m" "clojure.main" "script/figwheel.clj"]})
