package ru.sber.springsec.controller

import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.sber.springsec.service.BookRow
import ru.sber.springsec.service.BookService
import java.time.LocalDateTime
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
internal class MvcControllerTest {

    companion object {
        @JvmStatic
        fun getRequests(): Array<Arguments> = arrayOf(
            Arguments.of(post("/app/add")),
            Arguments.of(get("/app/asd/view")),
            Arguments.of(get("/app/list")),
            Arguments.of(post("/app/asd/edit")),
            Arguments.of(post("/app/asd/delete"))
        )
    }
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var service: BookService
    private val one = BookRow("test", "test address")
    private val two = BookRow("test2", "test address 2")
    private lateinit var testAuthCookie: Cookie

    @BeforeEach
    fun setup() {
        service.add(one)
        testAuthCookie = Cookie("auth", LocalDateTime.now().minusDays(1).toString())
    }

    @AfterEach
    fun teardown() = service.deleteAll()

    @ParameterizedTest
    @MethodSource("getRequests")
    fun requestWithNoAuth(request: MockHttpServletRequestBuilder) {
        mockMvc.perform(request).andDo(print()).andExpect(status().isUnauthorized)
    }

    @Test @WithMockUser(username = "app")
    fun add() {
        mockMvc.perform(post("/app/add").param("name", two.name)
                .param("address", two.address).cookie(testAuthCookie))
                .andDo(print()).andExpect(status().is3xxRedirection)
                .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))

        checkView(two.name, two.address)
    }

    @Test @WithMockUser(username = "app")
    fun view() = checkView(one.name, one.address)

    @Test @WithMockUser(username = "app")
    fun list() {
        mockMvc.perform(get("/app/list").cookie(testAuthCookie))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andExpect(content().string(containsString(one.name)))
            .andExpect(content().string(containsString(one.address)))
            .andExpect(content().string(containsString("/app/${one.name}/view")))
            .andExpect(content().string(containsString("/app/${one.name}/delete")))
    }

    @Test @WithMockUser(username = "app")
    fun edit() {
        mockMvc.perform(post("/app/{name}/edit", one.name)
                .param("name", one.name).param("address", two.address)
                .cookie(testAuthCookie)).andDo(print()).andExpect(status().is3xxRedirection)
                .andExpect(MockMvcResultMatchers.view().name("redirect:/app/${one.name}/view"))

        checkView(one.name, two.address)
    }

    @Test @WithMockUser(username = "app", roles = ["ADMIN"])
    fun delete() {
        mockMvc.perform(post("/app/{name}/delete", one.name).cookie(testAuthCookie))
            .andDo(print()).andExpect(status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))

        mockMvc.perform(get("/app/list").cookie(testAuthCookie))
            .andDo(print()).andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andExpect(content().string(not(containsString(one.name))))
            .andExpect(content().string(not(containsString(one.address))))
            .andExpect(content().string(not(containsString("/app/${one.name}/view"))))
            .andExpect(content().string(not(containsString("/app/${one.name}/delete"))))
    }

    @Test
    @WithMockUser(username = "app")
    fun deleteWithNoAdminUser() {
        mockMvc.perform(post("/app/{name}/delete", one.name).cookie(testAuthCookie))
            .andDo(print()).andExpect(status().isForbidden)
    }

    private fun checkView(name: String, address: String) {
        mockMvc.perform(get("/app/{name}/view", name).cookie(testAuthCookie))
            .andDo(print()).andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("view"))
            .andExpect(content().string(containsString(name)))
            .andExpect(content().string(containsString(address)))
            .andExpect(content().string(containsString("/app/$name/edit")))
    }
}