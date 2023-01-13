package com.example.demo

import com.example.demo.controller.BookController
import com.example.demo.persistance.BookEntity
import com.example.demo.persistance.BookRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(BookController::class)
class BookWebControllerTests {

    @MockBean
    private lateinit var bookRepository: BookRepository
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun shouldGet() {
       val result = mockMvc.perform(MockMvcRequestBuilders.get(("/sleep")))
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("Good night!"))
    }
}
