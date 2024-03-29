package com.example.credit.application.system.controller

import com.example.credit.application.system.dto.CustomerDTO
import com.example.credit.application.system.dto.CustomerUpdateDTO
import com.example.credit.application.system.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerResourceTest {

    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private  lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    companion object{
        const val URL = "/api/customer"
    }

    @BeforeEach fun setup() = customerRepository.deleteAll()

    @AfterEach fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should created a customer and return status 201`(){
        val customerDTO = buildCustomerDTO()

        val valueAsString = objectMapper.writeValueAsString(customerDTO)

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("18765170857"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("felipe@gmail.com"))
                .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `should not create a customer with same CPF and return status 409`(){

        customerRepository.save(buildCustomerDTO().toEntity())
        val customerDTO = buildCustomerDTO()

        val valueAsString = objectMapper.writeValueAsString(customerDTO)

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(MockMvcResultMatchers.status().isConflict)
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.dao.DataIntegrityViolationException"))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not create a customer with empty name and return status 400`(){

        val customerDTO = buildCustomerDTO(firstName = StringUtils.EMPTY)
        val valueAsString = objectMapper.writeValueAsString(customerDTO)

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class org.springframework.web.bind.MethodArgumentNotValidException"))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should found a customer by id and return status 200`(){

        val customer = customerRepository.save(buildCustomerDTO().toEntity())

        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("18765170857"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("felipe@gmail.com"))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not found a customer by id and return status 400`(){

       val invalidId = 5

        mockMvc.perform(MockMvcRequestBuilders.get("$URL/$invalidId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class com.example.credit.application.system.exception.BusinessException"))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should delete a customer by id and return status 204`(){

        val customer = customerRepository.save(buildCustomerDTO().toEntity())

        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not delete a customer by id and return status 400`(){

        val invalidId = 5

        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/$invalidId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class com.example.credit.application.system.exception.BusinessException"))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should update a costumer and return status 200`(){
        val customer = customerRepository.save(buildCustomerDTO().toEntity())
        val customerUpdateDTO = buildCustomerUpdateDTO()

        val valueAsString = objectMapper.writeValueAsString(customerUpdateDTO)

        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Felipe Up"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("18765170857"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("felipe@gmail.com"))
                .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not update a costumer with invalid id an return 400 status`(){
        val invalidId = 5

        customerRepository.save(buildCustomerDTO().toEntity())
        val customerUpdateDTO = buildCustomerUpdateDTO()

        val valueAsString = objectMapper.writeValueAsString(customerUpdateDTO)

        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=${invalidId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("class com.example.credit.application.system.exception.BusinessException"))
                .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCustomerDTO(
            firstName: String = "Felipe",
            lastName: String = "Souza",
            cpf: String = "18765170857",
            income: BigDecimal = BigDecimal.valueOf(8800.0),
            email: String = "felipe@gmail.com",
            password: String = "123",
            zipCode: String = "58454555",
            street: String = "Rua dos estudantes"
    )
    = CustomerDTO(
        firstName = firstName,
            lastName = lastName,
            cpf = cpf,
            income = income,
            email = email,
            password = password,
            zipCode = zipCode,
            street = street
    )

    private fun buildCustomerUpdateDTO(
            firstName: String = "Felipe Up",
            lastName: String = "Souza",
            income: BigDecimal = BigDecimal.valueOf(500.0),
            zipCode: String = "59148590",
            street: String = "Rua dos ocupados"
    )
            = CustomerUpdateDTO(
            firstName = firstName,
            lastName = lastName,
            income = income,
            zipCode = zipCode,
            street = street
    )
}