package com.sonici16.composeskill.util

fun removeHtmlTags(text: String): String {
    return text.replace(Regex("<.*?>"), "")
}