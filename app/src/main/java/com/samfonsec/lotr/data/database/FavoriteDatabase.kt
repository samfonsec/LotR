package com.samfonsec.lotr.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.samfonsec.lotr.data.dao.FavoriteDao
import com.samfonsec.lotr.data.model.Movie

@Database(
    entities = [Movie::class],
    version = 1
)
@TypeConverters(FavoriteTypeConverter::class)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private const val DB_FILE_NAME = "favorite_db"

        @Volatile
        private var instance: FavoriteDatabase? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context): FavoriteDatabase {
            return Room.databaseBuilder(context, FavoriteDatabase::class.java, DB_FILE_NAME).build()
        }
    }
}