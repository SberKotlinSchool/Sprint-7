package sber.mvc.application.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
class AppControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun addTestEntry() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("firstName", "Irina")
                .param("lastName", "Romanova")
                .param("address", "Moscow")
                .param("email", "romanova_98@gmail.com")
                .param("phone", "8-999-999-99-99")
        )
    }

    @WithMockUser(username = "admin", password = "testPass123", authorities = ["ROLE_ADMIN"])
    @Test
    fun addEntry() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("firstName", "Test1")
                .param("lastName", "Test2")
                .param("address", "Moscow")
                .param("email", "test3")
                .param("phone", "8-999-999-99-99")
        )
            .andExpect(MockMvcResultMatchers.status().`is`(302))
    }

    @WithMockUser(username = "admin", password = "testPass123", authorities = ["ROLE_ADMIN"])
    @Test
    fun listEntries() {
        mockMvc.perform(MockMvcRequestBuilders.get("/app/list/"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Irina")))
    }

    @WithMockUser(username = "admin", password = "testPass123", authorities = ["ROLE_ADMIN"])
    @Test
    fun viewEntry() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/app/list?firstName=Irina")).andReturn()
            .response
            .contentAsString

        val id = result.split("<td>", "</td>")[1]

        mockMvc.perform(MockMvcRequestBuilders.get("/app/${id}/view"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("view"))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Irina")))
    }

    @WithMockUser(username = "admin", password = "testPass123", authorities = ["ROLE_ADMIN"])
    @Test
    fun editEntry() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/1/edit")
                .param("firstName", "Polina")
        ).andExpect(MockMvcResultMatchers.status().`is`(302))

        mockMvc.perform(MockMvcRequestBuilders.get("/app/list"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Polina")))

        mockMvc.perform(MockMvcRequestBuilders.get("/app/1/delete"))
    }

    @WithMockUser(username = "admin", password = "testPass123", authorities = ["ROLE_ADMIN"])
    @Test
    fun deleteEntry() {
        mockMvc.perform(MockMvcRequestBuilders.get("/app/1/delete"))
            .andExpect(MockMvcResultMatchers.status().`is`(302))
    }
}