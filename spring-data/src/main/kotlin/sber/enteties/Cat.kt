package sber.enteties

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="cat")
class Cat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 1,

    @NaturalId(mutable=true)
    @Column(name = "name", length = 127)
    var name: String,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id" )
    var catClass: CatClass? = null,

    @Column(name = "color")
    var color: String,

    @Column(name = "birth_date")
    var birthDate: LocalDate,


    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "mother",
        referencedColumnName = "id",
        nullable = true)
    var mother: Cat?=null,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "brothers",
        referencedColumnName = "id",
        nullable = true)
    var brothers: MutableList<Cat?>?= null,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Cat(id=$id, name='$name', color='$color', " +
                "catType=$catClass, " +
                "birthDate=$birthDate," +
                "mother=$mother)"
    }
}

