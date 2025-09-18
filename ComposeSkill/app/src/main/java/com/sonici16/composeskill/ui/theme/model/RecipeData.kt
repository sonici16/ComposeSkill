package com.sonici16.composeskill.ui.theme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Root(
    val COOKRCP01: Cookrcp01
): Parcelable
@Parcelize
data class Cookrcp01(
    val total_count: String,
    val row: List<Row>,
    val RESULT: Result
): Parcelable
@Parcelize
data class Result(
    val MSG: String,
    val CODE: String,
): Parcelable

@Parcelize
data class Row(
    val RCP_SEQ:String,  //일련번호
    val RCP_NM:String, //메뉴명
    val RCP_WAY2:String, //	조리방법
    val RCP_PAT2:String, //	요리종류
    val INFO_WGT:String, //	중량(1인분)
    val INFO_ENG:String, //	열량
    val INFO_CAR:String, //	탄수화물
    val INFO_PRO:String, //	단백질
    val INFO_FAT:String, //	지방
    val INFO_NA:String, //	나트륨
    val ATT_FILE_NO_MAIN:String, //	이미지경로(소)
    val RCP_PARTS_DTLS:String, //	재료정보
    val MANUAL01:String, //	만드는법_01
    val MANUAL_IMG01:String, //	만드는법_이미지_01
    val MANUAL02:String, //	만드는법_02
    val MANUAL_IMG02:String, //	만드는법_이미지_02
    val MANUAL03:String, //	만드는법_03
    val MANUAL_IMG03:String, //	만드는법_이미지_03
    val MANUAL04:String, //	만드는법_04
    val MANUAL_IMG04:String, //	만드는법_이미지_04
    val MANUAL05:String, //	만드는법_05
    val MANUAL_IMG05:String, //	만드는법_이미지_05
    val MANUAL06:String, //	만드는법_06
    val MANUAL_IMG06:String, //	만드는법_이미지_06
    val MANUAL07:String, //	만드는법_07
    val MANUAL_IMG07:String, //	만드는법_이미지_07
    val MANUAL08:String, //	만드는법_08
    val MANUAL_IMG08:String, //	만드는법_이미지_08
    val MANUAL09:String, //만드는법_09
    val MANUAL_IMG09:String, //	만드는법_이미지_09
    val MANUAL10:String, //	만드는법_10
    val MANUAL_IMG10:String, //	만드는법_이미지_10
    val MANUAL11:String, //	만드는법_11
    val MANUAL_IMG11:String, //	만드는법_이미지_11
    val MANUAL12:String, //만드는법_12
    val MANUAL_IMG12:String, //	만드는법_이미지_12
    val MANUAL13:String, //만드는법_13
    val MANUAL_IMG13:String, //	만드는법_이미지_13
    val MANUAL14:String, //만드는법_14
    val MANUAL_IMG14:String, //	만드는법_이미지_14
    val MANUAL15:String, //만드는법_15
    val MANUAL_IMG15:String, //	만드는법_이미지_15
    val MANUAL16:String, //만드는법_16
    val MANUAL_IMG16:String, //	만드는법_이미지_16
    val MANUAL17:String, //만드는법_17
    val MANUAL_IMG17:String, //	만드는법_이미지_17
    val MANUAL18:String, //만드는법_18
    val MANUAL_IMG18:String, //	만드는법_이미지_18
    val MANUAL19:String, //만드는법_19
    val MANUAL_IMG19:String, //	만드는법_이미지_19
    val MANUAL20:String, //만드는법_20
    val MANUAL_IMG20:String, //	만드는법_이미지_20
): Parcelable

@Parcelize
data class ManualData(
    val manual: String,
    val manualImage: String
): Parcelable

