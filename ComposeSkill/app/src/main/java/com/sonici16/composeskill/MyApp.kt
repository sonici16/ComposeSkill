package com.sonici16.composeskill

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sonici16.composeskill.components.CustomBottomNavigationBar
import com.sonici16.composeskill.navigaton.NavGraph
import com.sonici16.composeskill.navigaton.Screen

@Composable
fun MyApp(viewModel: NaverShoppingViewModel) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route ?: Screen.Home.route


    Scaffold(
        bottomBar = {
            CustomBottomNavigationBar(
                navController = navController
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavGraph(navController,viewModel)
        }
    }
}
