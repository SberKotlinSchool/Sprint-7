package ru.sber.springmvc.controller

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import ru.sber.springmvc.model.UserEditDTO


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
internal class UserControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    fun userListViewTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(view().name("users"))
            .andExpect(content().string(containsString("Список пользователей")))
    }
    @Test
    fun userListViewNotAuthorizedTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("http://localhost/login"))
    }

    @WithMockUser(username = "user1", roles = ["USER"])
    @Test
    fun userFindViewTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/search"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(view().name("find-user"))
            .andExpect(content().string(containsString("Найти пользователя")))
    }

    @WithMockUser(username = "user1", roles = ["USER"])
    @Test
    fun userCreateViewTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/create"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(view().name("user"))
            .andExpect(content().string(containsString("Создать пользователя")))
    }

    @WithMockUser(username = "user1", roles = ["USER"])
    @Test
    fun userEditViewTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/edit/2"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(view().name("user"))
            .andExpect(content().string(containsString("Изменить пользователя")))
    }

    @WithMockUser(username = "user1", roles = ["USER"], authorities = ["ROLE_USER"])
    @Test
    fun userSaveTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/user/save")
                .with(csrf().asHeader())
                .flashAttr("user", createUserEditDTO()))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/"))
    }

    @WithMockUser(username = "user1", roles = ["DELETE_USER"])
    @Test
    fun userDeleteNormalTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/user/delete/3"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/"))
    }

    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    fun userDeleteNotAuthorizedTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/user/delete/3"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isForbidden)
    }

    fun createUserEditDTO(): UserEditDTO {
        val userEditDTO = UserEditDTO()
        userEditDTO.id = 0
        userEditDTO.name = "name"
        userEditDTO.login = "login"
        userEditDTO.password = "password"
        userEditDTO.address = "address"
        return userEditDTO
    }
}