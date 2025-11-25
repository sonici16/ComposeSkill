package com.sonici16.composeskill.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sonici16.composeskill.NaverShoppingViewModel
import com.sonici16.composeskill.model.ShoppingItem

@Composable
fun SearchScreen(
    navController: NavController? = null,
    viewModel: NaverShoppingViewModel
) {
    var query by remember { mutableStateOf("") }

    val results by viewModel.searchResults.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        query = ""
        viewModel.resetSearch()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        SearchTopBar(
            query = query,
            onBack = { navController?.navigateUp() },
            onQueryChange = { query = it },
            onSearch = {
                if (query.length >= 2) {
                    viewModel.search(query)
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        when {
            loading -> CenterCircle()
            error != null -> CenterText("오류 발생: $error", Color.Red)
            results.isEmpty() -> EmptySearchUI()
            else -> ShoppingResultGrid(
                results = results,
                onItemClick = { index ->
                    navController?.navigate("detail/search/$index")
                }
            )
        }
    }
}

@Composable
fun CenterCircle() = Box(Modifier.fillMaxSize(), Alignment.Center) {
    CircularProgressIndicator()
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
    onBack: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        IconButton(onClick = onBack) {
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
fun EmptySearchUI() {
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
@Composable
fun ShoppingResultGrid(results: List<ShoppingItem>, onItemClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(results.size) { index ->
            val item = results[index]

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF4F4F4))
                    .clickable { onItemClick(index) }
            ) {
                AsyncImage(
                    model = item.image,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(130.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = item.title.replace("<b>", "").replace("</b>", ""),
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
