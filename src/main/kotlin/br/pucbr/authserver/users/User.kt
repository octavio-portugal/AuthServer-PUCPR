package br.pucbr.authserver.users

import br.pucbr.authserver.roles.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.jetbrains.annotations.NotNull

@Entity
@Table(name="tbluser")
class User (
    @Id @GeneratedValue
    var id: Long? = null,
    @NotNull
    var name: String = "",
    @Column(unique = true, nullable = false)
    var email: String = "",
    @NotNull
    var password: String ="",

    @ManyToMany
    @JoinTable(
        name="UserRole",
        joinColumns = [JoinColumn(name= "idUser")],
        inverseJoinColumns = [JoinColumn(name= "idRole")]
    )
    @JsonIgnore
    val roles: MutableSet<Role> = mutableSetOf()
)