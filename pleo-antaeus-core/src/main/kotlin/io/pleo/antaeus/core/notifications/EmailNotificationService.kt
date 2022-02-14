package io.pleo.antaeus.core.notifications

import io.pleo.antaeus.core.services.NotificationService
import io.pleo.antaeus.models.Invoice
import mu.KotlinLogging

/**
 * Concrete implementation of NotificationService designed to send notification via email
 */
class EmailNotificationService : NotificationService {
    private val logger = KotlinLogging.logger {}

    override fun chargedSuccessfully(invoice: Invoice) {
        throw UnsupportedOperationException()
    }

    override fun insufficientFunds(invoice: Invoice) {
        throw UnsupportedOperationException()
    }

    override fun customerNotFound(invoice: Invoice) {
        throw UnsupportedOperationException()
    }

    override fun currencyMismatch(invoice: Invoice) {
        throw UnsupportedOperationException()
    }
}