package br.pucbr.authserver.books

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BookRepository: JpaRepository<Book, Long>  {
    fun findByTitle(title: String): Book?
}