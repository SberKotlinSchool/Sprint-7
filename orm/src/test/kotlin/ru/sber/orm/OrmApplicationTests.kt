package ru.sber.orm

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.sber.orm.dao.ModelDAO
import ru.sber.orm.entities.Manufacturer
import ru.sber.orm.entities.Model
import ru.sber.orm.enums.VehicleType
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class OrmApplicationTests {

	private lateinit var sessionFactory: SessionFactory
	private lateinit var modelDAO: ModelDAO

	@BeforeEach
	fun init() {
		sessionFactory = Configuration().configure()
			.addAnnotatedClass(Manufacturer::class.java)
			.addAnnotatedClass(Model::class.java)
			.buildSessionFactory()

		modelDAO = ModelDAO(sessionFactory)
	}

	@Test
	fun testDAO() {

		val manufacturer = Manufacturer()
		manufacturer.name = "Toyota"

		val modelPrius = Model()
		modelPrius.name = "Prius"
		modelPrius.type = VehicleType.CAR
		modelPrius.manufacturer = manufacturer

		val modelMR2 = Model()
		modelMR2.name = "MR-2"
		modelMR2.type = VehicleType.CAR
		modelMR2.manufacturer = manufacturer

		modelDAO.save(modelPrius)
		modelDAO.save(modelMR2)

		val foundModel = modelDAO.findByName("Prius")
		val foundById = modelDAO.findById(foundModel?.id ?: 0)

		assertEquals(modelPrius.name, foundModel?.name)
		assertEquals(modelPrius.name, foundById?.name)
	}

	@Test
	fun testFindAll() {
		val allModels = modelDAO.findAll()
		assertNotNull(allModels)
	}

}
