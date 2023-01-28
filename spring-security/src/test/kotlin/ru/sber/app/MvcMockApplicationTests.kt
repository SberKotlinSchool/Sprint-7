package ru.sber.app

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ru.sber.app.endpoint.UserController
import ru.sber.app.repository.AddressBookRepository

@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MvcMockApplicationTests {
	@Autowired
	private lateinit var mockMvc : MockMvc
	@Autowired
	private lateinit var addressBookRepository: AddressBookRepository
	@Autowired
	private lateinit var userController: UserController

	@Test
	fun contextLoads() {
		assertThat(userController).isNotNull
	}

	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	@Test
	fun testAppAddPost() {
		mockMvc.perform(post("/app/add")
			.param("firstName", "Петр")
			.param("lastName", "Петров")
			.param("city", "Петровск"))
			.andDo(print())
	}

	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	@Test
	fun testAppAddGet() {
		mockMvc.perform(get("/app/add"))
			.andDo(print())
			.andExpect(status().isOk)
			.andExpect(view().name("add"))
	}

	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	@Test
	fun testAppList() {
		mockMvc.perform(get("/app/list"))
			.andDo(print())
			.andExpect(status().isOk)
			.andExpect(view().name("list"))
			.andExpect(model().attributeExists("notes"))
	}

	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	@Test
	fun testViewApp() {
		mockMvc.perform(get("/app/view/1"))
			.andDo(print())
			.andExpect(status().isOk)
			.andExpect(view().name("view"))
			.andExpect(model().attributeExists("note"))
	}

	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	@Test
	fun testEditApp() {
		mockMvc.perform(post("/app/view/2")
			.param("firstName", "Петр")
			.param("lastName", "Петров")
			.param("city", "Петровск"))
			.andDo(print())
	}

	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	@Test
	fun testDeleteAppRoleAdmin() {
		val dataExpected = addressBookRepository.findAll().size
		mockMvc.perform(get("/app/remove/7"))
			.andDo(print())
		assertEquals(dataExpected - 1, addressBookRepository.findAll().size)
	}

	@WithMockUser(username = "user1", password = "user1")
	@Test
	fun testDeleteAppRoleUser() {
		val dataExpected = addressBookRepository.findAll().size
		mockMvc.perform(get("/app/remove/1"))
			.andDo(print())
		assertEquals(dataExpected, addressBookRepository.findAll().size)
	}
}
