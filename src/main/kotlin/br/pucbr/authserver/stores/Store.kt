package br.pucbr.authserver.stores

import br.pucbr.authserver.books.Book
import jakarta.persistence.*

@Entity
class Store(
    @Id @GeneratedValue
    val id: Long? = null,
    @Column(unique = true, nullable = false)
    val name: String,

    @OneToMany
    @JoinTable(
        name = "BookStore",
        joinColumns = [JoinColumn(name = "idStore")],
        inverseJoinColumns = [JoinColumn(name = "idBook")]
    )
    val books: MutableSet<Book> = mutableSetOf()
)
