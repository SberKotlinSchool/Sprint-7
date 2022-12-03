package com.example.demo.endpoint

import com.example.demo.endpoint.dto.RequestData
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
    @Test
    fun test_list_shouldSucceedWith200() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @WithMockUser(username = "test1", password = "test1", roles = ["USER"])
    @Test
    fun test_list_shouldBeForbidden() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list"))
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @WithMockUser(username = "test", password = "test", roles = ["API"])
    @Test
    fun test_add_successful() {
        val requestData = RequestData("FirstName", "SecondName", true)

        val actualResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/add?requestData=${requestData}"))

        actualResult
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun test_list_redirect_to_loginform() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list"))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
    }

}