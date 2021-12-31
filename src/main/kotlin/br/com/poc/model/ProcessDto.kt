package br.com.poc.model

import java.math.BigDecimal

data class ProcessDto (
    val id: Long? = null,
    val cpf: String = "",
    val dataOfBird: String = "",
    val email: String = "",
    val phone: String = "",
    val transactionAmount: String = "",
    val points: BigDecimal = BigDecimal.ONE,
    val processDate: String = "",
    val status: String = "",
)