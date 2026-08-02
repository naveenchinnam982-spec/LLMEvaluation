package project.llmevaluation.models

import com.google.gson.annotations.SerializedName

data class EvaluationResult(
    @SerializedName("model") val model: String,
    @SerializedName("accuracy") val accuracy: Double,
    @SerializedName("coherence") val coherence: Double,
    @SerializedName("perplexity") val perplexity: Double,
    @SerializedName("bleu") val bleu: Double,
    @SerializedName("rouge") val rouge: Double,
    @SerializedName("semantic_similarity") val semanticSimilarity: Double,
    @SerializedName("fluency") val fluency: Double,
    @SerializedName("overall_score") val overallScore: Double
)
