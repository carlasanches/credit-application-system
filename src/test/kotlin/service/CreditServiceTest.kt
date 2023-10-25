package service

import com.example.credit.application.system.entity.Credit
import com.example.credit.application.system.entity.Customer
import com.example.credit.application.system.exception.BusinessException
import com.example.credit.application.system.repository.CreditRepository
import com.example.credit.application.system.service.impl.CreditService
import com.example.credit.application.system.service.impl.CustomerService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK
    lateinit var creditRepository: CreditRepository

    @MockK
    lateinit var customerService: CustomerService

    @InjectMockKs
    lateinit var creditService: CreditService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should create credit`() {
        //given (data for test)
        val fakeCredit: Credit = buildCredit()
        val fakeCustomerId = 1L

        every { customerService.findById(fakeCustomerId) } returns fakeCredit.customer!!
        every { creditRepository.save(fakeCredit) } returns fakeCredit // Simulates adding a credit to the database

        //when (function tested)
        val actual: Credit = creditService.save(fakeCredit)

        //then (assertions: the result is the expected?)
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
        verify(exactly = 1) { customerService.findById(fakeCustomerId) }
    }

    @Test
    fun `should find credit list by customer id`() {

        //given
        val fakeCustomerId: Long = Random().nextLong()
        val fakeCreditList: List<Credit> = listOf(buildCredit(), buildCredit(), buildCredit())
        every { creditRepository.findAllByCustomerId(fakeCustomerId) } returns fakeCreditList

        //when
        val actual: List<Credit> = creditRepository.findAllByCustomerId(fakeCustomerId)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).isSameAs(fakeCreditList)

        verify(exactly = 1) { creditRepository.findAllByCustomerId(fakeCustomerId) }
    }

    @Test
    fun `should find a credit by the credit code`() {

        //given
        val fakeCustomerId = 1L
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit()

        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit

        //when
        val actual: Credit = creditService.findByCreditCode(fakeCustomerId, fakeCreditCode)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Credit::class.java)
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `should not find credit by invalid code and throw BusinessException`() {

        //given
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCustomerId = 1L

        every { creditRepository.findByCreditCode(fakeCreditCode) } returns null

        //when

        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeCustomerId, fakeCreditCode) }
            .withMessage("Credit code $fakeCreditCode not found")

        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `should not find customer id and throw IllegalArgumentException`() {

        //given
        val fakeCustomerId: Long = Random().nextLong()
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit()

        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit

        //when

        //then
        Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeCustomerId, fakeCreditCode) }
            .withMessage("Contact admin")

        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(600.0),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusMonths(2L),
        numberOfInstallments: Int = 4,
        customer: Customer = CustomerServiceTest.buildCustomer()
    ): Credit = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer
    )
}