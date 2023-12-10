package ru.sber.persistence

import javax.persistence.*

@Entity
@Table(name = "user_authority")
data class UserAuthorityEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    var username: String,
    var password: String,

    @OneToMany(cascade = [CascadeType.ALL])
    var userAuthorityGroupList: MutableList<UserAuthorityGroupEntity> = mutableListOf(),

    var expired: Boolean,
    var locked: Boolean,
    var credExpired: Boolean,
    var enabled: Boolean


)