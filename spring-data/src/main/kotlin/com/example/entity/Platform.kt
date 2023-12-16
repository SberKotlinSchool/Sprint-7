import javax.persistence.*

@Entity
data class Platform(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: String,

    @ManyToMany(mappedBy = "platforms")
    var players: MutableSet<Player> = mutableSetOf()
)
