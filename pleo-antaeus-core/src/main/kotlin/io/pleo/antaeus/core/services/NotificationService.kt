package io.pleo.antaeus.core.services

import io.pleo.antaeus.models.Invoice

/**
 * Send a notification to a customer with a detailed explanation about a payment problem
 * TODO: think about hexagonal architecture
 */
interface NotificationService {
    /**
     * Send notification when customer account was charged successfully
     */
    fun chargedSuccessfully(invoice: Invoice)

    /**
     * Send notification when customer account was nor charged successfully because balance did not allow the charge.
     */
    fun insufficientFunds(invoice: Invoice)

    /**
     * Send notification when customer was not found on a bank side
     */
    fun customerNotFound(invoice: Invoice)

    /**
     * Send notification when the currency does not match the customer account currency
     */
    fun currencyMismatch(invoice: Invoice)
}


