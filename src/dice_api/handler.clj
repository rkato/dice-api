(ns dice-api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))


(s/defschema Result
  {:total s/Int
   :roll [s/Int]})


(defn roll-dice [n s]
  (let [roll (take n (repeatedly #(inc (rand-int s))))]
    {:total (reduce + roll)
     :roll roll}))

(s/defschema Request
  {:num s/Int
   :sides s/Int})

(def app
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "Dice-api"
                   :description "Compojure Api example"}
            :tags [{:name "api", :description "some apis"}]}}}

   (context "/api" []
            :tags ["api"]

            (POST "/roll" []
                  :body [{:keys [num sides]} Request]
                  :return Result
                  :summary "Given a correct request body with keys :num and :sides, returns result of roll"
                  (ok (roll-dice num sides)))

            (GET "/roll/:n/:s" []
                 :path-params [n :- s/Int
                               s :- s/Int]
                 :return Result
                 :summary "Rolls :n dice of sides :s"
                 (ok (roll-dice n s)))
            
            (GET "/roll" []
                 :return Result
                 :summary "Rolls a six sided die"
                 (ok  (roll-dice 1 6) ))

            )))
