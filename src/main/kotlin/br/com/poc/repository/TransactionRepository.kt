package br.com.poc.repository

import br.com.poc.entity.Accumulation
import br.com.poc.entity.Process
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository


@Repository
interface TransactionRepository: JpaRepository<Process, Long> {

    @Query("FROM Process p WHERE p.status = :pending")
    fun findByStatus(pending: String?): List<Process>
}