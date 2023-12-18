package com.dokl57.springmvc.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import lombok.EqualsAndHashCode

@Entity
@EqualsAndHashCode
class Role(
    @Id
    @GeneratedValue
    var id: Long,
    var role: String
) {
    constructor() : this(1, "")
}
