package ru.sber.springdata.Entities

import jakarta.persistence.*
import java.io.Serializable

@Entity
@IdClass(AccountClientId::class)
class RelationshipClientAccount(
    @Id
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var clientId: Client? = null,

    @Id
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var accountId: Account? = null,
) : Serializable


data class AccountClientId(
    var clientId: Long? = null,
    var accountId: Long? = null
) : Serializable