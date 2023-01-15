package dao

import entity.Address
import org.hibernate.SessionFactory

class AddressDao(
    override val sessionFactory: SessionFactory
) : BaseDao<Address> {
    fun findById(id: Long): Address? = findById(id, Address::class.java)
    fun findAll(): List<Address> = findAll(Address::class.java.simpleName)
}