package com.dicoding.ecosorter.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val title: String,
    val description: String,
    val photo: String,
) : Parcelable