module UserList where


import Html exposing (..)
import Html.Attributes exposing (..)
import Debug exposing (log)
import Models exposing (..)


userItem : User -> Html
userItem user =
  li [] [
      div [ class "user-item" ] [
          p [] [ text (toString user.username) ],
          p [] [ text (toString user.email) ]
      ]
  ]


userList : UserList -> Html
userList users =
  section [ class "user-list" ] [
      ul [] (List.map userItem users)
  ]
