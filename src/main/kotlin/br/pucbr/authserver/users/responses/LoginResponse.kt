package br.pucbr.authserver.users.responses

data class LoginResponse (
    val token: String,
    val user: UserResponse
)
