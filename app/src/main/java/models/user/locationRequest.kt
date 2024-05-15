package models.user

data class locationRequest(
    val location: Location
) {
    data class Location(
        val type: String = "Point", // Default value set to "Point"
        val coordinates: List<Double> // Array of doubles for coordinates
    )
}