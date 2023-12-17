package com.example.demo.controller

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var repository: EntityRepository

    private fun url(s: String) = "http://localhost:${port}/${s}"

    @Test
    fun hello() {
        //given
        val expect = "Hello!"
        //when
        val resp = restTemplate.exchange(
            url("/hello"),
            HttpMethod.GET,
            HttpEntity(null, null),
            String::class.java
        )
        //then
        assertEquals(resp.statusCode, HttpStatus.OK)
        assertEquals(resp.body, expect)
    }

    @Test
    fun entityTest() {
        //given
        val entity = repository.save(Entity(null, "message"))
        //when
        val resp = restTemplate.exchange(
            url("${entity.id}/view"),
            HttpMethod.GET,
            HttpEntity(null, null),
            Entity::class.java
        )
        //then
        assertEquals(resp.statusCode, HttpStatus.OK)
        assertEquals(resp.body, entity)
    }
}