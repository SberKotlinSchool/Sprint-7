package ru.morningcake.addressbook.dto

import org.hibernate.validator.constraints.Length
import ru.morningcake.addressbook.constant.ControlQuestionType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern


data class UserRegistrationDto (
    /** Имя и фамилия  */
    @NotBlank(message = "Необходимо ввести имя и/или фамилию")
    val name: String? = null,

    /** Логин - уникальное значение  */
    @NotBlank(message = "Необходимо ввести логин")
    val login: String? = null,

    /** Пароль  */
    @NotBlank(message = "Необходимо ввести пароль")
    @Length(min = 6, message = "Длина пароля")
    @Pattern(
        regexp = "^(?=.*[\\d])(?=.*[{}\\[\\]/~`_<>;:.+\\-|!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[\\da-zA-Z{}\\[\\]/~`_<>;:.+\\-|!@#$%^&*]{6,}$",
        message = "Требования к паролю: латиница, длина минимум 6 символов, минимум одна заглавная буква, один спецсимвол и одна цифра"
    )
    val password: String? = null,

    /** Повторите пароль  */
    @NotBlank(message = "Необходимо повторно ввести пароль")
    val repeatedPassword: String? = null,

    /** Вопрос  */
    @NotNull(message = "Контрольный вопрос должен быть выбран")
    val questionType: ControlQuestionType? = null,

    /** Ответ  */
    @NotBlank(message = "Ответ на контрольный вопрос должен быть указан")
    val questionAnswer: String? = null,
)





