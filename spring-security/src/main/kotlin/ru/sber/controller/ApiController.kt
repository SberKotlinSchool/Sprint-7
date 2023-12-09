package ru.sber.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.sber.model.Note
import ru.sber.repository.NoteRepository

@RestController
@RequestMapping("/api")
class ApiController(@Autowired val noteRepository: NoteRepository) {

    @GetMapping("/list")
    fun list(@RequestParam name: String?, @RequestParam address: String?, @RequestParam phone: String?): List<Note> {
        if (name != null) return noteRepository.searchNoteByName(name)
        if (address != null) return noteRepository.searchNoteByAddress(address)
        if (phone != null) return noteRepository.searchNoteByPhone(phone)
        return noteRepository.findAll() as List<Note>
    }

    @GetMapping("/{id}/view")
    fun view(@PathVariable id: Long): Note? {
        return noteRepository.getNoteById(id)
    }

    @PostMapping("/add")
    fun add(@RequestBody note: Note) {
        noteRepository.save(note)
    }

    @PutMapping("/{id}/edit")
    fun edit(@RequestBody note: Note, @PathVariable id: Long) {
        note.id = id
        noteRepository.save(note)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    fun delete(@PathVariable id: Long) {
        noteRepository.deleteById(id)
    }
}