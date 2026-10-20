package com.example.a3apis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.a3apis.data.repository.AppRepository
import com.example.a3apis.ui.MainViewModel
import com.example.a3apis.ui.screens.ExploreScreen
import com.example.a3apis.ui.screens.FavoritesScreen
import com.example.a3apis.ui.screens.HomeScreen
import com.example.a3apis.ui.theme._3apisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _3apisTheme {
                _3apisApp()
            }
        }
    }
}

@Composable
fun _3apisApp() {
    val context = LocalContext.current.applicationContext
    val viewModel: MainViewModel = remember { MainViewModel(AppRepository(context = context)) }
    val uiState by viewModel.uiState.collectAsState()
    val favoriteCards by viewModel.favoriteCards.collectAsState()
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painterResource(destination.icon),
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            when (currentDestination) {
                AppDestinations.HOME -> HomeScreen(
                    uiState = uiState,
                    onRefreshClick = { viewModel.loadDailyOracle() },
                    onFavoriteToggle = { viewModel.toggleFavorite(it) },
                    modifier = modifier
                )

                AppDestinations.EXPLORE -> ExploreScreen(
                    uiState = uiState,
                    onFilterSelected = { viewModel.filterByCategory(it) },
                    onFavoriteToggle = { viewModel.toggleFavorite(it) },
                    modifier = modifier
                )

                AppDestinations.FAVORITES -> FavoritesScreen(
                    favoriteCards = favoriteCards,
                    onFavoriteToggle = { viewModel.toggleFavorite(it) },
                    modifier = modifier
                )
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("Inicio", R.drawable.ic_home),
    EXPLORE("Explorar", R.drawable.ic_account_box),
    FAVORITES("Favoritos", R.drawable.ic_favorite),
}
