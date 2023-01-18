package ru.morningcake.addressbook.dto;

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern


data class ChangePasswordDto (

    /** Старый пароль  */
    @NotBlank(message = "Необходимо ввести старый пароль")
    val oldPassword: String? = null,

    /** Пароль  */
    @NotBlank(message = "Необходимо ввести новый пароль") @Length(min = 6, message = "Длина пароля")
    @Pattern(
        regexp = "^(?=.*[\\d])(?=.*[{}\\[\\]/~`_<>;:.+\\-|!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[\\da-zA-Z{}\\[\\]/~`_<>;:.+\\-|!@#$%^&*]{6,}$",
        message = "Требования к паролю: латиница, длина минимум 6 символов, минимум одна заглавная буква, один спецсимвол и одна цифра"
    )
    val newPassword: String? = null,

    /** Повторите пароль  */
    @NotBlank(message = "Необходимо повторно ввести пароль")
    val repeatedPassword: String? = null
)
