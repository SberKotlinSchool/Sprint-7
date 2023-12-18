package com.dokl57.springmvc.controller

import com.dokl57.springmvc.model.Entry
import com.dokl57.springmvc.service.AddressBookService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(MvcController::class)
@AutoConfigureMockMvc(addFilters = false)
class MvcControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var addressBookService: AddressBookService

    private lateinit var entry: Entry

    @BeforeEach
    fun setup() {
        entry = Entry(1L, "Test Name", "Test Address")
        given(addressBookService.getEntryById(anyLong())).willReturn(entry)
        given(addressBookService.getEntries(anyString())).willReturn(listOf(entry))
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `test showAddEntry`() {
        mockMvc.perform(get("/app/add"))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }

    @WithMockUser(username = "user1", roles = ["ADMIN"])
    @Test
    fun `test addEntry`() {
        mockMvc.perform(
            post("/app/add")
                .flashAttr("entry", entry)
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `test getEntries`() {
        mockMvc.perform(get("/app/list"))
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
            .andExpect(model().attributeExists("entries"))
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `view entry`() {
        mockMvc.perform(get("/app/{id}/view", 1))
            .andExpect(status().isOk)
            .andExpect(view().name("view"))
            .andExpect(model().attributeExists("entry"))
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `test showEditEntry`() {
        mockMvc.perform(get("/app/{id}/edit", 1))
            .andExpect(status().isOk)
            .andExpect(status().is2xxSuccessful)
            .andExpect(view().name("edit"))
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `test editEntry`() {
        mockMvc.perform(
            post("/app/{id}/edit", 1)
                .flashAttr("entry", entry)
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/app/list"))
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `delete entry`() {
        mockMvc.perform(get("/app/{id}/delete", 1))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }
}