package ru.morningcake.addressbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.morningcake.addressbook.constant.Role
import ru.morningcake.addressbook.entity.User
import ru.morningcake.addressbook.service.UserService

@SpringBootApplication
class AddressBookApplication

fun main(args: Array<String>) {
    val ctx = runApplication<AddressBookApplication>(*args)
    val userService = ctx.getBean(UserService::class.java)

    userService.registration(
        User(
            name = "Юзверь", login = "u", hash = "User@1", roles = mutableSetOf(Role.USER)
        )
    )
    userService.registration(
        User(
            name = "Аннигилятор", login = "d", hash = "Delete@1", roles = mutableSetOf(Role.USER, Role.DELETER)
        )
    )
    userService.registration(
        User(
            name = "Админ", login = "a", hash = "Admin@1", roles = mutableSetOf(Role.ADMIN)
        )
    )
}
