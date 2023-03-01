package ru.sber.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.view.RedirectView
import ru.sber.dto.Address

@SpringBootTest
@AutoConfigureMockMvc
class MvcControllerTest {
    private var mockMvc: MockMvc? = null
    @Autowired
    private lateinit var context: WebApplicationContext

    private val address = Address("n", "a", "p")


    @BeforeEach
    fun setup () {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply { SecurityMockMvcConfigurers.springSecurity() }
            .build()
    }

    @WithMockUser(username = "user1", password = "user1", roles = ["API"])
    @Test
    fun addAddress() {
        mockMvc!!
            .perform(
                post("/app/add")
                    .flashAttr("address", address)
            )
            .andExpect(status().is3xxRedirection)
            .andExpect { view() == RedirectView("list") }
    }

    @WithMockUser(username = "user1", password = "user1", roles = ["API"])
    @Test
    fun listAddress() {
        mockMvc!!
            .perform(get("/app/list"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(view().name("list"))
    }

    @WithMockUser(username = "user1", password = "user1", roles = ["API"])
    @Test
    fun viewTest() {
        mockMvc!!.perform(
            post("/app/add")
                .flashAttr("address", address)
        )
        mockMvc!!
            .perform(get("/app/0/view"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(view().name("view"))
    }

    @WithMockUser(username = "user1", password = "user1", roles = ["API"])
    @Test
    fun edit() {
        mockMvc!!
            .perform(
                post("/app/0/edit")
                    .flashAttr("address", Address())
            )
            .andExpect(status().is3xxRedirection)
            .andExpect { view() == RedirectView("../list") }
    }

    @WithMockUser(username = "user1", password = "user1", roles = ["API"])
    @Test
    fun delete() {
        mockMvc!!
            .perform(post("/app/0/delete"))
            .andExpect { view() == RedirectView("../list") }
    }
}