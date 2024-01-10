package ru.sber.springmvc.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import ru.sber.security.dto.Student
import ru.sber.security.service.AddressBookService

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ExtendWith(SpringExtension::class)
internal class RestControllerTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var addressBookService: AddressBookService

    private lateinit var testStudent01: Student
    private lateinit var testStudent02: Student

    private fun url(path: String) = "http://localhost:00/$path"

    @BeforeEach
    fun setUp() {
        testStudent01 = createTestEntity("FN_One", "FA_One", "89111234567")
        testStudent02 = createTestEntity("FN_Two", "FA_Two", "89211234567")
    }

    @AfterEach
    fun tearDown() = addressBookService.deleteAll()

    @Test
    fun add() {
        val sizeBefore = addressBookService.size()
        val response = restTemplate.postForEntity(url("/api/add"), testStudent02, Nothing::class.java)
        val sizeAfter = addressBookService.size()

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(1, sizeAfter - sizeBefore)
    }

    @Test
    fun view() {
        val response = restTemplate.getForEntity(url("/api/${testStudent01.entityId}/view"), Student::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertEntityEquals(response.body, testStudent01)
    }

    @Test
    fun edit() {
        val response = restTemplate.exchange(
            url("/api/${testStudent01.entityId}/edit"), HttpMethod.PUT,
            HttpEntity(testStudent02, apiUser()), Nothing::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.ACCEPTED)
    }

    @Test
    fun list() {
        val response =
            restTemplate.exchange(url("/api/list"), HttpMethod.GET, HttpEntity<Nothing>(apiUser()), List::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertEntityInList(response.body, testStudent01)
    }

    @Test
    fun delete() {
        val sizeBefore = addressBookService.size()
        val response = restTemplate.exchange(
            url("/api/${testStudent01.entityId}/delete"), HttpMethod.DELETE,
            HttpEntity(testStudent01.entityId, admin()), Nothing::class.java
        )
        val sizeAfter = addressBookService.size()

        assertThat(response.statusCode).isEqualTo(HttpStatus.ACCEPTED)
        assertEquals(1, sizeBefore - sizeAfter)
    }

    @Test
    fun listWithoutGrants() {
        val response =
            restTemplate.exchange(url("/api/list"), HttpMethod.GET, HttpEntity<Nothing>(appUser()), List::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun deleteWithoutGrants() {
        val response = restTemplate.exchange(
            url("/api/${testStudent01.entityId}/delete"), HttpMethod.DELETE,
            HttpEntity(testStudent01.entityId, apiUser()), Nothing::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    private fun createTestEntity(fullName: String, fullAddress: String, phoneNumber: String): Student {
        return Student(addressBookService.getNextEntityId(), fullName, fullAddress, phoneNumber)
    }

    private fun apiUser(): HttpHeaders = authHeader("USER_API", "Qq123456")
    private fun appUser(): HttpHeaders = authHeader("USER_APP", "Qq123456")
    private fun admin(): HttpHeaders = authHeader("ADMIN", "Qq123456")

    private fun authHeader(username: String, password: String): HttpHeaders {
        val headers = HttpHeaders()
        headers.setBasicAuth(username, password)
        return headers
    }

    private fun assertEntityEquals(actual: Student?, expected: Student) {
        assertThat(actual?.entityId).isEqualTo(expected.entityId)
        assertThat(actual?.fullName).isEqualTo(expected.fullName)
        assertThat(actual?.fullAddress).isEqualTo(expected.fullAddress)
        assertThat(actual?.phoneNumber).isEqualTo(expected.phoneNumber)
    }

    private fun assertEntityInList(entities: List<*>?, expected: Student) {
        assertTrue(entities?.any { it.toString().contains(expected.fullName!!) } ?: false)
        assertTrue(entities?.any { it.toString().contains(expected.fullAddress!!) } ?: false)
        assertTrue(entities?.any { it.toString().contains(expected.phoneNumber!!) } ?: false)
    }
}
