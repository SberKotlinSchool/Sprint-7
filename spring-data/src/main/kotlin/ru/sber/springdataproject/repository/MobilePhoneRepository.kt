package ru.sber.springdataproject.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.springdataproject.entity.MobilePhone

@Repository
interface MobilePhoneRepository : CrudRepository<MobilePhone,Long>{
}