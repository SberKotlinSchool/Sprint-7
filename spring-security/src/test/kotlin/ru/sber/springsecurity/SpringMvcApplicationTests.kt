package ru.sber.springsecurity

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
class SpringMvcApplicationTests {

	@Autowired
	private lateinit var mockMvc: MockMvc
	@Autowired
	private lateinit var webApplicationContext: WebApplicationContext

	@BeforeEach
	fun setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply {
				springSecurity()
			}
			.build()
	}

	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	@Test
	fun `test should return list`() {
		mockMvc.perform(get("/app"))
			.andExpect(status().isOk)
			.andExpect(view().name("app"))
			.andExpect(model().attributeExists("notes"))
	}

	@WithMockUser(username = "user", password = "user", roles = ["USER"])
	@Test
	fun `test edit note`() {
		mockMvc.perform(
			get("/message/edit/1")
		)
			.andExpect(status().isOk)
			.andExpect(view().name("message"))
			.andExpect(model().attributeExists("user"))
	}

	@WithMockUser(username = "abc", password = "abc", roles = ["USER"])
	@Test
	fun `access denied for non-authorized user`() {

		mockMvc.perform(
			get("/message/delete/1")
		)
			.andExpect(status().isMethodNotAllowed)

	}
}
