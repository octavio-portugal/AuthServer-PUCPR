package br.pucbr.authserver.users

import br.pucbr.authserver.users.requests.LoginRequest
import br.pucbr.authserver.users.requests.UserRequest
import br.pucbr.authserver.users.responses.UserResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping()
    fun insert(@RequestBody @Valid user: UserRequest) = ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.save(user.toUser()))

    @GetMapping
    fun findAll(
        @RequestParam sortDir: String? = null,
        @RequestParam role: String? = null
    ) =
        SortDir.entries.firstOrNull { it.name == (sortDir ?: "ASC").uppercase() }
            ?.let { userService.findAll(it, role) }
            ?.map { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.badRequest()


    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) =
        userService.findByIdOrNull(id)
            ?.let { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "WebToken")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void> =
        if (userService.delete(id)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()

    @PutMapping("/{id}/roles/{role}")
    fun grant(
        @PathVariable id: Long,
        @PathVariable role: String
    ): ResponseEntity<Void> =
        if (userService.addRole(id, role)) ResponseEntity.ok().build()
        else ResponseEntity.badRequest().build()

    @PutMapping("/{id}/books/{title}")
    fun grantBook(
        @PathVariable id: Long,
        @PathVariable title: String
    ): ResponseEntity<Void> =
        if (userService.addBook(id, title)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()

    @PostMapping("/login")
    fun login(@Valid @RequestBody login: LoginRequest) =
        userService.login(login.email!!, login.password!!)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

}