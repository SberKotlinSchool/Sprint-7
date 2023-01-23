package com.example.demo

import com.example.demo.controller.Controller
import com.example.demo.persistance.Entity
import com.example.demo.service.DatabaseService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyLong
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(Controller::class)
class ControllerTests {

    private companion object {

        const val TEST_GET_SERIALIZED_RESPONSE = "{\"id\":1,\"description\":\"test description\"}"
        val TEST_GET_ENTITY = Entity(id = 1, description = "test description")

        const val TEST_GET_REQUEST_PATH = "/get/1"
    }

    @MockBean
    private lateinit var databaseService: DatabaseService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun `set up`() {
        `when`(databaseService.getById(anyLong())).thenReturn(TEST_GET_ENTITY)
    }

    @Test
    fun `test get by id`() {
        mockMvc.perform(get(TEST_GET_REQUEST_PATH))
            .andExpect(status().isOk)
            .andExpect(content().json(TEST_GET_SERIALIZED_RESPONSE))
    }
}
