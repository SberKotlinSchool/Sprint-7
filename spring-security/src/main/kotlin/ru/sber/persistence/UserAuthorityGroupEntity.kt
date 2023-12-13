package ru.sber.persistence

import javax.persistence.*

@Entity
@Table(name = "user_authority_group")
class UserAuthorityGroupEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,

        @Enumerated(value = EnumType.STRING)
        var group: AuthorityGroup

        )

enum class AuthorityGroup {
    USER,
    ADMIN,
    API
}