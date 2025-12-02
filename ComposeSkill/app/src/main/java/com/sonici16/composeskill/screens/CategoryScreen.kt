package com.sonici16.composeskill.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sonici16.composeskill.NaverShoppingViewModel
import com.sonici16.composeskill.components.ShoppingResultItem
import com.sonici16.composeskill.model.CategoryNode
import com.sonici16.composeskill.navigaton.Screen

@Composable
fun CategoryScreen(
    navController: NavController,
    viewModel: NaverShoppingViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.initCategory(context) // CSV 로딩
    }

    val categoryTree by viewModel.categoryTree.collectAsState()
    val selectedPath by viewModel.selectedPath.collectAsState()
    val items by viewModel.items.collectAsState()
    val loading by viewModel.loading.collectAsState()

    if (categoryTree.isEmpty() && loading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }


    // 카테고리 or 상품 표시를 결정하는 상태
    val showProducts = items.isNotEmpty()

    val currentList =
        if (selectedPath.isEmpty())
            categoryTree
        else
            selectedPath.last().children


    Scaffold(
        topBar = {
            TopBar(
                canPop = selectedPath.isNotEmpty() || showProducts,
                onBack = {
                    if (showProducts) {
                        viewModel.clearItems() // ← 여기
                    } else {
                        val popped = viewModel.popCategory()
                        if (!popped) navController.popBackStack()
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Breadcrumb(selectedPath)

            when {
                loading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                // 상품이 있으면 → 상품 먼저 표시
                items.isNotEmpty() -> {
                    Text(
                        text = "상품 목록",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    ShoppingResultItem(
                        results = items,
                        onItemClick = { index ->
                            navController.navigate("detail/category/$index")
                        },
                        viewModel = viewModel
                    )
                }

                // 상품이 없고, 카테고리 있으면 → 카테고리 표시
                currentList.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(currentList) { node ->
                            CategoryRow(
                                text = node.name,
                                onClick = { viewModel.selectCategory(node) }
                            )
                            Divider()
                        }
                    }
                }

                // 마지막 fallback
                else -> {
                    EmptyUI()
                }
            }



        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    canPop: Boolean,
    onBack: () -> Unit
) {
    TopAppBar(
        title = { Text("카테고리", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            if (canPop) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로",
                        tint = Color.Black
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(0.dp)) // null 대신 공간 차지 방지
            }
        }
    )
}

@Composable
fun CategoryRow(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, fontSize = 18.sp)
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
    }
}

@Composable
fun Breadcrumb(path: List<CategoryNode>) {
    if (path.isEmpty()) return

    Row(Modifier.padding(12.dp)) {
        path.forEachIndexed { index, node ->
            Text(node.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            if (index < path.lastIndex) Text(" > ")
        }
    }
}

@Composable
fun EmptyUI() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Text("카테고리를 선택해주세요.", color = MaterialTheme.colorScheme.secondary)
    }
}

