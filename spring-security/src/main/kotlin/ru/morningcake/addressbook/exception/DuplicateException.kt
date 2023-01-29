package ru.morningcake.addressbook.exception;

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


/**
 * Ошибка - параметр уже занят
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Duplicate")
class DuplicateException(msg: String?) : IllegalArgumentException(msg)
