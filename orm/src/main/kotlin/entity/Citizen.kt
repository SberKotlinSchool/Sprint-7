package entity

import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne


@Entity
data class Citizen(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @NaturalId(mutable = true)
    var firstName: String,
    val secondName: String,
    @OneToOne
    val address: Address,
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(targetEntity = ContactInformation::class, mappedBy = "citizen", fetch = FetchType.EAGER,)
    var contactInformation: List<ContactInformation> = emptyList(),
) {
    override fun toString(): String {
        return "Citizen(id=$id, name='$firstName $secondName', address='$address', " +
                "contact information=$contactInformation)"
    }
}