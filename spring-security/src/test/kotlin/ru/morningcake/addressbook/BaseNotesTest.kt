package ru.morningcake.addressbook

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import ru.morningcake.addressbook.dao.NoteRepository
import ru.morningcake.addressbook.dao.UserRepository
import ru.morningcake.addressbook.dto.NoteDto
import ru.morningcake.addressbook.entity.Note
import java.nio.charset.StandardCharsets


abstract class BaseNotesTest : BaseMvcTest() {

    @Autowired
    protected lateinit var noteRepository : NoteRepository
    @Autowired
    protected lateinit var userRepository: UserRepository

    protected lateinit var note1 : Note
    protected lateinit var note2 : Note

    @BeforeEach
    protected fun init() {
        note1 = noteRepository.create(
            NoteDto(name = "Name1", phone = "+71234567890", address = "Address1", comment = "Comment1")
        )
        note2 = noteRepository.create(
            NoteDto(name = "Name2", phone = "81234567890", address = "Address2", comment = "")
        )
    }

    @AfterEach
    protected fun clear() {
        noteRepository.clear()
    }

    protected fun getHtmlResponse(mvcResult : MvcResult) : String {
        return mvcResult.response.contentAsString.replace(System.lineSeparator(), "")
    }

    protected fun getJsonResponse(mvcResult : MvcResult) : String {
        return mvcResult.response.contentAsString
    }

    protected fun getExceptionResponse(mvcResult : MvcResult) : java.lang.Exception? {
        return mvcResult.resolvedException
    }

    // region REQUESTS WITH COOKIES

    protected fun GET(uri : String, status : ResultMatcher) : MvcResult {
        return mockMvc.perform(
            get(uri)
                .characterEncoding(StandardCharsets.UTF_8)
        ).andExpect(status).andReturn()
    }

    protected fun apiGET(uri : String, status : ResultMatcher) : MvcResult {
        return GET(uri, status)
    }

    protected fun apiPOST(uri : String, content : String, status : ResultMatcher) : MvcResult {
        return mockMvc.perform(
            post(uri)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
        ).andExpect(status).andReturn()
    }

    protected fun POST(uri : String, content : String, status : ResultMatcher) : MvcResult {
        return mockMvc.perform(
            post(uri)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content(content)
                .with(csrf())
        ).andExpect(status).andReturn()
    }

    protected fun apiPUT(uri : String, content : String, status : ResultMatcher) : MvcResult {
        return mockMvc.perform(
            put(uri)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
        ).andExpect(status).andReturn()
    }

    protected fun apiDELETE(uri : String, status : ResultMatcher) : MvcResult {
        return mockMvc.perform(
            delete(uri)
                .characterEncoding(StandardCharsets.UTF_8)
        ).andExpect(status).andReturn()
    }

    // endregion
}