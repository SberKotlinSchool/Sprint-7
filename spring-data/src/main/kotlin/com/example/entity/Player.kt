import javax.persistence.*

@Entity
data class Player(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: String,

    @ManyToMany
    @JoinTable(
        name = "player_platform",
        joinColumns = [JoinColumn(name = "player_id")],
        inverseJoinColumns = [JoinColumn(name = "platform_id")]
    )
    var platforms: MutableSet<Platform> = mutableSetOf()
)
