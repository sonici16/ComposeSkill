package com.sonici16.composeskill.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sonici16.composeskill.NaverShoppingViewModel
import com.sonici16.composeskill.components.ShoppingResultItem

@Composable
fun SearchScreen(
    navController: NavController? = null,
    viewModel: NaverShoppingViewModel
) {
    var query by remember { mutableStateOf("") }

    val results by viewModel.searchResults.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    // 검색 화면 진입 시 이전 결과 초기화
    LaunchedEffect(Unit) {
        query = ""
        //viewModel.resetSearch()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 검색창 UI
        SearchTopBar(
            query = query,
            onBack = { navController?.navigateUp() },
            onQueryChange = { query = it },
            onSearch = {
                if (query.length >= 2) {
                    viewModel.search(query)
                }
            },
            viewModel = viewModel
        )

        Spacer(Modifier.height(12.dp))

        // 화면 상태에 따라 UI 분기
        when {
            error != null ->
                CenterText("오류 발생: $error", Color.Red)
            // 검색 결과 없음 + 로딩 아님 → 초기 안내 화면
            results.isEmpty() && !loading ->
                EmptySearchUI(
                    onTagClick = { tag ->
                        query = tag
                        viewModel.search(tag)
                    }
                )

            else -> {
                // 스크롤 튐 방지를 위해 LazyVerticalGrid는 항상 유지
                Box(Modifier.fillMaxSize()) {

                    ShoppingResultItem(
                        results = results,
                        onItemClick = { index ->
                            navController?.navigate("detail/search/$index")
                        },
                        viewModel = viewModel
                    )

                    // 페이징 로딩 시 그리드 위에 표시 (그리드를 재생성하지 않음)
                    if (loading && results.isNotEmpty()) {
                        CircularProgressIndicator(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CenterText(text: String, color: Color = Color.Black) =
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Text(text, color = color)
    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    viewModel: NaverShoppingViewModel
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        IconButton(onClick =
            { viewModel.resetSearch() }) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "뒤로가기",
                tint = Color.Black
            )
        }

        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF2F2F2))
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(10.dp))

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboard?.hide()
                            onSearch()
                        }
                    ),
                    decorationBox = { inner ->
                        if (query.isEmpty()) {
                            Text("상품명을 입력하세요", color = Color.Gray)
                        }
                        inner()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
@Composable
fun EmptySearchUI(
    onTagClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text("🔥 인기 검색어", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        val tags = listOf("고양이집", "고양이장난감", "캣타워", "강아지용품", "사료")

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            tags.forEach { tag ->
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF6F6F6), RoundedCornerShape(20.dp))
                        .clickable { onTagClick(tag) }   // 클릭 시 검색 실행!
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(tag)
                }
            }
        }

        Spacer(Modifier.height(30.dp))
        Text("검색해서 원하는 상품을 찾아보세요!", color = Color.Gray)
    }
}




