package com.example.credit.application.system.controller

import com.example.credit.application.system.dto.CustomerDTO
import com.example.credit.application.system.dto.CustomerUpdateDTO
import com.example.credit.application.system.dto.CustomerViewDTO
import com.example.credit.application.system.service.impl.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customer")
class CustomerResource(
        private val customerService: CustomerService
) {

    @PostMapping
    fun save(@RequestBody @Valid customerDTO: CustomerDTO) : ResponseEntity<CustomerViewDTO> {
        val customerSaved = this.customerService.save(customerDTO.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerViewDTO(customerSaved))
    }

    @GetMapping("/{customerId}")
    fun findById(@PathVariable customerId: Long): ResponseEntity<CustomerViewDTO> {

        val customerDB = this.customerService.findById(customerId);
        return ResponseEntity.ok(CustomerViewDTO(customerDB))
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable customerId: Long) = this.customerService.deleteById(customerId)

    @PatchMapping
    fun update(@RequestParam(value = "customerId") customerId: Long,
               @RequestBody @Valid customerUpdateDTO: CustomerUpdateDTO): ResponseEntity<CustomerViewDTO> {
        val customerDB = this.customerService.findById(customerId)
        val customer = customerUpdateDTO.toEntity(customerDB)
        val customerUpdated = this.customerService.save(customer)

        return ResponseEntity.ok(CustomerViewDTO(customerUpdated))
    }
}