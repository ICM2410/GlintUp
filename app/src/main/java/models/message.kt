package models

data class messageRequest(val chat: String, val content: String)

data class message(val sender: String, val content: String, val createdAt: String)

data class chatMessages(val messages: List<message>)