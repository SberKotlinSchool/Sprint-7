package ru.sber.springdata.entities

import javax.persistence.Embeddable

@Embeddable
class PersonalData(
    var passport: String,
    var snils: String
)
