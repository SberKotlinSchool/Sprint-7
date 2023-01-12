package entity

import javax.persistence.*

@Entity
@Table(schema = "orm", name = "company")
data class Company (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(name = "ticker", nullable = false, length = 5)
    var ticker: String,

    @Column(name = "exchange", nullable = false)
    @Enumerated(EnumType.STRING)
    var exchange: Exchange = Exchange.NYSE
)

enum class Exchange {
    NYSE, NASDAQ, SSE, BSE
}