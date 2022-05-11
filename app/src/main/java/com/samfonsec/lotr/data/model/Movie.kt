package com.samfonsec.lotr.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samfonsec.lotr.util.Constants.FAVORITES_TABLE_NAME

@Entity(tableName = FAVORITES_TABLE_NAME)
data class Movie(
    @PrimaryKey
    val _id: String,
    val name: String,
    val runTimeInMinutes: Int,
    val budgetInMillions: Int,
    val boxOfficeRevenueInMillions: Double,
    val academyAwardNominations: Int,
    val academyAwardWins: Int,
    val rottenTomatoesScore: Double,
    var isFavorite: Boolean = false
)
