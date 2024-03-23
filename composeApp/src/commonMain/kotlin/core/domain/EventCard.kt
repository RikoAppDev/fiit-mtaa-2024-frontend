package core.domain


data class Event(
    val id: String,
    val name: String,
    val createdAt: String, // Assuming CURRENT_TIMESTAMP will be converted to a Date object
    val happeningAt: String,
    val thumbnailURL: String,
    val locationLat: Double,
    val locationLon: Double,
    val capacity: Int,
    val salaryType: String,
    val toolingRequired: String,
    val toolingProvided: String,
    val status: String,
    val salary: String,
    val user: User
)

data class User(
    val id: String,
    val name: String,
    val avatarURL: String
)
