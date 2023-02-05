package io.vorotov.employees.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class HireEmployeeRq(

    @NotEmpty
    val fullName: String? = null,

    val address: String? = null,

    val phone: String? = null,

    @Email
    val email: String? = null

)