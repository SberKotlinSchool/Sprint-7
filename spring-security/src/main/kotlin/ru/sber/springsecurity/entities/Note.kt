package ru.sber.springsecurity.entities

import ru.sber.springsecurity.utils.RequestUtils.Companion.DAY_MONTH_HMS
import ru.sber.springsecurity.utils.RequestUtils.Companion.notes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Notes")
class Note {

    @Id
    @GeneratedValue
    val id: Long = 0

    var author: String = ""

    var content: String = ""

    var postDate: String = ""

    override fun toString(): String {
        return "${postDate}\t$author:\t$content"
    }

    companion object {
        fun saveNote(note: Note) {
            note.postDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DAY_MONTH_HMS))
            notes.add(note)
        }

        fun deleteNote(id: Int) {
            if (notes.size > id) notes.removeAt(id)
        }

        fun editNote(id: Int, note:Note) {
            if (notes.size > id) {
                note.postDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DAY_MONTH_HMS))
                notes[id] = note
            }
        }
    }
}