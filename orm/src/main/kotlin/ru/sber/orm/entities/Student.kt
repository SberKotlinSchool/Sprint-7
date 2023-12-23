package ru.sber.orm.entities

import org.hibernate.annotations.NaturalId
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
class Student(
  @Id
  @GeneratedValue
  var id: Long = 0,

  var name: String,

  @NaturalId
  var email: String,

  @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], mappedBy = "students")
  var subjects: MutableList<Subject> = mutableListOf()
){

  override fun toString(): String = "id=$id, name=$name"

}