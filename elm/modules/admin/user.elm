module User where


import Json.Decode as Json exposing ((:=))
import Models exposing (..)
import Task exposing (..)
import Effects exposing (..)
import Http exposing (..)
import Html exposing (..)
import Debug exposing (log)


getUsers : Task Never Action
getUsers =
 let req = Task.map (\ul -> LoadUsers ul) (Http.get users "http://localhost:3000/api/users")
     --succ = case (Task.toResult req) of
       --         Err err -> log err
         --       Ok hotels -> req
 in
   Task.onError req (\err -> Task.succeed (LoadUsers []))




users : Json.Decoder UserList
users =
 let user =
      Json.object3 User
        ("id" := Json.float)
        ("username" := Json.string)
        ("email" := Json.string)
 in
   Json.list user
