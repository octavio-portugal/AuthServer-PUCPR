package br.pucbr.authserver.stores

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository: JpaRepository<Store, Long> {
    fun findByName(name: String): Store?

    @Query(
        "select distinct s from Store s " +
                "join s.books b " +
                "where b.title = :title " +
                "order by s.name"
    )
    fun findStoreByBooks(title: String): List<Store>
}