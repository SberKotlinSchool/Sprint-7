package enteties

import javax.persistence.*


@Entity
class CatClass (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column
    var catType: String
) {
        override fun toString(): String {
            return "CatClass(id=$id, catType='$catType')"
        }
    }
