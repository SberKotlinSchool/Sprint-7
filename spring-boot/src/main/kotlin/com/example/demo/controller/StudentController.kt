package com.example.demo.controller

import com.example.demo.persistance.Student
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/students")
class StudentController {
    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getStudents() = listOf(Student(1, "Петя", "1пр"), Student(2, "Вася", "3пр"))


}