package com.example.demo.controller

data class RestResponse<T>(
        val success: Boolean,
        val data: T
)