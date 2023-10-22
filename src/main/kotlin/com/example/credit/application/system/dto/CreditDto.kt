package com.example.credit.application.system.dto

import com.example.credit.application.system.entity.Credit
import com.example.credit.application.system.entity.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto (
    @field:NotNull(message = "Invalid input. Credit Value must not be empty") val creditValue: BigDecimal,
    @field:Future(message = "The day of installment must not be in the past.") val dayFirstOfInstallment: LocalDate,
    @field:Max(value = 48, message = "The number of installments must be below 48") val numberOfInstallments: Int,
    @field:NotNull(message = "Invalid input. Customer Id must not be empty") val customerId: Long
){

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
