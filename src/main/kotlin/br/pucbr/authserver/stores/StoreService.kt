package br.pucbr.authserver.stores

import br.pucbr.authserver.books.BookRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StoreService (
    val storeRepository: StoreRepository,
    val bookRepository: BookRepository
) {

    fun findAll(): List<Store> = storeRepository.findAll()

    fun findByName(name: String): Store? = storeRepository.findByName(name)

    fun insert(store: Store) =
        storeRepository.save(store)

    fun addBook(id: Long, bookTitle: String): Boolean {
        val store = storeRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("User $id not found!")

        if (store.books.any { it.title == bookTitle }) return false

        val book = bookRepository.findByTitle(bookTitle)
            ?: throw IllegalArgumentException("Invalid book $bookTitle!")
        store.books.add(book)
        storeRepository.save(store)
        return true
    }

}