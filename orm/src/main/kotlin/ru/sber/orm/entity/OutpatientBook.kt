package ru.sber.orm.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class OutpatientBook (
    @Id
    @GeneratedValue
    var id: Long = 0,
    @CreationTimestamp
    var hospitalization: LocalDateTime,
    @UpdateTimestamp
    var discharge: LocalDateTime
) {
    override fun toString(): String {
        return "OutpatientBook(id=$id, hospitalization='$hospitalization', discharge='$discharge')"
    }
}