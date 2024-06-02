package br.pucbr.authserver.books.request

import br.pucbr.authserver.books.Book

class BookRequest(
    val title: String,
    val author: String,
    val unit: Int,
    val price: Double?
) {
    fun toBook() = Book(
        title = title!!,
        author = author!!,
        unit = unit,
        price = price ?: 0.0
    )
}