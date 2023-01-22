package ru.sbrf.addressbook

import org.apache.commons.lang3.StringUtils
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ru.sbrf.addressbook.core.UserDetailsAdapter
import ru.sbrf.addressbook.core.UserRepository

@AutoConfigureMockMvc
@DirtiesContext
@TestInstance(PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AddressBookMvcControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var repository: UserRepository

    private var userDetailsAdapter: UserDetailsAdapter? = null;

    @BeforeAll
    fun initUser() {
        userDetailsAdapter = UserDetailsAdapter(repository.findById(1L).get())
    }

    @Test
    fun addEmployeeGetTest() {
        mockMvc.perform(get("/app/add").with(csrf())
            .with(user(userDetailsAdapter)))
            .andExpect(status().isOk)
            .andExpect(view().name("add"))
    }


    @Test
    fun addEmployeePostTest() {
        mockMvc.perform(
            post("/app/add")
                .param("firstName", "Имян")
                .param("lastName", "Фамильевич")
                .param("patronymic", "Отчествич")
                .param("address", "Ул. Птушкина, д. Колотушкина")
                .param("phone", "стационарный")
                .param("email", "test@email.com")
                .with(csrf())
                .with(user(userDetailsAdapter))
        ).andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }


    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun listEmployeesGetAll() {
        addValidEmployee(mockMvc)
        mockMvc.perform(get("/app/list").with(csrf())
            .with(user(userDetailsAdapter)))
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
            .andExpect(content().string(containsString("Имян")))
    }

    @Test
    fun listEmployeesGetByQuery() {
        addValidEmployee(mockMvc)
        addSpecificEmployee(mockMvc, "Александр", "Пушкин")
        mockMvc.perform(get("/app/list").param("lastname", "Пушкин").with(csrf())
            .with(user(userDetailsAdapter)))
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
            .andExpect(content().string(containsString("Пушкин")))
            .andExpect(content().string(not(containsString("Имян"))))
    }

    @Test
    fun viewEmployee() {
        addValidEmployee(mockMvc)
        val rs = mockMvc.perform(get("/app/list")
            .with(csrf())
            .with(user(userDetailsAdapter)))
            .andReturn()
            .response
            .getContentAsString()

        val id = StringUtils
            .substringBetween(rs, "    <tr>\n        <td>", "</td>")
            .toInt()

        mockMvc.perform(get("/app/${id}/view")
            .with(csrf())
            .with(user(userDetailsAdapter)))
            .andExpect(status().isOk)
    }

    @Test
    fun deleteEmployee() {
        addValidEmployee(mockMvc)
        mockMvc.perform(get("/app/5/delete")
            .with(csrf())
            .with(user(userDetailsAdapter)))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun editCustomers() {
        addValidEmployee(mockMvc)
        mockMvc.perform(
            post("/app/5/edit")
                .param("firstName", "ИмянEdit")
                .param("lastName", "ФамильевичEdit")
                .with(csrf())
                .with(user(userDetailsAdapter))
        ).andExpect(status().is3xxRedirection)
        .andExpect(view().name("redirect:/app/list"))

        mockMvc.perform(
            get("/app/list")
            .with(csrf())
            .with(user(userDetailsAdapter))
        ).andExpect(status().isOk)
        .andExpect(content().string(containsString("ИмянEdit")))
    }


    private fun addSpecificEmployee(mvc: MockMvc, name: String, lastName: String): ResultActions =
        mvc.perform(
            post("/app/add")
                .param("firstName", name)
                .param("lastName", lastName)
                .param("patronymic", "Отчествич")
                .param("address", "Ул. Птушкина, д. Колотушкина")
                .param("phone", "стационарный")
                .param("email", "test@email.com")
                .with(csrf())
                .with(user(userDetailsAdapter))
        )


    private fun addValidEmployee(mvc: MockMvc): ResultActions =
        mvc.perform(
            post("/app/add")
                .param("firstName", "Имян")
                .param("lastName", "Фамильевич")
                .param("patronymic", "Отчествич")
                .param("address", "Ул. Птушкина, д. Колотушкина")
                .param("phone", "стационарный")
                .param("email", "test@email.com")
                .with(csrf())
                .with(user(userDetailsAdapter))
        )
}