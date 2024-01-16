package com.example.credit.application.system.service

import com.example.credit.application.system.Status
import com.example.credit.application.system.exception.BusinessException
import com.example.credit.application.system.model.Address
import com.example.credit.application.system.model.Credit
import com.example.credit.application.system.model.Customer
import com.example.credit.application.system.repository.CreditRepository
import com.example.credit.application.system.service.impl.CreditService
import com.example.credit.application.system.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK lateinit var creditRepository: CreditRepository
    @MockK lateinit var customerService: CustomerService
    @InjectMockKs lateinit var creditService: CreditService

    @Test
    fun `should save credit`(){
        val fakeCredit = buildCredit()
        val fakeCustomer = buildCustomer()
        every { creditRepository.save(fakeCredit) } returns fakeCredit
        every { fakeCredit.customer?.let { customerService.save(it) } } returns fakeCustomer
        every { fakeCustomer.id?.let { customerService.findById(it) } } returns fakeCustomer

        val actualCredit = creditService.save(fakeCredit)

        Assertions.assertThat(actualCredit).isNotNull
        Assertions.assertThat(actualCredit).isSameAs(fakeCredit)
        Assertions.assertThat(actualCredit).isExactlyInstanceOf(Credit::class.java)

        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should found all credit by customer id`(){
        val fakeCreditList = mutableListOf(buildCredit())
        val fakeCustomer = buildCustomer()

        every { fakeCustomer.id?.let { creditRepository.findAllByCustomer(it) } } returns fakeCreditList

        val actualCreditList = fakeCustomer.id?.let { creditService.findAllByCustomer(it) }

        Assertions.assertThat(actualCreditList).isNotEmpty
        Assertions.assertThat(actualCreditList).size().isEqualTo(1)

        verify(exactly = 1) { fakeCustomer.id?.let { creditRepository.findAllByCustomer(it) } }
    }

    @Test
    fun `should found credit by creditCode`(){
        val creditCodeFake = UUID.randomUUID()
        val fakeCredit = buildCredit(creditCode = creditCodeFake)

        every { creditRepository.findByCreditCode(creditCodeFake) } returns fakeCredit

        val actualCredit = fakeCredit.customer?.id?.let { creditService.findByCreditCode(it, creditCodeFake) }

        Assertions.assertThat(actualCredit).isNotNull
        Assertions.assertThat(actualCredit).isSameAs(fakeCredit)
        Assertions.assertThat(actualCredit).isExactlyInstanceOf(Credit::class.java)

        verify(exactly = 1) { creditRepository.findByCreditCode(creditCodeFake) }
    }

    @Test
    fun `should not found credit by creditCode and throwing a RuntimeException`(){
        val creditCodeFake = UUID.randomUUID()
        val fakeCredit = buildCredit(creditCode = creditCodeFake)

        every { creditRepository.findByCreditCode(creditCodeFake) } returns fakeCredit

        Assertions.assertThatExceptionOfType(RuntimeException::class.java)
                .isThrownBy { creditService.findByCreditCode(3 , creditCodeFake)}
                .withMessage("Contact admin.")

        verify(exactly = 1) { creditRepository.findByCreditCode(creditCodeFake) }
    }

    private fun buildCredit(
            creditCode: UUID = UUID.randomUUID(),
            creditValue: BigDecimal = BigDecimal.valueOf(5000.0),
            dayFirstInstallment: LocalDate = LocalDate.now().plusDays(40L),
            numberOfInstallments: Int = 48,
            status: Status = Status.APPROVED,
            customer: Customer = buildCustomer(),
            id: Long = 1L

    ) = Credit(
         creditCode = creditCode,
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            status = status,
            customer = customer,
            id = id
    )


    private fun buildCustomer(
            firstName: String = "Felipe",
            lastName: String = "Souza",
            cpf: String = "93779452081",
            email: String = "felipe@gmail.com",
            password: String = "123",
            zipCode: String = "59100111",
            street: String = "Rua dos Estudos",
            income: BigDecimal = BigDecimal.valueOf(1000.0),
            id: Long = 1L
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
                    ),
                    id = id
            )
}