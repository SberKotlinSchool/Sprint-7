package entity

import javax.persistence.*

@Entity
class History(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    val userChecker: UserChecker,

    @ManyToOne(fetch = FetchType.EAGER)
    var algorithm: Algorithm,

    var number: Long,

    var result: Boolean
)