package com.kgomez.myfirstapp.features.loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kgomez.myfirstapp.R
import com.kgomez.myfirstapp.core.formatMoney
import com.kgomez.myfirstapp.core.ui.RadioOptionGroup
import com.kgomez.myfirstapp.ui.theme.MyFirstAppTheme

@Composable
fun LoanCalculatorScreen(
    onBack: () -> Unit,
    viewModel: LoanViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LoanCalculatorContent(
        state = state,
        onBack = onBack,
        onPrincipalChange = viewModel::onPrincipalChange,
        onInstallmentsChange = viewModel::onInstallmentsChange,
        onRateChange = viewModel::onRateChange,
        onTypeChange = viewModel::onTypeChange,
        onCalculateClick = viewModel::onCalculateClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoanCalculatorContent(
    state: LoanState,
    onBack: () -> Unit,
    onPrincipalChange: (String) -> Unit,
    onInstallmentsChange: (String) -> Unit,
    onRateChange: (String) -> Unit,
    onTypeChange: (LoanType) -> Unit,
    onCalculateClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.loan_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.principal,
                onValueChange = onPrincipalChange,
                label = { Text(stringResource(R.string.loan_principal)) },
                singleLine = true,
                isError = state.principalError,
                supportingText = {
                    if (state.principalError) Text(stringResource(R.string.loan_error_principal))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.installments,
                onValueChange = onInstallmentsChange,
                label = { Text(stringResource(R.string.loan_installments)) },
                singleLine = true,
                isError = state.installmentsError,
                supportingText = {
                    if (state.installmentsError) Text(stringResource(R.string.loan_error_installments))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.monthlyRate,
                onValueChange = onRateChange,
                label = { Text(stringResource(R.string.loan_rate)) },
                singleLine = true,
                isError = state.rateError,
                supportingText = {
                    if (state.rateError) Text(stringResource(R.string.loan_error_rate))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            RadioOptionGroup(
                title = stringResource(R.string.loan_type),
                options = LoanType.entries,
                selected = state.type,
                label = { type ->
                    stringResource(
                        when (type) {
                            LoanType.SIMPLE -> R.string.loan_type_simple
                            LoanType.FRENCH -> R.string.loan_type_french
                        }
                    )
                },
                onSelected = onTypeChange
            )
            Button(onClick = onCalculateClick, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.loan_button))
            }
            state.result?.let { result ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.loan_result_installment,
                                formatMoney(result.installment)
                            ),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(stringResource(R.string.loan_result_total, formatMoney(result.total)))
                        Text(stringResource(R.string.loan_result_profit, formatMoney(result.profit)))
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun LoanCalculatorPreview() {
    MyFirstAppTheme {
        LoanCalculatorContent(
            state = LoanState(
                principal = "1000000", installments = "12", monthlyRate = "2",
                result = LoanResult(103333.33, 1240000.0, 240000.0)
            ),
            onBack = {}, onPrincipalChange = {}, onInstallmentsChange = {},
            onRateChange = {}, onTypeChange = {}, onCalculateClick = {}
        )
    }
}