package sber

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import sber.enteties.Cat
import sber.enteties.CatClass
import sber.repositories.CatRepository
import java.time.LocalDate

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class SpringJpaApplicationTest{

    @Autowired
    lateinit var catRepository: CatRepository


    @Test
    fun createCatsTest(){
        val british = CatClass(catType = "British")
        val gustavMomPolina = Cat(1,
            name = "Polina",
            catClass = british,
            color = "Gold",
            birthDate = LocalDate.now().minusYears(20)
        )

        val gustav = Cat(2,
            name = "Gustav",
            catClass = british,
            color = "Grey",
            birthDate = LocalDate.now().minusYears(10),
            mother = gustavMomPolina,
            brothers = null
        )
        catRepository.save(gustavMomPolina)
        catRepository.save(gustav)

        // when


        // then
        val foundGustav = catRepository.findByName("Gustav")
        assert(!foundGustav.isEmpty())
        println("Test 1 : GustavTest")
        println(foundGustav)
        assert(foundGustav.first().name == "Gustav")

        val foundGold = catRepository.findByColor("Grey")
        println("Test 3 : Grey cats:")
        println(foundGold.first())
        assert(foundGold.first().name == "Gustav")
    }

}