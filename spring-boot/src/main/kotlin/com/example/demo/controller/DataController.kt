package com.example.demo.controller

import com.example.demo.persistance.DataEntity
import com.example.demo.persistance.DataEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/app")
@RestController
class DataController {
    @Autowired
    private lateinit var repository: DataEntityRepository

    @GetMapping("/data")
    fun getData(@RequestParam id: Long): ResponseEntity<DataEntity> {
        val dataEntity = repository.getById(id)
        return ResponseEntity.ok(dataEntity)
    }
}