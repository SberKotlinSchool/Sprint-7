//import enteties.HomeAddress

import entity.*
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Order::class.java)
        .addAnnotatedClass(Trader::class.java)
        .addAnnotatedClass(Stock::class.java)
        .addAnnotatedClass(Company::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val orderDao = OrderDAO(sessionFactory)
        val stockDao = StockDAO(sessionFactory)
        val traderDao = TraderDAO(sessionFactory)
        val companyDao = CompanyDAO(sessionFactory)

        var sber = companyDao.save(Company(ticker = "SBER", exchange = Exchange.NASDAQ))
        var sberStock = stockDao.save(Stock(company = sber, type = StockType.NORMAL))
        var trader = traderDao.save(Trader(name = "Rich Buratino"))

        var sberOrder = orderDao.save(Order(stock = sberStock, quantity = 10, trader = trader))
        var sberOrder2 = orderDao.save(Order(stock = sberStock, quantity = 5, trader = trader))

        val foundTrader = traderDao.find(trader.id)

        var foundOrder = orderDao.find(sberOrder.id)
        foundOrder = orderDao.find(sberOrder2.id)
        val trader1 = foundOrder?.trader
        val orders = trader1?.orders

        val foundOrders = orderDao.findAll()

        orderDao.delete(sberOrder2)

    }
}

class OrderDAO(private val sessionFactory: SessionFactory) {
    fun save(order: Order): Order {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(order)
            session.transaction.commit()
        }
        return order
    }

    fun find(id: Long): Order? {
        val result: Order?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Order::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Order> {
        val result: List<Order>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Order").list() as List<Order>
            session.transaction.commit()
        }
        return result
    }

    fun delete(order: Order) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.delete(order)
            session.transaction.commit()
        }
    }
}

class CompanyDAO(private val sessionFactory: SessionFactory) {
    fun save(company: Company): Company {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(company)
            session.transaction.commit()
        }
        return company
    }
}

class TraderDAO(private val sessionFactory: SessionFactory) {
    fun save(trader: Trader): Trader {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(trader)
            session.transaction.commit()
        }
        return trader
    }
    
    fun find(id: Long): Trader? {
        val result: Trader?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Trader::class.java, id)
            session.transaction.commit()
        }
        return result
    }
}

class StockDAO(private val sessionFactory: SessionFactory) {
    fun save(stock: Stock): Stock {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(stock)
            session.transaction.commit()
        }
        return stock
    }
}