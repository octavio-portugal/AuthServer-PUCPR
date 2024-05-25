package br.pucbr.authserver

import br.pucbr.authserver.roles.Role
import br.pucbr.authserver.roles.RoleRepository
import br.pucbr.authserver.users.User
import br.pucbr.authserver.users.UserRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class Bootstrapper(
    val userRepository: UserRepository,
    val rolesRepository: RoleRepository
) : ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val adminRole = rolesRepository.findByName("ADMIN")
            ?: rolesRepository.save(Role(name = "ADMIN", description = "System administrator"))
                .also { rolesRepository.save(Role(name = "USER", description = "Premium user")) }

        if (userRepository.findByRole(adminRole.name).isEmpty()){
            val admin = User(
                name = "Auth Server administrator",
                email = "admin@authserver.com",
                password = "admin",
            )
            admin.roles.add(adminRole)
            userRepository.save(admin)
        }
    }

}