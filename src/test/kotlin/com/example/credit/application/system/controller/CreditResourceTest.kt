package com.example.credit.application.system.controller

import com.example.credit.application.system.dto.CreditDto
import com.example.credit.application.system.dto.CustomerDto
import com.example.credit.application.system.entity.Customer
import com.example.credit.application.system.repository.CreditRepository
import com.example.credit.application.system.repository.CustomerRepository
import com.example.credit.application.system.service.CustomerServiceTest
import com.example.credit.application.system.service.impl.CustomerService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {

    @Autowired private lateinit var creditRepository: CreditRepository
    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private lateinit var customerService: CustomerService
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper // transform a class into String

    companion object {
        const val URL: String = "/api/credits"
        const val URL_CUSTOMER: String = "/api/customers"
    }

    @BeforeEach fun setup() {
        creditRepository.deleteAll() // before each test, delete all items from db
        customerRepository.deleteAll() // before each test, delete all items from db
    }
    @AfterEach fun tearDown() {
        creditRepository.deleteAll() // after each test, delete all items from db
        customerRepository.deleteAll() // after each test, delete all items from db
    }

    @Test
    fun `should create a credit and return 201 status`() {
        //given
        val creditDto: CreditDto = buildCreditDto()
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)

        //when
        mockMvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isCreated)

        //then

    }

    private fun buildCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(600.0),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusMonths(2L),
        numberOfInstallments: Int = 4,
        customer: Customer = CustomerServiceTest.buildCustomer()
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstOfInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customer.id
    )
}