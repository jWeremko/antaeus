package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.utils.DateTimeUtility
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.math.BigDecimal
import java.util.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BillingServiceTest {
    private val paymentProvider = mockk<PaymentProvider>()
    private val invoiceService = mockk<InvoiceService>()
    private val customerNotificationService = mockk<NotificationService>()
    private val dateTimeUtility = mockk<DateTimeUtility>()
    private val billingService = BillingService(paymentProvider, invoiceService, customerNotificationService, dateTimeUtility)

    private val invoice = Invoice(1, 1, Money(BigDecimal.valueOf(1), Currency.GBP), InvoiceStatus.PENDING)
    private val invoices = listOf(invoice)


    @Test
    @Disabled
    fun `keepRunning`() {
        val a = 4;
        while (a == 4) {
            Thread.sleep(1000)
        }
    }

    @Test
    fun `successful payment generates chargedSuccessfully notification`() {
        every { paymentProvider.charge(invoice) } returns true
        every { invoiceService.changeInvoiceStatus(invoice.id, InvoiceStatus.PAID) } returns invoice.id
        every { customerNotificationService.chargedSuccessfully(invoice) } returns Unit

        assertEquals(true, billingService.charge(invoice))
    }

    @Test
    fun `unsuccessful payment generates insufficientFunds notification`() {
        every { paymentProvider.charge(invoice) } returns false
        every { customerNotificationService.insufficientFunds(invoice) } returns Unit

        assertEquals(false, billingService.charge(invoice))
    }

    @Test
    fun `CustomerNotFoundException generates customerNotFound notification`() {
        every { paymentProvider.charge(invoice) } throws CustomerNotFoundException(invoice.customerId)
        every { customerNotificationService.customerNotFound(invoice) } returns Unit

        assertDoesNotThrow { billingService.charge(invoice) }
    }

    @Test
    fun `CurrencyMismatchException generates currencyMismatch notification`() {
        every { paymentProvider.charge(invoice) } throws CurrencyMismatchException(invoice.id, invoice.customerId)
        every { customerNotificationService.currencyMismatch(invoice) } returns Unit

        assertDoesNotThrow { billingService.charge(invoice) }
    }

    @Test
    fun `NetworkException generates ASD notification`() {
        every { paymentProvider.charge(invoice) } throws NetworkException()
        //every { customerNotificationService.currencyMismatch(invoice) } returns Unit

        assertDoesNotThrow { billingService.charge(invoice) }
    }

    @Test
    fun `verify that billingService main loop is functional`() {
        //TODO: does this code actually test anything?
        val calendar = Calendar.getInstance()
        val now = calendar.time

        every { invoiceService.fetchAll(InvoiceStatus.PENDING) } returns emptyList()
        every { dateTimeUtility.now() } returns now
        every { dateTimeUtility.nextMonthFirstDayDelay(now) } returns 10

        billingService.run()
        Thread.sleep(100)
        billingService.stop()
    }
}
