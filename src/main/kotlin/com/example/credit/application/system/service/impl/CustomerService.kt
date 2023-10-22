package com.example.credit.application.system.service.impl

import com.example.credit.application.system.entity.Customer
import com.example.credit.application.system.exception.BusinessException
import com.example.credit.application.system.repository.CustomerRepository
import com.example.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class CustomerService(
        private val customerRepository: CustomerRepository // Repository injection for db access
) : ICustomerService {
    override fun save(customer: Customer): Customer =
            this.customerRepository.save(customer)

    override fun findById(id: Long): Customer =
            this.customerRepository.findById(id).orElseThrow {
                throw BusinessException("Id $id not found")
            }

    override fun delete(id: Long) {
        val customer: Customer = this.findById(id)
        this.customerRepository.delete(customer)
    }

}