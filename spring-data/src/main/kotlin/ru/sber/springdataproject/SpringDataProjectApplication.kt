package ru.sber.springdataproject

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springdataproject.entity.Accessory
import ru.sber.springdataproject.entity.MobilePhone
import ru.sber.springdataproject.repository.AccessoriesRepository
import ru.sber.springdataproject.repository.MobilePhoneRepository

@SpringBootApplication
class SpringDataProjectApplication(private val mobilePhoneRepository: MobilePhoneRepository, private val accessoriesRepository: AccessoriesRepository) : CommandLineRunner {

    override fun run(vararg args: String?) {

        //create phones
        val mobilePhone1 = MobilePhone(phone_name = "Phone-1", weight = 150, processor = 1, memory = 1)
        val mobilePhone2 = MobilePhone(phone_name = "Phone-2", weight = 300, processor = 3, memory = 2)
        val mobilePhone3 = MobilePhone(phone_name = "Super-Phone-4", weight = 1000, processor = 20, memory = 33)

        mobilePhoneRepository.saveAll(listOf(mobilePhone1,mobilePhone2,mobilePhone3))


        //Read Phones
        var phone: MobilePhone = mobilePhoneRepository.findById(2).get()
        println("Найден мобильный телефон: $phone \n")

        //Create accessory
        val accessory1 = Accessory(accessoryName = "Cover", mobilePhone = phone!!)
        val accessory2 = Accessory(accessoryName = "Power Station", mobilePhone = phone)
        accessoriesRepository.saveAll(listOf(accessory1,accessory2))

        //Read Phone with accessories
        phone = mobilePhoneRepository.findById(2).get()
        println("Найден мобильный телефон с аксессуарами $phone \n")

        //edit phone
        phone?.phone_name += " Edited"
        mobilePhoneRepository.save(phone)

        //Read edited Phone
        phone = mobilePhoneRepository.findById(2).get()
        println("Найден мобильный телефон с аксессуарами $phone \n")

        //Delete accessory
        phone.accessories.forEach { accessory ->
            accessoriesRepository.delete(accessory)
            println("Аксессуар $accessory удален")
        }

        //Read phone without accessories
        phone = mobilePhoneRepository.findById(2).get()
        println("Найден мобильный телефон без аксессуаров $phone \n")

        //Delete phone
        mobilePhoneRepository.delete(phone!!)
        val nullPhone = mobilePhoneRepository.findById(3)
        if (nullPhone == null) {
            println("Телефон  $phone был удален\n")
        }
        
    }

}


fun main(args: Array<String>) {
    runApplication<SpringDataProjectApplication>(*args)
}
