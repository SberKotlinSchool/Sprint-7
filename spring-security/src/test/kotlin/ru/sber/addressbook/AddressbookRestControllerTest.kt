package ru.sber.addressbook

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
import org.springframework.http.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureEmbeddedDatabase(
    provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
class AddressbookRestControllerTest {

    @LocalServerPort
    private var port: Int = 8080


    @Autowired
    private lateinit var mockMvc: MockMvc

    private fun url(path: String): String {
        return "http://localhost:${port}/${path}"
    }


    @BeforeEach
    fun setUp() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/add")
                .content("""{"name" : "danil", "phone" : "8-999-999-99-99"}""")
                .contentType(MediaType.APPLICATION_JSON)
        )
    }



    @Test
    @WithMockUser(username = "api", password = "api", roles = ["API"])
    fun `add client test`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/add")
                .content("""{"name" : "max", "phone" : "8-888-888-88-88"}""")
                .contentType(MediaType.APPLICATION_JSON)
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get(url("api/list"))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("max")))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("8-888-888-88-88")))
    }


    @Test
    @WithMockUser(username = "api", password = "api", roles = ["API"])
    fun `list clients test`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(url("api/list"))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("danil")))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("8-999-999-99-99")))
    }


    @Test
    @WithMockUser(username = "pavel", password = "pavel", roles = ["API"])
    fun `delete clients test bad`() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/danil/delete")
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @WithMockUser(username = "api", password = "api", roles = ["ADMIN"])
    fun `delete clients test good`() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/danil/delete")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

}