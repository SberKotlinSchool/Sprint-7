package com.example.demo

import ru.sber.orm.dao.PatientDao
import ru.sber.orm.entity.Hospital
import ru.sber.orm.entity.OutpatientBook
import ru.sber.orm.entity.Patient
import jdk.nashorn.internal.objects.NativeRegExp.test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.hibernate.SessionFactory
import org.junit.jupiter.api.Test
import org.hibernate.cfg.Configuration
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

class PatientDaoTests {

	private fun getSessionFactory(): SessionFactory {
		return Configuration().configure()
			.addAnnotatedClass(PatientDao::class.java)
			.addAnnotatedClass(Patient::class.java)
			.addAnnotatedClass(Hospital::class.java)
			.addAnnotatedClass(OutpatientBook::class.java)
			.buildSessionFactory()

	}

	@Test
	fun testSaveSuccessfull() {
		val sessionFactory = getSessionFactory()

		sessionFactory.use { sessionFactory ->
			val dao = PatientDao(sessionFactory)

			val patient = Patient(
				firstName = "Георгий",
				lastName = "Иванов",
				outpatientBook = OutpatientBook(hospitalization= LocalDateTime.now(), discharge = LocalDateTime.now()),
				hospitals = mutableListOf(Hospital(name = "Айболит"))
			)
			dao.save(patient)
			val foundPatient = dao.find(patient.id)
			assertNotNull(foundPatient)
			Assertions.assertEquals(patient.firstName, foundPatient?.firstName)
			Assertions.assertEquals(patient.lastName, foundPatient?.lastName)
			Assertions.assertEquals(patient.hospitals.size, foundPatient?.hospitals?.size)
			Assertions.assertTrue(patient.hospitals[0] == foundPatient?.hospitals!![0])
			Assertions.assertEquals(patient.outpatientBook.hospitalization, foundPatient.outpatientBook.hospitalization)
		}


	}
}


