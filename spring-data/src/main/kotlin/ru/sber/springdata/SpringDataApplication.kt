package ru.sber.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springdata.persistence.entity.*
import ru.sber.springdata.persistence.repository.TankRepository

@SpringBootApplication
class SpringDataApplication(
	private val tankRepository: TankRepository
) : CommandLineRunner {

	override fun run(vararg args: String?) {

		val tank1 = Tank (
			tankType = TankType.T_80
			, engine = Engine(type = EngineType.B_84MS, power = 1100, serialNumber = "125684949")
			, ammo = mutableListOf(
				Ammo(type = AmmoType.BULLET, caliber = AmmoCaliber.MM_7_62, quantity = 2000),
				Ammo(type = AmmoType.BULLET, caliber = AmmoCaliber.MM_12_7, quantity = 300),
				Ammo(type = AmmoType.SABOT, caliber = AmmoCaliber.MM_125, quantity = 15),
				Ammo(type = AmmoType.HE, caliber = AmmoCaliber.MM_125, quantity = 21),
				Ammo(type = AmmoType.TUR, caliber = AmmoCaliber.MM_125, quantity = 6),
			)
		)

		val tank2 = Tank (
			tankType = TankType.CHALLENGER_2
			, engine = Engine(type = EngineType.CV12, power = 1200, serialNumber = "RF125Y49")
			, ammo = mutableListOf(
				Ammo(type = AmmoType.BULLET, caliber = AmmoCaliber.MM_7_62, quantity = 4000),
				Ammo(type = AmmoType.SABOT, caliber = AmmoCaliber.MM_120, quantity = 12),
				Ammo(type = AmmoType.HEAT, caliber = AmmoCaliber.MM_120, quantity = 20),
				Ammo(type = AmmoType.HESH, caliber = AmmoCaliber.MM_120, quantity = 20),
			)
		)

		// Create
		tankRepository.saveAll(listOf(tank1, tank2) )

		// Read
		val found = tankRepository.findById(tank1.id)
		println("Found tank: $found")

		// Update
		tank1.tankType = TankType.T_90
		tankRepository.save(tank1)

		// Delete
		tankRepository.delete(tank2)
	}

}

fun main(args: Array<String>) {
	runApplication<SpringDataApplication>(*args)
}
