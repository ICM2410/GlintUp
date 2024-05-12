package models.user

data class locUser(
    val _id: String,
    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val cc: String,
    val available: Boolean,
    val __v: Int,
    val latitude: String,
    val longitude: String,
    val profile_picture: String
)
