package com.dokl57.springmvc.model

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode
import java.util.*

@Entity
@Data
@EqualsAndHashCode
@Table(name = "users")
class User(
    @Id
    @GeneratedValue
    var id: Long = 0,
    var login: String,
    var password: String,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var roles: MutableList<Role>
) {
    constructor() : this(1, "", "", Collections.emptyList())

}