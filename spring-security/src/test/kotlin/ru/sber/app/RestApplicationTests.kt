package ru.sber.app

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.bind.annotation.PostMapping
import ru.sber.app.endpoint.dto.RequestData
import ru.sber.app.repository.AddressBookRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApplicationTests {
    @LocalServerPort
    private var port: Int = 0
    @Autowired
    private lateinit var addressBookRepository: AddressBookRepository
    @Autowired
    private lateinit var restTemplate: TestRestTemplate
    private val requestData = RequestData("Алексей","Алексеев","Алексеевка", true)

    private fun url(s: String): String {
        return "http://localhost:${port}/${s}"
    }

    @Test
    fun testFormAuthentication() {
        val res = restTemplate.getForEntity(url("/login"), String::class.java)
        assertEquals(res.statusCode, HttpStatus.OK)
    }

    @Test
    fun testAdd() {
        val res = restTemplate.exchange(
            url("/api/add"),
            HttpMethod.POST,
            HttpEntity(requestData,HttpHeaders()),
            String::class.java)
        assertEquals(HttpStatus.FOUND, res.statusCode)
    }

    @Test
    fun testList() {
        val res = restTemplate.exchange(
            url("/api/list"),
            HttpMethod.GET,
            HttpEntity(requestData,HttpHeaders()),
            String::class.java)
        assertEquals(HttpStatus.OK, res.statusCode)
        assertThat(res.body).isNotNull
    }

    @Test
    fun testView() {
        val res = restTemplate.exchange(
            url("/api/view/7"),
            HttpMethod.GET,
            HttpEntity(requestData,HttpHeaders()),
            String::class.java)
        assertEquals(HttpStatus.OK, res.statusCode)
        assertThat(res.body).isNotNull
    }

    @Test
    fun testEdit() {
        val res = restTemplate.exchange(
            url("/api/edit/7"),
            HttpMethod.PUT,
            HttpEntity(requestData,HttpHeaders()),
            String::class.java)
        assertEquals(HttpStatus.FOUND, res.statusCode)
    }

    @Test
    fun testDelete() {
        val res = restTemplate.exchange(
            url("/api/remove/7"),
            HttpMethod.DELETE,
            HttpEntity(requestData,HttpHeaders()),
            String::class.java)
        assertEquals(HttpStatus.FOUND, res.statusCode)
    }
}