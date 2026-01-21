package com.prince.cinemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.prince.cinemon.ui.navigation.NavApp
import com.prince.cinemon.ui.theme.MovieTVDiscoveryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieTVDiscoveryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavApp()
                }
            }
        }
    }
}
