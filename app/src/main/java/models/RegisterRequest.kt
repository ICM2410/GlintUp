package models

data class RegisterRequest(val name: String, val email:String,val password: String,val phone: String,val birthdate: String,val gender: String,val preferences: String, val prefered_distance: Int,val hobbies: List<Map<String, Any>>)
