package ru.sber.springdata.persistence.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "libraries")
class LibraryEntity(
    @Id
    @GeneratedValue
    var id: Long = 0,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
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