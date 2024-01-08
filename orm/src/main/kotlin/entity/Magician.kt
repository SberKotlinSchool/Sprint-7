package entity

import org.hibernate.annotations.NaturalId
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne


@Entity
data class Magician(
    @Id
    @GeneratedValue
    val id: Long,
    @Column(name = "first_name", length = 127)
    var firstName: String,
    @Column(name = "second_name", length = 127)
    val secondName: String,
    @Column(name = "address")
    val address: Address,
    @Column(name = "contactInformation")
    val contactInformation: ContactInformation,
    @OneToMany(mappedBy = "magicianId", cascade = [CascadeType.ALL])
    @Column(name = "competition")
    val competition: List<Competition>,
) {
    override fun toString(): String {
        return "Magician(id=$id, name='$firstName $secondName', address='$address', " +
                "contact information=$contactInformation, nomination=$competition)"
    }
}