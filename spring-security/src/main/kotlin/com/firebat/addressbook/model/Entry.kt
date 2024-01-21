package com.firebat.addressbook.model

data class Entry(
    var id: Long = 0, // для getId() в ObjectIdentityImpl()
    val name: String,
    val address: String
)