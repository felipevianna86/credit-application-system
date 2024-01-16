package com.example.credit.application.system.dto

import com.example.credit.application.system.model.Credit
import com.example.credit.application.system.model.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(
        @field:NotNull(message = "Credit Value doesn't be empty.") val creditValue: BigDecimal,
        @field:Future val dayFirstOfInstallment: LocalDate,
        @field:Min(value = 1, message = "Number must be Greater or Equals to 1")
        @field:Max(value = 60, message = "Number must be max 60.")
        val numberOfInstallments: Int,
        @field:NotNull(message = "Customer ID doesn't be empty.") val customerId: Long
){

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
            dayFirstInstallment = this.dayFirstOfInstallment,
            numberOfInstallments = this.numberOfInstallments,
            customer = Customer(
                    id = this.customerId
            )
    )
}
