package br.pucbr.authserver.books.response

import br.pucbr.authserver.books.Book

class BookResponse (
    val id: Long,
    val title: String,
    val author: String,
    val price: Double
) {
    constructor(b : Book): this(
        id = b.id!!,
        title = b.title,
        author = b.author,
        price = b.price
    )
}