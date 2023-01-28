package enteties

import javax.persistence.*

@Entity
@Table(name="Инвентарь")
class Inventory (
    @Id
    @GeneratedValue
    var id: Long = 0,
    @Basic
    var inTheBag: String,
    var inThePocket: String
)