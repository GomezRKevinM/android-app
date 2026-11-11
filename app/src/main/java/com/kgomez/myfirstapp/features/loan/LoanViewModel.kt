package com.kgomez.myfirstapp.features.loan

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoanState(
    val principal: String = "",
    val installments: String = "",
    val monthlyRate: String = "",
    val type: LoanType = LoanType.SIMPLE,
    val result: LoanResult? = null,
    val principalError: Boolean = false,
    val installmentsError: Boolean = false,
    val rateError: Boolean = false
)

class LoanViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoanState())
    val state: StateFlow<LoanState> = _state.asStateFlow()
    fun onPrincipalChange(v: String) = _state.update { it.copy(principal = v) }
    fun onInstallmentsChange(v: String) = _state.update { it.copy(installments = v) }
    fun onRateChange(v: String) = _state.update { it.copy(monthlyRate = v) }
    fun onTypeChange(t: LoanType) = _state.update { it.copy(type = t) }
    fun onCalculateClick() = _state.update { s ->
        val principal = s.principal.replace(',', '.').toDoubleOrNull()?.takeIf { it > 0 }
        val installments = s.installments.toIntOrNull()?.takeIf { it > 0 }
        val ratePercent = s.monthlyRate.replace(',', '.').toDoubleOrNull()?.takeIf { it >= 0 }
        if (principal == null || installments == null || ratePercent == null) {
            s.copy(
                result = null,
                principalError = principal == null,
                installmentsError = installments == null,
                rateError = ratePercent == null
            )
        } else {
            val rate = ratePercent / 100 // 2 (%) -> 0.02
            val result = when (s.type) {
                LoanType.SIMPLE -> simpleLoan(principal, installments, rate)
                LoanType.FRENCH -> frenchLoan(principal, installments, rate)
            }
            s.copy(
                result = result,
                principalError = false,
                installmentsError = false,
                rateError = false
            )
        }
    }
}