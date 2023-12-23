package ru.sber.springjpademo.persistence.entity

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "student")
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