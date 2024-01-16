package com.example.credit.application.system.controller

import com.example.credit.application.system.dto.CustomerDTO
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
    fun `should created a customer and return 201`(){
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
    fun `should not create a customer with same CPF and return 409`(){

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
    fun `should not create a customer with empty name and return 400`(){

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
}