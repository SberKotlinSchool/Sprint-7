package ru.company


import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AddressBookRestController {
    @LocalServerPort
    private val port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private fun url(path: String): String {
        return "http://localhost:${port}/${path}"
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    val jsonData = """{"fio" : "Ivanov", "address" : "town"}"""


    @BeforeEach
    fun setUp() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/add")
                .content(jsonData)
                .contentType(MediaType.APPLICATION_JSON)
        )
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = ["API"])
    fun addClient() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/add")
                .content(jsonData)
                .contentType(MediaType.APPLICATION_JSON)
        )   .andExpect(jsonPath("$.address").value("town"))
            .andExpect(jsonPath("$.fio").value("Ivanov"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }


    @Test
    @WithMockUser(username = "user", password = "user", roles = ["API"])
    fun getAllClients() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(url("api/list"))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$[0].address").value("town"))
            .andExpect(jsonPath("$[0].fio").value("Ivanov"))
    }


    @Test
    @WithMockUser(username = "user", password = "user", roles = ["API"])
    fun deleteClientWithoutRight() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/1/delete")
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }


    @Test
    @WithMockUser(username = "user", password = "user", roles = ["ADMIN"])
    fun deleteClient() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/1/delete")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}
