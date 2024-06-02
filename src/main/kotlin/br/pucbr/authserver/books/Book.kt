package br.pucbr.authserver.books

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "tblbook")
class Book (
    @Id @GeneratedValue
    val id: Long? = null,
    @Column(unique = true, nullable = false)
    val title: String,
    @NotNull
    val author: String,
    @NotNull
    val unit: Int,
    val price: Double,
)