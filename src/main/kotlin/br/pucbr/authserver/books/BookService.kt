package br.pucbr.authserver.books

import br.pucbr.authserver.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BookService(
    private val repository: BookRepository
) {
    fun save(book: Book) = repository.save(book)

    fun findAll(): List<Book> = repository.findAll()

    fun findByTitle(title: String): Book? = repository.findByTitle(title)

    fun delete(title: String): Boolean {
        val book = repository.findByTitle(title) ?: return false
        repository.delete(book)
        log.warn("User deleted. id={} title={}", book.id, book.title)
        return true
    }

    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }
}
