package com.example.credit.application.system.dto

import com.example.credit.application.system.model.Address
import com.example.credit.application.system.model.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDTO(
        @field:NotEmpty(message = "First Name doesn't be empty.") val firstName: String,
        @field:NotEmpty(message = "Last Name doesn't be empty.") val lastName: String,
        @field:CPF(message = "Invalid CPF.") val cpf: String,
        @field:NotNull(message = "Income doesn't be empty.") val income: BigDecimal,
        @field:Email(message = "Invalid E-mail.") val email: String,
        @field:NotEmpty(message = "Password doesn't be empty.") val password: String,
        @field:NotEmpty(message = "Zip Code doesn't be empty.") val zipCode: String,
        @field:NotEmpty(message = "Street doesn't be empty.") val street: String
) {

    fun toEntity(): Customer = Customer(
            firstName = this.firstName,
            lastName = this.lastName,
            cpf = this.cpf,
            email = this.email,
            income = this.income,
            password = this.password,
            address = Address(
                    zipCode = this.zipCode,
                    street = this.street
            )
    )
}