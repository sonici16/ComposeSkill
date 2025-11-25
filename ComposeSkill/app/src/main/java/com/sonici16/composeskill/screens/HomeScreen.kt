package com.sonici16.composeskill.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import com.sonici16.composeskill.NaverShoppingViewModel
import com.sonici16.composeskill.model.ShoppingItem
import com.sonici16.composeskill.util.removeHtmlTags

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    navController: NavController? = null,
    viewModel: NaverShoppingViewModel,
    onItemClick: (Int) -> Unit
) {
    val mainItems by viewModel.mainItems.collectAsState()
    val itemsList by viewModel.itemsList.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load("고양이집")   // 기본 검색어
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item { TopBar() }
        item { Spacer(Modifier.height(16.dp)) }

        item {
            when {
                mainItems.isNotEmpty() -> ShoppingPager(mainItems, onItemClick)
                loading -> LoaderBox()
                error != null -> ErrorText(error!!)
            }
        }

        item { Spacer(Modifier.height(24.dp)) }

        item {
            when {
                itemsList.isNotEmpty() -> ShoppingSection(itemsList, onItemClick)
                loading -> LoaderBox()
                error != null -> ErrorText(error!!)
            }
        }

        item { Spacer(Modifier.height(60.dp)) }
    }
}

@Composable
fun LoaderBox() = Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(200.dp),
    contentAlignment = Alignment.Center
) {
    CircularProgressIndicator()
}

@Composable
fun ErrorText(msg: String) {
    Text("Error: $msg", color = Color.Red, modifier = Modifier.padding(16.dp))
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, bottom = 30.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Naver Shopping", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShoppingPager(items: List<ShoppingItem>, onItemClick: (Int) -> Unit) {
    val pagerState = rememberPagerState()
    val total = items.size

    Column {
        HorizontalPager(
            count = total,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) { page ->
            val item = items[page]

            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f),   // 정사각형으로 맞춤
                shape = RoundedCornerShape(16.dp)
            ) {
                AsyncImage(
                    model = item.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        val current = items[pagerState.currentPage]

        Text(
            text = removeHtmlTags(current.title),
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "${current.lprice}원 | ${current.mallName}",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}

@Composable
fun ShoppingSection(items: List<ShoppingItem>, onItemClick: (Int) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text("추천 상품", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items) { item ->
                val index = items.indexOf(item)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(80.dp)
                        .clickable { onItemClick(index) }
                ) {
                    AsyncImage(
                        model = item.image,
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = removeHtmlTags(item.title),
                        fontSize = 11.sp,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
