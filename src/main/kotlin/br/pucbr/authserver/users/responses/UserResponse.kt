package br.pucbr.authserver.users.responses

import br.pucbr.authserver.books.Book
import br.pucbr.authserver.roles.Role
import br.pucbr.authserver.users.User

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val roles: MutableSet<Role>,
    val books: MutableSet<Book>,
) {
    constructor(u: User): this(
        id = u.id!!,
        name = u.name,
        email = u.email,
        roles = u.roles,
        books = u.books,
    )
}
