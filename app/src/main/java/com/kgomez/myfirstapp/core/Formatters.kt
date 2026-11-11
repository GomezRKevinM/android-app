package com.kgomez.myfirstapp.core
import java.text.NumberFormat
import java.util.Currency

/** Dinero en la moneda del dispositivo (para la calculadora de creditos). */
fun formatMoney(value: Double): String =
    NumberFormat.getCurrencyInstance().format(value)

/** Dinero en una moneda especifica por su codigo ISO (para el convertidor). */
fun formatMoney(value: Double, currencyCode: String): String {
    val format = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance(currencyCode)
    return format.format(value)
}