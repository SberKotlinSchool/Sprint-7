package ru.sber.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser
import ru.sber.model.Note
import ru.sber.repository.NoteRepository
import java.time.LocalDateTime
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ApiControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var repository: NoteRepository

    private fun url(s: String) = "http://localhost:${port}/${s}"

    private val headers = HttpHeaders().also { it.add("Cookie", "auth=${LocalDateTime.now()}") }
    private var testNote = Note(name = "NAME", address = "ADDRESS", phone = "+89999999999")
    private var testNote2 = Note(name = "NAME2", address = "ADDRESS2", phone = "+82222222222")

    @BeforeEach
    fun cleanBase() {
        repository.deleteAll()
    }

    @Test
    @WithMockUser(username = "api", password = "api", roles = ["API"])
    fun add() {
        //given
        //when
        val resp = restTemplate.exchange(
            url("/api/add"),
            HttpMethod.POST,
            HttpEntity(testNote, headers),
            Unit::class.java
        )
        //then
        assertEquals(resp.statusCode, HttpStatus.OK)

        val result = repository.findAll().first()
        assertEquals(result.name, testNote.name)
    }

    @Test
    @WithMockUser(username = "api", password = "api", roles = ["API"])
    fun list() {
        //given
        repository.save(testNote)
        repository.save(testNote2)
        //when
        val resp = restTemplate.exchange(
            url("/api/list"),
            HttpMethod.GET,
            HttpEntity(null, headers),
            Array<Note>::class.java
        )
        //then
        assertEquals(resp.statusCode, HttpStatus.OK)
        assertContentEquals(resp.body?.toList(), listOf(testNote, testNote2))
    }

    @Test
    @WithMockUser(username = "api", password = "api", roles = ["API"])
    fun view() {
        //given
        testNote = repository.save(testNote)
        testNote2 = repository.save(testNote2)
        //when
        val resp = restTemplate.exchange(
            url("/api/${testNote2.id}/view"),
            HttpMethod.GET,
            HttpEntity(null, headers),
            Note::class.java
        )
        //then
        assertEquals(resp.statusCode, HttpStatus.OK)
        assertEquals(resp.body, testNote2)
    }


    @Test
    @WithMockUser(username = "api", password = "api", roles = ["API"])
    fun edit() {
        //given
        testNote = repository.save(testNote)
        //when
        val resp = restTemplate.exchange(
            url("/api/${testNote.id}/edit"),
            HttpMethod.PUT,
            HttpEntity(testNote2, headers),
            Unit::class.java
        )
        //then
        assertEquals(resp.statusCode, HttpStatus.OK)
        val result = repository.getNoteById(testNote.id!!)
        assertEquals(result.name, testNote2.name)
    }

    @Test
    @WithMockUser(username = "api", password = "api", roles = ["API"])
    fun deleteNotPermit() {
        //given
        testNote = repository.save(testNote)
        //when
        val resp = restTemplate.exchange(
            url("/api/${testNote.id}/delete"),
            HttpMethod.DELETE,
            HttpEntity(null, headers),
            Unit::class.java
        )
        //then
        // TODO
//        assertEquals(resp.statusCode, HttpStatus.FOUND)
//        assertContentEquals(emptyList(), repository.findAll())
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
    fun delete() {
        //given
        testNote = repository.save(testNote)
        //when
        val resp = restTemplate.exchange(
            url("/api/${testNote.id}/delete"),
            HttpMethod.DELETE,
            HttpEntity(null, headers),
            Unit::class.java
        )
        //then
        assertEquals(resp.statusCode, HttpStatus.OK)
        assertContentEquals(emptyList(), repository.findAll())
    }

    @ParameterizedTest
    @ValueSource(strings = ["/api/add", "/api/list", "/api/0/view", "/api/0/edit", "/api/0/delete"])
    fun `should login`(url: String) {
        //given
        repository.save(testNote)
        //when
        val resp = restTemplate.exchange(
            url(url),
            HttpMethod.GET,
            HttpEntity(null, null),
            String::class.java
        )
        //then
        assertEquals(resp.statusCode, HttpStatus.OK)
        assertTrue(resp.body.toString().contains("password"))
    }
}