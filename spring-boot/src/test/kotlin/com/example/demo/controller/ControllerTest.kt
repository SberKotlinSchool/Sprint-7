package com.example.demo.controller


import com.example.demo.persistance.Author
import com.example.demo.persistance.AuthorRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
internal class ControllerTest {

    @Autowired
    private lateinit var repository: AuthorRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testGetAuthor() {
        val author = Author(1, firstName = "Test", secondName = "Testov")
        repository.save(author)

        val actualResult = mockMvc.perform(MockMvcRequestBuilders.get("/app/author?id=${author.id}"))

        actualResult
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(author.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(author.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondName").value(author.secondName))
    }
}