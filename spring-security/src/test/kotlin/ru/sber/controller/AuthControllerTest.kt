package ru.sber.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.sber.persistence.AddressEntity
import ru.sber.service.AddressService

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var addressService: AddressService


    @AfterEach
    fun clearDb() {
        addressService.deleteAll()
    }

    @Test
    fun `unauthorized GET request`() {
        val reqBuilder = request(HttpMethod.GET, "/app/list")

        mockMvc.perform(reqBuilder)
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("http://localhost/login"))
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["USER"])
    @Test
    fun `authorized GET request`() {
        val reqBuilder = request(HttpMethod.GET, "/app/list")

        mockMvc.perform(reqBuilder)
                .andExpect(status().is2xxSuccessful)
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["USER"])
    @Test
    fun `forbidden DELETE request`() {
        val createdAddress = addressService.createAddress(AddressEntity(name = "улица Шкловского"))

        val reqBuilder = request(HttpMethod.POST, "/app/${createdAddress.id}/delete")

        mockMvc.perform(reqBuilder)
                .andExpect(status().`is`(HttpStatus.FORBIDDEN.value()))

        assertEquals(listOf(createdAddress), addressService.findAll())
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["ADMIN"])
    @Test
    fun `authorized  DELETE request`() {
        val createdAddress = addressService.createAddress(AddressEntity(name = "улица Шкловского"))

        val reqBuilder = request(HttpMethod.POST, "/app/${createdAddress.id}/delete")

        mockMvc.perform(reqBuilder)
                .andExpect(status().is2xxSuccessful)

        assertEquals(listOf<AddressEntity>(), addressService.findAll())
    }


    @WithMockUser(username = "some_user", password = "some_password", roles = ["USER"])
    @Test
    fun `unauthorized API request`() {
        addressService.createAddress(AddressEntity(name = "улица Шкловского"))

        val reqBuilder = request(HttpMethod.GET, "/api/list")

        mockMvc.perform(reqBuilder)
                .andExpect(status().`is`(HttpStatus.FORBIDDEN.value()))
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["API"])
    @Test
    fun `authorized API request`() {
        addressService.createAddress(AddressEntity(name = "улица Шкловского"))

        val reqBuilder = request(HttpMethod.GET, "/api/list")

        mockMvc.perform(reqBuilder)
                .andExpect(status().is2xxSuccessful)

    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["API"])
    @Test
    fun `unauthorized API DELETE request`() {
        val createdAddress = addressService.createAddress(AddressEntity(name = "улица Шкловского"))

        val reqBuilder = request(HttpMethod.POST, "/api/${createdAddress.id}/delete")

        mockMvc.perform(reqBuilder)
                .andExpect(status().`is`(HttpStatus.FORBIDDEN.value()))

        assertEquals(listOf(createdAddress), addressService.findAll())
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["ADMIN"])
    @Test
    fun `authorized API DELETE request`() {
        val createdAddress = addressService.createAddress(AddressEntity(name = "улица Шкловского"))

        val reqBuilder = request(HttpMethod.DELETE, "/api/${createdAddress.id}/delete")

        mockMvc.perform(reqBuilder)
                .andExpect(status().is2xxSuccessful)

        assertEquals(listOf<AddressEntity>(), addressService.findAll())
    }

}