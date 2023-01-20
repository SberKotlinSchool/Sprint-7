package entities

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
@Table(name = "services", schema = "sberbank")
class Service (

    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long = 0,

    @Column(name = "name")
    var name: String? = null,

    @ManyToMany(mappedBy = "services", fetch = FetchType.LAZY)
    @Cascade(value = [CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST])
    var cards: MutableSet<Card> =  mutableSetOf()

)