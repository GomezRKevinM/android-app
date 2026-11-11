package com.kgomez.myfirstapp.features.converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Button
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
fun CurrencyConverterScreen(
    onBack: () -> Unit,
    viewModel: CurrencyConverterViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CurrencyConverterContent(
        state = state,
        onBack = onBack,
        onAmountChange = viewModel::onAmountChange,
            onFromChange = viewModel::onFromChange,
        onToChange = viewModel::onToChange,
        onConvertClick = viewModel::onConvertClick
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyConverterContent(
    state: ConverterState,
    onBack: () -> Unit,
    onAmountChange: (String) -> Unit,
    onFromChange: (CurrencyOption) -> Unit,
    onToChange: (CurrencyOption) -> Unit,
    onConvertClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.converter_title)) },
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
                value = state.amount,
                onValueChange = onAmountChange,
                label = { Text(stringResource(R.string.converter_amount)) },
                singleLine = true,
                isError = state.hasError,
                supportingText = {
                    if (state.hasError) Text(stringResource(R.string.converter_error))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            RadioOptionGroup(
                title = stringResource(R.string.converter_from),
                options = CurrencyOption.entries,
                selected = state.from,
                label = { it.code },
                onSelected = onFromChange
            )
            RadioOptionGroup(
                title = stringResource(R.string.converter_to),
                options = CurrencyOption.entries,
                selected = state.to,
                label = { it.code },
                onSelected = onToChange
            )

            state.result?.let { result ->
                Text(
                    text = stringResource(
                        R.string.converter_result,
                        formatMoney(result, state.to.code)
                    ),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun CurrencyConverterPreview() {
    MyFirstAppTheme {
        CurrencyConverterContent(
            state = ConverterState(amount = "50000", result = 12.5),
            onBack = {}, onAmountChange = {}, onFromChange = {},
            onToChange = {}, onConvertClick = {}
        )
    }
}
