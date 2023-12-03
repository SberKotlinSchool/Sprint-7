package com.example.demo

import com.example.demo.controller.MyController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MyController::class) //Чтобы не поднимать весь spring-context
class DemoApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `GET test`() {
        val res = mockMvc.perform(MockMvcRequestBuilders.get("/hello"))

        res.andExpect(status().isOk)
                .andExpect(content().json("""{"success": true, "data":"data ok"} """));
    }

}
