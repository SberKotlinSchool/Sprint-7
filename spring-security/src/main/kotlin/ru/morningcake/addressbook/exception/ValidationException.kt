package ru.morningcake.addressbook.exception;

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Request")
class ValidationException(msg: String?) : IllegalArgumentException(msg)
