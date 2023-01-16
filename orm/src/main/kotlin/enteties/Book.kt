package enteties

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

@Entity
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @NaturalId(mutable = true)
    var title: String,
    @ManyToOne(cascade = [CascadeType.ALL])
    var author: Author,
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var libraries: MutableList<Library>,
    @CreationTimestamp
    var createdTime: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Book(id=$id, title='$title', author=$author, libraries=$libraries)"
    }
}
