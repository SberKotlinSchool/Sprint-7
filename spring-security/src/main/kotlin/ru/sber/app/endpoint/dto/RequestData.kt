package ru.sber.app.endpoint.dto

data class RequestData(
    val firstName: String,
    val lastName: String,
    val city: String,
    var public: Boolean?
)
