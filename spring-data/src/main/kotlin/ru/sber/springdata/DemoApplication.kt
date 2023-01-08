package ru.sber.springdata

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springdata.entity.Hospital
import ru.sber.springdata.entity.OutpatientBook
import ru.sber.springdata.entity.Patient
import ru.sber.springdata.repository.PatientRepository
import java.time.LocalDateTime


@SpringBootApplication
open class DemoApplication: CommandLineRunner {

	@Autowired
	lateinit var patientRepository: PatientRepository

	override fun run(vararg args: String?) {
		val patient1 = Patient( firstName = "Георгий", lastName = "Иванов",
			outpatientBook = OutpatientBook(hospitalization = LocalDateTime.now(),
				discharge = LocalDateTime.now()),
			hospitals = mutableListOf(Hospital(name = "Айболит"))
		)
		val patient2 = Patient( firstName = "Георгий", lastName = "Иванов",
			outpatientBook = OutpatientBook(hospitalization = LocalDateTime.now(),
				discharge = LocalDateTime.now()),
			hospitals = mutableListOf(Hospital(name = "Айболит"))
		)

		patientRepository.save(patient1)
		patientRepository.save(patient2)


		val foundPatient1 = patientRepository.findById(patient1.id)
		println("Первый пациент: $foundPatient1")
		println()


		val allPatient = patientRepository.findAll() as List<Patient>
		println("Количество пациентов: ${allPatient.size}")
	}

}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
