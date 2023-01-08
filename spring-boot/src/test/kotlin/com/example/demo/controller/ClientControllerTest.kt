package com.example.demo.controller

import com.example.demo.service.ClientService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerTests {

    val PHONE_NUMBER = "+7 999 999-99-99"

    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockBean
    private lateinit var serviceMock: ClientService

    @Test
    fun getPhoneNumberSuccessTest() {
        `when`(serviceMock.getPhoneNumberById(1)).thenReturn(PHONE_NUMBER)
        val result = mockMvc.perform(MockMvcRequestBuilders.get(("/api/1/phone")))
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(PHONE_NUMBER))
    }
}