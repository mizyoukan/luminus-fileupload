(ns luminus-fileupload.routes.home
  (:require [compojure.core :refer :all]
            [luminus-fileupload.layout :as layout]
            [luminus-fileupload.util :as util]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
