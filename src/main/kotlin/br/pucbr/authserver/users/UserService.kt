package br.pucbr.authserver.users

import br.pucbr.authserver.exception.BadRequestException
import br.pucbr.authserver.roles.RoleRepository
import br.pucbr.authserver.security.Jwt
import br.pucbr.authserver.users.responses.LoginResponse
import br.pucbr.authserver.users.responses.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val jwt: Jwt
) {

    fun save(user: User) = userRepository.save(user)

    fun findAll(
        dir: SortDir,
        role: String?
    ) =
        role?.let { r ->
            when (dir) {
                SortDir.ASC -> userRepository.findByRole(r.uppercase()).sortedBy { it.name }
                SortDir.DESC -> userRepository.findByRole(r.uppercase()).sortedBy { it.name }
            }
        } ?: when (dir) {
            SortDir.ASC -> userRepository.findAll(Sort.by("name").ascending())
            SortDir.DESC -> userRepository.findAll(Sort.by("name").descending())
        }


    fun findByIdOrNull(id: Long) = userRepository.findByIdOrNull(id)

    fun delete(id: Long): Boolean {
        val user = userRepository.findByIdOrNull(id) ?: return false
        if (user.roles.any{ it.name == "ADMIN" }) {
            val count = userRepository.findByRole("ADMIN").size
            if (count == 1) throw BadRequestException("Cannot delete the last system admin!")
        }
        userRepository.delete(user)
        log.warn("User deleted. id={} name={}", user.id, user.name)
        return true
    }

    fun addRole(id: Long, roleName: String): Boolean {
        val user = userRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("User $id not found!")

        if (user.roles.any { it.name == roleName }) return false

        val role = roleRepository.findByName(roleName)
            ?: throw IllegalArgumentException("Invalid role $roleName")
        user.roles.add(role)
        userRepository.save(user)
        return true
    }

    fun login(email: String, password: String): LoginResponse? {
        val user = userRepository.findByEmail(email).firstOrNull()

        if (user == null) {
            log.warn("User {} not found!", email)
            return null
        }
        if (password != user.password) {
            log.warn("Invalid password")
            return null
        }
        log.info("Userlogged in: id={}, name={}", user.id, user.name)
        return LoginResponse(
            token = jwt.createToken(user),
            UserResponse(user)
        )

    }

    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }
}