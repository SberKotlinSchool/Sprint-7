package ru.morningcake.addressbook.exception;

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


/**
 * Ошибка аутентификации
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Authentication Failed")
class AuthenticationFailedException(msg: String?) : RuntimeException(msg)
