package com.example.luminalearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.luminalearn.presentation.navigation.AppNavHost
import com.example.luminalearn.ui.theme.LuminaLearnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LuminaLearnTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}