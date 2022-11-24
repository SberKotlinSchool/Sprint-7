import entity.MobilePhone
import config.MobileConfig
import dao.MobilePhoneDao


fun main() {

    val sessionFactory = MobileConfig().getSessionFactory()
    val session = sessionFactory.openSession();

    val mobilePhone1 = MobilePhone(phone_name = "Phone-1", weight = 150, processor = 1, memory = 1)
    val mobilePhone2 = MobilePhone(phone_name = "Phone-2", weight = 190, processor = 2, memory = 1)

    sessionFactory.use {sessionFactory ->

        val mobilePhoneDao = MobilePhoneDao(sessionFactory)

        mobilePhoneDao.save(mobilePhone1)


        var phone : MobilePhone? = mobilePhoneDao.findById(26)
        println("Найден мобильный телефон: $phone \n")

//        var found1 : List<MobilePhone> = mobilePhoneDao.findAll()
//        println("Найдены мобильные телефоны: $found1 \n")

    }


}