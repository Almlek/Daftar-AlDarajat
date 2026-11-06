package com.alborihi.myapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.alborihi.myapp.presentation.navigation.AppNavHost
import com.alborihi.myapp.presentation.ui.theme.MyAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity الرئيسي للتطبيق.
 * نقطة الدخول الوحيدة (single entry point).
 * يتولى: Edge-to-Edge، Theme، Navigation Host.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}
