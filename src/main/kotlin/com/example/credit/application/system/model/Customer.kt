package com.example.credit.application.system.model

data class Customer(
        var firstName: String = "",
        var lastName: String = "",
        var cpf: String = "",
        var email: String = "",
        var password: String = "",
        var address: Address = Address(),
        var credits: List<Credit> = mutableListOf(),
        val id: Long? = null
)