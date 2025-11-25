package com.sonici16.composeskill.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sonici16.composeskill.navigaton.Screen

@Composable
fun CustomBottomNavigationBar(
    navController: NavController
) {
    val items = listOf(
        Screen.Home to Icons.Default.Home,
        Screen.Search to Icons.Default.Search,
        Screen.Add to Icons.Default.Add,
        Screen.Menu to Icons.Default.Menu,
        Screen.Profile to Icons.Default.Person
    )

    // 현재 라우트
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, bottom = 25.dp, end = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (screen, icon) ->

                val isSelected = currentRoute == screen.route

                // 선택된 탭만 살짝 커지게 하는 애니메이션
                val iconSize by animateDpAsState(
                    targetValue = if (isSelected) 34.dp else 28.dp,
                    label = ""
                )

                // 선택된 탭은 다른 색상
                val iconColor = if (isSelected) Color.Yellow else Color.White

                IconButton(
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = screen.route,
                        tint = iconColor,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}

