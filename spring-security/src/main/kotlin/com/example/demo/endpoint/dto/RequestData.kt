package com.example.demo.endpoint.dto

data class RequestData(
    val firstName: String,
    val secondName: String,
    var public: Boolean?
)
