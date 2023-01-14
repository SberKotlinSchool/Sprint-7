package entity

import javax.persistence.*

@Entity
@Table(schema = "orm", name = "order")
class Order (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_id", nullable = false)
    var stock: Stock,

    @Column(name = "qnt", nullable = false)
    var quantity: Int,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trader_id")
    var trader: Trader

    )