/*
    Implements endpoints related to invoices.
 */

package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus

class InvoiceService(private val dal: AntaeusDal) {

    fun fetchAll(): List<Invoice> {
        return dal.fetchInvoices()
    }

    fun fetchAll(status: InvoiceStatus): List<Invoice> {
        return dal.fetchInvoices(status)
    }

    fun fetchAll(status: InvoiceStatus, pageRequest: AntaeusDal.PageRequest): List<Invoice> {
        return dal.fetchInvoices(status, pageRequest)
    }

    fun fetch(id: Int): Invoice {
        return dal.fetchInvoice(id) ?: throw InvoiceNotFoundException(id)
    }

    fun changeInvoiceStatus(id: Int, status: InvoiceStatus): Int {
        return dal.updateInvoiceStatus(id, status)
    }

    fun executeForEach(status: InvoiceStatus, pageSize : Int, invoiceCallback : (invoice: Invoice) -> Boolean) {
        val pageRequest = AntaeusDal.PageRequest(pageSize)
        while(true) {
            val invoices = dal.fetchInvoices(status, pageRequest)
            if (invoices.isEmpty()) {
                break
            }

            invoices.forEach { invoice ->
                invoiceCallback(invoice)
            }
            pageRequest.nextPage()
        }
    }
}
