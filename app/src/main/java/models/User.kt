package models

data class User(val name: String, val birthdate: String, val gender: String, val profile_picture: List<String>, val _id: String)

data class proximityResponse(val users: List<User>)


data class chatResponse(val users: List<User>, val chats: chats)