package ru.sber.addressbook.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.util.NestedServletException
import ru.sber.addressbook.data.Contact
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressBookControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private var mockMvc: MockMvc? = null

    @BeforeEach
    fun setup () {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply { springSecurity() }
            .build()
    }


    @WithMockUser(username = "user1", password = "user1", roles = ["API"])
    @Test
    fun getAddressBookTest() {
        mockMvc!!.perform(get("/app/list"))
            .andExpect {
                status().isOk()
                content().string("addressbook")

            }
    }

    @WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
    @Test
    fun createGETTest() {
        mockMvc!!.perform(get("/app/add"))
            .andExpect {
                status().isOk()
                content().string("contact")
            }
    }

    @WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
    @Test
    fun createPOSTTest() {
        mockMvc!!.perform(post("/app/add")
            .flashAttr("contact", Contact(
                "Сидоров", "Александр", "Федорович",
                LocalDate.parse("10.02.2001", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                "+7 111 111-11-11", "sidorova@mail.ru"
            )
            ))
            .andExpect {
                status().is3xxRedirection
                redirectedUrl("/app/list")
            }
    }

    @WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
    @Test
    fun editGETTest() {
        mockMvc!!.perform(get("/app/1/edit"))
            .andExpect {
                status().isOk()
                content().string("contact")
            }
    }

    @WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
    @Test
    fun editPOSTTest() {
        mockMvc!!.perform(post("/app/1/edit")
            .flashAttr("contact", Contact(
                "Попов", "Иван", "Сергеевич",
                LocalDate.parse("01.01.1999", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                "+7 333 333-33-33", "popovi@mail.ru"
            )
            ))
            .andExpect {
                status().is3xxRedirection
                redirectedUrl("/app/list")
            }
    }

    @WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
    @Test
    fun readGETTest() {
        mockMvc!!.perform(get("/app/1/view"))
            .andExpect {
                status().isOk()
                content().string("contact")
            }
    }

    @WithMockUser(username = "user2", password = "user2", roles = ["API_WITH_DELETE"])
    @Test
    fun deleteGETTest() {
        mockMvc!!.perform(get("/app/2/delete"))
            .andExpect {
                status().is3xxRedirection
                redirectedUrl("/app/list")
            }
    }

    @WithMockUser(username = "user1", password = "user1", roles = ["API"])
    @Test
    fun deleteWithoutPermissionGETTest() {
        assertThrows<NestedServletException> {
            mockMvc!!.perform(get("/app/2/delete"))
        }
    }

}

