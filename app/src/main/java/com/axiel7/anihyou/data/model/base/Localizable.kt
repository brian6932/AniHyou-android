package com.axiel7.anihyou.data.model.base

import androidx.compose.runtime.Composable

fun interface Localizable {
    @Composable
    fun localized(): String
}