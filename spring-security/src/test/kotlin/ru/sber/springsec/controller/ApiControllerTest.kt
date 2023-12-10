package ru.sber.springsec.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import ru.sber.springsec.service.BookRow
import ru.sber.springsec.service.BookService

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
internal class ApiControllerTest {

    companion object {
        @JvmStatic
        fun getRequests(): Array<Arguments> = arrayOf(
            Arguments.of("/api/add", HttpMethod.POST),
            Arguments.of("/api/asd/view", HttpMethod.GET),
            Arguments.of("/api/list", HttpMethod.GET),
            Arguments.of("/api/asd/edit", HttpMethod.PUT),
            Arguments.of("/api/asd/delete", HttpMethod.DELETE)
        )
    }

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var service: BookService
    private val one = BookRow("test", "test address")
    private val two = BookRow("test2", "test address 2")

    @BeforeEach
    fun setUp() = service.add(one)
    @AfterEach
    fun tearDown() = service.deleteAll()

    private fun url(path: String) = "http://localhost:${port}${path}"

    @ParameterizedTest
    @MethodSource("getRequests")
    fun requestWithNoAuth(endpoint: String, method: HttpMethod) {
        val resp = restTemplate.exchange(
            url(endpoint), method,
            HttpEntity<Nothing>(HttpHeaders()),
            typeReference<List<BookRow>>())

        assertThat(resp.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @ParameterizedTest
    @MethodSource("getRequests")
    fun requestWithNoApiRole(endpoint: String, method: HttpMethod) {
        val resp = restTemplate.exchange(
            url(endpoint), method,
            HttpEntity<Nothing>(noRolesUser()),
            typeReference<List<BookRow>>())

        assertThat(resp.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun add() {
        val resp = restTemplate.exchange(
            url("/api/add"), HttpMethod.POST,
            HttpEntity<BookRow>(two, apiUser()),
            typeReference<List<BookRow>>())

        assertThat(resp.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(resp.headers.containsKey("Location")).isTrue
        assertThat(resp.headers["Location"]).isEqualTo(listOf(url("/api/${two.name}/view")))
    }

    @Test
    fun view() {
        val resp = restTemplate.exchange(
            url("/api/${one.name}/view"), HttpMethod.GET,
            HttpEntity<Nothing>(apiUser()),
            BookRow::class.java)

        assertThat(resp.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resp.body?.name).isEqualTo(one.name)
        assertThat(resp.body?.address).isEqualTo(one.address)
    }

    @Test
    fun list() {
        val resp = restTemplate.exchange(
            url("/api/list"), HttpMethod.GET,
            HttpEntity<Nothing>(apiUser()),
            typeReference<List<BookRow>>())

        assertThat(resp.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resp.body?.size).isEqualTo(1)
        assertThat(resp.body?.get(0)).isEqualTo(one)
    }

    @Test
    fun edit() {
        val resp = restTemplate.exchange(
            url("/api/${one.name}/edit"), HttpMethod.PUT,
            HttpEntity<String>(two.address, apiUser()), Nothing::class.java)

        assertThat(resp.statusCode).isEqualTo(HttpStatus.ACCEPTED)
    }

    @Test
    fun deleteWithAdmin() {
        val resp = restTemplate.exchange(
            url("/api/${one.name}/delete"), HttpMethod.DELETE,
            HttpEntity<String>(two.address, adminUser()), Nothing::class.java)

        assertThat(resp.statusCode).isEqualTo(HttpStatus.ACCEPTED)
    }

    @Test
    fun deleteWithNoAdmin() {
        val resp = restTemplate.exchange(
            url("/api/${one.name}/delete"), HttpMethod.DELETE,
            HttpEntity<String>(two.address, apiUser()), Nothing::class.java)

        assertThat(resp.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    private fun apiUser(): HttpHeaders = authHeader("api", "api")
    private fun adminUser(): HttpHeaders = authHeader("admin", "admin")
    private fun noRolesUser(): HttpHeaders = authHeader("app", "app")

    private fun authHeader(username: String, password: String): HttpHeaders {
        val headers = HttpHeaders()
        headers.setBasicAuth(username, password)
        return headers
    }
}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}