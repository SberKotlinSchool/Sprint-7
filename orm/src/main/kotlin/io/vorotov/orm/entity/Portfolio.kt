package io.vorotov.orm.entity

import javax.persistence.*

@Entity
@Table(name = "PORTFOLIOS")
class Portfolio() {

    constructor(fio: String, age: Int? = null, user: User? = null): this() {
        this.user = user
        this.fio = fio
        this.age = age
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var user: User? = null
    set(value) {
        field = value
        if (value?.portfolio != this) value?.portfolio == this
    }

    @Column(nullable = false)
    var fio: String? = null

    var age: Int? = null

}