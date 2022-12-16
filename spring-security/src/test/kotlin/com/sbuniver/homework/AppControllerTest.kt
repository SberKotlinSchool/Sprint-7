package com.sbuniver.homework

import org.hamcrest.Matchers.not
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class AppControllerTest {

    @Autowired
    private lateinit var mock: MockMvc

    @WithMockUser(value = "admin", password = "admin", roles = ["ADMIN"])
    @Test
    fun getAllAddressesTest() {
        mock.perform(MockMvcRequestBuilders.get("/app/list"))
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(StringContains("Andrey")))
    }

    @WithMockUser(value = "apiuser", password = "apiuser", roles = ["APIUSER"])
    @Test
    fun `getAllAddresses forbidden for APIUSER`() {
        mock.perform(MockMvcRequestBuilders.get("/app/list"))
            .andExpect(status().is4xxClientError)
    }

    @WithMockUser(value = "user", password = "user", roles = ["USER"])
    @Test
    fun getOneAddressTest() {
        mock.perform(MockMvcRequestBuilders.get("/app/2/view"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(StringContains("Alexey")))
            .andExpect(MockMvcResultMatchers.content().string(StringContains("Sochi")))
    }

    @WithMockUser(value = "user", password = "user", roles = ["USER"])
    @Test
    fun addAddressTest() {
        mock
            .perform(
                MockMvcRequestBuilders
                    .post("/app/add")
                    .param("name", "Newman")
                    .param("name", "Newman")
                    .param("city", "New York")
                    .param("street", "New Street")
                    .param("home", "100")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is3xxRedirection)

        mock.perform(MockMvcRequestBuilders.get("/app/list"))
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(StringContains("Newman")))
            .andExpect(MockMvcResultMatchers.content().string(StringContains("New York")))
            .andExpect(MockMvcResultMatchers.content().string(StringContains("New Street")))
    }

    @WithMockUser(value = "user", password = "user", roles = ["USER"])
    @Test
    fun deleteAddressTest() {
        mock.perform(MockMvcRequestBuilders.get("/app/list"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().string(StringContains("Andrey")))

        mock
            .perform(MockMvcRequestBuilders.delete("/app/5/delete"))
            .andDo(MockMvcResultHandlers.print())

        mock.perform(MockMvcRequestBuilders.get("/app/list"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().string(not(StringContains("Sergey"))))
    }

    @WithMockUser(value = "user", password = "user", roles = ["USER"])
    @Test
    fun editAddressTest() {
        mock.perform(
            MockMvcRequestBuilders
                .put("/app/6/edit")
                .param("name", "Newman")
                .param("name", "Newman")
                .param("city", "New York")
                .param("street", "New Street")
                .param("home", "100")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is3xxRedirection)

        mock.perform(MockMvcRequestBuilders.get("/app/list"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().string(not(StringContains("John"))))
            .andExpect(MockMvcResultMatchers.content().string(StringContains("Newman")))
    }
}