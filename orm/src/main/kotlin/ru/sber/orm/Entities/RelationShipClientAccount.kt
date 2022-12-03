package ru.sber.orm.Entities

import javax.persistence.*

@Entity
@Table(name = "RELATION_ACCOUNT_CLIENT")
class RelationshipClientAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rel_generator")
    @SequenceGenerator(name="rel_generator", sequenceName = "rel_seq", allocationSize=1)
    val id: Long? = null,

    @ManyToMany(cascade = [CascadeType.ALL])
    var clientId: MutableList<Client>? = null,

    @ManyToMany(cascade = [CascadeType.ALL])
    var creditId: MutableList<Account>? = null,
)
