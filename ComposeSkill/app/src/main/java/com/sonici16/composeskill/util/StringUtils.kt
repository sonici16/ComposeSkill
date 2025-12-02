package com.sonici16.composeskill.util
import android.content.Context
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.sonici16.composeskill.model.CategoryNode
import com.sonici16.composeskill.model.CsvRow

fun removeHtmlTags(text: String): String {
    return text.replace(Regex("<.*?>"), "")
}


fun loadCategoryCsv(context: Context): List<List<String>> {
    val input = context.assets.open("category.csv")
    val data = csvReader().readAllWithHeader(input)

    return data.flatMap { row ->
        val major = row["대분류"] ?: return@flatMap emptyList()
        val mids = parseList(row["중분류"])
        val smalls = parseList(row["소분류"])

        mids.flatMapIndexed { i, mid ->
            val small = smalls.getOrNull(i) ?: ""
            listOf(listOf(major, mid, small))
        }
    }
        .filter { list -> list.any { it.isNotBlank() && it != "없음" } }
}

private fun parseList(raw: String?): List<String> =
    raw?.removePrefix("[")?.removeSuffix("]") // [] 제거
        ?.split(",")
        ?.map { it.trim().removePrefix("'").removeSuffix("'") } // '문자열' 정리
        ?.filter { it.isNotBlank() && it != "없음" }
        ?: emptyList()


fun buildCategoryTree(csv: List<List<String>>): List<CategoryNode> {
    val root = mutableMapOf<String, CategoryNode>()

    csv.forEach { row ->
        var parentMap = root
        var parent: CategoryNode? = null

        row.forEach { category ->
            if (category.isBlank()) return@forEach

            val node = parentMap[category] ?: CategoryNode(category, mutableListOf())
            if (parent != null) {
                val childList = parent!!.children as MutableList
                if (!childList.any { it.name == node.name }) {
                    childList.add(node)
                }
            }
            parentMap[category] = node
            parent = node
            parentMap = (node.children as MutableList).associateBy { it.name }.toMutableMap()
        }
    }

    return root.values.toList()
}



