package entity

import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Embeddable
data class ContactInformation(
    val phoneNumber: String,
    val email: String
)
