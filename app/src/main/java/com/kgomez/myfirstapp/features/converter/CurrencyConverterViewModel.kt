package com.kgomez.myfirstapp.features.converter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ConverterState(
    val amount: String = "",
    var from: CurrencyOption = CurrencyOption.COP,
    var to: CurrencyOption = CurrencyOption.USD,
    val result: Double? = null,
    val hasError: Boolean = false
)

class CurrencyConverterViewModel : ViewModel() {
    private val _state = MutableStateFlow(ConverterState())
    val state: StateFlow<ConverterState> = _state.asStateFlow()
    fun onAmountChange(value: String) = _state.update {
        it.copy(amount = value)
    }
    fun onFromChange(option: CurrencyOption) = _state.update {
        it.from = option
        if (it.amount.replace(',', '.').toDoubleOrNull() != 0.0){
            onConvertClick()
        }
        it.copy(from = option)
    }
    fun onToChange(option: CurrencyOption) = _state.update {
        it.to = option
        if (it.amount.replace(',', '.').toDoubleOrNull() != 0.0){
            onConvertClick()
        }
        it.copy(to = option)
    }
    fun onConvertClick() = _state.update { current ->

        // acepta "12,5" y "12.5"
        val amount = current.amount.replace(',', '.').toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            current.copy(result = null, hasError = true)
        } else {
            current.copy(
                result = convertCurrency(amount, current.from, current.to),
                hasError = false
            )
        }
    }
}
