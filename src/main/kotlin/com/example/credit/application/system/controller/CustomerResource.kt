package com.example.credit.application.system.controller

import com.example.credit.application.system.dto.CustomerDTO
import com.example.credit.application.system.dto.CustomerUpdateDTO
import com.example.credit.application.system.dto.CustomerViewDTO
import com.example.credit.application.system.service.impl.CustomerService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customer")
class CustomerResource(
        private val customerService: CustomerService
) {

    @PostMapping
    fun save(@RequestBody customerDTO: CustomerDTO) : String{
        val customerSaved = this.customerService.save(customerDTO.toEntity())
        return "Customer ${customerSaved.email} was saved"
    }

    @GetMapping("/{customerId}")
    fun findById(@PathVariable customerId: Long): CustomerViewDTO {

        val customerDB = this.customerService.findById(customerId);
        return CustomerViewDTO(customerDB)
    }

    @DeleteMapping("/{customerId}")
    fun deleteById(@PathVariable customerId: Long) = this.customerService.deleteById(customerId)

    @PatchMapping
    fun update(@RequestParam(value = "customerId") customerId: Long,
               customerUpdateDTO: CustomerUpdateDTO): CustomerViewDTO {
        val customerDB = this.customerService.findById(customerId)
        val customerUpdated = this.customerService.save(customerUpdateDTO.toEntity(customerDB))

        return CustomerViewDTO(customerUpdated)
    }
}