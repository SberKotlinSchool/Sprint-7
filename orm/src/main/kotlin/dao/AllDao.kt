package dao
import entities.*
import org.hibernate.SessionFactory


class IdentityDao(private val sessionFactory: SessionFactory) {

    fun find(name: String): Identity? {
        val result: Identity?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.byNaturalId(Identity::class.java).using("name", name).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun save(identity: Identity) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.save(identity)
            it.transaction.commit()
        }
    }
}

class ContractTypeDao(private val sessionFactory: SessionFactory) {
    fun find(name: String): ContractType? {
        val result: ContractType?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.byNaturalId(ContractType::class.java).using("name", name).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun save(ctypes: ContractType) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.save(ctypes)
            it.transaction.commit()
        }
    }
}

class ProductDao(private val sessionFactory: SessionFactory) {
    fun save(product: Product) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.save(product)
            it.transaction.commit()
        }
    }

    fun findProducts(): List<Product> {
        val result: List<Product>
        sessionFactory.openSession().use { it ->
            it.beginTransaction()
            result = it.createQuery("from Product", Product::class.java).resultList
            it.transaction.commit()
        }
        return result
    }
}