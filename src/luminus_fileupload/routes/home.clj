(ns luminus-fileupload.routes.home
  (:require [compojure.core :refer :all]
            [luminus-fileupload.layout :as layout]
            [luminus-fileupload.db.core :as db]
            [clojure.java.io :as io]
            [ring.util.response :as ring]
            [noir.response :as resp]
            [noir.validation :as vali])
  (:import [java.io File]))

(defn home-page [& [filename commen]]
  (layout/render "home.html" {:files (db/list-files)
                              :file filename
                              :comment commen
                              :file-error (vali/on-error :file first)
                              :comment-error (vali/on-error :comment :first)}))

(defn about-page []
  (layout/render "about.html"))

(defn valid? [filename tempfile commen]
  (vali/rule (vali/has-value? filename)
             [:file "file is not specified"])
  (vali/rule (vali/max-length? filename 256)
             [:file "file name is less than or equal to 256 byte"])
  (vali/rule (vali/max-length? commen 256)
             [:comment "comment is less than or equal to 256 byte"])
  (not (vali/errors? :file :comment)))

(defn upload-file [filename tempfile content-type commen]
  (try
    (if (valid? filename tempfile commen)
      (do
        (db/save-file filename tempfile content-type commen)
        (resp/redirect "/"))
      (home-page filename commen))
    (catch Exception ex
      (vali/rule false [:file (.getMessage ex)])
      (home-page))
    (finally
      (when-not (nil? tempfile)
        (io/delete-file tempfile)))))

(defn download-file [id]
  (db/get-file-fn id
                  (fn [{:keys [filename data content_type size]}]
                    (-> (ring/response (.getBinaryStream data))
                        (ring/content-type content_type)
                        (ring/header "Content-Disposition" (format "inline; filename=\"%s\"" filename))
                        (ring/header "Content-Length" size)))))

(defn delete-file [id]
  (db/delete-file id)
  (resp/redirect "/"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (POST "/upload" {{{filename :filename tempfile :tempfile content-type :content-type} :file
                    commen :comment} :params}
        (upload-file filename tempfile content-type commen))
  (GET "/download" {{id :id} :params}
       (download-file id))
  (GET "/delete" {{id :id} :params}
       (delete-file id)))
