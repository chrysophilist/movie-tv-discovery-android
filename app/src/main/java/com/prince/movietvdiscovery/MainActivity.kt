package com.prince.movietvdiscovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.prince.movietvdiscovery.ui.details.DetailsScreen
import com.prince.movietvdiscovery.ui.details.DetailsViewModel
import com.prince.movietvdiscovery.ui.home.HomeScreen
import com.prince.movietvdiscovery.ui.home.HomeViewModel
import com.prince.movietvdiscovery.ui.navigation.NavApp
import com.prince.movietvdiscovery.ui.theme.MovieTVDiscoveryTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieTVDiscoveryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    val viewModel : HomeViewModel = koinViewModel()
//                    val DetailviewModel : DetailsViewModel = koinViewModel()
////                    HomeScreen(viewModel)
//                    DetailsScreen(
//                        3173903,
//                        DetailviewModel
//                    )
                    NavApp()
                }
            }
        }
    }
}
