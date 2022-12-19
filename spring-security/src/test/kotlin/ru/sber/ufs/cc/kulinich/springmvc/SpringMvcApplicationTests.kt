package ru.sber.ufs.cc.kulinich.springmvc

import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import javax.servlet.http.Cookie

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringMvcApplicationTests {
    @LocalServerPort
    private var port: Int = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    private fun getAuthCookie(): Cookie =
        Cookie("auth", "${LocalDate.now()}")

    @BeforeEach
    fun addContacts() {
        mockMvc.perform(post("/app/add")
            .cookie(getAuthCookie())
            .param("name", "Roma")
            .param("phone", "89151371111"))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }


    @Test
    @WithMockUser(username = "admin", password = "user", roles = ["ADMIN"])
    fun addContactstest() {
        mockMvc.perform(post("/app/add")
                .cookie(getAuthCookie())
                .param("name", "Kostya")
                .param("phone", "89151371112"))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))

        mockMvc.perform(post("/app/add")
                .cookie(getAuthCookie())
                .param("name", "Gustav")
                .param("phone", "89151371112"))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }


    @Test
    @WithMockUser(username = "admin", password = "user", roles = ["ADMIN"])
    fun goToListTest() {
        mockMvc.perform(get("/app/list")
                .cookie(getAuthCookie()))
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
    }



    @Test
    @WithMockUser(username = "admin", password = "user", roles = ["ADMIN"])
    fun viewTest() {
        mockMvc.perform(get("/app/1397711761/view")
                .cookie(getAuthCookie()))
            .andExpect(status().isOk)
            .andExpect(view().name("view"))
    }

    @Test
    @WithMockUser(username = "admin", password = "user", roles = ["ADMIN"])
    fun deleteTest() {
        mockMvc.perform(get("/app/${"89151371111".hashCode()}/delete")
                .cookie(getAuthCookie()))
            .andExpect(status().is3xxRedirection)
    }

    @Test
    @WithMockUser(username = "admin", password = "user", roles = ["ADMIN"])
    fun editTest() {
        //ok
        mockMvc.perform(
            post("/app/${"89151371111".hashCode()}/edit")
                .cookie(getAuthCookie())
                .param("name", "Leha")
                .param("phone", "89151221111111"))
            .andExpect(status().is3xxRedirection)
    }
}
