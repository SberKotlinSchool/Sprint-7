package entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Language (
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    var language_id: Long = 0,

    var name: String
)
{
    override fun toString(): String {
        return "Language(language_id=$language_id, name=$name)"
    }
}