package ru.sber.addressbook

import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import ru.sber.addressbook.dto.AddressModel

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

  @Autowired
  private lateinit var context: WebApplicationContext

  @Autowired
  private lateinit var mockMvc: MockMvc

  @BeforeEach
  fun addTestEntry() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply<DefaultMockMvcBuilder>(springSecurity())
        .build()
  }

  @Test
  @WithMockUser(username = "admin", password = "admin", authorities = ["APP_ACCESS"])
  fun addAddress() {
    val address = AddressModel(name = "Иванов Иван Иванович", address = "Улица Пушкина, 2")


    mockMvc.post("/app/add") {
      param("name", address.name)
      param("address", address.address)
    }
        .andDo {
          print()
        }
        .andExpect {
          status { is3xxRedirection() }
          redirectedUrl("/app/list")
        }
  }

  @Test
  fun addRedirectToLogin() {
    val address = AddressModel(name = "Иванов Иван Иванович", address = "Улица Пушкина, 2")

    mockMvc.post("/app/add") {
      param("name", address.name)
      param("address", address.address)
    }
        .andDo {
          print()
        }
        .andExpect {
          status { is3xxRedirection() }
          redirectedUrl("http://localhost/login")
        }
  }

  @Test
  @WithMockUser(username = "admin", password = "admin", authorities = ["API_ACCESS"])
  fun addForbidden() {
    val address = AddressModel(name = "Иванов Иван Иванович", address = "Улица Пушкина, 2")

    mockMvc.post("/app/add") {
      param("name", address.name)
      param("address", address.address)
    }
        .andDo {
          print()
        }
        .andExpect {
          status { isForbidden() }
        }
  }

  @Test
  @WithMockUser(username = "admin", password = "admin", authorities = ["API_ACCESS"])
  fun getListOk() {
    mockMvc.get("/api/list")
        .andDo { print() }
        .andExpect {
          status { isOk() }
        }
  }
}