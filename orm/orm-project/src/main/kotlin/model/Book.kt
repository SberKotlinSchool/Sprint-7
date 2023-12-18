package model
import javax.persistence.*

@Entity
@Table(name = "books")
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    var author: Author // понятно, что в реальности у книги может быть не один автор, но это упрощение
)