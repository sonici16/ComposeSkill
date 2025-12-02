package com.sonici16.composeskill.navigaton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sonici16.composeskill.NaverShoppingViewModel
import com.sonici16.composeskill.screens.*
import com.sonici16.composeskill.screens.AddScreen
import com.sonici16.composeskill.screens.ProfileScreen
import com.sonici16.composeskill.screens.SearchScreen
import com.sonici16.composeskill.screens.HomeScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: NaverShoppingViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        // ---------------- Home ----------------
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel,
                onItemClick = { index ->
                    navController.navigate("detail/main/$index")
                }
            )
        }

        // ---------------- Search ----------------
        composable(Screen.Search.route) {
            SearchScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Screen.Add.route) { AddScreen() }
        composable(Screen.Category.route) {
            CategoryScreen(
                viewModel = viewModel,  // << 공용 ViewModel
                navController = navController
            )
        }
        composable(Screen.Profile.route) { ProfileScreen() }

        // ---------------- Detail ----------------
        composable("detail/{from}/{index}") { backStackEntry ->

            val from = backStackEntry.arguments?.getString("from") ?: ""
            val index = backStackEntry.arguments?.getString("index")?.toInt() ?: 0

            // ---------------- 모든 리스트 중 선택 ----------------
            val productList = when (from) {
                "main" -> viewModel.mainItems.collectAsState().value
                "list" -> viewModel.itemsList.collectAsState().value
                "search" -> viewModel.searchResults.collectAsState().value
                "category" -> viewModel.items.collectAsState().value
                else -> emptyList()
            }

            val product = productList.getOrNull(index)

            if (product == null) {
                Text("상품 정보를 불러올 수 없습니다.")
                return@composable
            }

            ShoppingDetailScreen(
                item = product,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
