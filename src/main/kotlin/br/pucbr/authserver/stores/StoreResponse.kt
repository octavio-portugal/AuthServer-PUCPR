package br.pucbr.authserver.stores

import br.pucbr.authserver.books.Book

class StoreResponse(
    val name: String,
    val books: MutableSet<Book>?
) {
    constructor(store: Store): this(name = store.name, store.books )
}