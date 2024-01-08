import dao.MagicianDao
import entity.Address
import entity.Competition
import entity.ContactInformation
import entity.Magician
import entity.Nomination
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Address::class.java)
        .addAnnotatedClass(Competition::class.java)
        .addAnnotatedClass(ContactInformation::class.java)
        .addAnnotatedClass(Magician::class.java)
        .addAnnotatedClass(Nomination::class.java)
        .buildSessionFactory()

    sessionFactory.use { sf ->
        val dao = MagicianDao(sessionFactory)

        val magician1 = Magician(
            id = 1,
            firstName = "Anton",
            secondName = "Rogozhin",
            address = Address("Russia", "Moscow"),
            contactInformation = ContactInformation(phoneNumber = "+79111112233", email = "qwe@asd.ru"),
            competition = listOf(
                Competition(1, 1, Nomination.STAGE, 1),
                Competition(2, 1, Nomination.ONE_TRICK, 2)
            ),
        )

        val magician2 = Magician(
            id = 2,
            firstName = "Victpria",
            secondName = "Rogozhina",
            address = Address("Russia", "Moscow"),
            contactInformation = ContactInformation(phoneNumber = "+79223334455", email = "asd@zxc.ru"),
            competition = listOf(
                Competition(3, 2, Nomination.STAGE, 2),
                Competition(4, 2, Nomination.ONE_TRICK, 3)
            ),
        )

        val magician3 = Magician(
            id = 3,
            firstName = "Roman",
            secondName = "Veselov",
            address = Address("Russia", "SPB"),
            contactInformation = ContactInformation(phoneNumber = "+79334445566", email = "zxc@vbn.ru"),
            competition = listOf(
                Competition(5, 3, Nomination.KIDS_STAGE, 1),
                Competition(6, 3, Nomination.KIDS_MICROMAGIC, 1)
            ),
        )

        dao.save(magician1)
        dao.save(magician2)
        dao.save(magician3)

        println("${dao.findAll()}")
        println("${dao.findById(1)}")
        println("${dao.find(ContactInformation( phoneNumber = "+79334445566", email = "zxc@vbn.ru"))}")
        magician2.firstName = "Виктория"
        println("${dao.update(magician2)}")
        dao.delete(magician1)
        println("${dao.findAll()}")
    }
}