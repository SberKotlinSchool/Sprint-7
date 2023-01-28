package ru.sber.addressbook.model

import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    @Column(nullable = false, unique = true)
    var login: String,
    var password: String,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var roles: MutableSet<Role>
)