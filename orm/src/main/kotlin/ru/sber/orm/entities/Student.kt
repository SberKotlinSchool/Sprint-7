package ru.sber.orm.entities

import org.hibernate.annotations.NaturalId
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Student(
  @Id
  @GeneratedValue
  var id: Long = 0,

  var name: String,

  @NaturalId
  var email: String
){

  override fun toString(): String = "id=$id, name=$name"

}