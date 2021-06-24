(defproject authorizer "0.1.0-SNAPSHOT"
  :description "Authorizer: test case for NDA company"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "2.3.1"]
                 [clojure.java-time "0.3.2"]]
  :main ^:skip-aot authorizer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
