package ru.sber.springdata

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.sber.springdata.entity.Manufacturer
import ru.sber.springdata.entity.Model
import ru.sber.springdata.repository.ModelRepository

@SpringBootTest
internal class SpringDataApplicationTest {

    @Autowired
    lateinit var modelRepository: ModelRepository

    val newModel = Model().also { model ->
        model.name = "Chaser"
        model.type = VehicleType.CAR
        model.manufacturer = Manufacturer().also {
            it.name = "Toyota"
        }
    }

    @Test
    fun testSaveAndFind() {

        assertDoesNotThrow {
            val saved = modelRepository.save(newModel)
            val foundModel = modelRepository.findById(saved.id)

            assertEquals(saved.id, foundModel.get().id)

            val allModels = modelRepository.findAll() as List<Model>

            assertEquals(1, allModels.size)
        }
    }

    @Test
    fun testDelete() {

        assertDoesNotThrow {
            val findAll = modelRepository.findAll()
            for (model in findAll) {
                modelRepository.deleteById(model.id)
            }

            assertEquals(0, modelRepository.count())
        }
    }

    @Test
    fun testUpdate() {
        assertDoesNotThrow {
            val saved = modelRepository.save(newModel)

            modelRepository.updateModelNameById(saved.id, "Legend")

            val updatedModel = modelRepository.findById(saved.id)

            assertNotEquals(saved, updatedModel.get())
            assertEquals("Legend", updatedModel.get().name)
        }
    }
}