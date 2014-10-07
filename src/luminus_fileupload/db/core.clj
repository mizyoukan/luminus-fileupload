(ns luminus-fileupload.db.core
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [luminus-fileupload.db.schema :as schema]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc])
  (:import [java.io File FileInputStream]))

(defdb db schema/db-spec)

(defentity files)

(def ^:private ^:const save-file-sql
  "INSERT INTO files (name, data, content_type, size, comment, created) VALUES (?, ?, ?, ?, ?, NOW())")

(defn save-file [filename tempfile content-type commen]
  (with-open [fis (FileInputStream. tempfile)]
    (let [filesize (.length tempfile)]
      (jdbc/with-db-transaction [t-con schema/db-spec]
        (with-open [ps (.prepareStatement (t-con :connection) save-file-sql)]
          (doto ps
            (.setString 1 filename)
            (.setBinaryStream 2 fis filesize)
            (.setString 3 content-type)
            (.setInt 4 filesize)
            (.setString 5 commen)
            .executeUpdate))))))

(defn list-files []
  (select files
          (fields :id :name :size :comment :created)
          (order :created :desc)))

(def ^:private ^:const get-file-sql
  "SELECT name AS filename, data, content_type, size FROM files WHERE id = ?")

(defn get-file-fn [id file-fn]
  (jdbc/with-db-transaction [t-con schema/db-spec]
    (jdbc/query t-con
                [get-file-sql id]
                :row-fn file-fn
                :result-set-fn first)))

(defn delete-file [id]
  (delete files (where {:id id})))
