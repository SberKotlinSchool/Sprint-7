package enteties

import javax.persistence.*

@Entity
@Table(name="Статьи")
class Article (
    @Id
    @GeneratedValue
    var id: Long = 0,
    var science: String,
    var publicistic: String,
)