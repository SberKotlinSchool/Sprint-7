package ru.sber.security.controller

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.sber.security.dto.Student
import ru.sber.security.service.AddressBookService
import java.time.LocalDateTime
import javax.servlet.http.Cookie

@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
internal class MvcControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var addressBookService: AddressBookService

    private lateinit var testAuthCookie: Cookie
    private lateinit var testStudent01: Student
    private lateinit var testStudent02: Student

    @BeforeEach
    fun setUp() {
        testStudent01 = Student(addressBookService.getNextEntityId(), "FN_One", "FA_One", "89111234567")
        addressBookService.add(testStudent01)
        testStudent02 = Student(addressBookService.getNextEntityId(), "FN_Two", "FA_Two", "89211234567")
        testAuthCookie = Cookie("auth", LocalDateTime.now().plusMinutes(5).toString())
    }

    @AfterEach
    fun tearDown() = addressBookService.deleteAll()

    @Test
    @WithMockUser(username = "USER_APP", roles = ["APP"])
    fun add() {
        performAdd(testStudent02)
        isViewExpected(
            addressBookService.size().toString(),
            testStudent02.fullName!!,
            testStudent02.fullAddress!!,
            testStudent02.phoneNumber!!
        )
    }

    @Test
    @WithMockUser(username = "USER_APP", roles = ["APP"])
    fun view() {
        isViewExpected(
            testStudent01.entityId.toString(),
            testStudent01.fullName!!,
            testStudent01.fullAddress!!,
            testStudent01.phoneNumber!!
        )
    }

    @Test
    @WithMockUser(username = "USER_APP", roles = ["APP"])
    fun edit() {
        testStudent01.entityId?.let { performEdit(it, testStudent02) }
        isViewExpected(
            testStudent01.entityId.toString(),
            testStudent02.fullName!!,
            testStudent02.fullAddress!!,
            testStudent02.phoneNumber!!
        )
    }

    @Test
    @WithMockUser(username = "USER_APP", roles = ["APP"])
    fun list() {
        performList()
        assertListContent(testStudent01)
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = ["ADMIN"])
    fun delete() {
        testStudent01.entityId?.let { performDelete(it) }
        performList()
        assertListContent(testStudent01, shouldContain = false)
    }

    @Test
    @WithMockUser(username = "USER_API", roles = ["API"])
    fun listWithoutGrants() {
        mockMvc.perform(
            get("/app/list")
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "USER_APP", roles = ["APP"])
    fun deleteWithoutGrants() {
        mockMvc.perform(
            get("/app/{entityId}/delete", testStudent01.entityId)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().isForbidden)
    }

    private fun performAdd(student: Student) {
        mockMvc.perform(
            post("/app/add")
                .flashAttr("entity", student)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))
    }

    private fun performEdit(entityId: Long, student: Student) {
        mockMvc.perform(
            post("/app/{entityId}/edit", entityId)
                .flashAttr("entity", student)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))
    }

    private fun performList() {
        mockMvc.perform(
            get("/app/list")
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
    }

    private fun performDelete(entityId: Long) {
        mockMvc.perform(
            get("/app/{entityId}/delete", entityId)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))
    }

    private fun assertListContent(student: Student, shouldContain: Boolean = true) {
        val content = mockMvc.perform(
            get("/app/list")
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andReturn().response.contentAsString

        val containsFullName = content.contains(student.fullName!!)
        val containsFullAddress = content.contains(student.fullAddress!!)
        val containsPhoneNumber = content.contains(student.phoneNumber!!)

        if (shouldContain) {
            assert(containsFullName && containsFullAddress && containsPhoneNumber)
        } else {
            assert(!containsFullName && !containsFullAddress && !containsPhoneNumber)
        }
    }

    private fun isViewExpected(entityId: String, fullName: String, fullAddress: String, phoneNumber: String) {
        mockMvc.perform(
            get("/app/{entityId}/view", entityId)
                .cookie(testAuthCookie)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("view"))
            .andExpect(content().string(containsString(fullName)))
            .andExpect(content().string(containsString(fullAddress)))
            .andExpect(content().string(containsString(phoneNumber)))
    }
}
