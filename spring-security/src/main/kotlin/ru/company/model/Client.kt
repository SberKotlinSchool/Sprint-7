package ru.company.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Entity
data class Client(
    @Id
    @GeneratedValue
    var id: Long? = 1,
    @field:NotEmpty
    var fio: String? = null,
    @field:NotEmpty
    var address: String? = null,
    var phone: String? = null,
    @field:Email
    var email: String? = null
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Client

        if (id != other.id) return false
        if (fio != other.fio) return false
        if (address != other.address) return false
        if (phone != other.phone) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (fio?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        return result
    }
}
