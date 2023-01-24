package ru.sber.springmvc.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sber.springmvc.model.UserDTO
import ru.sber.springmvc.service.UserService

@RestController
@RequestMapping("/api")
class RestApiController {

    lateinit var userService: UserService
        @Autowired set

    @GetMapping("/users")
    fun getUserList() : ResponseEntity<List<UserDTO>> =
        ResponseEntity.ok(UserDTO.of(userService.getUserList()))

    @GetMapping("/users/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUser(@PathVariable id: Long) : ResponseEntity<UserDTO> =
        userService.findById(id)
            .map { ResponseEntity.ok(UserDTO.of(it)) }
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build())

    @DeleteMapping("/user/delete/{id}")
    @PreAuthorize("hasRole('DELETE_USER')")
    fun deleteUser(@PathVariable id : Long) : ResponseEntity<String> {
        return userService.findById(id)
            .map {
                userService.deleteUser(id)
                ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted")
            }
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
    }
}