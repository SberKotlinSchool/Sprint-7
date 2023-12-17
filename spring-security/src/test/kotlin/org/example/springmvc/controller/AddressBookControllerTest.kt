package org.example.springmvc.controller

import org.example.springmvc.entity.Contact
import org.example.springmvc.service.AddressBookService
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import javax.servlet.http.Cookie

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
internal class AddressBookControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var addressBookService: AddressBookService
    private lateinit var testAuthCookie: Cookie
    private lateinit var testContact01: Contact
    private lateinit var testContact02: Contact

    @BeforeEach
    fun setUp() {
        testContact01 = Contact(1, "One", "Moscow", "89111111111")
        addressBookService.add(testContact01)
        testContact02 = Contact(2, "Two", "Tomsk", "89222222222")
        testAuthCookie = Cookie("auth", LocalDateTime.now().plusMinutes(15).toString())
    }

    @AfterEach
    fun tearDown() = addressBookService.deleteAll()

    @Test
    @WithMockUser(username = "USER_APP", roles = ["APP"])
    fun add() {
        mockMvc.perform(
            post("/app/add")
                .flashAttr("contact", testContact02)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))
        isViewExpected(
            2.toString(),
            testContact02.name!!,
            testContact02.city!!,
            testContact02.phone!!
        )
    }

    @Test
    @WithMockUser(username = "USER_APP", roles = ["APP"])
    fun view() {
        isViewExpected(
            testContact01.id.toString(),
            testContact01.name!!,
            testContact01.city!!,
            testContact01.phone!!
        )
    }

    @Test
    @WithMockUser(username = "USER_APP", roles = ["APP"])
    fun edit() {
        mockMvc.perform(
            post("/app/{id}/edit", testContact01.id)
                .flashAttr("contact", testContact02)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))
        isViewExpected(
            testContact01.id.toString(),
            testContact02.name!!,
            testContact02.city!!,
            testContact02.phone!!
        )
    }

    @Test
    @WithMockUser(username = "USER_APP", roles = ["APP"])
    fun list() {
        mockMvc.perform(get("/app/list").cookie(testAuthCookie))
            .andDo(print()).andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andExpect(content().string(containsString("/app/add")))
            .andExpect(content().string(containsString("/app/${testContact01.id}/view")))
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = ["ADMIN"])
    fun delete() {
        mockMvc.perform(post("/app/{id}/delete", testContact01.id).cookie(testAuthCookie))
            .andDo(print()).andExpect(status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))

        mockMvc.perform(get("/app/list").cookie(testAuthCookie))
            .andDo(print()).andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andExpect(content().string(not(containsString(testContact01.name))))
            .andExpect(content().string(not(containsString(testContact01.city))))
            .andExpect(content().string(not(containsString("/app/${testContact01.id}/view"))))
            .andExpect(content().string(not(containsString("/app/${testContact01.id}/delete"))))
    }

    @Test
    @WithMockUser(username = "USER_API", roles = ["API"])
    fun listWithoutGrants() {
        mockMvc.perform(
            get("/app/list")
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "USER_APP", roles = ["APP"])
    fun deleteWithoutGrants() {
        mockMvc.perform(
            post("/app/{id}/delete", testContact01.id)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().isForbidden)
    }

    private fun isViewExpected(id: String, name: String, city: String, phone: String) {
        mockMvc.perform(
            get("/app/{id}/view", id)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("view"))
            .andExpect(content().string(containsString(name)))
            .andExpect(content().string(containsString(city)))
            .andExpect(content().string(containsString(phone)))
    }
}