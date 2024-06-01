package br.pucbr.authserver.users.requests

import br.pucbr.authserver.users.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserRequest(
    val name: String?,

    @field:Email
    val email: String?,

    @field:NotBlank
    val password: String?,
) {
    fun toUser() = User(
        name = name ?: "",
        email = email!!,
        password = password!!
    )
}