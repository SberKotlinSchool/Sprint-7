package ru.sber.orm.dao

import org.hibernate.cfg.Configuration
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.sber.orm.entity.*

internal class DroneDAOTest {

    private val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Drone::class.java)
        .addAnnotatedClass(Item::class.java)
        .addAnnotatedClass(Product::class.java)
        .buildSessionFactory()

    @Test
    fun create() {
        sessionFactory.use {
            val droneDAO = DroneDAO(it)
            val drone = Drone(
                serialNumber = "DSF123DRT",
                model = DroneModel.LIGHT_WEIGHT,
                //items = mutableListOf(Item(product = Product(name = "")))
            )
                .addItem(Product(name = "Product_1"))
                .addItem(Product(name = "Product_2"), 3)

            droneDAO.create(drone)

            assertNotNull(drone.id)
        }
    }

    @Test
    fun getById() {
        sessionFactory.use {
            val droneDAO = DroneDAO(it)
            val drone = Drone(
                serialNumber = "DSF567WER",
                model = DroneModel.LIGHT_WEIGHT
            )
                .addItem(Product(name = "Product_3"),3)

            droneDAO.create(drone)

            val actual = droneDAO.getById(drone.id!!)

            assertNotNull(actual?.id)
            assertEquals(drone.serialNumber, actual?.serialNumber)
            assertEquals(1, actual?.items?.count())
            assertEquals("Product_3", actual?.items?.firstOrNull()?.product?.name)
        }
    }

    @Test
    fun update() {
        sessionFactory.use {
            val droneDAO = DroneDAO(it)
            val drone = Drone(
                serialNumber = "DSF567WER",
                model = DroneModel.LIGHT_WEIGHT
            )
            droneDAO.create(drone)

            drone.droneState = DroneState.LOADING

            droneDAO.update(drone)

            val drone1 = droneDAO.getById(drone.id!!)

            assertNotNull(drone1?.id)
            assertEquals(DroneState.LOADING, drone1?.droneState)
        }
    }

    @Test
    fun delete() {
        sessionFactory.use {
            val droneDAO = DroneDAO(it)
            val drone = Drone(
                serialNumber = "ADF558WER",
                model = DroneModel.LIGHT_WEIGHT
            )
            droneDAO.create(drone)

            droneDAO.delete(drone)

            val drone1 = droneDAO.getById(drone.id!!)
            assertNull(drone1)
        }
    }
}