package com.example.credit.application.system.controller

import com.example.credit.application.system.dto.CreditDTO
import com.example.credit.application.system.dto.CreditViewDTO
import com.example.credit.application.system.dto.CreditViewListDTO
import com.example.credit.application.system.model.Credit
import com.example.credit.application.system.service.impl.CreditService
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credit")
class CreditResource(
        private val creditService: CreditService
) {

    @PostMapping
    fun save(@RequestBody creditDTO: CreditDTO): String{
        val creditSaved = this.creditService.save(creditDTO.toEntity())
        return "Credit ${creditSaved.creditCode} - Customer ${creditSaved.customer?.firstName} was saved successfully!"
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long) : List<CreditViewListDTO> {
        return this.creditService.findAllByCustomer(customerId).
                stream().map {
                    credit: Credit -> CreditViewListDTO(credit)
        }.collect(Collectors.toList())
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(
            @RequestParam(value = "customerId") customerId: Long,
            @PathVariable creditCode: UUID
    ) : CreditViewDTO {

        val creditDB = this.creditService.findByCreditCode(customerId, creditCode)
        return CreditViewDTO(creditDB)
    }
}