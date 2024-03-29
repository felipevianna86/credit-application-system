package com.example.credit.application.system.dto

import com.example.credit.application.system.model.Credit
import java.math.BigDecimal
import java.util.UUID

data class CreditViewListDTO(
        val creditCode: UUID,
        val creditValue: BigDecimal,
        val numberOfInstallments: Int
){

    constructor(credit: Credit) : this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments
    )
}
