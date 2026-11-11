package com.kgomez.myfirstapp.features.converter

enum class CurrencyOption(val code: String, val perUsd: Double) {
    USD("USD", 1.0),
    COP("COP", 4000.0),
    EUR("EUR", 0.92)
}

fun convertCurrency(amount: Double, from: CurrencyOption, to: CurrencyOption): Double =
    amount / from.perUsd * to.perUsd

