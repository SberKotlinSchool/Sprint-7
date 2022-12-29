package ru.sber.springdata

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springdata.entity.Manufacturer
import ru.sber.springdata.entity.Model
import ru.sber.springdata.repository.ModelRepository


@SpringBootApplication
class SpringDataApplication: CommandLineRunner {

	@Autowired
	lateinit var modelRepository: ModelRepository
	override fun run(vararg args: String?) {
		val newModel = Model().also { model ->
			model.name = "Chaser"
			model.type = VehicleType.CAR
			model.manufacturer = Manufacturer().also {
				it.name = "Toyota"
			}
		}

		val saved = modelRepository.save(newModel)
		val foundModel = modelRepository.findById(saved.id)

		println("Saved and found models are equal: ${saved.id == foundModel.get().id}")

		val allModels = modelRepository.findAll() as List<Model>

		println("Found models: ${allModels.size}")

		modelRepository.updateModelNameById(saved.id, "Legend")

		val updatedModel = modelRepository.findById(saved.id)

		println("Updated name is: ${updatedModel.get().name}")

		val findAll = modelRepository.findAll()
		for (model in findAll) {
			modelRepository.deleteById(model.id)
		}

		println("Repository after delete is empty: ${modelRepository.count() == 0L}")
	}
}

fun main(args: Array<String>) {
	runApplication<SpringDataApplication>(*args)
}
