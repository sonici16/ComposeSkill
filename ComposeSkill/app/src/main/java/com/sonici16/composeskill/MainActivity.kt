package com.sonici16.composeskill
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import com.sonici16.composeskill.ui.theme.model.Row
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // "국" → 메인 카드
        viewModel.loadMainRecipes(1, 10, "국")

        // "반찬" → ArtistSection
        viewModel.loadRecipesList(1, 10, "반찬")

        setContent {
            MainScreen(viewModel)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(viewModel: RecipeViewModel) {
    val mainRecipes by viewModel.mainRecipes.collectAsState()
    val recipesList by viewModel.recipesList.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(bottomBar = { CustomBottomNavigationBar() }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            item { TopBar() }
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // ✅ 메인 카드 Pager → 국
            item {
                when {
                    mainRecipes.isNotEmpty() -> MainCardPager(mainRecipes)
                    isLoading -> CircularProgressIndicator()
                    error != null -> Text("Error: $error", color = Color.Red)
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // ✅ ArtistSection → 반찬
            item { RecipeSection(recipesList) }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("appName", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "알림",
            tint = Color.Black // 선 색상만 적용됨 (안은 흰색으로 표현됨)
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainCardPager(recipes: List<Row>) {
    val pagerState = rememberPagerState()
    val totalPages = recipes.size

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            count = totalPages,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) { page ->
            val recipe = recipes[page]

            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box {
                    AsyncImage(
                        model = recipe.ATT_FILE_NO_MAIN,
                        contentDescription = recipe.RCP_NM,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // 오른쪽 아래 넘버링
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            "${page + 1}/$totalPages",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (recipes.isNotEmpty()) {
            val currentRecipe = recipes[pagerState.currentPage]
            Text(
                currentRecipe.RCP_NM,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Text(
                "${currentRecipe.INFO_ENG} kcal | 단백질 ${currentRecipe.INFO_PRO} g",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
fun RecipeSection(recipes: List<Row>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("레시피 리스트", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp) // ✅ 일정한 간격
        ) {
            items(recipes) { recipe ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(80.dp) // ✅ 텍스트 폭 제한
                ) {
                    AsyncImage(
                        model = recipe.ATT_FILE_NO_MAIN,
                        contentDescription = recipe.RCP_NM,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        recipe.RCP_NM,
                        fontSize = 12.sp,
                        maxLines = 2, // ✅ 한 줄만
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis // ✅ 말줄임표 처리
                    )
                }
            }
        }
    }
}


@Composable
fun CustomBottomNavigationBar(
    selectedIndex: Int = 0,
    onItemSelected: (Int) -> Unit = {}
) {
    val items = listOf(
        Icons.Default.Home  to "홈",
        Icons.Default.Search to "검색",
        Icons.Default.Add to "추가", // 가운데 + 버튼
        Icons.Default.Menu to "메뉴",
        Icons.Default.Person to "내 정보"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp) // 화면 끝과 살짝 띄우기
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp)) // 둥근 바
                .background(Color.Black),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, (icon, label) ->
                IconButton(onClick = { onItemSelected(index) }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (selectedIndex == index) Color.White else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}