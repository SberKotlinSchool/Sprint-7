package ru.sber.orm.Entities

import java.io.Serializable
import javax.persistence.*

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