package models

data class match(val users: List<String>, val createdAt: String)


data class matchResponse(val message: String, val match: match)