package ru.sber.springdata.persistence.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Tank(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tank_type")
    var tankType: TankType,

    @OneToOne(cascade = [CascadeType.ALL])
    var engine: Engine,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var ammo: MutableList<Ammo>? = null,

    @CreationTimestamp
    @Column(name = "created_time")
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    @Column(name = "updated_time")
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Tank(id = $id, tankType=$tankType, engine=$engine, ammo=$ammo)"
    }
}

enum class TankType {
      T_80
    , T_90
    , CHALLENGER_2
    , ABRAMS
    , LEOPARD_2
}