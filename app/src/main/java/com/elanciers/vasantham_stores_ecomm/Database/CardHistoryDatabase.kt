package com.elanciers.vasantham_stores_ecomm.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [CardHistoryData::class], version = 1, exportSchema = false)
/*@TypeConverters(
    DateConverters::class
)*/
abstract class CardHistoryDatabase : RoomDatabase() {
    abstract fun cardDao(): CardHistoryDao?

    companion object {
        private const val DATABASE_NAME = "CardHistory"
        private val LOCK = Object()
        private var sDatabase: CardHistoryDatabase? = null
        fun getDatabase(context: Context): CardHistoryDatabase? {
            if (sDatabase == null) {
                //synchronized(LOCK) {
                /*val MIGRATION_1_2 = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE NewCart ADD COLUMN price TEXT")
                        database.execSQL("ALTER TABLE NewCart ADD COLUMN total TEXT")
                    }
                }*/
                    sDatabase = Room.databaseBuilder(
                        context.applicationContext,
                        CardHistoryDatabase::class.java,
                        DATABASE_NAME
                    )
                        //.addMigrations(MIGRATION_1_2)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                //}
            }
            return sDatabase
        }
    }
}