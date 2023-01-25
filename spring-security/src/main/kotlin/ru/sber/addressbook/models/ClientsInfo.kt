package ru.sber.addressbook.models

import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = "public", name="clients_info")
data class CLientsInfo (
    @Id
    @GeneratedValue
    //var id : Int = 0,
    val name : String,
    val phone : String
)