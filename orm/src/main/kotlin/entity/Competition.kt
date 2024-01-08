package entity

import org.hibernate.annotations.Cascade
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

@Entity
data class Competition(
    @Id
    @GeneratedValue
    val id: Long,
    val magicianId: Long,
    val nomination: Nomination,
    val lotNumber: Int
)
