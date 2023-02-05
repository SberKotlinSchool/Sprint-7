package com.example.demo

import com.example.demo.DemoApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest
class MessageControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `check message for id=1`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/message/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("first message"))
    }

}