package br.pucbr.authserver.stores

import br.pucbr.authserver.books.request.BookRequest

data class StoreRequest(
    val name: String,
) {
    fun toStore() = Store(name = name!!)
}