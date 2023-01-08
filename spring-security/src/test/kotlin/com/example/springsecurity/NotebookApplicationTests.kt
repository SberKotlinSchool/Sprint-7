package com.example.springsecurity

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest
class NotebookApplicationTests {

	@Autowired
	private lateinit var mockMvc: MockMvc


    @WithMockUser(username = "abc", password = "abc", roles = ["ADMIN"])
    @Test
    fun test_mainpage_success() {
        mockMvc.perform(MockMvcRequestBuilders.get("/mainpage"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }


    @WithMockUser(username = "abc", password = "abc", roles = ["ADMIN"])
    @Test
    fun test_new_note_success() {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/note/new")
            .param("text", "some note")
            .param("login", "abc"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @WithMockUser(username = "abc", password = "abc", roles = ["ADMIN"])
    @Test
    fun test_delete_success() {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/note/delete")
            .param("id", "1")
            .param("login", "abc"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @WithMockUser(username = "abc", password = "abc", roles = ["USER"])
    @Test
    fun test_delete_fail() {
        mockMvc.perform(MockMvcRequestBuilders.post("/note/delete")
            .param("id", "1")
            .param("login", "abc"))
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }
}
