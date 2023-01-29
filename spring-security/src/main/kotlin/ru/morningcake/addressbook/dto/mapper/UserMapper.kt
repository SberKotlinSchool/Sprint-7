package ru.morningcake.addressbook.dto.mapper

import ru.morningcake.addressbook.dto.UserRegistrationDto
import ru.morningcake.addressbook.entity.User

fun userFromDto(dto : UserRegistrationDto) : User {
    return User(
        name = dto.name, login = dto.login!!, hash = dto.password!!,
//        question = ControlQuestion(
//            type = dto.questionType!!, answer = dto.questionAnswer
//        )
    )
}