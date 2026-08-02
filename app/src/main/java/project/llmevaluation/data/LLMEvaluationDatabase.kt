package project.llmevaluation.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class LLMEvaluationDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: LLMEvaluationDatabase? = null

        fun getDatabase(context: Context): LLMEvaluationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LLMEvaluationDatabase::class.java,
                    "llm_evaluation_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
