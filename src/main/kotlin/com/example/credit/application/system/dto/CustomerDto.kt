package com.example.credit.application.system.dto

import com.example.credit.application.system.entity.Address
import com.example.credit.application.system.entity.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "Invalid input. First name must not be empty") val firstName: String,
    @field:NotEmpty(message = "Invalid input. Last name must not be empty") val lastName: String,
    @field:NotEmpty(message = "Invalid input. CPF must not be null")
    @field:CPF(message = "Invalid CPF") val cpf: String,
    @field:NotNull(message = "Invalid input. Income must not be empty") val income: BigDecimal,
    @field:Email(message = "Invalid email")
    @field:NotEmpty(message = "Invalid input. Email must not be empty") val email: String,
    @field:NotEmpty(message = "Invalid input. Password must not be empty") val password: String,
    @field:NotEmpty(message = "Invalid input. Zip Code must not be empty") val zipCode: String,
    @field:NotEmpty(message = "Invalid input. Street must not be empty") val street: String
) {

    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )
}
