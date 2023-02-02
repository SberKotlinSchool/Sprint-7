package io.vorotov.employees.dto

import javax.validation.constraints.NotEmpty

data class Employee(

    val id: Long? = 1,

    @NotEmpty
    val fullName: String? = null,

    val address: String? = null,

    val phone: String? = null,

    val email: String? = null

)