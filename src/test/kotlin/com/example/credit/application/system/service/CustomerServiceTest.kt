package com.example.credit.application.system.service

import com.example.credit.application.system.exception.BusinessException
import com.example.credit.application.system.model.Address
import com.example.credit.application.system.model.Customer
import com.example.credit.application.system.repository.CustomerRepository
import com.example.credit.application.system.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK lateinit var customerRepository: CustomerRepository
    @InjectMockKs lateinit var customerService: CustomerService

    @Test
    fun `should create customer`(){
        val fakeCostumer = buildCustomer()
        every { customerRepository.save(any()) } returns fakeCostumer

        val actualCostumer = customerService.save(fakeCostumer)

        Assertions.assertThat(actualCostumer).isNotNull
        Assertions.assertThat(actualCostumer).isSameAs(fakeCostumer)
        verify(exactly = 1) { customerRepository.save(fakeCostumer) }
    }

    @Test
    fun `should find customer by id`(){
        val fakeID = Random().nextLong()
        val fakeCustomer = buildCustomer(id = fakeID)
        every { customerRepository.findById(fakeID) } returns Optional.of(fakeCustomer)

        val actualCustomer = customerService.findById(fakeID)

        Assertions.assertThat(actualCustomer).isNotNull
        Assertions.assertThat(actualCustomer).isSameAs(fakeCustomer)
        Assertions.assertThat(actualCustomer).isExactlyInstanceOf(Customer::class.java)
        verify(exactly = 1) { customerRepository.findById(fakeID) }
    }

    @Test
    fun `should not found customer by id throwing BusinessException`() {
        val fakeID = Random().nextLong()
        every { customerRepository.findById(fakeID) } returns Optional.empty()

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
                .isThrownBy { customerService.findById(fakeID) }
                .withMessage("Id $fakeID not found.")

        verify(exactly = 1) { customerRepository.findById(fakeID) }

    }

    @Test
    fun `should delete customer by id`(){
        val fakeID = Random().nextLong()
        val fakeCustomer = buildCustomer(id = fakeID)
        every { customerRepository.findById(fakeID) } returns Optional.of(fakeCustomer)
        every { customerRepository.delete(fakeCustomer) } just runs

        customerService.deleteById(fakeID)

        verify(exactly = 1) { customerRepository.findById(fakeID) }
        verify(exactly = 1) { customerRepository.delete(fakeCustomer) }

    }


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