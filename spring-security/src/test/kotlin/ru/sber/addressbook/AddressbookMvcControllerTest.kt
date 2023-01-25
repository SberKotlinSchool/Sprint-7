package ru.sber.addressbook

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
	provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
	type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
class AddressbookMvcControllerTest {

	@Autowired
	private lateinit var mockMvc: MockMvc


	@BeforeEach
	fun setUp() {
		mockMvc.perform(
			MockMvcRequestBuilders.post("/app/add")
				
				.param("name", "Алексей")
				.param("phone", "8-999-999-99-99")
		)
	}

	@Test
	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	fun `add client test done`() {
		mockMvc.perform(
			MockMvcRequestBuilders.post("/app/add")
				
				.param("name", "Иван")
				.param("phone", "8-888-888-88-88")
		)
			.andExpect(MockMvcResultMatchers.status().`is`(302))
	}


	@Test
	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	fun `list clients test`() {
		mockMvc.perform(MockMvcRequestBuilders.get("/app/list"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.view().name("list"))
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Алексей")))
	}
	@Test
	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	fun `list two clients test`() {
		mockMvc.perform(
			MockMvcRequestBuilders.post("/app/add")
				
				.param("name", "Иван")
				.param("phone", "8-888-888-88-88")
		)
		mockMvc.perform(MockMvcRequestBuilders.get("/app/list"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.view().name("list"))
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Алексей")))
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Иван")))
	}

	@Test
	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	fun `view clients test`() {
		mockMvc.perform(MockMvcRequestBuilders.get("/app/Алексей/view"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.view().name("view"))
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Алексей")))
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("8-999-999-99-99")))
	}


	@Test
	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	fun `delete clients test`() {
		mockMvc.perform(MockMvcRequestBuilders.post("/app/Алексей/delete"))
			.andExpect(MockMvcResultMatchers.status().`is`(302))
	}

	@Test
	@WithMockUser(username = "admin", password = "admin", roles = ["ADMIN"])
	fun `edit clients test`() {
		mockMvc.perform(
			MockMvcRequestBuilders.post("/app/Алексей/edit")
				
				.param("name", "Игорь")
				.param("phone", "8-777-777-77-77")
		)
			.andExpect(MockMvcResultMatchers.status().`is`(302))

		mockMvc.perform(MockMvcRequestBuilders.get("/app/Игорь/view"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.view().name("view"))
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Игорь")))
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("8-777-777-77-77")))
	}

}
