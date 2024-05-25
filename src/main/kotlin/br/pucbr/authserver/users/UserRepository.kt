package br.pucbr.authserver.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByEmail(email: String): List<User>

    @Query(
        "select distinct u from User u" +
            " join u.roles r" +
            " where r.name = :role" +
            " order by u.name"
    )
    fun findByRole(role: String): List<User>
}
//class UserRepository {
//    private var lastId = 0L
//    private val users = mutableMapOf<Long, User>()
//
//    fun save(user: User): User {
//        val id  = user.id
//        if (id == null){
//            lastId += 1
//            user.id = lastId
//            users[lastId] = user
//        } else {
//            users[id] = user
//        }
//        return user
//    }
//
//    fun findAll(): List<User> = users.values.toList()
//
//    fun findByIdOrNull(id: Long): User? = users[id]

//        fun delete(id: Long) = users.remove(id)
//}