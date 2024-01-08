package entity

import javax.persistence.Embeddable

@Embeddable
class Address(
    val country: String,
    val city: String
) {
    override fun toString(): String {
        return "$country, $city"
    }
}
