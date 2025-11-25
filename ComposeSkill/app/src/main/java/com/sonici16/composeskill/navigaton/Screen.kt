package com.sonici16.composeskill.navigaton


// 앱의 각 화면을 구분하는 route 정의
sealed class Screen(val route: String) {
    object Home : Screen("home")        // 홈 화면
    object Search : Screen("search")    // 검색 화면
    object Add : Screen("add")          // 추가 화면 (+ 버튼)
    object Menu : Screen("menu")        // 메뉴 화면
    object Profile : Screen("profile")  // 내 정보 화면

    object Detail : Screen("detail/{recipeId}") { // 레시피 화면
        fun passId(id: String) = "detail/$id"
    }
}
