package entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "libraries")
class LibraryEntity(
    @Id
    @GeneratedValue
    var id: Long = 0,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var address: LibraryAddressEntity,
    @Column(name = "library_name")
    @NaturalId
    var name: String,
    @Column(name = "has_abonement_program")
    var hasAbonementProgram: Boolean,
    @CreationTimestamp
    @Column(name = "created_time")
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    @Column(name = "updated_time")
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String = "LibraryEntity(id=$id, name=$name, hasAbonentProgram=$hasAbonementProgram)"
}