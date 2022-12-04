package ru.sber.springdata

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.sber.springdata.Entities.Client
import ru.sber.springdata.Repositories.ClientRepository
import java.time.LocalDate

//@RunWith(SpringRunner::class)
@SpringBootTest
class ClientRepositoryTests {

	@Autowired
	private lateinit var clientRepository: ClientRepository

	val clientTestEntity = Client(
		firstName = "Firstname1",
		middleName = "MiddleName1",
		lastName = "lastName1",
		birthDate = LocalDate.now(),
	)

	@Test
	fun saveClientAndFindById() {
		assertDoesNotThrow {
			clientRepository.save(clientTestEntity)
		}
		assertTrue(clientRepository.existsById(1))
	}

	@Test
	fun updateClientProperties() {
		assertDoesNotThrow {
			clientRepository.save(clientTestEntity)
			clientRepository.updateUserFioById("Updated Name", "Updated LastName", "MiddleName New", 1)
		}

		val updatedClient = clientRepository.findById(1).get()

		assertTrue(updatedClient.firstName == "Updated Name" && updatedClient.lastName == "Updated LastName" && updatedClient.middleName == "MiddleName New")
	}

	@Test
	fun deleteClient() {
		assertDoesNotThrow {
			clientRepository.save(clientTestEntity)

			clientRepository.deleteById(1)
		}

		assertFalse(clientRepository.existsById(1))
	}
}
