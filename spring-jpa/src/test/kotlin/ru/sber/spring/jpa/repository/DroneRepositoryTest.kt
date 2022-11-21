package ru.sber.spring.jpa.repository

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import ru.sber.spring.jpa.entity.Drone
import ru.sber.spring.jpa.entity.DroneModel
import ru.sber.spring.jpa.entity.DroneState
import ru.sber.spring.jpa.entity.Product

@SpringBootTest
class DroneRepositoryTest {

    @Autowired
    private lateinit var droneRepository: DroneRepository

    @Test
    fun create() {

        val drone = Drone(serialNumber = "ASDE123SDAF", model = DroneModel.LIGHT_WEIGHT)
        droneRepository.save(drone)

        Assertions.assertNotNull(drone.id)
    }

    @Test
    fun getById() {

        val drone = Drone(
            serialNumber = "DSF567WER",
            model = DroneModel.LIGHT_WEIGHT
        )
            .addItem(Product(name = "Product_3"), 3)

        droneRepository.save(drone)

        val actual = droneRepository.getReferenceById(drone.id!!)

        Assertions.assertNotNull(actual.id)
        Assertions.assertEquals(drone.serialNumber, actual.serialNumber)
        Assertions.assertEquals(1, actual.items.count())
        Assertions.assertEquals("Product_3", actual.items.firstOrNull()?.product?.name)
    }

    @Test
    fun update() {
        val drone = Drone(
            serialNumber = "DSF567WER",
            model = DroneModel.LIGHT_WEIGHT
        )
        droneRepository.save(drone)

        drone.droneState = DroneState.LOADING

        droneRepository.save(drone)

        val actual = droneRepository.getReferenceById(drone.id!!)

        Assertions.assertNotNull(actual.id)
        Assertions.assertEquals(DroneState.LOADING, actual.droneState)
    }

    @Test
    fun delete() {

        val drone = Drone(
            serialNumber = "ADF558WER",
            model = DroneModel.LIGHT_WEIGHT
        )
        droneRepository.save(drone)

        droneRepository.delete(drone)

        Assertions.assertThrowsExactly(JpaObjectRetrievalFailureException::class.java) {
            droneRepository.getReferenceById(drone.id!!)
        }
    }

}