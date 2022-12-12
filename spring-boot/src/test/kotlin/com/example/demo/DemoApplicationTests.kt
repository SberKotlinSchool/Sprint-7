package com.example.demo

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.boot.test.context.SpringBootTest

@DataJpaTest
class DemoApplicationTests {

	@Autowired lateinit var repo: EntityRepository

	@Test
	fun contextLoads() {
	}

	@Test
	fun testJpa() {
		val e = Entity("entity")
		assertNull(e.id)
		repo.save(e)
		assertNotNull(e.id)
		assertTrue(repo.findAll().contains(e))
	}
}
