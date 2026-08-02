package project.llmevaluation.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity)

    @Query("SELECT * FROM evaluation_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM evaluation_history WHERE prompt LIKE '%' || :searchQuery || '%' OR modelName LIKE '%' || :searchQuery || '%' ORDER BY timestamp DESC")
    fun searchHistory(searchQuery: String): Flow<List<HistoryEntity>>

    @Query("DELETE FROM evaluation_history WHERE id = :id")
    suspend fun deleteHistory(id: Long)

    @Query("DELETE FROM evaluation_history")
    suspend fun deleteAllHistory()
}
