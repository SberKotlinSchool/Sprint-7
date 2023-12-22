package ru.sber.springmvc

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppControllerTest {
    private companion object {
        private const val NAME = "123"
        private const val ADDRESS = "666"
        private const val PHONE = "999"
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun addPersonGetTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/app/person/add").headers(getAuthHeaders("admin")))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("add"))
    }

    @Test
    fun addPersonPostTest() {
        addTestPerson()
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))

    }

    @Test
    fun listPersonsTest() {
        addTestPerson()
        checkListRequestPerform(MockMvcRequestBuilders.get("/app/list").headers(getAuthHeaders("admin")))
        checkListRequestPerform(MockMvcRequestBuilders.get("/app/list").headers(getAuthHeaders("admin"))
            .param("name", NAME))
    }

    @Test
    fun deletePersonTest() {
        addTestPerson()
        mockMvc.perform(MockMvcRequestBuilders.delete("/app/person/1/delete").headers(getAuthHeaders("admin")))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))
    }

    @Test
    fun editPersonTest() {
        addTestPerson()
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/person/1/edit").headers(getAuthHeaders("admin"))
                .param("name", "$NAME Edit")
                .param("address", ADDRESS)
                .param("phone", PHONE)
        )
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/list").headers(getAuthHeaders("admin"))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("$NAME Edit")))
    }

    private fun addTestPerson(): ResultActions {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/app/person/add").headers(getAuthHeaders("admin"))
                .param("name", NAME)
                .param("address", ADDRESS)
                .param("phone", PHONE)
        )
    }

    private fun checkListRequestPerform(requestBuilder: RequestBuilder) {
        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(NAME)))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(ADDRESS)))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(PHONE)))
    }

    fun getAuthHeaders(role: String): HttpHeaders {
        val headers = HttpHeaders()
        headers.setBasicAuth(role, role)
        return headers
    }
}