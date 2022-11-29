package ru.sber.spring.mvc.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import ru.sber.spring.mvc.model.Book
import ru.sber.spring.mvc.service.BookService

@Controller
@RequestMapping(value = ["/book"])
class BookController(private val bookService: BookService) {

    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITE')")
    @GetMapping("/add")
    fun getAddBookPage(model: Model): String {
        model.addAttribute("book", Book())
        return "add"
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITE')")
    @PostMapping("/add")
    fun addBook(@ModelAttribute("book") book: Book): String {
        bookService.add(book)
        return "main"
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('READ')")
    @GetMapping("/list")
    fun getBooksList(model: Model): String {
        val books: List<Book> = bookService.getBooks()
        model.addAttribute("books", books)
        return "list"
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('READ')")
    @GetMapping("/{id}/view")
    fun viewById(model: Model, @PathVariable id: Int): String {
        val book: Book = bookService.getById(id) ?: throw Error("Book is not found")
        model.addAttribute("book",book)
        return "view"
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITE')")
    @PostMapping("/{id}/edit")
    fun edit(@PathVariable id: Int, @RequestBody book: Book): String {
        val entity: Book = bookService.getById(id) ?: throw Error("Book is not found")
        with(entity) {
            name = book.name
            author= book.author
        }
        bookService.update(entity)
        return "main"
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITE')")
    @GetMapping("/{id}/delete")
    fun delete(@PathVariable id: Int):String {
        bookService.delete(id)
        return "main"
    }

}