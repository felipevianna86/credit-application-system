package com.example.credit.application.system.service

import com.example.credit.application.system.model.Customer

interface ICustomerService {

    fun save(customer: Customer): Customer

    fun findById(customerId: Long): Customer

    fun deleteById(customerId: Long)
}