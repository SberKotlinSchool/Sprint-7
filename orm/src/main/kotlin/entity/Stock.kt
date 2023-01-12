package entity

import javax.persistence.*

@Entity
@Table(schema = "orm", name = "stock")
data class Stock (
    @Id
    @GeneratedValue
    var id: Long = 0,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    var company: Company,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    var type: StockType = StockType.NORMAL

)

enum class StockType {
    NORMAL, PRIVILEGE
}