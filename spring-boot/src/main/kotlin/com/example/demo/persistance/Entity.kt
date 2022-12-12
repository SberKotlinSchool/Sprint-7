package com.example.demo.persistance

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Entity {

    constructor(name: String) {
        this.name = name
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    var name: String?

}
