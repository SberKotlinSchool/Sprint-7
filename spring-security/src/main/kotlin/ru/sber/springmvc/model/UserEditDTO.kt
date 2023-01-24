package ru.sber.springmvc.model

import java.io.Serializable

class UserEditDTO : Serializable {
    var id: Long = 0
    var name: String? = null
    var login: String? = null
    var password: String? = null
    var address: String? = null
}