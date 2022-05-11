package com.samfonsec.lotr.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character(
    val _id: String,
    val name: String,
    val race: String,
    val gender: String,
    val birth: String,
    val death: String,
    val spouse: String,
    val height: String,
    val hair: String,
    val realm: String,
    val wikiUrl: String?,
) : Parcelable
