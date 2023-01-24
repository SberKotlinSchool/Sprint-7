package ru.sber.springmvc.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.net.URI

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApiControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private fun url(s: String): String {
        return "http://localhost:${port}${s}"
    }

    @Test
    fun getUserListNormalTest() {
        val resp = restTemplate.exchange(URI(url("/api/users")), HttpMethod.GET,
            createHttpEntity("apiuser", "apiuser"), List::class.java)
        assertNotNull(resp)
        assertNotNull(resp.headers)
        assertTrue(resp.headers[HttpHeaders.CONTENT_TYPE]?.contains(MediaType.APPLICATION_JSON_VALUE) ?: false)
        assertNotNull(resp.body)
        assertTrue(resp.body is List<*>)
        assertEquals(3, resp.body?.size)
        val user = resp.body?.get(0)
        assertEquals("admin", (user as Map<*,*>)["login"])
    }

    @Test
    fun getUserListNotAuthorizedTest() {
        val resp = restTemplate.exchange(URI(url("/api/users")), HttpMethod.GET,
            createHttpEntity("user1", "user1"), String::class.java)
        assertNotNull(resp)
        assertEquals(HttpStatus.FORBIDDEN, resp.statusCode)
    }

    @Test
    fun getUserTest() {
        val resp = restTemplate.exchange(URI(url("/api/users/1")), HttpMethod.GET, createHttpEntity("apiuser", "apiuser"), Map::class.java)
        assertNotNull(resp)
        assertNotNull(resp.headers)
        assertTrue(resp.headers[HttpHeaders.CONTENT_TYPE]?.contains(MediaType.APPLICATION_JSON_VALUE) ?: false)
        assertNotNull(resp.body)
        assertTrue(resp.body is Map<*, *>)
        assertEquals("admin", resp.body?.get("login"))
    }

    fun createHttpEntity(username: String, password: String) : HttpEntity<Unit> {
        val headers = HttpHeaders()
        headers.add(HttpHeaders.COOKIE, getCookieForUser(username, password))
        return HttpEntity<Unit>(headers)
    }

    private fun getCookieForUser(username: String, password: String): String {
        val form: MultiValueMap<String, String> = LinkedMultiValueMap()
        form["username"] = username
        form["password"] = password
        val loginResponse = restTemplate.postForEntity("/login", HttpEntity(form, HttpHeaders()), String::class.java)
        return loginResponse.headers["Set-Cookie"]!![0]
    }
}