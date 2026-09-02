package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Flashcard::class], version = 1, exportSchema = false)
abstract class FlashcardDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: FlashcardDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FlashcardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlashcardDatabase::class.java,
                    "flashcard_database"
                )
                .addCallback(FlashcardDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class FlashcardDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialDeck(database.flashcardDao())
                    }
                }
            }

            suspend fun populateInitialDeck(dao: FlashcardDao) {
                val starterCards = listOf(
                    Flashcard(
                        question = "What is the primary function of mitochondria in eukaryotic cells?",
                        answer = "Mitochondria generate most of the chemical energy (ATP) needed to power the cell's biochemical reactions.",
                        category = "Science"
                    ),
                    Flashcard(
                        question = "What is Kotlin's null safety mechanism designed to prevent?",
                        answer = "It is designed to eliminate NullPointerExceptions (NPEs) through nullable (?) and non-nullable types.",
                        category = "Programming"
                    ),
                    Flashcard(
                        question = "What is the capital of Japan?",
                        answer = "Tokyo",
                        category = "Geography"
                    ),
                    Flashcard(
                        question = "What is the speed of light in a vacuum?",
                        answer = "Approximately 299,792 kilometers per second (about 300,000 km/s or 186,000 miles/s).",
                        category = "Physics"
                    ),
                    Flashcard(
                        question = "What is the difference between val and var in Kotlin?",
                        answer = "'val' declares a read-only (immutable reference) variable, whereas 'var' declares a mutable variable.",
                        category = "Programming"
                    ),
                    Flashcard(
                        question = "What is the largest planet in our solar system?",
                        answer = "Jupiter",
                        category = "Astronomy"
                    )
                )
                dao.insertAll(starterCards)
            }
        }
    }
}
