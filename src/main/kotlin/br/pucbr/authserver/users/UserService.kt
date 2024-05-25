package br.pucbr.authserver.users

import br.pucbr.authserver.roles.RoleRepository
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
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
        } ?:
        when (dir) {
            SortDir.ASC -> userRepository.findAll(Sort.by("name").ascending())
            SortDir.DESC -> userRepository.findAll(Sort.by("name").descending())
        }


    fun findByIdOrNull(id: Long) = userRepository.findByIdOrNull(id)

    fun delete(id: Long) = userRepository.deleteById(id)

    fun addRole(id: Long, roleName: String): Boolean {
        val user = userRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("User $id not found!")

        if (user.roles.any { it.name == roleName}) return false

        val role = roleRepository.findByName(roleName)
            ?: throw IllegalArgumentException("Invalid role $roleName")
        user.roles.add(role)
        userRepository.save(user)
        return true
    }
}