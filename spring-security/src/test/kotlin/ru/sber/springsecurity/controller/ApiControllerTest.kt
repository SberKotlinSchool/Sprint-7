package ru.sber.springsecurity.controller

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
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import ru.sber.springsecurity.model.AddressBookRow
import ru.sber.springsecurity.service.AddressBookService

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ApiControllerTest {
    companion object {
        @JvmStatic
        fun getRequestsToAllEndpoints(): Array<Arguments> = arrayOf(
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
    private lateinit var service: AddressBookService
    private val testRow = AddressBookRow("test", "test address")
    private val testRow2 = AddressBookRow("test2", "test address 2")

    @BeforeEach
    fun setUp() {
        service.add(testRow)
    }

    @AfterEach
    fun tearDown() {
        service.deleteAll()
    }

    private fun url(path: String): String {
        return "http://localhost:${port}${path}"
    }

    @ParameterizedTest
    @MethodSource("getRequestsToAllEndpoints")
    fun requestWithNoAuthReturnsUnauthorized(endpoint: String, method: HttpMethod) {
        val resp = restTemplate.exchange(
            url(endpoint),
            method,
            HttpEntity<Nothing>(HttpHeaders()),
            typeReference<List<AddressBookRow>>()
        )

        assertThat(resp.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @ParameterizedTest
    @MethodSource("getRequestsToAllEndpoints")
    fun requestWithNoApiRoleReturnsForbidden(endpoint: String, method: HttpMethod) {
        val resp = restTemplate.exchange(
            url(endpoint),
            method,
            HttpEntity<Nothing>(noRolesUserAuthHeader()),
            typeReference<List<AddressBookRow>>()
        )

        assertThat(resp.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun add() {
        val resp = restTemplate.exchange(
            url("/api/add"),
            HttpMethod.POST,
            HttpEntity<AddressBookRow>(testRow2, apiUserAuthHeader()),
            typeReference<List<AddressBookRow>>()
        )

        assertThat(resp.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(resp.headers.containsKey("Location")).isTrue
        assertThat(resp.headers["Location"]).isEqualTo(listOf(url("/api/${testRow2.name}/view")))
    }

    @Test
    fun view() {
        val resp = restTemplate.exchange(
            url("/api/${testRow.name}/view"),
            HttpMethod.GET,
            HttpEntity<Nothing>(apiUserAuthHeader()),
            AddressBookRow::class.java
        )

        assertThat(resp.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resp.body?.name).isEqualTo(testRow.name)
        assertThat(resp.body?.address).isEqualTo(testRow.address)
    }

    @Test
    fun list() {
        val resp = restTemplate.exchange(
            url("/api/list"),
            HttpMethod.GET,
            HttpEntity<Nothing>(apiUserAuthHeader()),
            typeReference<List<AddressBookRow>>()
        )

        assertThat(resp.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resp.body?.size).isEqualTo(1)
        assertThat(resp.body?.get(0)).isEqualTo(testRow)
    }

    @Test
    fun edit() {
        val resp = restTemplate.exchange(
            url("/api/${testRow.name}/edit"),
            HttpMethod.PUT,
            HttpEntity<String>(testRow2.address, apiUserAuthHeader()),
            Nothing::class.java
        )

        assertThat(resp.statusCode).isEqualTo(HttpStatus.ACCEPTED)
    }

    @Test
    fun deleteWithAdminUser() {
        val resp = restTemplate.exchange(
            url("/api/${testRow.name}/delete"),
            HttpMethod.DELETE,
            HttpEntity<String>(testRow2.address, adminUserAuthHeader()),
            Nothing::class.java
        )

        assertThat(resp.statusCode).isEqualTo(HttpStatus.ACCEPTED)
    }

    @Test
    fun deleteWithNoAdminUserReturnsForbidden() {
        val resp = restTemplate.exchange(
            url("/api/${testRow.name}/delete"),
            HttpMethod.DELETE,
            HttpEntity<String>(testRow2.address, apiUserAuthHeader()),
            Nothing::class.java
        )

        assertThat(resp.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    private fun apiUserAuthHeader(): HttpHeaders = authHeader("api", "api")
    private fun adminUserAuthHeader(): HttpHeaders = authHeader("admin", "admin")
    private fun noRolesUserAuthHeader(): HttpHeaders = authHeader("app", "app")

    private fun authHeader(username: String, password: String): HttpHeaders {
        val headers = HttpHeaders()
        headers.setBasicAuth(username, password)
        return headers
    }
}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
