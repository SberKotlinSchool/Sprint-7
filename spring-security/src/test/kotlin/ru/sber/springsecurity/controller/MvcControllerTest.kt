package ru.sber.springsecurity.controller

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
import ru.sber.springsecurity.model.AddressBookRow
import ru.sber.springsecurity.service.AddressBookService
import java.time.LocalDateTime
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
internal class MvcControllerTest {

    companion object {
        @JvmStatic
        fun getRequestsToAllEndpoints(): Array<Arguments> = arrayOf(
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
    private lateinit var service: AddressBookService
    private val testRow = AddressBookRow("test", "test address")
    private val testRow2 = AddressBookRow("test2", "test address 2")
    private lateinit var testAuthCookie: Cookie

    @BeforeEach
    fun setup() {
        service.add(testRow)
        testAuthCookie = Cookie("auth", LocalDateTime.now().minusDays(1).toString())
    }

    @AfterEach
    fun teardown() {
        service.deleteAll()
    }

    @ParameterizedTest
    @MethodSource("getRequestsToAllEndpoints")
    fun requestWithNoAuthReturnsUnauthorized(request: MockHttpServletRequestBuilder) {
        mockMvc.perform(request)
            .andDo(print())
            .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(username = "app")
    fun add() {
        mockMvc.perform(
            post("/app/add")
                .param("name", testRow2.name)
                .param("address", testRow2.address)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))

        checkView(testRow2.name, testRow2.address)
    }

    @Test
    @WithMockUser(username = "app")
    fun view() {
        checkView(testRow.name, testRow.address)
    }

    @Test
    @WithMockUser(username = "app")
    fun list() {
        mockMvc.perform(get("/app/list").cookie(testAuthCookie))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andExpect(content().string(containsString(testRow.name)))
            .andExpect(content().string(containsString(testRow.address)))
            .andExpect(content().string(containsString("/app/${testRow.name}/view")))
            .andExpect(content().string(containsString("/app/${testRow.name}/delete")))
    }

    @Test
    @WithMockUser(username = "app")
    fun edit() {
        mockMvc.perform(
            post("/app/{name}/edit", testRow.name)
                .param("name", testRow.name)
                .param("address", testRow2.address)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/${testRow.name}/view"))

        checkView(testRow.name, testRow2.address)
    }

    @Test
    @WithMockUser(username = "app", roles = ["ADMIN"])
    fun deleteWithAdminUser() {
        mockMvc.perform(post("/app/{name}/delete", testRow.name).cookie(testAuthCookie))
            .andDo(print())
            .andExpect(status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))

        mockMvc.perform(get("/app/list").cookie(testAuthCookie))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andExpect(content().string(not(containsString(testRow.name))))
            .andExpect(content().string(not(containsString(testRow.address))))
            .andExpect(content().string(not(containsString("/app/${testRow.name}/view"))))
            .andExpect(content().string(not(containsString("/app/${testRow.name}/delete"))))
    }

    @Test
    @WithMockUser(username = "app")
    fun deleteWithNoAdminUserReturnsForbidden() {
        mockMvc.perform(post("/app/{name}/delete", testRow.name).cookie(testAuthCookie))
            .andDo(print())
            .andExpect(status().isForbidden)
    }

    private fun checkView(name: String, address: String) {
        mockMvc.perform(get("/app/{name}/view", name).cookie(testAuthCookie))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("view"))
            .andExpect(content().string(containsString(name)))
            .andExpect(content().string(containsString(address)))
            .andExpect(content().string(containsString("/app/$name/edit")))
    }
}
