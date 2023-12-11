package ru.shadowsith.addressbook.controllers

import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.context.WebApplicationContext
import ru.shadowsith.addressbook.dto.Record
import ru.shadowsith.addressbook.repositories.AddressBookRepository
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
class AddressBookControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var addressBookRepository: AddressBookRepository

    @BeforeEach
    fun setup () {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply { SecurityMockMvcConfigurers.springSecurity() }
            .build()
    }
    @Test
    @WithMockUser(username = "adminapi", password = "admin", roles = ["USER"])
    fun addRecord() {
        val record = Record(name = "qwe", address = "address", phone = "123")
        mockMvc.perform(post("/app/add")
            .flashAttr("record", record)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is3xxRedirection)
        .andExpect(view().name("redirect:/app/list"))
        .andExpect(redirectedUrl("/app/list"))
    }

    @Test
    @WithMockUser(username = "adminapi", password = "admin", roles = ["USER"])
    fun addRecordView() {
        mockMvc.perform(get("/app/add"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("add"))
            .andExpect(content().string(containsString("Адресная книга")))
    }

    @Test
    @WithMockUser(username = "adminapi", password = "admin", roles = ["USER"])
    fun getRecords() {
        mockMvc.perform(get("/app/list"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("list"))
            .andExpect(content().string(containsString("Адресная книга")))
            .andExpect(content().string(containsString("Добавить запись")))
    }

    @Test
    @WithMockUser(username = "adminapi", password = "admin", roles = ["DELETE"])
    fun deleteRecord() {
        mockMvc.perform(get("/app/1/delete"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/app/list"))
            .andExpect(redirectedUrl("/app/list"))
    }

    @Test
    @WithMockUser(username = "adminapi", password = "admin", roles = ["USER"])
    fun getRecord() {
        val expectedRecord = addressBookRepository.save(Record(name = "asd", address = "zxcxzcz", phone = "32423"))

        mockMvc.perform(get("/app/${expectedRecord.id}/view")
            .content("id"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("view"))
            .andExpect(content().string(containsString("Запись из адресной книги")))
            .andExpect(content().string(containsString(expectedRecord.address)))
            .andExpect(content().string(containsString(expectedRecord.name)))
            .andExpect(content().string(containsString(expectedRecord.phone)))
    }

    @Test
    @WithMockUser(username = "adminapi", password = "admin", roles = ["USER"])
    fun changeRecord() {
        val record = addressBookRepository.save(Record(name = "qwe", address = "address", phone = "123"))
        val changeRecord = record.copy(
            name = "ewq",
            address = "sserdda",
            phone = "321"
        )
        mockMvc.perform(post("/app/${record.id}/edit")
            .flashAttr("record", changeRecord))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("view"))
            .andExpect(content().string(containsString("Запись из адресной книги")))

        val expectedRecord = addressBookRepository.findById(record.id!!).get()
        assertEquals(expectedRecord, changeRecord)
    }


}