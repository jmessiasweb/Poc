package br.com.poc.entity

import java.math.BigDecimal
import javax.persistence.*

@Entity
class Process (
    @Id @GeneratedValue(
        generator = "seq_generator",
        strategy = GenerationType.SEQUENCE
    ) @SequenceGenerator(name = "seq_generator", sequenceName = "SEQ_DATABASE", allocationSize = 1) var id: Long?,
    @Column(name = "cpf", nullable = false, unique = false) var cpf: String,
    @Column var dataOfBird: String,
    @Column
    var email: String,
    @Column var name: String,
    @Column
    var phone: String,
    @Column var transactionAmount: BigDecimal,
    @Column
    var points: BigDecimal,
    @Column
    var processDate: String,
    @Column
    var status: String
)