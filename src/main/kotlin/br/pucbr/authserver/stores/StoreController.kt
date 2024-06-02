package br.pucbr.authserver.stores

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/stores")
@RestController
class StoreController(
    private val storeService: StoreService
) {
    @GetMapping
    fun list() =
        storeService.findAll()
            .map { StoreResponse(it) }
            .let { ResponseEntity.ok(it) }

    @PostMapping
    fun insert(@Valid @RequestBody store: StoreRequest) =
        store.toStore()
            .let { storeService.insert(it) }
            .let { StoreResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @PutMapping("/{id}/books/{title}")
    fun addBook(
        @PathVariable id: Long,
        @PathVariable title: String
    ): ResponseEntity<Void> =
        if (storeService.addBook(id, title)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()

}