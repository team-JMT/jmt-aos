package org.gdsc.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [RegisteredRestaurant::class], version = 5)
abstract class RestaurantDatabase: RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    companion object {
        const val DATABASE_NAME = "restaurant-db"
        private var INSTANCE: RestaurantDatabase? = null

        fun getInstance(context: Context): RestaurantDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RestaurantDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_4_5)
                    .build()
                INSTANCE = instance
                instance
            }
        }
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE restaurant ADD COLUMN groupId INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE restaurant ADD COLUMN groupName TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}