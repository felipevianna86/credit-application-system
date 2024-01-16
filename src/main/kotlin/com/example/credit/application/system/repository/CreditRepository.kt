package com.example.credit.application.system.repository

import com.example.credit.application.system.model.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CreditRepository: JpaRepository<Credit, Long>{

    fun findByCreditCode(creditCode: UUID): Credit

    @Query(value = "SELECT C FROM CREDIT C WHERE C.CUSTOMER_ID = ?1 ", nativeQuery = true)
    fun findAllByCustomer(customerId: Long): List<Credit>
}