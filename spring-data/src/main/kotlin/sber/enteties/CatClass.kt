package sber.enteties

import javax.persistence.*


@Entity
@Table(name="cat_class")
class CatClass (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "cat_type")
    var catType: String
) {
        override fun toString(): String {
            return "CatClass(id=$id, catType='$catType')"
        }
    }
