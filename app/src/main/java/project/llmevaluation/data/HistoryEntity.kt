package project.llmevaluation.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evaluation_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val prompt: String,
    val modelName: String,
    val response: String,
    val timestamp: Long,
    
    // Evaluation Metrics
    val accuracy: Double,
    val coherence: Double,
    val perplexity: Double,
    val bleu: Double,
    val rouge: Double,
    val semanticSimilarity: Double,
    val fluency: Double,
    val overallScore: Double
)
