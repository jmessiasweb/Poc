package br.com.poc.entity

import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Introspected
class Accumulation (
    @Id @GeneratedValue(
        generator = "seq_generator_ac",
        strategy = GenerationType.SEQUENCE
    ) @SequenceGenerator(name = "seq_generator_ac", sequenceName = "SEQ_DATABASE_AC", allocationSize = 1) var id: Long?,
    @Column(name = "cpf", nullable = false, unique = false) var cpf: String,
    @Column var transactionAmount: BigDecimal,
    processDate: String,
        ){
    @Column
    var processDate: String? = processDate
}