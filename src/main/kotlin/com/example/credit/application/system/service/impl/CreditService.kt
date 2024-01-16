package com.example.credit.application.system.service.impl

import com.example.credit.application.system.model.Credit
import com.example.credit.application.system.repository.CreditRepository
import com.example.credit.application.system.service.ICreditService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditService(
        private val creditRepository: CreditRepository,
        private val customerService: CustomerService
): ICreditService {

    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = credit.customer?.id?.let { customerService.findById(it) }
        }

        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> =
        this.creditRepository.findAllByCustomer(customerId)

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val creditDB: Credit = this.creditRepository.findByCreditCode(creditCode)

        return if(creditDB.customer?.id == customerId) creditDB else throw RuntimeException("Contact admin.")
    }
}