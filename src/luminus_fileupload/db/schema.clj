(ns luminus-fileupload.db.schema
  (:require [clojure.java.jdbc :as sql]
            [noir.io :as io]))

(def db-store "site.db")

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname (str (io/resource-path) db-store)
              :user "sa"
              :password ""
              :make-pool? true
              :naming {:keys clojure.string/lower-case
                       :fields clojure.string/upper-case}})
(defn initialized?
  "checks to see if the database schema is present"
  []
  (.exists (new java.io.File (str (io/resource-path) db-store ".mv.db"))))

(defn create-files-table
  []
  (sql/db-do-commands
    db-spec
    (sql/create-table-ddl
      :files
      [:id :INT "PRIMARY KEY AUTO_INCREMENT"]
      [:name "VARCHAR(256) NOT NULL"]
      [:data :BLOB "NOT NULL"]
      [:content_type "VARCHAR(128) NOT NULL"]
      [:size :INT "NOT NULL"]
      [:comment "VARCHAR(256)"]
      [:created :DATETIME "NOT NULL"])))

(defn create-tables
  "creates the database tables used by the application"
  []
  (create-files-table))
