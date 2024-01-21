package com.firebat.addressbook.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.util.NestedServletException

/* FIXME есть ли возможность проверить SecurityConfig, тот же редирект неавторизованных пользователей?
    В локальных тестах вижу только работу @PreAuthorize(), ее проверял с помощью @WithMockUser
    После перехода на Java 8 и добавления Spring Security стал непредсказуемо работать TestRestTemplate,
    так и не понял, что значит его статус 302
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RestControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    lateinit var mockMvc: MockMvc

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
    @WithMockUser(roles = ["API"])
    fun addEntry() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(url("/api/add"))
                .content("{ \"name\" : \"user\", \"address\" : \"Address\" }")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun addEntryWithoutAuth() {
        assertThrows<NestedServletException> { // FIXME исключение Authentication required, полученное при создании ACL sid обернуто вот в это
            mockMvc.perform(
                MockMvcRequestBuilders.post(url("/api/add"))
                    .content("{ \"name\" : \"user\", \"address\" : \"Address\" }")
                    .contentType(APPLICATION_JSON)
            )
        }
    }

    @Test
    @WithMockUser(roles = ["API_DELETE"])
    fun deleteEntry() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(url("/api/0/delete"))
        )
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(roles = ["API_DELETE"])
    fun deleteNonExistingEntry() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(url("/api/9/delete"))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(roles = ["API"])
    fun deleteEntryWithoutDeleteRole() {
        assertThrows<NestedServletException> { // FIXME исключение Access Denied обернуто вот в это
            mockMvc.perform(
                MockMvcRequestBuilders.delete(url("/api/0/delete"))
            )
                .andExpect(status().isOk)
        }
    }

    private fun url(path: String) = "http://localhost:${port}/${path}"
}