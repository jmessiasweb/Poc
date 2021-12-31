package br.com.poc.repository

import br.com.poc.entity.Accumulation
import io.lettuce.core.dynamic.annotation.Param
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
//@CacheConfig(cacheNames = ["acumulo"])
interface AccumulationRepository: JpaRepository<Accumulation, Long> {

    @Query(
        "select * from accumulation a where a.process_date < :processDateTime",
        nativeQuery = true
    )
    fun findAccumulationByProcessDateTime(
        @Param("processDateTime") processDateTime: String
    ): List<Accumulation>
}