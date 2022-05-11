package com.samfonsec.lotr.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quote(
    val _id: String,
    val dialog: String,
    val movie: String,
    val character: String
) : Parcelable
