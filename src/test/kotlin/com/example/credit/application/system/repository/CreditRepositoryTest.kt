package com.example.credit.application.system.repository

import com.example.credit.application.system.model.Address
import com.example.credit.application.system.model.Credit
import com.example.credit.application.system.model.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {

    @Autowired lateinit var creditRepository: CreditRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    @BeforeEach fun setup(){
        customer = testEntityManager.persist(buildCustomer())
        credit1 = testEntityManager.persist(buildCredit(customer = customer))
        credit2 = testEntityManager.persist(buildCredit(customer = customer))
    }

    @Test
    fun `should found credit by creditCode`(){
        val creditCode1 = UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")
        val creditCode2 = UUID.fromString("018b2f19-e79e-7d6a-a56d-29feb6211b04")

        credit1.creditCode = creditCode1
        credit2.creditCode = creditCode2

        val fakeCredit1 = creditRepository.findByCreditCode(creditCode1)
        val fakeCredit2 = creditRepository.findByCreditCode(creditCode2)

        Assertions.assertThat(fakeCredit1).isNotNull
        Assertions.assertThat(fakeCredit2).isNotNull
        Assertions.assertThat(fakeCredit1).isSameAs(credit1)
        Assertions.assertThat(fakeCredit2).isSameAs(credit2)

    }

    @Test
    fun `should found credits by customer id`(){
        val customerId = 1L

        val creditsList = creditRepository.findAllByCustomer(customerId)

        Assertions.assertThat(creditsList).isNotEmpty
        Assertions.assertThat(creditsList).size().isEqualTo(2)
        Assertions.assertThat(creditsList).contains(credit1, credit2)
    }

    private fun buildCredit(
            creditValue: BigDecimal = BigDecimal.valueOf(5000.0),
            dayFirstInstallment: LocalDate = LocalDate.now().plusDays(40L),
            numberOfInstallments: Int = 48,
            customer: Customer,
    ) = Credit(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            customer = customer,
    )


    private fun buildCustomer(
            firstName: String = "Felipe",
            lastName: String = "Souza",
            cpf: String = "93779452081",
            email: String = "felipe@gmail.com",
            password: String = "123",
            zipCode: String = "59100111",
            street: String = "Rua dos Estudos",
            income: BigDecimal = BigDecimal.valueOf(1000.0)
    ) =
            Customer(
                    firstName = firstName,
                    lastName = lastName,
                    cpf = cpf,
                    email = email,
                    income = income,
                    password = password,
                    address = Address(
                            zipCode = zipCode,
                            street = street
                    )
            )
}