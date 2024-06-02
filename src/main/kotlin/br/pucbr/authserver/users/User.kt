package br.pucbr.authserver.users

import br.pucbr.authserver.books.Book
import br.pucbr.authserver.roles.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "tbluser")
class User(
    @Id @GeneratedValue
    var id: Long? = null,
    @NotNull
    var name: String = "",
    @Column(unique = true, nullable = false)
    var email: String = "",
    @NotNull
    var password: String = "",

    @ManyToMany
    @JoinTable(
        name = "UserRole",
        joinColumns = [JoinColumn(name = "idUser")],
        inverseJoinColumns = [JoinColumn(name = "idRole")]
    )
    val roles: MutableSet<Role> = mutableSetOf(),

    @OneToMany
    @JoinTable(
        name = "UserBook",
        joinColumns = [JoinColumn(name = "idUser")],
        inverseJoinColumns = [JoinColumn(name = "idBook")]
    )
    val books: MutableSet<Book> = mutableSetOf()
)