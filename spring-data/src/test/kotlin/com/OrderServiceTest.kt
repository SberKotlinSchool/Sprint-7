package com

import com.com.entity.*
import com.com.repository.CompanyRepository
import com.com.repository.OrderRepository
import com.com.repository.StockRepository
import com.com.repository.TraderRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
internal class OrderServiceTest {

    @Autowired
    private lateinit var orderRepository: OrderRepository
    @Autowired
    private lateinit var companyRepository: CompanyRepository
    @Autowired
    private lateinit var stockRepository: StockRepository
    @Autowired
    private lateinit var traderRepository: TraderRepository

    @Test
    fun initTest() {
        var sber = companyRepository.save(Company(ticker = "SBER", exchange = Exchange.NASDAQ))
        var sberStock = stockRepository.save(Stock(company = sber, type = StockType.NORMAL))
        var trader = traderRepository.save(Trader(name = "Rich Buratino"))

        var sberOrder = orderRepository.save(Order(stock = sberStock, quantity = 10, trader = trader))
        var sberOrder2 = orderRepository.save(Order(stock = sberStock, quantity = 5, trader = trader))

        val foundTrader = traderRepository.findById(trader.id)

        var foundOrder = orderRepository.findById(sberOrder.id).orElseThrow()
        foundOrder = orderRepository.findById(sberOrder2.id).orElseThrow()
        val trader1 = foundOrder.trader
        val orders = trader1.orders

        val foundOrders = orderRepository.findAll()

        orderRepository.delete(sberOrder2)
    }
}