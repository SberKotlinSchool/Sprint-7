package ru.sber.AddressBook

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.context.WebApplicationContext
import ru.sber.AddressBook.Model.CustomerModel
import ru.sber.AddressBook.Model.ResponseDescription


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddressBookRestTests() {
    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private fun url(path: String) = "http://localhost:$port/$path"

    private fun getCookieForUserAndSetForTests(
        username: String,
        password: String,
        loginUrl: String
    ): HttpHeaders {
        val header = HttpHeaders()
        val form: MultiValueMap<String, String> = LinkedMultiValueMap()
        form.set("username", username)
        form.set("password", password)
        val loginResponse: ResponseEntity<String> = restTemplate.postForEntity(
            loginUrl,
            HttpEntity(form, HttpHeaders()),
            String::class.java
        )

        val cookie = loginResponse.headers["Set-Cookie"]!![0]
        header.add("Cookie", cookie)

        return header
    }

    private val testCustomer = CustomerModel(
        firstName = "FirstName1",
        lastName = "LastName1",
        middleName = "MiddleName1",
        phone = "+79234622135",
        email = "oijoi",
        address = "address"
    )

    private lateinit var cookieHeader: HttpHeaders

    @BeforeAll
    fun setCookie() {
        cookieHeader = getCookieForUserAndSetForTests("apiuser", "apiuser", "/login")
    }

    @Test
    fun `test should redirect to Login Page if cookie not found`() {

        val response = restTemplate.getForObject(
            url("/api/list"),
            String::class.java
        )
        assertTrue(
            response.contains("<h2 class=\"form-signin-heading\">Please sign in</h2>")
        )
    }

    @Test
    fun `test should deny access for user without ROLE_API`() {
        val response = restTemplate.exchange(

            url("api/list"),
            HttpMethod.GET,
            HttpEntity(null, getCookieForUserAndSetForTests("user1", "user1", "/login")),
            String::class.java
        )
        assertTrue(response.statusCode.is4xxClientError)

    }

    @Test
    fun `get list`() {
        val response = restTemplate.exchange(
            url("api/list"),
            HttpMethod.GET,
            HttpEntity(null, cookieHeader),
            CustomerModel::class.java
        )

        println(response.body)

        assertTrue(response.statusCode.is2xxSuccessful)
        assertTrue(response.hasBody())
    }

    @Test
    fun `post add row`() {
        val response = restTemplate.postForEntity(
            url("api/add"),
            HttpEntity(testCustomer, cookieHeader),
            ResponseDescription::class.java
        )

        assert(response.statusCode.is2xxSuccessful)
        assert(response.body?.result == "OK")
        assert(response.body?.description == "Added row")
    }

    @Test
    fun `put edit row`() {

        val response = restTemplate.exchange(
            url("api/edit/1"),
            HttpMethod.PUT,
            HttpEntity(testCustomer, cookieHeader),
            ResponseDescription::class.java
        )

        println(response.body)

        assertTrue(response.statusCode.is2xxSuccessful)
        assertTrue(response.body?.result == "OK")
        assertTrue(response.body?.description == "Edited row for client 1")
    }

    @Test
    fun `get view row`() {
        val response = restTemplate.exchange(
            url("api/view?id=1"),
            HttpMethod.GET,
            HttpEntity(null, cookieHeader),
            CustomerModel::class.java
        )

        assertTrue(response.statusCode.is2xxSuccessful)
        assertTrue(
            response.body?.lastName == "LastName1" && response.body?.firstName == "FirstName1"
        )

    }

    @Test
    fun `delete row by user apiuser that doesn't have permission`() {
        val response = restTemplate.exchange(
            url("api/delete/1"),
            HttpMethod.DELETE,
            HttpEntity(null, cookieHeader),
            ResponseDescription::class.java
        )

        assertTrue(response.statusCode.is4xxClientError)
    }

    @Test
    fun `delete row by user admin`() {
        val response = restTemplate.exchange(
            url("api/delete/1"),
            HttpMethod.DELETE,
            HttpEntity(null, getCookieForUserAndSetForTests("admin", "admin", "/login")),
            ResponseDescription::class.java
        )

        assertTrue(response.statusCode.is2xxSuccessful)
        assertTrue(response.body?.result == "OK")
        assertTrue(response.body?.description == "Deleted 1")
    }


}