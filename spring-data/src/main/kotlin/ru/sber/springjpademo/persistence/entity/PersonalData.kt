package ru.sber.springjpademo.persistence.entity

import javax.persistence.Embeddable

@Embeddable
class PersonalData(
    var passport: String,
    var snils: String
)
