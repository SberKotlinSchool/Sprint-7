import dao.ContractTypeDao
import dao.IdentityDao
import dao.ProductDao
import entities.*
import org.hibernate.cfg.Configuration

fun main() {
	val sessionFactory = Configuration().configure()
		.addAnnotatedClass(Product::class.java)
		.addAnnotatedClass(ProductGroup::class.java)
		.addAnnotatedClass(Identity::class.java)
		.addAnnotatedClass(ContractType::class.java)
		.buildSessionFactory()

	sessionFactory.use {
		val identityDao = IdentityDao(it)
		val identityName = "Life Policy"

		val identity =
			identityDao.find(identityName) ?: Identity(name = identityName)
				.also { identity -> identityDao.save(identity) }

		val ctypesDao = ContractTypeDao(it)

		val nameCtype1 = "NSG"
		val ctype1 = ctypesDao.find(nameCtype1) ?: ContractType(name = nameCtype1)
			.also { ctype -> ctypesDao.save(ctype) }

		val nameCtype2 = "ISG"
		val ctype2 = ctypesDao.find(nameCtype2) ?: ContractType(name = nameCtype2)
			.also { ctype -> ctypesDao.save(ctype) }

		val productDao = ProductDao(it)
		val product = Product(
			type = FrequencyType.Month,
			productGroup = ProductGroup(identity = identity, groupPosition = "7"),
			name = "Family Asset",
			ctypes = listOf(ctype1, ctype2)
		)

		productDao.save(product)

		val products = productDao.findProducts()
		println(products)
	}

}