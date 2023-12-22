package ru.sber.springmvc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import ru.sber.springmvc.domain.Person

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ImportAutoConfiguration(exclude = [OAuth2ClientAutoConfiguration::class, OAuth2ResourceServerAutoConfiguration::class])
class ApiControllerTest {
    private companion object {
        private const val NAME = "123"
        private const val ADDRESS = "666"
        private const val PHONE = "999"
    }

    @LocalServerPort
    private val port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun addPersonTestSuccessful() {
        val actualResponse = addTestPerson()
        assertNotNull(actualResponse)
        assertNotNull(actualResponse.body)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        val actualPerson = actualResponse.body as Person
        assertPerson(actualPerson)
    }

    @Test
    fun getListTest() {
        addTestPerson()
        val actualResponse = restTemplate.exchange(
            "http://localhost:$port/api/list",
            HttpMethod.GET,
            HttpEntity("{name: \"$NAME\"}", getAuthHeaders("api")),
            Array<Person>::class.java
        )
        assertNotNull(actualResponse)
        assertNotNull(actualResponse.body)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(1, actualResponse.body?.size)
        val actualPerson = actualResponse.body?.first() as Person
        assertPerson(actualPerson)
    }

    @Test
    fun viewTest() {
        addTestPerson()
        val resp =
            restTemplate.exchange(
                "http://localhost:$port/api/person/1",
                HttpMethod.GET,
                HttpEntity(null, getAuthHeaders("api")),
                Person::class.java
            )
        assertEquals(HttpStatus.OK, resp.statusCode)
        assertEquals(NAME, resp.body?.name)
    }

    @Test
    fun deleteTest() {
        addTestPerson()
        val resp = restTemplate.exchange(
            "http://localhost:$port/api/person/1/delete",
            HttpMethod.DELETE,
            HttpEntity(null, getAuthHeaders("admin")),
            String::class.java
        )
        assertEquals(HttpStatus.OK, resp.statusCode)
    }

    @Test
    fun deleteTestWithoutAuth() {
        val resp = restTemplate.exchange(
            "http://localhost:$port/api/person/1/delete",
            HttpMethod.DELETE,
            null,
            String::class.java
        )
        assertEquals(HttpStatus.FOUND, resp.statusCode)
    }

    fun getAuthHeaders(role: String): HttpHeaders {
        val headers = HttpHeaders()
        headers.setBasicAuth(role, role)
        return headers
    }

    @Test
    fun editCustomerTest() {
        addTestPerson()
        val newName = "$NAME New"
        val person = Person(0, newName, ADDRESS, PHONE)
        val resp = restTemplate.exchange(
            "http://localhost:$port/api/person/1/edit",
            HttpMethod.PUT,
            HttpEntity(person, getAuthHeaders("api")),
            String::class.java
        )
        assertEquals(HttpStatus.OK, resp.statusCode)

        val actualResponse =
            restTemplate.exchange(
                "http://localhost:$port/api/list",
                HttpMethod.GET,
                HttpEntity(null, getAuthHeaders("api")),
                Array<Person>::class.java
            )
        assertEquals(1, actualResponse.body?.size)
        val actualPerson = actualResponse.body?.first() as Person
        assertEquals(newName, actualPerson.name)
    }

    private fun assertPerson(person: Person) {
        assertEquals(NAME, person.name)
        assertEquals(ADDRESS, person.address)
        assertEquals(PHONE, person.phone)
    }

    private fun addTestPerson(): ResponseEntity<Person> {
        val person = Person(0, NAME, ADDRESS, PHONE)
        return restTemplate.postForEntity(
            "http://localhost:$port/api/person/add",
            HttpEntity(person, getAuthHeaders("api")),
            Person::class.java
        )
    }
}