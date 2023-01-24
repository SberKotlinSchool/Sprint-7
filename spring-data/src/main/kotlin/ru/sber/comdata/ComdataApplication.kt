package ru.sber.comdata

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.CommandLineRunner
import ru.sber.comdata.persistence.entities.*
import ru.sber.comdata.persistence.repository.ContractTypeRepository
import ru.sber.comdata.persistence.repository.IdentityRepository
import ru.sber.comdata.persistence.repository.ProductRepository

@SpringBootApplication
class ComdataApplication
	(
	private val identityRepository: IdentityRepository,
	private val contractTypeRepository: ContractTypeRepository,
	private val productRepository: ProductRepository
) : CommandLineRunner {
	override fun run(vararg args: String?) {
		val identityName1 = "Life Policy"
		val identityName2 = "Ticket to the future"

		val identity1 = identityRepository.findByName(identityName1) ?: Identity(name = identityName1)
			.let { identityRepository.save(it) }

		val identity2 = identityRepository.findByName(identityName2) ?: Identity(name = identityName2)
			.let { identityRepository.save(it) }

		val nameCtype1 = "ISG"
		val nameCtype2 = "NSG"
		val ctype1 = contractTypeRepository.findByName(nameCtype1) ?: ContractType(name = nameCtype1)
			.let { contractTypeRepository.save(it) }

		val ctype2 = contractTypeRepository.findByName(nameCtype2) ?: ContractType(name = nameCtype2)
			.let { contractTypeRepository.save(it) }

		Product(
			type = FrequencyType.Month,
			productGroup = ProductGroup(identity = identity1, groupPosition = "7"),
			name = "Family Asset",
			ctypes = listOf(ctype1, ctype2)
		)
			.let { productRepository.save(it) }
		Product(
			type = FrequencyType.Year,
			productGroup = ProductGroup(identity = identity2, groupPosition = "8"),
			name = "Managed Сapital",
			ctypes = listOf(ctype1, ctype2)
		)
			.let { productRepository.save(it) }
		val products = productRepository.findAll()
		println(products)
	}
}
fun main(args: Array<String>) {
	runApplication<ComdataApplication>(*args)
}
