package ru.sber.ufs.cc.kulinich.springmvc


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ru.sber.ufs.cc.kulinich.springmvc.models.AddressBookModel
import java.time.LocalDate
import javax.servlet.http.Cookie

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SpringRESTApplicationTests {

    @LocalServerPort
    private var port: Int = 0


    private fun getAuthCookie(): Cookie =
        Cookie("auth", "${LocalDate.now()}")



    @Autowired
    lateinit var mockMvc: MockMvc

    private fun url(path: String): String {
        return "http://localhost:${port}/${path}"
    }

    val testData = """{"name" : "roman", "phone" : "89151111111"}"""

    @BeforeEach
    fun setUp() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/add")
                .content(testData)
                .contentType(MediaType.APPLICATION_JSON)
        )
    }



    @Test
    @WithMockUser(username = "user", password = "user", roles = ["API"])
    fun addClient() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/add")
                .content(testData)
                .contentType(MediaType.APPLICATION_JSON)
        )   .andExpect(jsonPath("$.phone").value("89151111111"))
            .andExpect(jsonPath("$.name").value("roman"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }


    @Test
    @WithMockUser(username = "user", password = "user", roles = ["API"])
    fun getAllClients() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(url("api/list"))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].phone").value("89151111111"))
            .andExpect(jsonPath("$[0].name").value("roman"))
    }


    @Test
    @WithMockUser(username = "gustav", password = "user", roles = ["API"])
    fun deleteClientWithoutRight() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/1334912332/delete")
        )
            .andExpect(status().isMethodNotAllowed)
    }

}
