package ru.sber.springdata.entities

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class AuthorContacts(
    @Column(nullable = true)
    var phone: String? = null,

    @Column(nullable = true)
    var email: String? = null
)
