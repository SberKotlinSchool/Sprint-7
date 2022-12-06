import entity.MobilePhone
import config.MobileConfig
import dao.AccessoryDao
import dao.MobilePhoneDao
import entity.Accessory


fun main() {

    //@OneToMany in MobilePhone class
    //@ManyToOne in Accessory class

    val sessionFactory = MobileConfig().getSessionFactory()

    val mobilePhone1 = MobilePhone(phone_name = "Phone-1", weight = 150, processor = 1, memory = 1)


    sessionFactory.use { sessionFactory ->


        val mobilePhoneDao = MobilePhoneDao(sessionFactory)
        val accessoryDao = AccessoryDao(sessionFactory)

        //Create Phone
        val phoneId: Long = mobilePhoneDao.save(mobilePhone1)

        //Read Phone
        var phone: MobilePhone? = mobilePhoneDao.findById(phoneId)
        println("Найден мобильный телефон: $phone \n")

        //Create accessory
        val accessory1 = Accessory(accessoryName = "Cover", mobilePhone = phone!!)
        val accessory2 = Accessory(accessoryName = "Power Station", mobilePhone = phone)

        accessoryDao.save(accessory1)
        accessoryDao.save(accessory2)

        //Update
        phone = mobilePhoneDao.findById(phoneId)
        phone?.phone_name += " Edited"
        phone?.let { mobilePhoneDao.update(it) }
        println("Измененный мобильный телефон: $phone \n c аксессуарами")


        //Delete accessory
        phone?.accessories?.forEach { accessory ->
            accessoryDao.delete(accessory)
            println("Аксессуар $accessory удален")
        }
        //Delete phone
        mobilePhoneDao.delete(phone!!)
        val nullPhone = mobilePhoneDao.findById(phoneId)
        if (nullPhone == null) {
            println("Телефон  $phone был удален\n")
        }
    }


}