package io.pleo.antaeus.core.notifications

import io.pleo.antaeus.core.services.NotificationService
import io.pleo.antaeus.models.Invoice
import mu.KotlinLogging

class DummyNotificationService : NotificationService {
    private val logger = KotlinLogging.logger {}

    override fun chargedSuccessfully(invoice: Invoice) {
        logger.info { "chargedSuccessfully: $invoice" }
    }

    override fun insufficientFunds(invoice: Invoice) {
        logger.info { "insufficientFunds: $invoice" }
    }

    override fun customerNotFound(invoice: Invoice) {
        logger.info { "customerNotFound: $invoice" }
    }

    override fun currencyMismatch(invoice: Invoice) {
        logger.info { "currencyMismatch: $invoice" }
    }
}