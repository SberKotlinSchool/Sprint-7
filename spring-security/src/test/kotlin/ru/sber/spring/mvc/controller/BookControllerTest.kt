package ru.sber.spring.mvc.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import ru.sber.spring.mvc.model.Book
import ru.sber.spring.mvc.service.BookService

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
internal class BookControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var bookService: BookService

    private val mapper = jacksonObjectMapper()

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply { springSecurity() }
            .build()
    }

    @Test
    fun getMainPage() {
        mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(view().name("main"))
            .andExpect(status().isOk)
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    fun getAddBookPage() {
        mockMvc.perform(get("/book/add"))
            .andDo(print())
            .andExpect(view().name("add"))
            .andExpect(status().isOk)
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    fun addBook() {
        val book = Book(name = "new book", author = "Pushkin")

        mockMvc.perform(
            post("/book/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(book))
        )
            .andDo(print())
            .andExpect(view().name("main"))
            .andExpect(status().isOk)
    }

    @WithMockUser(username = "user", roles = ["READ"])
    @Test
    fun getBooksList() {
        bookService.add(Book(name = "new book 1", author = "Pushkin"))
        bookService.add(Book(name = "new book 2", author = "Pushkin"))

        mockMvc.perform(get("/book/list"))
            .andDo(print())
            .andExpect(view().name("list"))
            .andExpect(status().isOk)
    }

    @WithMockUser(username = "user", roles = ["READ"])
    @Test
    fun viewById() {
        val book = Book(name = "new book 1", author = "Pushkin")
        bookService.add(book)

        mockMvc.perform(get("/book/${book.id}/view"))
            .andDo(print())
            .andExpect(view().name("view"))
            .andExpect(status().isOk)
    }

    @WithMockUser(username = "user", roles = ["WRITE"])
    @Test
    fun edit() {
        val book = Book(name = "new book", author = "Pushkin")
        bookService.add(book)

        book.name = "updated book"

        mockMvc.perform(
            post("/book/${book.id}/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(book))
        )
            .andDo(print())
            .andExpect(view().name("main"))
            .andExpect(status().isOk)

        val newBook = bookService.getById(book.id!!)!!
        assertEquals("updated book", newBook.name)
    }

    @WithMockUser(username = "user", roles = ["WRITE"])
    @Test
    fun delete() {
        val book = Book(name = "new book", author = "Pushkin")
        bookService.add(book)

        mockMvc.perform(get("/book/${book.id}/delete"))
            .andDo(print())
            .andExpect(view().name("main"))
            .andExpect(status().isOk)

        assertEquals(0, bookService.getBooks().size)
    }
}