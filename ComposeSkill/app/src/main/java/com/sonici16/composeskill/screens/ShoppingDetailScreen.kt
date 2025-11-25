package com.sonici16.composeskill.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sonici16.composeskill.model.ShoppingItem

@Composable
fun ShoppingDetailScreen(
    item: ShoppingItem,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // 상단 이미지 + 뒤로가기
        Box {
            AsyncImage(
                model = item.image,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            // 상단 그라데이션
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                        )
                    )
            )

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(16.dp)
                    .size(36.dp)
                    .align(Alignment.TopStart)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // 상품명
        Text(
            text = item.title.replace("<b>", "").replace("</b>", ""),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        // 가격
        Text(
            text = "${item.lprice}원",
            fontSize = 22.sp,
            color = Color(0xFFE53935),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        // 쇼핑몰
        Text(
            text = "판매처: ${item.mallName}",
            color = Color.DarkGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        // 브랜드
        if (!item.brand.isNullOrEmpty())
            Text(
                text = "브랜드: ${item.brand}",
                color = Color.DarkGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

        Spacer(Modifier.height(20.dp))
        Divider(Modifier.padding(horizontal = 16.dp))

        // 카테고리 정보
        Text(
            text = "카테고리",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text("• ${item.category1}", fontSize = 15.sp)
            Text("• ${item.category2}", fontSize = 15.sp)
            Text("• ${item.category3}", fontSize = 15.sp)
            Text("• ${item.category4}", fontSize = 15.sp)
        }

        Spacer(Modifier.height(20.dp))
        Divider(Modifier.padding(horizontal = 16.dp))

        // 링크 이동 버튼
        TextButton(
            onClick = {
                // 외부 브라우저로 이동
                // (원하면 CustomTab으로 변경 가능)
            },
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "상품 보러가기",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(40.dp))
    }
}
