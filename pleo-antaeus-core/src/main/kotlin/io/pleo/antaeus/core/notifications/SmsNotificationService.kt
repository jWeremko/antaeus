package io.pleo.antaeus.core.notifications

import io.pleo.antaeus.core.services.NotificationService
import io.pleo.antaeus.models.Invoice

/**
 * Concrete implementation of NotificationService designed to send notification via sms
 */
class SmsNotificationService : NotificationService {

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