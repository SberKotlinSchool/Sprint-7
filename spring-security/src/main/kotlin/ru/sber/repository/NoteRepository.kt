package ru.sber.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.model.Note

@Repository
interface NoteRepository : CrudRepository<Note, Long> {

    fun getNoteById(id: Long): Note

    fun searchNoteByName(s: String): List<Note>

    fun searchNoteByAddress(s: String): List<Note>

    fun searchNoteByPhone(s: String): List<Note>

//    fun getById(id: Long): Note?
//    fun getAll(): List<Note>
//    fun searchWithFilter(name: String? = null, address: String? = null, phone: String? = null): List<Note>
//    fun searchByName(name: String): List<Note>
//    fun searchByAddress(address: String): List<Note>
//    fun searchByPhone(phone: String): List<Note>
//    fun save(note: Note)
//    fun deleteById(id: Long)
//    fun updateById(id: Long, note: Note)
//    fun deleteAll()
}