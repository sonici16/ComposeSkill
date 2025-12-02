package com.sonici16.composeskill.model

data class CsvRow(
    val top: String,
    val mid: String,
    val sub: String,
    val detail: String
)

data class CategoryNode(
    val name: String,
    val children: MutableList<CategoryNode> = mutableListOf()
)