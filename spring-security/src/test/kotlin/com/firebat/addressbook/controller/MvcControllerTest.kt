package com.firebat.addressbook.controller

import com.firebat.addressbook.model.Entry
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.util.NestedServletException

/* FIXME есть ли возможность проверить SecurityConfig, тот же редирект неавторизованных пользователей?
    В локальных тестах вижу только работу @PreAuthorize(), ее проверял с помощью @WithMockUser
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MvcControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var context: WebApplicationContext

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply { springSecurity() }
            .build()
    }

    @Test
    fun showAddEntry() {
        mockMvc.perform(
            get("/app/add")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("add"))
    }

    @Test
    @WithMockUser
    fun addEntry() {
        mockMvc.perform(
            post("/app/add")
                .flashAttr("entry", Entry(name = "user", address = "Address"))
        )
            .let { expectMainPageRedirect(it) }
    }

    @Test
    fun addEntryWithoutAuth() {
        assertThrows<NestedServletException> { // FIXME исключение Authentication required, полученное при создании ACL sid обернуто вот в это
            mockMvc.perform(
                post("/app/add").flashAttr(
                    "entry",
                    Entry(name = "user", address = "Address")
                )
            )
        }
    }

    @Test
    fun getEntries() {
        mockMvc.perform(
            get("/app/list")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
    }

    @Test
    fun viewEntry() {
        mockMvc.perform(
            get("/app/0/view")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("view"))
    }

    @Test
    fun viewNonExistingEntry() {
        mockMvc.perform(
            get("/app/9/view")
        )
            .let { expectMainPageRedirect(it) }
    }

    @Test
    fun showEditEntry() {
        mockMvc.perform(
            get("/app/2/edit")
        )
            .andExpect(status().isOk)
            .andExpect(view().name("edit"))
    }

    @Test
    fun showEditNonExistingEntry() {
        mockMvc.perform(
            get("/app/9/edit")
        )
            .let { expectMainPageRedirect(it) }
    }

    @Test
    fun editEntry() {
        mockMvc.perform(
            post("/app/2/edit")
                .flashAttr("entry", Entry(name = "user", address = "Address"))
        )
            .let { expectMainPageRedirect(it) }
    }

    @Test
    @WithMockUser(roles = ["API_DELETE"])
    fun deleteEntry() {
        mockMvc.perform(
            get("/app/3/delete")
        )
            .let { expectMainPageRedirect(it) }
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun deleteEntryWithoutDeleteRole() {
        assertThrows<NestedServletException> { // FIXME исключение Access Denied обернуто вот в это
            mockMvc.perform(get("/app/1/delete"))
        }
    }

    private fun expectMainPageRedirect(resultActions: ResultActions) {
        resultActions
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/app/list"))
    }
}