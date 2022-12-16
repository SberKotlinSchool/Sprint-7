package com.sbuniver.homework

import com.sbuniver.homework.dto.AddressBook
import com.sbuniver.homework.dto.AddressDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.URI


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
internal class ApiControllerTest {

    @LocalServerPort
    private val port: Int = 0

    lateinit var apiUrl: String

    val restTemplate = RestTemplate()
    val addressBook = AddressBook()

    @BeforeEach
    fun prepare() {
        apiUrl = "http://localhost:$port/api"
    }

    fun createHeaders(username: String, password: String): HttpHeaders? {
        val credentials = LinkedMultiValueMap<String, String>().apply {
            this.add("username", username)
            this.add("password", password)
        }

        val loginResponse = restTemplate.postForEntity(
            "http://localhost:$port/login",
            HttpEntity(credentials, HttpHeaders()),
            String::class.java
        )

        val authCookie = loginResponse.headers["Set-Cookie"]?.get(0)

        return HttpHeaders().apply {
            this.add("Cookie", authCookie)
        }
    }

    @Test
    fun `list by admin`() {
        val responseEntity = restTemplate
            .exchange(
                URI("$apiUrl/list"),
                HttpMethod.GET,
                HttpEntity(null, createHeaders("admin", "pass")),
                String::class.java
            )
        responseEntity.body?.let { assert(it.contains("Andrey")) }
    }

    @Test
    fun `list by apiuser`() {
        val responseEntity = restTemplate
            .exchange(
                URI("$apiUrl/list"),
                HttpMethod.GET,
                HttpEntity(null, createHeaders("apiuser", "pass")),
                String::class.java
            )
        responseEntity.body?.let { assert(it.contains("Andrey")) }
    }

    @Test
    fun add() {
        val dto = AddressDto(999, "Newman", "NY", "NewStreet", 9)
        val responseEntity = restTemplate.exchange(
            URI("$apiUrl/add"),
            HttpMethod.POST,
            HttpEntity(dto, createHeaders("apiuser", "pass")),
            Boolean::class.java
        )
        assert(responseEntity.statusCode.is2xxSuccessful)
    }

    @Test
    fun getById() {
        val dto = restTemplate.exchange(
            URI("$apiUrl/1/view"),
            HttpMethod.GET,
            HttpEntity(null, createHeaders("apiuser", "pass")),
            AddressDto::class.java
        ).body
        assert(dto!! == addressBook.get(1))
    }

    @Test
    fun editById() {
        val dto = AddressDto(2, "Newman", "NY", "NewStreet", 9)
        val responseEntity = restTemplate.exchange(
            URI("$apiUrl/edit"),
            HttpMethod.PUT,
            HttpEntity(dto, createHeaders("apiuser", "pass")),
            AddressDto::class.java
        )
        assert(responseEntity.statusCode.is2xxSuccessful)
    }

    @Test
    fun deleteById() {
        val responseEntity = restTemplate.exchange(
            URI("$apiUrl/1/delete"),
            HttpMethod.DELETE,
            HttpEntity(null, createHeaders("apiuser", "pass")),
            Any::class.java
        )
        assert(responseEntity.statusCode.is2xxSuccessful)
    }
}