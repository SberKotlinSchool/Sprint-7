package ru.sber.app

import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import ru.sber.domain.Record
import ru.sber.domain.RecordRepository

@SpringBootTest
@AutoConfigureMockMvc
class MvcControllerTest {

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Autowired
  private lateinit var repository: RecordRepository

  @BeforeEach
  fun setUp() {
    repository.deleteAll()
  }

  fun initRepository(name: String): Record =
    repository.save(
      Record(
        name = name, lastName = "lastname", secondName = "secondname",
        phoneNumber = "134354", city = "city", street =  "street",
        houseNumber = 1, postcode = 123234, username = "user"
      ))

  @Test
  fun getListForbiddenForUnauthorizedUser() {
    mockMvc
      .perform(get("/app/list"))
      .andExpect(status().is3xxRedirection)
  }

  @Test
  @WithMockUser(username = "user", password = "pass", roles = ["API"])
  fun getListForbiddenForApiRole() {
    mockMvc
      .perform(get("/app/list"))
      .andExpect(status().isForbidden)
  }


  @Test
  @WithMockUser(username = "user", password = "pass", roles = ["APP","ADMIN"])
  fun getListSuccessForAppAndAdminRole() {
    val name = "testAppList"
    initRepository(name)
    mockMvc.perform(get("/app/list"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isOk())
      .andExpect(view().name("main"))
      .andExpect(content().string(containsString(name)))
  }

  @Test
  @WithMockUser(username = "user", password = "pass", roles = ["APP","ADMIN"])
  fun getSearchListSuccessForAppAndAdminRole() {
    val name = "testAppSearchList"
    initRepository(name)
    mockMvc.perform(get("/app/search").param("query", "testAppSearchList"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isOk())
      .andExpect(view().name("main"))
      .andExpect(content().string(containsString(name)))
  }

  @Test
  @WithMockUser(username = "user", password = "pass", roles = ["APP"])
  fun addRecordSuccessForAppRole() {
    mockMvc.perform(post("/app/add").with(csrf())
      .flashAttr("record", Record(name = "testAddRecord")))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(redirectedUrl("/app/list"))

    mockMvc.perform(get("/app/list"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isOk())
      .andExpect(view().name("main"))
      .andExpect(content().string(containsString("testAddRecord")))
  }

  @Test
  @WithMockUser(username = "user", password = "pass", roles = ["ADMIN"])
  fun addRecordForbiddenForAdminRole() {
    mockMvc.perform(post("/app/add").with(csrf())
      .flashAttr("record", Record(name = "testAddRecord")))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isForbidden)
  }

  @Test
  @WithMockUser(username = "user", password = "pass", roles = ["APP","ADMIN"])
  fun viewRecordSuccessForAppAndAdminRole() {
    val name = "testAppView"
    val id = initRepository(name).id
    mockMvc.perform(get("/app/${id}/view"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isOk())
      .andExpect(view().name("view"))
      .andExpect(content().string(containsString(name)))
  }

  @Test
  @WithMockUser(username = "user", password = "pass", roles = ["APP"])
  fun editRecordSuccessForAppRole() {
    val name = "testAppEdit"
    val id = initRepository(name).id
    mockMvc.perform(
      post("/app/${id}/edit").with(csrf())
        .flashAttr("record", Record(name = "testEditRecord"))
    )
      .andDo(MockMvcResultHandlers.print())
      .andExpect(redirectedUrl("/app/${id}/view"))

    mockMvc.perform(
      get("/app/${id}/view")
    )
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isOk())
      .andExpect(view().name("view"))
      .andExpect(content().string(containsString("testEditRecord")))
  }

  @Test
  @WithMockUser(username = "user", password = "pass", roles = ["ADMIN"])
  fun editRecordForbiddenForAdminRole() {
    mockMvc.perform(
      post("/app/1/edit").with(csrf())
        .flashAttr("record", Record(name = "testEditRecord"))
    )
    .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isForbidden)
  }

  @Test
  @WithMockUser(username = "user", password = "pass", roles = ["ADMIN"])
  fun deleteRecordSuccessForAdminRole() {
    val name = "testAppDelete"
    val id = initRepository(name).id
    mockMvc.perform(post("/app/${id}/delete")
      .with(csrf()))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(redirectedUrl("/app/list"))

    mockMvc.perform(get("/app/list"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isOk())
      .andExpect(view().name("main"))
      .andExpect(content().string(not(containsString(name))))
  }

  @Test
  @WithMockUser(username = "user", password = "pass", roles = ["APP"])
  fun deleteRecordForbiddenForAppRole() {
    mockMvc.perform(post("/app/1/delete")
      .with(csrf()))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isForbidden)
  }
}