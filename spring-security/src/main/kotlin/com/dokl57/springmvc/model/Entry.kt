package com.dokl57.springmvc.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import lombok.Data

@Entity
@Data
class Entry(
    @Id
    @GeneratedValue
    var id: Long,
    val name: String,
    val address: String
) {
    constructor() : this(1, "", "")
}