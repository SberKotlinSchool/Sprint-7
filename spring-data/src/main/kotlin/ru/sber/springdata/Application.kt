package ru.sber.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springdata.entity.Citizen
import ru.sber.springdata.entity.ContactInformation
import ru.sber.springdata.entity.Address
import ru.sber.springdata.entity.ContactType
import ru.sber.springdata.repository.AddressRepository
import ru.sber.springdata.repository.ContactInformationRepository
import ru.sber.springdata.repository.CitizenRepository

@SpringBootApplication
class Application(
    private val citizenRepository: CitizenRepository,
    private val addressRepository: AddressRepository,
    private val contactInformationRepository: ContactInformationRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val citizen1 = Citizen(
            firstName = "Anton",
            secondName = "Rogozhin",
            address = addressRepository.save(Address(country = "Russia", city = "Moscow"))
        )
        val citizen2 = Citizen(
            firstName = "Victoria",
            secondName = "Rogozhina",
            address = addressRepository.save(Address(country = "Russia", city = "Moscow"))
        )
        val citizen3 = Citizen(
            firstName = "Roman",
            secondName = "Veselov",
            address = addressRepository.save(Address(country = "Russia", city = "SPB"))
        )

        val contactsM1 = listOf(
            ContactInformation(type = ContactType.PHONE, value = "79112223344", citizen = citizen1),
            ContactInformation(type = ContactType.EMAIL, value = "qwe@asd.ru", citizen = citizen1)
        )
        val contactsM2 = listOf(
            ContactInformation(type = ContactType.PHONE, value = "79223334455", citizen = citizen2),
            ContactInformation(type = ContactType.SITE, value = "magician.ru", citizen = citizen2)
        )
        val contactsM3 = listOf(
            ContactInformation(type = ContactType.PHONE, value = "79334445566", citizen = citizen3),
            ContactInformation(type = ContactType.EMAIL, value = "asd@zxc.ru", citizen = citizen3),
            ContactInformation(type = ContactType.SITE, value = "best-magician.ru", citizen = citizen3)
        )

        citizen1.contactInformation = contactsM1
        citizen2.contactInformation = contactsM2
        citizen3.contactInformation = contactsM3

        citizenRepository.saveAll(listOf(citizen1, citizen2, citizen3))
        println(citizenRepository.findAll())
        println(citizenRepository.findById(2).get())
        println(citizenRepository.count())
        println(citizenRepository.delete(citizen3))
        println(citizenRepository.findAll())
    }
}


fun main(args: Array<String>) {
    runApplication<Application>(*args)
}