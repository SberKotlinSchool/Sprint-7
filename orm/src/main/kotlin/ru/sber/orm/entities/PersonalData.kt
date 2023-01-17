package ru.sber.orm.entities

import javax.persistence.Embeddable

@Embeddable
class PersonalData(
    var passport: String,
    var snils: String
)
