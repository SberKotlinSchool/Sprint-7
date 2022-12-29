package ru.sber.springdata

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
//class SpringDataApplication: CommandLineRunner {
class SpringDataApplication {

//	@Autowired
//	lateinit var modelRepository: ModelRepository
//	override fun run(vararg args: String?) {
//		val newModel = Model().also { model ->
//			model.name = "Chaser"
//			model.type = VehicleType.CAR
//			model.manufacturer = Manufacturer().also {
//				it.name = "Toyota"
//			}
//		}
//
//		val saved = modelRepository.save(newModel)
//		val foundModel = modelRepository.findById(saved.id)
//
//		println("Saved and found models are equal: ${saved.id == foundModel.get().id}")
//
//		val allModels = modelRepository.findAll() as List<Model>
//
//		println("Found models: ${allModels.size}")
//
//		modelRepository.updateModelNameById(saved.id, "Legend")
//
//		val updatedModel = modelRepository.findById(saved.id)
//
//		println("Updated name is: ${updatedModel.get().name}")
//
//		val findAll = modelRepository.findAll()
//		for (model in findAll) {
//			modelRepository.deleteById(model.id)
//		}
//
//		println("Repository after delete is empty: ${modelRepository.count() == 0L}")
//	}
}

fun main(args: Array<String>) {
	runApplication<SpringDataApplication>(*args)
}
