package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class InvoiceServiceTest {
    private val dal = mockk<AntaeusDal> {
        every { fetchInvoice(404) } returns null
    }

    private val invoiceService = InvoiceService(dal = dal)
    private val invoice = Invoice(1, 1, Money(BigDecimal.valueOf(1), Currency.GBP), InvoiceStatus.PENDING)
    private val invoices = listOf(invoice)

    @Test
    fun `will throw if invoice is not found`() {
        assertThrows<InvoiceNotFoundException> {
            invoiceService.fetch(404)
        }
    }

    @Test
    fun `executeForEach should callback function expected number of times`() {
        class Counter() {
            var callCount = 0

            fun call(invoice : Invoice) : Boolean {
                this.callCount = this.callCount + 1
                return true
            }
        }
        val counter = Counter()

        every { dal.fetchInvoices(InvoiceStatus.PENDING, any()) } returnsMany listOf(invoices, invoices, emptyList())

        invoiceService.executeForEach(InvoiceStatus.PENDING, 2, counter::call)
        assertEquals(2, counter.callCount)
    }
}
