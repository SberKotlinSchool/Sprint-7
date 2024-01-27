import dao.MagicianDao
import entity.Address
import entity.ContactInformation
import entity.Citizen
import entity.ContactType
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Address::class.java)
        .addAnnotatedClass(ContactInformation::class.java)
        .addAnnotatedClass(Citizen::class.java)
        .addAnnotatedClass(ContactType::class.java)
        .buildSessionFactory()

    sessionFactory.use {
        val dao = MagicianDao(sessionFactory)

        val citizen1 = Citizen(
            firstName = "Anton",
            secondName = "Rogozhin",
            address = Address(country = "Russia", city = "Moscow")
        )
        val citizen2 = Citizen(
            firstName = "Victoria",
            secondName = "Rogozhina",
            address = Address(country = "Russia", city = "Moscow")
        )
        val citizen3 = Citizen(
            firstName = "Roman",
            secondName = "Veselov",
            address = Address(country = "Russia", city = "SPB")
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


        listOf(citizen1, citizen2, citizen3).forEach {
            dao.save(it)
        }

        println("${dao.findAll()}")
        println("\n${dao.findById(1)}")
        println("\n${dao.find("Roman")}")
        citizen2.firstName = "Виктория"
        println("\n${dao.update(citizen2)}")
        dao.delete(citizen1)
        println("\n${dao.findAll()}")
    }


}