package com.sonici16.composeskill.ui.theme.model

class EnumContainer {
    enum class ContentType(val value: String) {
        밥("밥"), 반찬("반찬"), 국("국"), 후식("후식"), 기본("밥");

        companion object {
            fun fromInt(value: Int): ContentType {
                return when (value) {
                    1 -> 밥
                    2 -> 반찬
                    3 -> 국
                    4 -> 후식
                    else -> 기본
                }
            }
        }
    }

}