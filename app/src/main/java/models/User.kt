package models

data class User(val name: String, val birthdate: String, val gender: String, val profile_picture: List<String>)

data class proximityResponse(val users: List<User>)