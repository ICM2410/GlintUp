package models

data class User(val name: String, val birthdate: String, val gender: String, val preferences: String, val hobbies: List<String>, val profile_picture: List<String>)

data class User_list(val List: List<User>)