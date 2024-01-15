package com.example.credit.application.system.service.impl

import com.example.credit.application.system.model.Customer
import com.example.credit.application.system.repository.CustomerRepository
import com.example.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class CustomerService(private val customerRepository: CustomerRepository): ICustomerService {

    override fun save(customer: Customer): Customer =
            this.customerRepository.save(customer)

    override fun findById(customerId: Long): Customer =
            this.customerRepository.findById(customerId)
                    .orElseThrow{
                        throw RuntimeException("Id $customerId not found.")
                    }

    override fun deleteById(customerId: Long) {
        this.customerRepository.deleteById(customerId)
    }
}