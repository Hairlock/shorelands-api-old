module User where


import Json.Decode as Json exposing ((:=))
-- import Models exposing (..)
import Task exposing (..)
import Effects exposing (..)
import Http


getUsers : Task Never Action
getUsers =
  let req = Task.map (\ul LoadData ul) (Http.get users "http://localhost:3000/api/users")
  in
    Task.onError req (\err -> Task.succeed (LoadData []))


users : Json.Decoder UserList
users =
  let user =
    Json.object3 User
      ("id" := Json.int)
      ("name" := Json.string)
      ("email" := Json.string)
  in
    "users" := Json.list users
