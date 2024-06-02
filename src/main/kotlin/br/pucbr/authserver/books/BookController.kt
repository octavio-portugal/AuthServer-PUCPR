package br.pucbr.authserver.books

import br.pucbr.authserver.books.request.BookRequest
import br.pucbr.authserver.books.response.BookResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(
    private val service: BookService
) {

    @PostMapping
    fun insert(@RequestBody @Valid book: BookRequest) = ResponseEntity.status(HttpStatus.CREATED)
        .body(service.save(book.toBook()))
        ?: ResponseEntity.badRequest()


    @GetMapping
    fun listBooks() =
        service.findAll()
            .map { BookResponse(it) }
            .let { ResponseEntity.ok(it) }

    @GetMapping("/{title}")
    fun findByTitle(@PathVariable title: String)=
        service.findByTitle(title)
            ?.let { BookResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{title}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "WebToken")
    fun deleteByTitle(@PathVariable title: String): ResponseEntity<Void> =
        if (service.delete(title)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()

}