package com.sonici16.composeskill.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import com.sonici16.composeskill.model.Row
import com.sonici16.composeskill.model.parseIngredients
import com.sonici16.composeskill.model.toRecipeStepData

// -------------------------------------------------------------------------
// 1) 상세 화면
// -------------------------------------------------------------------------


@OptIn(ExperimentalPagerApi::class)
@Composable
fun RecipeDetailScreen(
    recipe: Row,
    onBack: () -> Unit
) {
    val stepData = recipe.toRecipeStepData()
    val ingredients = parseIngredients(recipe.RCP_PARTS_DTLS)
    val pagerState = rememberPagerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // 상단 대표 이미지 + 뒤로가기 버튼
        Box {
            AsyncImage(
                model = recipe.ATT_FILE_NO_MAIN,
                contentDescription = recipe.RCP_NM,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentScale = ContentScale.Crop
            )

            // 상단 그라데이션 오버레이
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
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
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // 레시피 제목
        Text(
            text = recipe.RCP_NM,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        // 영양 정보
        Text(
            text = "${recipe.INFO_ENG} kcal · 단백질 ${recipe.INFO_PRO}g · 지방 ${recipe.INFO_FAT}g",
            color = Color.DarkGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Divider(Modifier.padding(vertical = 16.dp, horizontal = 16.dp))

        // 재료 섹션
        Text(
            text = "재료",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            ingredients.forEach { item ->
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF2F2F2), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(text = item, fontSize = 14.sp)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // 조리 단계
        Text(
            text = "조리 단계",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(12.dp))

        HorizontalPager(
            count = stepData.steps.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    AsyncImage(
                        model = stepData.images[page],
                        contentDescription = "Step Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "STEP ${page + 1}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF444444)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = stepData.steps[page],
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // 페이지 인디케이터
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(stepData.steps.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 12.dp else 8.dp)
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.Black else Color.LightGray)
                )
            }
        }

        Spacer(Modifier.height(50.dp))
    }
}


