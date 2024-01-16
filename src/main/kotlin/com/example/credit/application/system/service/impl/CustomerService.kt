package com.example.credit.application.system.service.impl

import com.example.credit.application.system.exception.BusinessException
import com.example.credit.application.system.model.Customer
import com.example.credit.application.system.repository.CustomerRepository
import com.example.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(private val customerRepository: CustomerRepository): ICustomerService {

    override fun save(customer: Customer): Customer =
            this.customerRepository.save(customer)

    override fun findById(customerId: Long): Customer =
            this.customerRepository.findById(customerId)
                    .orElseThrow{
                        throw BusinessException("Id $customerId not found.")
                    }

    override fun deleteById(customerId: Long) {
        val customerDB = this.findById(customerId)
        this.customerRepository.delete(customerDB)
    }
}