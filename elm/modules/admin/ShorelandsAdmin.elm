module ShorelandsAdmin where


import Task exposing (..)
import Effects exposing (..)
import Html exposing (..)
import Html.Attributes exposing (..)
import Signal exposing (Address)
import StartApp as StartApp
import User exposing (..)
import Models exposing (..)
import UserList
import Debug exposing (log)


-- MODEL
initialModel : Model
initialModel =
  Model []


-- UPDATE
update : Action -> Model -> (Model, Effects Action)
update action model =
  case action of
    NoOp ->
      (model, Effects.none)
    LoadUsers users ->
      let logger = log "users" users
      in
          ({model | users <- users}, Effects.none)


-- VIEW
view : Address Action -> Model -> Html
view address model =
  div [] [
      section [ class "content" ] [
          (UserList.userList model.users)
      ]
  ]


-- APP
app =
    StartApp.start
    { init = (initialModel, Effects.task getUsers),
      view = view,
      update = update,
      inputs = [] }


main =
    app.html


port tasks : Signal (Task.Task Never ())
port tasks = app.tasks
