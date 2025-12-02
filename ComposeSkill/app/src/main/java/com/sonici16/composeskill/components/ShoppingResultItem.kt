package com.sonici16.composeskill.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sonici16.composeskill.NaverShoppingViewModel
import com.sonici16.composeskill.model.ShoppingItem
import com.sonici16.composeskill.navigaton.Screen
import com.sonici16.composeskill.util.removeHtmlTags

@Composable
fun ShoppingResultItem(
    results: List<ShoppingItem>,
    onItemClick: (Int) -> Unit,
    viewModel: NaverShoppingViewModel
) {
    val loading by viewModel.loading.collectAsState()

    // 스크롤 위치가 리셋되지 않도록 상태를 기억
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = gridState, // 스크롤 유지 핵심
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {

        /**
         *  핵심 1: itemsIndexed + key
         *
         *  - key를 주면 Compose가 아이템을 안정적으로 추적
         *  - 리스트가 업데이트되어도 스크롤 위치가 유지됨
         *  - productId는 네이버 쇼핑 상품의 고유 값이므로 key로 최적
         */
        itemsIndexed(
            items = results,
            key = { _, item -> item.productId }
        ) { index, item ->

            // 마지막 아이템에 도달 → 다음 페이지 로딩 요청
            // loading 값으로 중복 호출은 ViewModel에서 자동 방지
            if (index == results.lastIndex && !loading) {
                viewModel.loadNextPage()
            }

            // 상품 UI 1개
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
                        .fillMaxWidth()
                )

                Text(
                    text = removeHtmlTags(item.title),
                    modifier = Modifier.padding(8.dp),
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
}
