package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.utils.DateTimeUtility
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import kotlinx.coroutines.*
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

class BillingService(
        private val paymentProvider: PaymentProvider,
        private val invoiceService: InvoiceService,
        private val notificationService: NotificationService,
        private val dateTimeUtility: DateTimeUtility
) : CoroutineScope {
    private val logger = KotlinLogging.logger {}

    //NOTE: main reason to have defaultPageSize is memory footprint optimization
    //TODO: make it configurable
    private val defaultPageSize = 20
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Default + job


    fun stop() {
        this.job.cancel()
    }

    fun run() = launch {
        while (true) {
            delay(dateTimeUtility.nextMonthFirstDayDelay(dateTimeUtility.now()))

            val startTime = System.currentTimeMillis()
            invoiceService.executeForEach(InvoiceStatus.PENDING, defaultPageSize, ::charge)
            logger.info { "Invoices processing time : ${System.currentTimeMillis() - startTime}" }
        }
    }

    fun charge(invoice: Invoice): Boolean {
        try {
            if (paymentProvider.charge(invoice)) {
                invoiceService.changeInvoiceStatus(invoice.id, InvoiceStatus.PAID)
                notificationService.chargedSuccessfully(invoice)
                return true
            } else {
                notificationService.insufficientFunds(invoice)
            }
        } catch (customerNotFoundException: CustomerNotFoundException) {
            notificationService.customerNotFound(invoice)
            //TODO: reschedule payment, let's say in a week
        } catch (currencyMismatchException: CurrencyMismatchException) {
            notificationService.currencyMismatch(invoice)
        } catch (networkException: NetworkException) {
            //TODO: reschedule payment, later today
        }
        return false
    }
}
