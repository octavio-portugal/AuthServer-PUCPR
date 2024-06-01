package br.pucbr.authserver.users.requests

import br.pucbr.authserver.users.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRequest(
    val name: String?,

    @field:Email
    val email: String?,

    @field:Size(min = 8, max = 50)
    val password: String?,
) {
    fun toUser() = User(
        name = name ?: "",
        email = email!!,
        password = password!!
    )
}
