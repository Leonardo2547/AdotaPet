package com.example.adotapet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [AnimalEntity::class, InteresseAdocao::class],
    version = 5,
    exportSchema = false
)
abstract class AnimalDatabase : RoomDatabase() {

    abstract fun animalDao(): AnimalDao
    abstract fun interesseAdocaoDao(): InteresseAdocaoDao

    companion object {

        private val MIGRACAO_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE animais ADD COLUMN emProcessoAdocao INTEGER NOT NULL DEFAULT 0")
            }
        }
        private val MIGRACAO_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS interesses_adocao (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, animalId INTEGER NOT NULL, nomeInteressado TEXT NOT NULL, telefone TEXT NOT NULL, moradia TEXT NOT NULL, experiencia TEXT NOT NULL, criadoEm INTEGER NOT NULL)")
            }
        }

        @Volatile
        private var instancia: AnimalDatabase? = null

        fun getDatabase(context: Context): AnimalDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AnimalDatabase::class.java,
                    "adotapet_database"
                )
                    .addMigrations(MIGRACAO_3_4, MIGRACAO_4_5)
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { instancia = it }
            }
    }
}
