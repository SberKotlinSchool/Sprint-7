package ru.sber.spring.boot.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(Controller::class)
class ControllerTest {

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Test
  fun `GET spring should return content successfully with status 200 OK`(){
    val result = mockMvc.perform(MockMvcRequestBuilders.get(("/spring")))

    result
      .andExpect(MockMvcResultMatchers.status().isOk)
      .andExpect(MockMvcResultMatchers.content().string("hello"))
  }

}