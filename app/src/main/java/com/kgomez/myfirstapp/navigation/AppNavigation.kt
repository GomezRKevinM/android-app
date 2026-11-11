package com.kgomez.myfirstapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kgomez.myfirstapp.features.converter.CurrencyConverterScreen
import com.kgomez.myfirstapp.features.home.HomeScreen
import com.kgomez.myfirstapp.features.loan.LoanCalculatorScreen
import kotlinx.serialization.Serializable

@Serializable object Home
@Serializable object Converter
@Serializable object Loan
@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onConverter = { nav.navigate(Converter) },
                onLoan = { nav.navigate(Loan) }
            )
        }
        composable<Converter> {
            CurrencyConverterScreen(onBack = { nav.popBackStack() })
        }
        composable<Loan> {
            LoanCalculatorScreen(onBack = { nav.popBackStack() })
        }
    } // <-- el NavHost se cierra AQUI, despues de las tres rutas
}