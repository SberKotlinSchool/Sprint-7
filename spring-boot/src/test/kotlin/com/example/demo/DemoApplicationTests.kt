package com.example.demo

import com.example.demo.controller.Controller
import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.hibernate.persister.walking.spi.EntityIdentifierDefinition
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(Controller::class )
class ControllerTest {
	@Autowired
	private lateinit var mockMvc: MockMvc

	@Test
	fun `Get start status 200 OK test` () {
		val result = mockMvc.perform(MockMvcRequestBuilders.get(("/start")))

		result
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.content().string("GO!!!"))
	}
}

@DataJpaTest
class EntityRepositoryTest {
	@Autowired
	private lateinit var entityRepository: EntityRepository

	@Test
	fun `entity findById Test` () {
		//given
		val saveEntity = entityRepository.save(Entity(name = "name"))

		//when
		val foundEntity = saveEntity.id?.let { entityRepository.findById(it) }

		//then
		if (foundEntity != null) {
			assertTrue(foundEntity.get() == saveEntity)
		}
	}
}