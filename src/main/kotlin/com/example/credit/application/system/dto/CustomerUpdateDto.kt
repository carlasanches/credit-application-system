package com.example.credit.application.system.dto

import com.example.credit.application.system.entity.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "Invalid input. First name must not be empty") val firstName: String,
    @field:NotEmpty(message = "Invalid input. last name must not be empty") val lastName: String,
    @field:NotNull(message = "Invalid input. Income must not be empty") val income: BigDecimal,
    @field:NotEmpty(message = "Invalid input. Zip Code must not be empty") val zipCode: String,
    @field:NotEmpty(message = "Invalid input. Street must not be empty") val street: String
) {

    fun toEntity(customer: Customer) : Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.street = this.street
        customer.address.zipCode = this.zipCode

        return customer
    }
}
