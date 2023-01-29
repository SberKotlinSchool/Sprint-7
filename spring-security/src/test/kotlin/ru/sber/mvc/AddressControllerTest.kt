package ru.sber.mvc

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddressControllerTest {

    @Autowired
    private lateinit var mock: MockMvc

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    fun `test get list as admin`() {
        mock.perform(get("/app/list"))
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
    }

    @WithMockUser(username = "guest", roles = [""])
    @Test
    fun `test get list as guest`() {
        mock.perform(get("/app/list"))
            .andExpect(status().isForbidden)
    }

    @WithMockUser(username = "reader", roles = ["READER"])
    @Test
    fun `test get list with parameter as reader`() {
        mock.perform(get("/app/list").param("name", "Billy").param("phone", "7-924-202-35-02"))
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
            .andExpect(content().string(containsString("Billy")))
            .andExpect(content().string(containsString("7-924-202-35-02")))
    }

    @WithMockUser(username = "guest", roles = [""])
    @Test
    fun `test get list with parameter as guest`() {
        mock.perform(get("/app/list").param("name", "Billy").param("phone", "7-924-202-35-02"))
            .andExpect(status().isForbidden)
    }

    @WithMockUser(username = "reader", roles = ["READER"])
    @Test
    fun `test get view`() {
        mock.perform(get("/app/0/view"))
            .andExpect(status().isOk)
            .andExpect(view().name("view"))
    }

    @WithMockUser(username = "reader", roles = ["READER"])
    @Test
    fun `test add as reader`() {
        mock.perform(get("/app/add"))
            .andExpect(status().isForbidden)
    }

    @WithMockUser(username = "writer", roles = ["WRITER"])
    @Test
    fun `test add get as writer`() {
        mock.perform(get("/app/add"))
            .andExpect(status().isOk)
            .andExpect(view().name("add"))
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    fun `test add post as admin`() {
        mock.perform(post("/app/add")
            .param("name", "Jimmy")
            .param("phone", "7-956-489-51-62")
            .with(csrf()))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    fun `test edit get`() {
        mock.perform(get("/app/0/edit"))
            .andExpect(status().isOk)
            .andExpect(view().name("edit"))
    }

    @WithMockUser(username = "writer", roles = ["WRITER"])
    @Test
    fun `test edit post as writer`() {
        mock.perform(post("/app/1/edit")
            .param("name", "Jimmy")
            .param("phone", "7-956-489-51-62")
            .with(csrf()))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    fun `test delete by admin`() {
        mock.perform(get("/app/2/delete"))
            .andExpect(status().is3xxRedirection)

        mock.perform(get("/app/list")).andExpect(status().isOk)
            .andExpect(content().string( not(containsString("Israel")) ))
    }

    @WithMockUser(username = "reader", roles = ["READER"])
    @Test
    fun `test delete by reader`() {
        mock.perform(get("/app/1/delete"))
            .andExpect(status().isForbidden)
    }

}