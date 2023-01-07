package ru.sber.springsecurity

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApplicationTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private fun url(s: String): String {
        return "http://localhost:${port}/${s}"
    }

    @Test
    fun testAuthorSuccess() {
        var resp = restTemplate.getForObject(url("/api/message/author?name=admin"), String::class.java)
        assertTrue(resp.contains("Hello"))
        resp = restTemplate.getForObject<String>(url("/api/message/author?name=user"), String::class.java)
        assertTrue(resp.contains("Hello"))
    }

    @Test
    fun testAuthorFail() {
        var resp = restTemplate.getForObject(url("/api/message/author?name=noname"), String::class.java)
        assertTrue(resp.contains("[]"))
    }
}