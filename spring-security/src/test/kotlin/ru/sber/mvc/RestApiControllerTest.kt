package ru.sber.mvc

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import org.hamcrest.Matchers.containsString
import org.springframework.test.web.servlet.MockMvc
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import ru.sber.mvc.models.Address


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RestApiControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var mock: MockMvc

    private fun url(path: String): String {
        return "http://localhost:${port}/${path}"
    }

    private fun asJsonString(obj: Address) : String {
        try {
            return ObjectMapper().writeValueAsString(obj)
        } catch (e :Exception) {
            throw RuntimeException(e)
        }
    }

    @WithMockUser(username = "writer", roles = ["WRITER"])
    @Test
    fun `test get list api no API role`() {
        mock.perform(MockMvcRequestBuilders.get(url("/api/list")))
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @WithMockUser(username = "admin", roles = ["API"])
    @Test
    fun `test get list api with API role`() {
        mock.perform(MockMvcRequestBuilders.get(url("/api/list")))
            .andExpect(MockMvcResultMatchers.status().isOk)

    }

    @WithMockUser(username = "admin", roles = ["API"])
    @Test
    fun `test get list api`() {
        mock.perform( MockMvcRequestBuilders.get(url("/api/list")))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(content().string(containsString("John")))
            .andExpect(content().string(containsString("7-924-202-23-01")))
    }

    @WithMockUser(username = "admin", roles = ["API"])
    @Test
    fun `test get list api with parameter`() {
        mock.perform( MockMvcRequestBuilders.get(url("/api/list?name=Billy&phone=7-924-202-35-02")))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(content().string(containsString("Billy")))
            .andExpect(content().string(containsString("7-924-202-35-02")))
    }

    @WithMockUser(username = "admin", roles = ["API"])
    @Test
    fun `test get view api`() {
        mock.perform( MockMvcRequestBuilders.get(url("/api/0/view")))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(content().string(containsString("John")))
            .andExpect(content().string(containsString("7-924-202-23-01")))
    }

    @WithMockUser(username = "admin", roles = ["API"])
    @Test
    fun `test add api`() {
        mock.perform(
            MockMvcRequestBuilders.post(url("/api/add"))
                .with(csrf())
                .content(asJsonString(Address(id = null, name = "Jimmy", phone = "7-956-489-51-62", descr = "Bad man")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect(content().string(containsString("Jimmy")))
        .andExpect(content().string(containsString("7-956-489-51-62")))
    }

    @WithMockUser(username = "admin", roles = ["API"])
    @Test
    fun `test edit api new`() {

        mock.perform(
            MockMvcRequestBuilders.put(url("api/0/edit"))
                .with(csrf())
                .content(asJsonString(Address(id = 0, name = "Alexander", phone = "7-956-489-51-62", descr = "Bad man")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect(content().string(containsString("Alexander")))
        .andExpect(content().string(containsString("7-956-489-51-62")))
    }

    @WithMockUser(username = "writer", roles = ["WRITER"])
    @Test
    fun deleteAddressWhenRoleIsNotRest() {
        mock.perform(
            MockMvcRequestBuilders.delete(url("/api/0/delete"))
            .with(csrf())
        )
        .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @WithMockUser(username = "admin", roles = ["API"])
    @Test
    fun `test delete api`() {
        mock.perform(
            MockMvcRequestBuilders.delete(url("/api/0/delete"))
            .with(csrf())
        )
        .andExpect(MockMvcResultMatchers.status().isOk)

    }
}