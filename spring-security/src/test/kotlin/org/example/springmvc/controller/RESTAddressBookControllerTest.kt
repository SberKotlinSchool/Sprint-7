package org.example.springmvc.controller

import org.assertj.core.api.Assertions.assertThat
import org.example.springmvc.entity.Contact
import org.example.springmvc.service.AddressBookService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
internal class RESTAddressBookControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var addressBookService: AddressBookService
    private lateinit var testContact01: Contact
    private lateinit var testContact02: Contact
    private fun url(path: String) = "http://localhost:${port}/${path}"

    @BeforeEach
    fun setUp() {
        testContact01 = Contact(1, "One", "Moscow", "89111111111")
        addressBookService.add(testContact01)
        testContact02 = Contact(2, "Two", "Tomsk", "89222222222")
    }

    @AfterEach
    fun tearDown() = addressBookService.deleteAll()

    @Test
    fun add() {
        val sizeBefore = addressBookService.findContactsByCity(null).size
        val rs = restTemplate.exchange(
            url("/api"),
            HttpMethod.POST,
            HttpEntity(testContact02, apiUser()),
            Nothing::class.java
        )
        val sizeAfter = addressBookService.findContactsByCity(null).size
        assertEquals(HttpStatus.OK, rs.statusCode)
        assertEquals(1, sizeAfter - sizeBefore)
    }

    @Test
    fun view() {
        val rs = restTemplate.exchange(
            url("/api/${testContact01.id}"),
            HttpMethod.GET,
            HttpEntity<Contact>(apiUser()),
            Contact::class.java
        )
        assertThat(rs.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(rs.body?.id).isEqualTo(testContact01.id)
        assertThat(rs.body?.name).isEqualTo(testContact01.name)
        assertThat(rs.body?.city).isEqualTo(testContact01.city)
        assertThat(rs.body?.phone).isEqualTo(testContact01.phone)
    }

    @Test
    fun edit() {
        val rs = restTemplate.exchange(
            url("/api/${testContact01.id}"),
            HttpMethod.PUT,
            HttpEntity(testContact02, apiUser()),
            Contact::class.java
        )
        assertThat(rs.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun list() {
        val rs = restTemplate.exchange(
            url("/api/list"),
            HttpMethod.GET,
            HttpEntity<Nothing>(apiUser()),
            List::class.java
        )
        assertThat(rs.statusCode).isEqualTo(HttpStatus.OK)
        assertTrue(rs.body?.get(0).toString().contains(testContact01.name!!))
        assertTrue(rs.body?.get(0).toString().contains(testContact01.city!!))
        assertTrue(rs.body?.get(0).toString().contains(testContact01.phone!!))
    }

    @Test
    fun delete() {
        val sizeBefore = addressBookService.findContactsByCity(null).size
        val rs = restTemplate.exchange(
            url("/api/${testContact01.id}"),
            HttpMethod.DELETE,
            HttpEntity(testContact01, admin()),
            Nothing::class.java
        )
        val sizeAfter = addressBookService.findContactsByCity(null).size
        assertThat(rs.statusCode).isEqualTo(HttpStatus.OK)
        assertEquals(1, sizeBefore - sizeAfter)
    }

    @Test
    fun listWithoutGrants() {
        val rs = restTemplate.exchange(
            url("/api/list"),
            HttpMethod.GET,
            HttpEntity<Contact>(appUser()),
            List::class.java
        )
        assertThat(rs.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun deleteWithoutGrants() {
        val rs = restTemplate.exchange(
            url("/api/${testContact01.id}"),
            HttpMethod.DELETE,
            HttpEntity(testContact01.id, apiUser()),
            Nothing::class.java
        )
        assertThat(rs.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    private fun apiUser(): HttpHeaders = authHeader("USER_API", "Api123456")
    private fun appUser(): HttpHeaders = authHeader("USER_APP", "App123456")
    private fun admin(): HttpHeaders = authHeader("ADMIN", "password")

    private fun authHeader(username: String, password: String): HttpHeaders {
        val headers = HttpHeaders()
        headers.setBasicAuth(username, password)
        return headers
    }
}