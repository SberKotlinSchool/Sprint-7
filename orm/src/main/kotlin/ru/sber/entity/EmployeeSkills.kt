package ru.sber.entity

import java.time.LocalDate
import javax.persistence.Embeddable

@Embeddable
class EmployeeSkills(
        var disciplinaryPenalties: Boolean,
        var lastPenalty: LocalDate,
        var note: String
)