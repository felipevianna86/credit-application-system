package com.example.credit.application.system.dto

import com.example.credit.application.system.model.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDTO(
        @field:NotEmpty(message = "First Name doesn't be empty.") val firstName: String,
        @field:NotEmpty(message = "Last Name doesn't be empty.") val lastName: String,
        @field:NotNull(message = "Income doesn't be empty.") val income: BigDecimal,
        @field:NotEmpty(message = "Zip Code doesn't be empty.") val zipCode: String,
        @field:NotEmpty(message = "Street doesn't be empty.") val street: String
){
    fun toEntity(customer: Customer): Customer{
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.street = this.street
        customer.address.zipCode = this.zipCode

        return customer
    }
}
