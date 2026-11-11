package com.kgomez.myfirstapp.features.loan

import kotlin.math.pow

data class LoanResult(
    val installment: Double, // valor por cuota
    val total: Double, // cuota * numero de cuotas
    val profit: Double // total - valor del credito
)

enum class LoanType { SIMPLE, FRENCH }

/** Interes simple. monthlyRate en forma decimal: 2 % = 0.02 */
fun simpleLoan(principal: Double, installments: Int, monthlyRate: Double): LoanResult {
    val totalWithInterest = principal * (1 + monthlyRate * installments)
    val installment = totalWithInterest / installments
    val total = installment * installments
    return LoanResult(installment, total, total - principal)
}

/** Cuota fija (sistema frances). */
fun frenchLoan(principal: Double, installments: Int, monthlyRate: Double): LoanResult {
    val installment = if (monthlyRate == 0.0) {
        principal / installments
    } else {
        principal * monthlyRate / (1 - (1 + monthlyRate).pow(-installments))
    }
    val total = installment * installments
    return LoanResult(installment, total, total - principal)
}