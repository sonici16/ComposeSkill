package com.sonici16.composeskill.model

/**
 * 레시피 단계 + 이미지 데이터 모델
 */
data class RecipeStepData(
    val images: List<String>,
    val steps: List<String>
)

/**
 * Row → RecipeStepData 변환 함수
 * MANUAL / MANUAL_IMG 를 자동 매핑해준다.
 */
fun Row.toRecipeStepData(): RecipeStepData {
    val images = mutableListOf<String>()
    val steps = mutableListOf<String>()

    val manualTexts = listOf(
        MANUAL01, MANUAL02, MANUAL03, MANUAL04, MANUAL05,
        MANUAL06, MANUAL07, MANUAL08, MANUAL09, MANUAL10,
        MANUAL11, MANUAL12, MANUAL13, MANUAL14, MANUAL15,
        MANUAL16, MANUAL17, MANUAL18, MANUAL19, MANUAL20
    )

    val manualImages = listOf(
        MANUAL_IMG01, MANUAL_IMG02, MANUAL_IMG03, MANUAL_IMG04, MANUAL_IMG05,
        MANUAL_IMG06, MANUAL_IMG07, MANUAL_IMG08, MANUAL_IMG09, MANUAL_IMG10,
        MANUAL_IMG11, MANUAL_IMG12, MANUAL_IMG13, MANUAL_IMG14, MANUAL_IMG15,
        MANUAL_IMG16, MANUAL_IMG17, MANUAL_IMG18, MANUAL_IMG19, MANUAL_IMG20
    )

    for (i in manualTexts.indices) {
        val step = manualTexts[i]
        val img = manualImages[i]

        if (!step.isNullOrBlank() && !img.isNullOrBlank()) {
            steps.add(step)
            images.add(img)
        }
    }

    return RecipeStepData(images, steps)
}


// 재료 문자열(RCP_PARTS_DTLS)을 List<String> 으로 변환해주는 헬퍼 함수
fun parseIngredients(parts: String?): List<String> {
    if (parts.isNullOrBlank()) return emptyList()

    return parts
        // 여러 구분자(쉼표, 점, 슬래시, 줄바꿈)를 한 번에 처리
        .split(",", "·", "/", "\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
}